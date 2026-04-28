"""LangGraph 노드 함수들 - 각 노드가 매매 파이프라인의 한 단계를 담당한다."""

from datetime import datetime
from ..data.fetcher import fetch_all_stocks, get_latest_price
from ..strategies.technical import add_indicators, analyze_stock
from ..charts.plotter import plot_stock_chart
from ..config.settings import STOCK_UNIVERSE, TRADING_RULES


def data_collection_node(state: dict) -> dict:
    """1단계: 전체 유니버스 종목의 주가 데이터를 수집한다."""
    print("\n📊 [1/5] 주식 데이터 수집 중...")
    all_data = fetch_all_stocks()
    print(f"   ✅ {len(all_data)}개 종목 데이터 수집 완료")
    return {
        "stock_data": {ticker: df.to_dict() for ticker, df in all_data.items()},
        "phase": "data_collected",
    }


def technical_analysis_node(state: dict) -> dict:
    """2단계: 수집된 데이터에 기술적 분석을 적용한다."""
    import pandas as pd

    print("\n🔍 [2/5] 기술적 분석 수행 중...")
    stock_data = state.get("stock_data", {})
    results = []

    for ticker, data_dict in stock_data.items():
        name = STOCK_UNIVERSE.get(ticker, ticker)
        try:
            df = pd.DataFrame(data_dict)
            df.index = pd.to_datetime(df.index)
            df = add_indicators(df)
            analysis = analyze_stock(df)
            price = get_latest_price(df)

            results.append({
                "ticker": ticker,
                "name": name,
                "close": price["close"],
                "change_pct": price["change_pct"],
                "volume": price["volume"],
                "signal": analysis["signal"]["signal"],
                "score": analysis["signal"]["score"],
                "reasons": analysis["signal"]["reasons"],
                "indicators": analysis["indicators"],
            })
        except Exception as e:
            print(f"   ⚠️ {name}({ticker}) 분석 실패: {e}")

    results.sort(key=lambda x: x["score"], reverse=True)
    print(f"   ✅ {len(results)}개 종목 분석 완료")
    return {
        "analysis_results": results,
        "phase": "analyzed",
    }


def chart_generation_node(state: dict) -> dict:
    """3단계: 주요 종목의 차트를 생성한다."""
    import pandas as pd

    print("\n📈 [3/5] 차트 생성 중...")
    analysis_results = state.get("analysis_results", [])
    stock_data = state.get("stock_data", {})

    chart_paths = []
    # 상위 10개 종목 차트 생성
    for result in analysis_results[:10]:
        ticker = result["ticker"]
        if ticker in stock_data:
            try:
                df = pd.DataFrame(stock_data[ticker])
                df.index = pd.to_datetime(df.index)
                df = add_indicators(df)
                analysis = {"signal": {"signal": result["signal"], "score": result["score"]}}
                path = plot_stock_chart(df, ticker, result["name"], analysis)
                chart_paths.append(path)
                print(f"   📊 {result['name']} 차트 저장: {path}")
            except Exception as e:
                print(f"   ⚠️ {result['name']} 차트 생성 실패: {e}")

    print(f"   ✅ {len(chart_paths)}개 차트 생성 완료")
    return {"phase": "charts_generated"}


def recommendation_node(state: dict) -> dict:
    """4단계: 분석 결과를 기반으로 매수/매도 추천을 생성한다."""
    print("\n💡 [4/5] 매매 추천 생성 중...")
    results = state.get("analysis_results", [])

    buy_list = [r for r in results if r["signal"] in ("BUY", "STRONG_BUY")]
    sell_list = [r for r in results if r["signal"] in ("SELL", "STRONG_SELL")]
    hold_list = [r for r in results if r["signal"] == "HOLD"]

    recommendations = {
        "date": datetime.now().strftime("%Y-%m-%d %H:%M"),
        "buy": buy_list,
        "sell": sell_list,
        "hold": hold_list,
        "top_picks": buy_list[:3],
        "summary": {
            "total": len(results),
            "buy": len(buy_list),
            "sell": len(sell_list),
            "hold": len(hold_list),
        },
    }

    print(f"   ✅ 매수 추천: {len(buy_list)}종목, 매도 추천: {len(sell_list)}종목")
    return {
        "recommendations": recommendations,
        "phase": "recommended",
    }


def trading_decision_node(state: dict) -> dict:
    """5단계: 포트폴리오를 고려한 최종 매매 결정을 내린다."""
    print("\n🎯 [5/5] 매매 결정 중...")
    recommendations = state.get("recommendations", {})
    portfolio = state.get("portfolio", {
        "cash": TRADING_RULES["initial_capital"],
        "holdings": {},
        "total_value": TRADING_RULES["initial_capital"],
    })

    decisions = []
    capital = portfolio["cash"]
    max_per_stock = capital * TRADING_RULES["max_position_pct"]

    # 매수 결정
    for stock in recommendations.get("top_picks", []):
        if len(portfolio.get("holdings", {})) >= TRADING_RULES["max_stocks"]:
            break

        price = stock["close"]
        qty = int(max_per_stock // price)
        if qty > 0 and capital >= price * qty:
            decisions.append({
                "action": "BUY",
                "ticker": stock["ticker"],
                "name": stock["name"],
                "price": price,
                "quantity": qty,
                "total": price * qty,
                "reason": stock["reasons"],
                "score": stock["score"],
            })
            capital -= price * qty

    # 매도 결정 (보유 종목 중 매도 신호)
    sell_tickers = {s["ticker"] for s in recommendations.get("sell", [])}
    for ticker, holding in portfolio.get("holdings", {}).items():
        if ticker in sell_tickers:
            decisions.append({
                "action": "SELL",
                "ticker": ticker,
                "name": STOCK_UNIVERSE.get(ticker, ticker),
                "price": holding.get("current_price", 0),
                "quantity": holding.get("quantity", 0),
                "reason": ["매도 신호 발생"],
            })

    print(f"   ✅ 총 {len(decisions)}건 매매 결정 완료")
    return {
        "trade_decisions": decisions,
        "portfolio": portfolio,
        "phase": "decided",
    }
