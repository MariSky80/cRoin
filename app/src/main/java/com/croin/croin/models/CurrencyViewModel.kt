package com.croin.croin.models

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import com.croin.croin.database.CroinDatabase
import com.croin.croin.repositories.CurrencyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.croin.croin.database.entity.Currency
import kotlin.coroutines.CoroutineContext

class CurrencyViewModel(application: Application): AndroidViewModel(application) {

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    private var db: CroinDatabase = CroinDatabase.getDatabase(application)
    private val repository: CurrencyRepository
    val allCurrencies: LiveData<List<Currency>>
    val preferred: LiveData<Currency>

    init {
        repository = CurrencyRepository(db.currencyDao())
        allCurrencies = repository.allCurrencies
        preferred = repository.preferred
    }


    fun noFavorites() = scope.launch(Dispatchers.IO) {
        repository.noFavorites()
    }

    fun insert(currency: Currency) = scope.launch(Dispatchers.IO) {
        repository.insert(currency)
    }

    fun delete(currency: Currency) = scope.launch(Dispatchers.IO) {
        repository.delete(currency)
    }

    fun update(currency: Currency) = scope.launch(Dispatchers.IO) {
        repository.updated(currency)
    }


}
