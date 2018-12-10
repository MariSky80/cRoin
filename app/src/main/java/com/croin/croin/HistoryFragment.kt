package com.croin.croin


import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.croin.croin.adapters.RecognitionAdapter
import com.croin.croin.database.entity.Recognition
import com.croin.croin.models.RecognitionViewModel


/**
 * A simple [Fragment] subclass.
 */
class HistoryFragment : Fragment(), RecognitionAdapter.OnItemClickListener {

    private lateinit var recogintionViewModel: RecognitionViewModel

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val viewHistory =  inflater!!.inflate(R.layout.fragment_history, container, false)

        //RecognitionViewModel
        recogintionViewModel = ViewModelProviders.of(this).get(RecognitionViewModel::class.java)

        //RecyclerView recognitions list
        val recyclerView = viewHistory.findViewById<RecyclerView>(R.id.rvRecognition)
        val adapter = RecognitionAdapter(activity, this@HistoryFragment)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        //RecognitionViewModel
        recogintionViewModel = ViewModelProviders.of(this).get(RecognitionViewModel::class.java)

        recogintionViewModel.allRecognitions.observe(this, Observer { recognitions ->
            // Update the cached copy of the recognitions in the adapter.
            recognitions?.let { adapter.setRecognitions(it) }
        })

        return viewHistory
    }

    override fun onDeleteClick(recognition: Recognition) {
    }

//    override fun onItemClick(recognition: Recognition) {
//    }

}