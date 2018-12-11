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


/**
 * @author Maricel Bros Maimó
 *
 * RecognitionViewModel class.
 * Connects reposytory with activities and fragments.
 *
 */
class RecognitionViewModel(application: Application): AndroidViewModel(application) {

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    private var db: CroinDatabase = CroinDatabase.getDatabase(application)
    private val repository: RecognitionRepository
    val allRecognitions: LiveData<List<Recognition>>


    /**
     * Initializes LiveData variables and gets all recognitions registers.
     */
    init {
        repository = RecognitionRepository(db.recognitionDao())
        allRecognitions = repository.allRecognitions
    }


    /**
     * Get recognition
     *
     * @param Int id
     *
     * @return Recognition
     */
    fun recognition(id: Int) = scope.launch(Dispatchers.IO) {
        repository.recognition(id)
    }

    /**
     * Insert recognition
     *
     * @param Recognition
     */
    fun insert(recognition: Recognition) = scope.launch(Dispatchers.IO) {
        repository.insert(recognition)
    }


    /**
     * Delete recognition
     *
     * @param Recognition
     */
    fun delete(recognition: Recognition) = scope.launch(Dispatchers.IO) {
        repository.delete(recognition)
    }


    /**
     * Update recognition
     *
     * @param Recognition
     */
    fun update(recognition: Recognition) = scope.launch(Dispatchers.IO) {
        repository.updated(recognition)
    }


}