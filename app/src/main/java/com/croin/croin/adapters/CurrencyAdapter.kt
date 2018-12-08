package com.croin.croin.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.croin.croin.R
import com.croin.croin.database.entity.Currency

class CurrencyAdapter internal constructor(context: Context) : RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var currencies = emptyList<Currency>()

    inner class CurrencyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val currencyItemView: TextView = itemView.findViewById(R.id.tvCurrency)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val itemView = inflater.inflate(R.layout.currency_item, parent, false)
        return CurrencyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val current = currencies[position]
        when (current.symbol.isNullOrEmpty()) {
            true -> holder.currencyItemView.text = "${current.name} - ${current.id}"
            false -> holder.currencyItemView.text = "${current.name} - ${current.id} (${current.symbol})"
        }

    }

    internal fun setCurrencies(currencies: List<Currency>) {
        this.currencies = currencies
        notifyDataSetChanged()
    }

    override fun getItemCount() = currencies.size
}
