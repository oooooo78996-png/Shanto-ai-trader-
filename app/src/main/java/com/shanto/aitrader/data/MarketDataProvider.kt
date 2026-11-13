package com.shanto.aitrader.data

interface MarketDataProvider {
    suspend fun fetchCandles(
        symbol: String,
        timeframe: Timeframe,
        mode: MarketMode,
        limit: Int = 150
    ): MarketDataResult
}
