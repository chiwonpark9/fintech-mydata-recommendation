"""LangGraph 상태 정의"""

from typing import TypedDict, Annotated
from langgraph.graph.message import add_messages


class TradingState(TypedDict):
    """주식 매매 에이전트의 상태"""
    messages: Annotated[list, add_messages]
    stock_data: dict           # 수집된 주식 데이터
    analysis_results: list     # 기술적 분석 결과
    recommendations: dict      # 최종 추천 결과
    portfolio: dict            # 현재 포트폴리오 상태
    trade_decisions: list      # 매매 결정 내역
    phase: str                 # 현재 처리 단계
