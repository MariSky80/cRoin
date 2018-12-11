package com.croin.croin


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.croin.croin.adapters.RecognitionAdapter
import com.croin.croin.database.entity.Recognition
import com.croin.croin.models.RecognitionViewModel
import android.content.Intent
import android.net.Uri


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
        lateinit var dialog: AlertDialog

        val builder = AlertDialog.Builder(activity)
        builder.setTitle(getString(R.string.dialog_delete_title))
        builder.setMessage("${getString(R.string.dialog_delete_description)} ${recognition.name}?")

        val dialogClickListener = DialogInterface.OnClickListener{ _, which ->
            when(which){
                DialogInterface.BUTTON_POSITIVE -> {
                    recogintionViewModel.delete(recognition)
                }
            }
        }

        builder.setPositiveButton(R.string.dialog_yes,dialogClickListener)
        builder.setNeutralButton(R.string.dialog_cancel,dialogClickListener)

        dialog = builder.create()
        dialog.show()
    }

    override fun onLocationClick(recognition: Recognition) {
        lateinit var dialog: AlertDialog

        val builder = AlertDialog.Builder(activity)
        builder.setTitle(getString(R.string.dialog_location_title))
        builder.setMessage("${getString(R.string.dialog_location_description)} ${recognition.name}?")

        val dialogClickListener = DialogInterface.OnClickListener{ _, which ->
            when(which){
                DialogInterface.BUTTON_POSITIVE -> {
                    val strUri = "http://maps.google.com/maps?q=loc:${recognition.location} (${recognition.name})"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(strUri))
                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")
                    startActivity(intent)
                }
            }
        }

        builder.setPositiveButton(R.string.dialog_yes,dialogClickListener)
        builder.setNeutralButton(R.string.dialog_cancel,dialogClickListener)

        dialog = builder.create()
        dialog.show()
    }

}