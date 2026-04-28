"""차트 생성 모듈"""

import matplotlib
matplotlib.use("Agg")
import matplotlib.pyplot as plt
import matplotlib.dates as mdates
import mplfinance as mpf
import pandas as pd
import os

CHART_DIR = os.path.join(os.path.dirname(os.path.dirname(__file__)), "output_charts")


def plot_stock_chart(df: pd.DataFrame, ticker: str, name: str, analysis: dict) -> str:
    """종목의 1일 캔들 차트 + 기술적 지표를 생성하고 파일로 저장한다."""
    os.makedirs(CHART_DIR, exist_ok=True)

    # mplfinance용 인덱스 설정
    plot_df = df.copy()
    plot_df.index = pd.to_datetime(plot_df.index)
    plot_df.index.name = "Date"

    # 이동평균선을 위한 추가 플롯
    add_plots = []

    if "SMA5" in plot_df.columns and plot_df["SMA5"].notna().any():
        add_plots.append(mpf.make_addplot(plot_df["SMA5"], color="orange", width=1, label="SMA5"))
    if "SMA20" in plot_df.columns and plot_df["SMA20"].notna().any():
        add_plots.append(mpf.make_addplot(plot_df["SMA20"], color="blue", width=1, label="SMA20"))
    if "BB_Upper" in plot_df.columns and plot_df["BB_Upper"].notna().any():
        add_plots.append(mpf.make_addplot(plot_df["BB_Upper"], color="gray", linestyle="--", width=0.7))
        add_plots.append(mpf.make_addplot(plot_df["BB_Lower"], color="gray", linestyle="--", width=0.7))

    # RSI 서브플롯
    if "RSI" in plot_df.columns and plot_df["RSI"].notna().any():
        add_plots.append(mpf.make_addplot(plot_df["RSI"], panel=2, color="purple", ylabel="RSI"))

    # MACD 서브플롯
    if "MACD" in plot_df.columns and plot_df["MACD"].notna().any():
        add_plots.append(mpf.make_addplot(plot_df["MACD"], panel=3, color="blue", ylabel="MACD"))
        add_plots.append(mpf.make_addplot(plot_df["MACD_Signal"], panel=3, color="red"))
        colors = ["green" if v >= 0 else "red" for v in plot_df["MACD_Hist"].fillna(0)]
        add_plots.append(mpf.make_addplot(plot_df["MACD_Hist"], panel=3, type="bar", color=colors))

    signal_info = analysis.get("signal", {})
    signal_text = signal_info.get("signal", "N/A")
    score = signal_info.get("score", 0)
    title = f"{name}({ticker}) | Signal: {signal_text} (Score: {score})"

    style = mpf.make_mpf_style(
        base_mpf_style="charles",
        rc={"font.size": 8},
    )

    filepath = os.path.join(CHART_DIR, f"{ticker}_{name}.png")

    fig, axes = mpf.plot(
        plot_df,
        type="candle",
        style=style,
        title=title,
        volume=True,
        addplot=add_plots if add_plots else None,
        figsize=(14, 10),
        returnfig=True,
    )

    fig.savefig(filepath, dpi=150, bbox_inches="tight")
    plt.close(fig)

    return filepath
