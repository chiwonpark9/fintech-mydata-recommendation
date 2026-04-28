"""LangGraph 에이전트용 주식 분석 도구 모듈"""

from ..data.fetcher import fetch_stock_data, get_latest_price, fetch_all_stocks
from ..strategies.technical import add_indicators, analyze_stock
from ..charts.plotter import plot_stock_chart
from ..config.settings import STOCK_UNIVERSE


def tool_fetch_and_analyze(ticker: str) -> dict:
    """종목 데이터를 가져와서 기술적 분석을 수행한다."""
    name = STOCK_UNIVERSE.get(ticker, ticker)
    df = fetch_stock_data(ticker)
    df = add_indicators(df)
    analysis = analyze_stock(df)
    price = get_latest_price(df)
    chart_path = plot_stock_chart(df, ticker, name, analysis)

    return {
        "ticker": ticker,
        "name": name,
        "price": price,
        "analysis": analysis,
        "chart_path": chart_path,
    }


def tool_scan_universe() -> list[dict]:
    """전체 유니버스를 스캔하여 매매 신호가 있는 종목을 찾는다."""
    all_data = fetch_all_stocks()
    results = []

    for ticker, df in all_data.items():
        name = STOCK_UNIVERSE.get(ticker, ticker)
        df = add_indicators(df)
        analysis = analyze_stock(df)
        price = get_latest_price(df)

        results.append({
            "ticker": ticker,
            "name": name,
            "close": price["close"],
            "change_pct": price["change_pct"],
            "signal": analysis["signal"]["signal"],
            "score": analysis["signal"]["score"],
            "reasons": analysis["signal"]["reasons"],
            "rsi": analysis["indicators"]["RSI"],
        })

    results.sort(key=lambda x: x["score"], reverse=True)
    return results


def tool_recommend_stocks() -> dict:
    """주식 추천 결과를 생성한다."""
    scanned = tool_scan_universe()

    buy_candidates = [s for s in scanned if s["signal"] in ("BUY", "STRONG_BUY")]
    sell_candidates = [s for s in scanned if s["signal"] in ("SELL", "STRONG_SELL")]
    hold_candidates = [s for s in scanned if s["signal"] == "HOLD"]

    # 상위 매수 추천 종목 차트 생성
    chart_paths = []
    for stock in buy_candidates[:5]:
        try:
            result = tool_fetch_and_analyze(stock["ticker"])
            chart_paths.append(result["chart_path"])
        except Exception:
            pass

    return {
        "buy": buy_candidates,
        "sell": sell_candidates,
        "hold": hold_candidates,
        "charts": chart_paths,
        "summary": {
            "total_analyzed": len(scanned),
            "buy_count": len(buy_candidates),
            "sell_count": len(sell_candidates),
            "hold_count": len(hold_candidates),
        },
    }
