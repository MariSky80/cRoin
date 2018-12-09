package com.croin.croin.models

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.croin.croin.database.CroinDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.croin.croin.database.entity.Recognition
import com.croin.croin.repositories.RecognitionRepository
import kotlin.coroutines.CoroutineContext

class RecognitionViewModel(application: Application): AndroidViewModel(application) {

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    private var db: CroinDatabase = CroinDatabase.getDatabase(application)
    private val repository: RecognitionRepository
    val allRecognitions: LiveData<List<Recognition>>


    init {
        repository = RecognitionRepository(db.recognitionDao())
        allRecognitions = repository.allRecognitions
    }


    fun recognition(id: Int) = scope.launch(Dispatchers.IO) {
        repository.recognition(id)
    }

    fun insert(recognition: Recognition) = scope.launch(Dispatchers.IO) {
        repository.insert(recognition)
    }

    fun delete(recognition: Recognition) = scope.launch(Dispatchers.IO) {
        repository.delete(recognition)
    }

    fun update(recognition: Recognition) = scope.launch(Dispatchers.IO) {
        repository.updated(recognition)
    }


}