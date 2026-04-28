"""주식 매매 에이전트 설정"""

# 분석 대상 주요 종목 (코드: 이름)
STOCK_UNIVERSE = {
    "005930": "삼성전자",
    "000660": "SK하이닉스",
    "373220": "LG에너지솔루션",
    "207940": "삼성바이오로직스",
    "005380": "현대자동차",
    "000270": "기아",
    "068270": "셀트리온",
    "035420": "NAVER",
    "035720": "카카오",
    "051910": "LG화학",
    "006400": "삼성SDI",
    "028260": "삼성물산",
    "105560": "KB금융",
    "055550": "신한지주",
    "012330": "현대모비스",
    "066570": "LG전자",
    "003670": "포스코퓨처엠",
    "086790": "하나금융지주",
    "034730": "SK",
    "032830": "삼성생명",
}

# 기술적 분석 파라미터
TECHNICAL_PARAMS = {
    "sma_short": 5,
    "sma_long": 20,
    "rsi_period": 14,
    "rsi_oversold": 30,
    "rsi_overbought": 70,
    "macd_fast": 12,
    "macd_slow": 26,
    "macd_signal": 9,
    "bollinger_period": 20,
    "bollinger_std": 2,
    "volume_ma_period": 20,
}

# 매매 규칙
TRADING_RULES = {
    "max_position_pct": 0.2,       # 포트폴리오 대비 최대 포지션 비율
    "stop_loss_pct": -0.05,        # 손절 기준 (-5%)
    "take_profit_pct": 0.10,       # 익절 기준 (+10%)
    "initial_capital": 10_000_000, # 초기 투자금 (1000만원)
    "max_stocks": 5,               # 최대 보유 종목 수
}

# 데이터 수집 기간
DATA_PERIOD_DAYS = 60
