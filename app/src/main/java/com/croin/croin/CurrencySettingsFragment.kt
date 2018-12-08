package com.croin.croin


import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.Transformations
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
import android.util.Log
import com.croin.croin.database.entity.Currency


/**
 * @author Maricel Bros Maimó
 *
 * CurrencySettingsFragment subclass.
 *
 */
class CurrencySettingsFragment : Fragment(), View.OnClickListener, AdapterView.OnItemSelectedListener  {

    private var spContent: MutableList<CurrencyData> = arrayListOf()
    private var selectedCurrency: CurrencyData? = null
    private var rvwCurrency: RecyclerView? = null
    private lateinit var currencyViewModel: CurrencyViewModel

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val viewCurrency: View = inflater!!.inflate(R.layout.fragment_currency, container, false)


        rvwCurrency = viewCurrency.findViewById(R.id.rvCurrency)

        // Image button Add Currency
        val ibAddCurrency: ImageButton = viewCurrency.findViewById(R.id.ibAddCurrency)

        ibAddCurrency.minimumHeight = 32
        ibAddCurrency.minimumWidth = 28

        ibAddCurrency.setOnClickListener(this)

        //Currencies spinner
        val spCurrencies: Spinner = viewCurrency.findViewById(R.id.spCurrencies)
        spContent = getCurrenciesFromJSon()

        if (spContent != null) {
            val aCurrencies = ArrayAdapter(activity, android.R.layout.simple_spinner_dropdown_item, spContent)
            aCurrencies.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spCurrencies.adapter = aCurrencies
        }

        spCurrencies!!.setOnItemSelectedListener(this)

        //RecyclerView currencies list
        val recyclerView = viewCurrency.findViewById<RecyclerView>(R.id.rvCurrency)
        val adapter = CurrencyAdapter(activity)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        //CurrencyViewModel
        currencyViewModel = ViewModelProviders.of(this).get(CurrencyViewModel::class.java)

        currencyViewModel.allCurrencies.observe(this, Observer { currencies ->
            // Update the cached copy of the words in the adapter.
            currencies?.let { adapter.setCurrencies(it) }
        })

        return viewCurrency
    }


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

    private fun showDialog(){
        // Late initialize an alert dialog object
        lateinit var dialog:AlertDialog


        // Initialize a new instance of alert dialog builder object
        val builder = AlertDialog.Builder(activity)

        // Set a title for alert dialog
        builder.setTitle("Adding currency")

        // Set a message for alert dialog
        builder.setMessage("Are you sure to add ${selectedCurrency.toString()}?")


        // On click listener for dialog buttons
        val dialogClickListener = DialogInterface.OnClickListener{_,which ->
            when(which){
                DialogInterface.BUTTON_POSITIVE -> {
                    var symbol: String?
                    symbol = selectedCurrency!!.symbol

                    var preferred: Boolean

                    when (currencyViewModel.preferred.value) {
                        true -> preferred = true
                        false -> preferred = false
                        null -> preferred = false
                    }

                    val currency = Currency(selectedCurrency!!.iso, selectedCurrency!!.name, symbol, preferred)
                    currencyViewModel.insert(currency)
                }
            }
        }


        // Set the alert dialog positive/yes button
        builder.setPositiveButton("YES",dialogClickListener)

        // Set the alert dialog neutral/cancel button
        builder.setNeutralButton("CANCEL",dialogClickListener)


        // Initialize the AlertDialog using builder object
        dialog = builder.create()

        // Finally, display the alert dialog
        dialog.show()
    }

    override fun onClick(v: View?) {
        when (v) {
            ibAddCurrency -> {
                showDialog()

            }
        }
    }


    override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        selectedCurrency = parent.selectedItem as CurrencyData

    }


    override fun onNothingSelected(parent: AdapterView<*>) {
        // Nothing to do here.
    }


}






