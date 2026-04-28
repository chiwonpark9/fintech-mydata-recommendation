"""LangGraph 기반 주식 매매 에이전트 그래프 정의"""

from langgraph.graph import StateGraph, START, END
from .state import TradingState
from .nodes import (
    data_collection_node,
    technical_analysis_node,
    chart_generation_node,
    recommendation_node,
    trading_decision_node,
)


def build_trading_graph() -> StateGraph:
    """
    주식 매매 에이전트 그래프를 구성한다.

    플로우:
        START
          ↓
        데이터 수집 (data_collection)
          ↓
        기술적 분석 (technical_analysis)
          ↓
        차트 생성 (chart_generation)
          ↓
        매매 추천 (recommendation)
          ↓
        매매 결정 (trading_decision)
          ↓
        END
    """
    graph = StateGraph(TradingState)

    # 노드 등록
    graph.add_node("data_collection", data_collection_node)
    graph.add_node("technical_analysis", technical_analysis_node)
    graph.add_node("chart_generation", chart_generation_node)
    graph.add_node("recommendation", recommendation_node)
    graph.add_node("trading_decision", trading_decision_node)

    # 엣지 연결 (순차적 파이프라인)
    graph.add_edge(START, "data_collection")
    graph.add_edge("data_collection", "technical_analysis")
    graph.add_edge("technical_analysis", "chart_generation")
    graph.add_edge("chart_generation", "recommendation")
    graph.add_edge("recommendation", "trading_decision")
    graph.add_edge("trading_decision", END)

    return graph.compile()
