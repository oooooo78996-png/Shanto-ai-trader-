package com.shanto.aitrader.data

enum class Timeframe(val label: String, val binanceInterval: String, val seconds: Int) {
    TF_30S("30s", "1m", 30),
    TF_1M("1m", "1m", 60),
    TF_5M("5m", "5m", 300)
}

enum class MarketMode(val label: String) {
    REGULAR("REGULAR"),
    OTC("OTC"),
    SIMULATED("SIMULATED (EDU)")
}

enum class DataFeedQuality(val badgeText: String, val isReliable: Boolean) {
    LIVE("LIVE FEED • AUTHORIZED", true),
    AGGREGATED_30S("LIVE 1M BASE • 30S SUB-WINDOW", true),
    DELAYED("DELAYED FEED (>15s)", true),
    SIMULATED("SIMULATED • EDUCATIONAL ONLY", false),
    UNAVAILABLE("NO DATA • UNAVAILABLE", false)
}

enum class SignalBias(val titleEn: String, val titleBn: String) {
    BULLISH("BULLISH", "বুলিশ (ঊর্ধ্বমুখী সম্ভাবনা)"),
    BEARISH("BEARISH", "বিয়ারিশ (নিম্নমুখী সম্ভাবনা)"),
    NEUTRAL("NEUTRAL / NO TRADE", "নিরপেক্ষ / ট্রেড বিরতি")
}

data class Candle(
    val timestampMs: Long,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double,
    val volume: Double
) {
    val bodySize: Double get() = kotlin.math.abs(close - open)
    val totalRange: Double get() = (high - low).coerceAtLeast(1e-9)
    val upperWick: Double get() = high - maxOf(open, close)
    val lowerWick: Double get() = minOf(open, close) - low
    val isGreen: Boolean get() = close >= open
}

sealed class MarketDataResult {
    data class Available(
        val symbol: String,
        val timeframe: Timeframe,
        val mode: MarketMode,
        val candles: List<Candle>,
        val sourceName: String,
        val quality: DataFeedQuality,
        val fetchedAtMs: Long,
        val disclosureNote: String
    ) : MarketDataResult()

    data class Unavailable(
        val symbol: String,
        val timeframe: Timeframe,
        val mode: MarketMode,
        val reason: String,
        val reasonBn: String
    ) : MarketDataResult()
}

data class IndicatorSnapshot(
    val ema9: Double,
    val ema21: Double,
    val rsi14: Double,
    val macdLine: Double,
    val macdSignal: Double,
    val macdHistogram: Double,
    val bbUpper: Double,
    val bbMiddle: Double,
    val bbLower: Double,
    val atr14: Double,
    val nearestSupport: Double,
    val nearestResistance: Double,
    val candlePattern: String,
    val candlePatternBn: String,
    val momentumState: String,
    val volatilityState: String
)

data class CalibrationMetrics(
    val sampleCount: Int,
    val historicalWinRate: Double,
    val calibrationTier: String,
    val note: String
)

data class AnalysisReport(
    val id: Long = System.currentTimeMillis(),
    val symbol: String,
    val timeframe: Timeframe,
    val mode: MarketMode,
    val bias: SignalBias,
    val bullishScore: Int,
    val bearishScore: Int,
    val totalIndicators: Int,
    val indicators: IndicatorSnapshot?,
    val calibration: CalibrationMetrics?,
    val dataQuality: DataFeedQuality,
    val sourceLabel: String,
    val explanationBn: String,
    val candles: List<Candle>,
    val timestampMs: Long = System.currentTimeMillis()
)
