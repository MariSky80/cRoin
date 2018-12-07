package com.croin.croin.network

data class CurrencyMoshi (
    val name: String,
    val iso: String,
    val symbol: String
) {
    override fun toString(): String {
        return "$name ($iso)"
    }
}


