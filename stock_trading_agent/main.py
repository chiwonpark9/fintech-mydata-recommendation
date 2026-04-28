"""
주식 매매 에이전트 - 메인 실행 파일

LangGraph 기반으로 국내 주식 데이터를 수집하고,
기술적 분석을 수행하여 매수/매도 추천을 생성합니다.

사용법:
    python -m stock_trading_agent.main
"""

import warnings
warnings.filterwarnings("ignore")

from .agents.graph import build_trading_graph
from .utils.report import print_report
from .config.settings import TRADING_RULES


def main():
    print("🚀 주식 매매 에이전트를 시작합니다...")
    print(f"   초기 투자금: {TRADING_RULES['initial_capital']:,}원")
    print(f"   최대 보유 종목: {TRADING_RULES['max_stocks']}개")
    print(f"   손절 기준: {TRADING_RULES['stop_loss_pct']*100:.0f}% | 익절 기준: {TRADING_RULES['take_profit_pct']*100:.0f}%")

    # LangGraph 그래프 컴파일 및 실행
    graph = build_trading_graph()

    initial_state = {
        "messages": [],
        "stock_data": {},
        "analysis_results": [],
        "recommendations": {},
        "portfolio": {
            "cash": TRADING_RULES["initial_capital"],
            "holdings": {},
            "total_value": TRADING_RULES["initial_capital"],
        },
        "trade_decisions": [],
        "phase": "init",
    }

    # 그래프 실행
    final_state = graph.invoke(initial_state)

    # 결과 리포트 출력
    print_report(final_state)

    return final_state


if __name__ == "__main__":
    main()
