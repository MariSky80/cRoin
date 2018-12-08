package com.croin.croin.network

data class CurrencyData (
    val name: String,
    val iso: String,
    val symbol: String
) {
    override fun toString(): String {
        when (symbol.isNullOrEmpty()) {
            true -> return name
            false -> return "$name ($symbol)"
        }
    }
}


