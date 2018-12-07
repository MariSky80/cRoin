package com.croin.croin.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.croin.croin.R
import com.croin.croin.network.CurrencyMoshi
import kotlinx.android.synthetic.main.currency_item.view.*

class CurrencyAdapter(val context: Context, val items: List<CurrencyMoshi>) : RecyclerView.Adapter<CurrencyAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.currency_item, parent, false))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemName.text = items[position].toString()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemName = view.tvCurrency
    }
}