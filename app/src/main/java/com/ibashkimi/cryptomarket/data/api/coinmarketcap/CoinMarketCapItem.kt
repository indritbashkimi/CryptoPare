package com.ibashkimi.cryptomarket.data.api.coinmarketcap

data class CoinMarketCapItem(
        val id: String,
        val name: String,
        val symbol: String,
        val rank: String,
        val price_usd: String,
        val price_btc: String,
        val market_cap_usd: String,
        val percent_change_1h: String?,
        val percent_change_24h: String?,
        val percent_change_7d: String?,
        val last_updated: String,

        var price_aud: String?,
        var price_brl: String?,
        var price_cad: String?,
        var price_chf: String?,
        var price_clp: String?,
        var price_cny: String?,
        var price_czk: String?,
        var price_dkk: String?,
        var price_eur: String?,
        var price_gbp: String?,
        var price_hkd: String?,
        var price_huf: String?,
        var price_idr: String?,
        var price_ils: String?,
        var price_inr: String?,
        var price_jpy: String?,
        var price_krw: String?,
        var price_mxn: String?,
        var price_myr: String?,
        var price_nok: String?,
        var price_nzd: String?,
        var price_php: String?,
        var price_pkr: String?,
        var price_pln: String?,
        var price_rub: String?,
        var price_sek: String?,
        var price_sgd: String?,
        var price_thb: String?,
        var price_try: String?,
        var price_twd: String?,
        var price_zar: String?,

        var market_cap_aud: String?,
        var market_cap_brl: String?,
        var market_cap_cad: String?,
        var market_cap_chf: String?,
        var market_cap_clp: String?,
        var market_cap_cny: String?,
        var market_cap_czk: String?,
        var market_cap_dkk: String?,
        var market_cap_eur: String?,
        var market_cap_gbp: String?,
        var market_cap_hkd: String?,
        var market_cap_huf: String?,
        var market_cap_idr: String?,
        var market_cap_ils: String?,
        var market_cap_inr: String?,
        var market_cap_jpy: String?,
        var market_cap_krw: String?,
        var market_cap_mxn: String?,
        var market_cap_myr: String?,
        var market_cap_nok: String?,
        var market_cap_nzd: String?,
        var market_cap_php: String?,
        var market_cap_pkr: String?,
        var market_cap_pln: String?,
        var market_cap_rub: String?,
        var market_cap_sek: String?,
        var market_cap_sgd: String?,
        var market_cap_thb: String?,
        var market_cap_try: String?,
        var market_cap_twd: String?,
        var market_cap_zar: String?
)
