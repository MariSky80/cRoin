package com.croin.croin.network

/**
 * Currency custom object to fill currency spinner from CurrencySettingsFragment.
 *
 * @param String name
 * @param String iso (id)
 * @param String symbol
 */
data class CurrencyData (
    val name: String,
    val iso: String,
    val symbol: String
) {
    /**
     * Override toString.
     * Converts to string in name and symbol if it exists.
     */
    override fun toString(): String {
        when (symbol.isNullOrEmpty()) {
            true -> return name
            false -> return "$name ($symbol)"
        }
    }
}


