package com.croin.croin

import android.arch.lifecycle.Observer
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.croin.croin.adapters.CurrencyAdapter
import com.croin.croin.models.CurrencyViewModel
import com.croin.croin.network.CurrencyData
import kotlinx.android.synthetic.main.fragment_currency.*
import com.croin.croin.utilities.CURRENCY_DATA_FILENAME
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.io.IOException
import android.arch.lifecycle.ViewModelProviders
import com.croin.croin.database.entity.Currency


/**
 * @author Maricel Bros Maimó
 *
 * CurrencySettingsFragment subclass.
 *
 */
class CurrencySettingsFragment : Fragment(), View.OnClickListener, AdapterView.OnItemSelectedListener, CurrencyAdapter.OnItemClickListener  {

    private var spContent: MutableList<CurrencyData> = arrayListOf()
    private var selectedCurrency: CurrencyData? = null
    private lateinit var currencyViewModel: CurrencyViewModel

    /**
     * Overrides onCreateView default function from fragment behaviour.
     *
     * @param LayoutInflater
     * @param ViewGroup?
     * @param Bundle?
     *
     * @return View?
     */
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        // Inflate the layout for this fragment
        val viewCurrency: View = inflater!!.inflate(R.layout.fragment_currency, container, false)

        // Image button Add Currency
        val ibAddCurrency: ImageButton = viewCurrency.findViewById(R.id.ibAddCurrency)

        ibAddCurrency.minimumHeight = 32
        ibAddCurrency.minimumWidth = 28

        ibAddCurrency.setOnClickListener(this)


        //Currencies spinner
        val spCurrencies: Spinner = viewCurrency.findViewById(R.id.spCurrencies)
        spContent = getCurrenciesFromJSon()

        val aCurrencies = ArrayAdapter(activity, android.R.layout.simple_spinner_dropdown_item, spContent)
        aCurrencies.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCurrencies.adapter = aCurrencies

        spCurrencies.onItemSelectedListener = this

        //RecyclerView currencies list
        val recyclerView = viewCurrency.findViewById<RecyclerView>(R.id.rvCurrency)
        val adapter = CurrencyAdapter(activity, this@CurrencySettingsFragment)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        //CurrencyViewModel
        currencyViewModel = ViewModelProviders.of(this).get(CurrencyViewModel::class.java)

        currencyViewModel.allCurrencies.observe(this, Observer { currencies ->
            // Update the cached copy of the currencies in the adapter.
            currencies?.let { adapter.setCurrencies(it) }
        })


        return viewCurrency
    }


    /**
     * Gets a list of currency with id, symbol and name from a JSon stored at assets.
     *
     * @return MutableList<CurrencyData> a list of currencies sorted by name.
     *
     */
    private fun getCurrenciesFromJSon(): MutableList<CurrencyData> {

        val al: MutableList<CurrencyData> = mutableListOf()

        val jsonResponse = context.applicationContext.assets.open(CURRENCY_DATA_FILENAME).bufferedReader().use{
            it.readText()
        }

        val mCurrencyMoshi = Moshi.Builder().build()

        val type = Types.newParameterizedType(List::class.java, CurrencyData::class.java)
        val jsonAdapter: JsonAdapter<List<CurrencyData>> = mCurrencyMoshi.adapter(type)


        try {
            val currencies = jsonAdapter.fromJson(jsonResponse)
            for (i in currencies!!.indices) {
                al.add(currencies[i])
            }

            al.sortBy { it.name }


        } catch (e: IOException) {
            //e.printStackTrace()
        }

        return al
    }

    /**
     * Overrides onClick function from add currency button.
     * Adds selected currency from spinner to recycler view.
     *
     * @param View?
     *
     */
    override fun onClick(v: View?) {
        when (v) {
            ibAddCurrency -> {

                lateinit var dialog:AlertDialog

                val builder = AlertDialog.Builder(activity)
                builder.setTitle(getString(R.string.dialog_add_title))
                builder.setMessage("${getString(R.string.dialog_add_description)} ${selectedCurrency.toString()}?")

                val dialogClickListener = DialogInterface.OnClickListener{_,which ->
                    when(which){
                        DialogInterface.BUTTON_POSITIVE -> {
                            var symbol: String?
                            symbol = selectedCurrency!!.symbol

                            val currency = Currency(selectedCurrency!!.iso, selectedCurrency!!.name, symbol, false)
                            currencyViewModel.insert(currency)
                        }
                    }
                }

                builder.setPositiveButton(R.string.dialog_yes,dialogClickListener)
                builder.setNeutralButton(R.string.dialog_cancel,dialogClickListener)

                dialog = builder.create()
                dialog.show()

            }
        }
    }


    /**
     * Overrides onItemSelected default function from spinner.
     * Save current currency selected.
     *
     * @param AdapterView<*>
     * @param View
     * @param Int position
     * @param Long identifier
     *
     */
    override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
        selectedCurrency = parent.selectedItem as CurrencyData
    }


    /**
     * Overrides onNothingSelected default function from spinner.
     *
     */
    override fun onNothingSelected(parent: AdapterView<*>) {
        // Nothing to do here.
    }


    /**
     * Overrides onDeleteClick from the delete button of recyclerView item
     * Deletes a currency from the showed list.
     *
     * @param Currency entity currency from database.
     *
     */
    override fun onDeleteClick(currency: Currency) {

        lateinit var dialog:AlertDialog

        val builder = AlertDialog.Builder(activity)
        builder.setTitle(getString(R.string.dialog_delete_title))
        builder.setMessage("${getString(R.string.dialog_delete_description)} ${currency.name}?")

        val dialogClickListener = DialogInterface.OnClickListener{_,which ->
            when(which){
                DialogInterface.BUTTON_POSITIVE -> {
                    currencyViewModel.delete(currency)
                }
            }
        }

        builder.setPositiveButton(R.string.dialog_yes,dialogClickListener)
        builder.setNeutralButton(R.string.dialog_cancel,dialogClickListener)

        dialog = builder.create()
        dialog.show()
    }


    /**
     * Overrides onItemClick from the favorite button of recyclerView item
     * Add or delete currency favorite state.
     *
     * @param Currency entity currency from database.
     *
     */
    override fun onItemClick(currency: Currency) {

        lateinit var dialog:AlertDialog

        val builder = AlertDialog.Builder(activity)
        builder.setTitle(getString(R.string.dialog_fav_title))
        builder.setMessage("${getString(R.string.dialog_fav_description)} ${currency.name} ?")

        val dialogClickListener = DialogInterface.OnClickListener{_,which ->
            when(which){
                DialogInterface.BUTTON_POSITIVE -> {
                    currencyViewModel.noFavorites()
                    val currencyUpdated = Currency(currency.id, currency.name, currency.symbol, !currency.preferred)
                    currencyViewModel.update(currencyUpdated)
                }
            }
        }

        builder.setPositiveButton(R.string.dialog_yes,dialogClickListener)
        builder.setNeutralButton(R.string.dialog_cancel,dialogClickListener)

        dialog = builder.create()
        dialog.show()
    }


}






