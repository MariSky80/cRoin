package com.croin.croin.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.croin.croin.R
import com.croin.croin.database.entity.Currency


/**
 * @author Maricel Bros Maimó
 *
 * Recognitoin Adapter class.
 * Class to show a list of currencies with a recycler view.
 *
 */
class CurrencyAdapter internal constructor(context: Context, listener: OnItemClickListener) : RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {


    private var listenerDeleteButton: OnItemClickListener = listener
    /**
     * Interfave OnTiemClickListener that creates listeners from itemView items.
     */
    interface OnItemClickListener {
        fun onDeleteClick(currency: Currency)
        fun onItemClick(currency: Currency)
    }


    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var currencies = emptyList<Currency>()
    private val cContext = context


    /**
     * Inner class that contains each recognition itemView.
     *
     * @param RecyclerView.itemView(itemView)
     */
    inner class CurrencyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val currencyItemView: TextView = itemView.findViewById(R.id.tvCurrency)
        val currencyItemFav: ImageButton = itemView.findViewById(R.id.ibFav)
        private val currencyItemDelete: ImageButton = itemView.findViewById(R.id.ibDelete)
        fun bind(currency: Currency, listener: OnItemClickListener) {
            currencyItemDelete.setOnClickListener {
                listener.onDeleteClick(currency)
            }
            currencyItemFav.setOnClickListener{
                listener.onItemClick(currency)
            }
        }
    }


    /**
     * Overrides function onCreateViewHolder from system.
     *
     * @param ViewGroup
     * @param Int viewType
     *
     * @return CurrencyViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val itemView = inflater.inflate(R.layout.currency_item, parent, false)
        return CurrencyViewHolder(itemView)
    }


    /**
     * Overrides function onBindViewHolder from system.
     * Binds information of current item from viewHolder.
     *
     * @param RecognitonViewHolder
     * @param Int position
     *
     */
    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val current = currencies[position]

        when (current.symbol.isNullOrEmpty()) {
            true -> holder.currencyItemView.text = "${current.name} - ${current.id}"
            false -> holder.currencyItemView.text = "${current.name} - ${current.id} (${current.symbol})"
        }

        when (current.preferred) {
            true -> {
                holder.currencyItemFav.setImageResource(R.drawable.ic_star_solid)
                holder.currencyItemFav.contentDescription = cContext.getString(R.string.fav_description)
                Unit

            }
            false -> {
                holder.currencyItemFav.setImageResource(R.drawable.ic_star_regular)
                holder.currencyItemFav.contentDescription = cContext.getString(R.string.not_fav_description)
                Unit
            }
        }

        holder.bind(current, listenerDeleteButton)

    }


    /**
     * Set currencies to this.currencies.
     *
     * @param List<Currency> list of currencies entities.
     *
     */
    internal fun setCurrencies(currencies: List<Currency>) {
        this.currencies = currencies
        notifyDataSetChanged()
    }


    /**
     * Override function getItemCount from system.
     */
    override fun getItemCount() = currencies.size
}
