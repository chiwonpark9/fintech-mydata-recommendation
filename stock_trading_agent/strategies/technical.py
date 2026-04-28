"""기술적 분석 전략 모듈"""

import pandas as pd
import numpy as np
from ..config.settings import TECHNICAL_PARAMS


def add_indicators(df: pd.DataFrame) -> pd.DataFrame:
    """기술적 지표를 계산하여 DataFrame에 추가한다."""
    p = TECHNICAL_PARAMS
    close = df["Close"]
    high = df["High"]
    low = df["Low"]
    volume = df["Volume"]

    # 이동평균선
    df["SMA5"] = close.rolling(window=p["sma_short"]).mean()
    df["SMA20"] = close.rolling(window=p["sma_long"]).mean()

    # RSI
    delta = close.diff()
    gain = delta.where(delta > 0, 0).rolling(window=p["rsi_period"]).mean()
    loss = (-delta.where(delta < 0, 0)).rolling(window=p["rsi_period"]).mean()
    rs = gain / loss
    df["RSI"] = 100 - (100 / (1 + rs))

    # MACD
    ema_fast = close.ewm(span=p["macd_fast"], adjust=False).mean()
    ema_slow = close.ewm(span=p["macd_slow"], adjust=False).mean()
    df["MACD"] = ema_fast - ema_slow
    df["MACD_Signal"] = df["MACD"].ewm(span=p["macd_signal"], adjust=False).mean()
    df["MACD_Hist"] = df["MACD"] - df["MACD_Signal"]

    # 볼린저 밴드
    sma_bb = close.rolling(window=p["bollinger_period"]).mean()
    std_bb = close.rolling(window=p["bollinger_period"]).std()
    df["BB_Upper"] = sma_bb + p["bollinger_std"] * std_bb
    df["BB_Lower"] = sma_bb - p["bollinger_std"] * std_bb
    df["BB_Middle"] = sma_bb

    # 거래량 이동평균
    df["Volume_MA"] = volume.rolling(window=p["volume_ma_period"]).mean()
    df["Volume_Ratio"] = volume / df["Volume_MA"]

    return df


def generate_signal(df: pd.DataFrame) -> dict:
    """기술적 지표를 기반으로 매매 신호를 생성한다."""
    if len(df) < 30:
        return {"signal": "HOLD", "score": 0, "reasons": ["데이터 부족"]}

    last = df.iloc[-1]
    prev = df.iloc[-2]
    p = TECHNICAL_PARAMS

    score = 0
    reasons = []

    # 1. 이동평균 골든/데드 크로스
    if last["SMA5"] > last["SMA20"] and prev["SMA5"] <= prev["SMA20"]:
        score += 2
        reasons.append("골든크로스 발생 (SMA5 > SMA20)")
    elif last["SMA5"] < last["SMA20"] and prev["SMA5"] >= prev["SMA20"]:
        score -= 2
        reasons.append("데드크로스 발생 (SMA5 < SMA20)")
    elif last["SMA5"] > last["SMA20"]:
        score += 1
        reasons.append("단기 상승 추세 (SMA5 > SMA20)")
    else:
        score -= 1
        reasons.append("단기 하락 추세 (SMA5 < SMA20)")

    # 2. RSI
    rsi = last["RSI"]
    if rsi < p["rsi_oversold"]:
        score += 2
        reasons.append(f"RSI 과매도 구간 ({rsi:.1f})")
    elif rsi > p["rsi_overbought"]:
        score -= 2
        reasons.append(f"RSI 과매수 구간 ({rsi:.1f})")
    else:
        reasons.append(f"RSI 중립 ({rsi:.1f})")

    # 3. MACD
    if last["MACD"] > last["MACD_Signal"] and prev["MACD"] <= prev["MACD_Signal"]:
        score += 2
        reasons.append("MACD 매수 신호 (MACD > Signal 전환)")
    elif last["MACD"] < last["MACD_Signal"] and prev["MACD"] >= prev["MACD_Signal"]:
        score -= 2
        reasons.append("MACD 매도 신호 (MACD < Signal 전환)")
    elif last["MACD_Hist"] > 0:
        score += 1
        reasons.append("MACD 히스토그램 양수")
    else:
        score -= 1
        reasons.append("MACD 히스토그램 음수")

    # 4. 볼린저 밴드
    if last["Close"] < last["BB_Lower"]:
        score += 2
        reasons.append("볼린저 하단 돌파 (반등 기대)")
    elif last["Close"] > last["BB_Upper"]:
        score -= 1
        reasons.append("볼린저 상단 돌파 (과열 주의)")

    # 5. 거래량
    if last["Volume_Ratio"] > 2.0:
        reasons.append(f"거래량 급증 (평균 대비 {last['Volume_Ratio']:.1f}배)")
        if score > 0:
            score += 1

    # 최종 신호 결정
    if score >= 3:
        signal = "STRONG_BUY"
    elif score >= 1:
        signal = "BUY"
    elif score <= -3:
        signal = "STRONG_SELL"
    elif score <= -1:
        signal = "SELL"
    else:
        signal = "HOLD"

    return {"signal": signal, "score": score, "reasons": reasons}


def analyze_stock(df: pd.DataFrame) -> dict:
    """종목 분석 전체 프로세스를 실행한다."""
    df = add_indicators(df)
    signal = generate_signal(df)
    last = df.iloc[-1]

    return {
        "signal": signal,
        "indicators": {
            "SMA5": round(last["SMA5"], 0) if pd.notna(last["SMA5"]) else None,
            "SMA20": round(last["SMA20"], 0) if pd.notna(last["SMA20"]) else None,
            "RSI": round(last["RSI"], 2) if pd.notna(last["RSI"]) else None,
            "MACD": round(last["MACD"], 2) if pd.notna(last["MACD"]) else None,
            "MACD_Signal": round(last["MACD_Signal"], 2) if pd.notna(last["MACD_Signal"]) else None,
            "BB_Upper": round(last["BB_Upper"], 0) if pd.notna(last["BB_Upper"]) else None,
            "BB_Lower": round(last["BB_Lower"], 0) if pd.notna(last["BB_Lower"]) else None,
            "Volume_Ratio": round(last["Volume_Ratio"], 2) if pd.notna(last["Volume_Ratio"]) else None,
        },
    }
