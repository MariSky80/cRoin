package com.croin.croin.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.croin.croin.R
import com.croin.croin.database.entity.Recognition


class RecognitionAdapter internal constructor(context: Context, listener: OnItemClickListener) : RecyclerView.Adapter<RecognitionAdapter.RecognitonViewHolder>() {


    private var listenerDeleteButton: OnItemClickListener = listener
    interface OnItemClickListener {
        fun onDeleteClick(recognition: Recognition)
        //fun onItemClick(recognition: Recognition)
    }


    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var recognitions = emptyList<Recognition>()
    private val cContext = context

    inner class RecognitonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recognitionItemViewName: TextView = itemView.findViewById(R.id.tvName)
        //val recognitionItemClick: ImageButton = itemView.findViewById(R.id.ibFav)

        private val recognitionItemDelete: ImageButton = itemView.findViewById(R.id.ibDelete)
        fun bind(recognition: Recognition, listener: OnItemClickListener) {
            recognitionItemDelete.setOnClickListener {
                listener.onDeleteClick(recognition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecognitonViewHolder {
        val itemView = inflater.inflate(R.layout.recognition_item, parent, false)
        return RecognitonViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecognitonViewHolder, position: Int) {
        val current = recognitions[position]

        holder.recognitionItemViewName.text = current.name

        holder.bind(current, listenerDeleteButton)

    }

    internal fun setRecognitions(recognitions: List<Recognition>) {
        this.recognitions = recognitions
        notifyDataSetChanged()
    }

    override fun getItemCount() = recognitions.size
}
