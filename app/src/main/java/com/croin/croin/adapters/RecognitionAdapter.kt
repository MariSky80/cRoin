package com.croin.croin.adapters

import android.content.Context
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.croin.croin.R
import com.croin.croin.database.entity.Recognition



class RecognitionAdapter internal constructor(context: Context, listener: OnItemClickListener) : RecyclerView.Adapter<RecognitionAdapter.RecognitonViewHolder>() {


    private var listenerDeleteButton: OnItemClickListener = listener
    private var listenerLocationButton: OnItemClickListener = listener
    interface OnItemClickListener {
        fun onDeleteClick(recognition: Recognition)
        fun onLocationClick(recognition: Recognition)
        //fun onItemClick(recognition: Recognition)
    }


    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var recognitions = emptyList<Recognition>()
    private val cContext = context

    inner class RecognitonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recognitionItemViewName: TextView = itemView.findViewById(R.id.tvName)
        val recognitionItemViewDate: TextView = itemView.findViewById(R.id.tvDate)
        val recognitionItemViewQuantity: TextView = itemView.findViewById(R.id.tvQuantity)
        val recognitionImageView: ImageView = itemView.findViewById(R.id.ivThumbnail)

        private val recognitionItemDelete: ImageButton = itemView.findViewById(R.id.ibDelete)
        val recognitionItemLocation: ImageButton = itemView.findViewById(R.id.ibLocation)
        fun bind(recognition: Recognition, listener: OnItemClickListener) {
            recognitionItemDelete.setOnClickListener {
                listener.onDeleteClick(recognition)
            }
            recognitionItemLocation.setOnClickListener {
                listener.onLocationClick(recognition)
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

        holder.recognitionItemViewDate.text = DateFormat.format("dd/MM/yyyy", current.createdAt).toString()
        holder.recognitionImageView.setImageURI(Uri.parse(current.image))
        holder.recognitionItemViewQuantity.text = "${cContext.getString(R.string.value)} ${current.quantity.toString()} €"
        current.location?: run {
            holder.recognitionItemLocation.setImageResource(R.drawable.ic_map_marker_solid_disabled)
            holder.recognitionItemLocation.isEnabled = false
        }
        holder.bind(current, listenerLocationButton)
        holder.bind(current, listenerDeleteButton)


    }

    internal fun setRecognitions(recognitions: List<Recognition>) {
        this.recognitions = recognitions
        notifyDataSetChanged()
    }

    override fun getItemCount() = recognitions.size
}
