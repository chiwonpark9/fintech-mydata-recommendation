"""FinanceDataReader를 이용한 주식 데이터 수집"""

import FinanceDataReader as fdr
import pandas as pd
from datetime import datetime, timedelta
from ..config.settings import STOCK_UNIVERSE, DATA_PERIOD_DAYS


def fetch_stock_data(ticker: str, days: int = DATA_PERIOD_DAYS) -> pd.DataFrame:
    """개별 종목의 OHLCV 데이터를 가져온다."""
    end_date = datetime.now().strftime("%Y-%m-%d")
    start_date = (datetime.now() - timedelta(days=days)).strftime("%Y-%m-%d")
    df = fdr.DataReader(ticker, start_date, end_date)
    df.index = pd.to_datetime(df.index)
    return df


def fetch_all_stocks(tickers: dict = None) -> dict[str, pd.DataFrame]:
    """전체 유니버스 종목의 데이터를 수집한다."""
    if tickers is None:
        tickers = STOCK_UNIVERSE

    result = {}
    for code, name in tickers.items():
        try:
            df = fetch_stock_data(code)
            if not df.empty and len(df) >= 5:
                df["Name"] = name
                result[code] = df
        except Exception as e:
            print(f"[WARN] {name}({code}) 데이터 수집 실패: {e}")
    return result


def get_latest_price(df: pd.DataFrame) -> dict:
    """최신 가격 정보를 딕셔너리로 반환한다."""
    last = df.iloc[-1]
    prev = df.iloc[-2] if len(df) >= 2 else last
    change_pct = (last["Close"] - prev["Close"]) / prev["Close"] * 100
    return {
        "date": str(df.index[-1].date()),
        "open": int(last["Open"]),
        "high": int(last["High"]),
        "low": int(last["Low"]),
        "close": int(last["Close"]),
        "volume": int(last["Volume"]),
        "change_pct": round(change_pct, 2),
    }
