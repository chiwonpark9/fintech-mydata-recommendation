"""분석 결과 리포트 출력 모듈"""


def print_report(state: dict):
    """최종 분석 결과를 보기 좋게 출력한다."""
    recommendations = state.get("recommendations", {})
    decisions = state.get("trade_decisions", [])
    portfolio = state.get("portfolio", {})

    print("\n" + "=" * 70)
    print(f"  📋 주식 매매 분석 리포트 | {recommendations.get('date', '')}")
    print("=" * 70)

    summary = recommendations.get("summary", {})
    print(f"\n  분석 종목: {summary.get('total', 0)}개")
    print(f"  매수 신호: {summary.get('buy', 0)}개  |  매도 신호: {summary.get('sell', 0)}개  |  관망: {summary.get('hold', 0)}개")

    # 매수 추천
    buy_list = recommendations.get("buy", [])
    if buy_list:
        print(f"\n{'─' * 70}")
        print("  🟢 매수 추천 종목")
        print(f"{'─' * 70}")
        print(f"  {'종목명':<12} {'코드':<10} {'현재가':>10} {'등락률':>8} {'신호':>12} {'점수':>5} {'RSI':>6}")
        print(f"  {'─' * 65}")
        for s in buy_list:
            indicators = s.get("indicators", {})
            rsi = indicators.get("RSI", 0) if isinstance(indicators, dict) else 0
            print(f"  {s['name']:<12} {s['ticker']:<10} {s['close']:>10,} {s['change_pct']:>+7.2f}% {s['signal']:>12} {s['score']:>5} {rsi:>6.1f}")
        print()
        for s in buy_list[:3]:
            print(f"  📌 {s['name']}: {', '.join(s['reasons'][:3])}")

    # 매도 추천
    sell_list = recommendations.get("sell", [])
    if sell_list:
        print(f"\n{'─' * 70}")
        print("  🔴 매도 추천 종목")
        print(f"{'─' * 70}")
        print(f"  {'종목명':<12} {'코드':<10} {'현재가':>10} {'등락률':>8} {'신호':>12} {'점수':>5}")
        print(f"  {'─' * 65}")
        for s in sell_list:
            print(f"  {s['name']:<12} {s['ticker']:<10} {s['close']:>10,} {s['change_pct']:>+7.2f}% {s['signal']:>12} {s['score']:>5}")

    # 매매 결정
    if decisions:
        print(f"\n{'─' * 70}")
        print("  📝 매매 결정")
        print(f"{'─' * 70}")
        for d in decisions:
            action_emoji = "🟢" if d["action"] == "BUY" else "🔴"
            print(f"  {action_emoji} {d['action']} | {d['name']}({d['ticker']}) | {d['price']:,}원 x {d['quantity']}주 = {d['total']:,}원")

    # 포트폴리오
    print(f"\n{'─' * 70}")
    print("  💰 포트폴리오 현황")
    print(f"{'─' * 70}")
    print(f"  투자 가능 금액: {portfolio.get('cash', 0):,}원")
    print(f"  총 평가 금액: {portfolio.get('total_value', 0):,}원")

    print("\n" + "=" * 70)
    print("  ⚠️  본 분석은 기술적 지표 기반 참고용이며, 투자 판단은 본인의 책임입니다.")
    print("=" * 70 + "\n")
