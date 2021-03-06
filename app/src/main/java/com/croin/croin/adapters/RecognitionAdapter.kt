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


/**
 * @author Maricel Bros Maimó
 *
 * Recognitoin Adapter class.
 * Class to show a list of recognitions with a recycler view.
 *
 */
class RecognitionAdapter internal constructor(context: Context, listener: OnItemClickListener) : RecyclerView.Adapter<RecognitionAdapter.RecognitonViewHolder>() {


    private var listenerDeleteButton: OnItemClickListener = listener
    private var listenerLocationButton: OnItemClickListener = listener

    /**
     * Interfave OnTiemClickListener that creates listeners from itemView items.
     */
    interface OnItemClickListener {
        fun onDeleteClick(recognition: Recognition)
        fun onLocationClick(recognition: Recognition)
        //fun onItemClick(recognition: Recognition)
    }

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var recognitions = emptyList<Recognition>()
    private val cContext = context


    /**
     * Inner class that contains each recognition itemView.
     *
     * @param RecyclerView.itemView(itemView)
     */
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


    /**
     * Overrides function onCreateViewHolder from system.
     *
     * @param ViewGroup
     * @param Int viewType
     *
     * @return RecognitionViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecognitonViewHolder {
        val itemView = inflater.inflate(R.layout.recognition_item, parent, false)
        return RecognitonViewHolder(itemView)
    }


    /**
     * Overrides function onBindViewHolder from system.
     * Binds information of current item from viewHolder.
     *
     * @param RecognitonViewHolder
     * @param Int position
     *
     */
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


    /**
     * Set recognitions to this.recognitions.
     *
     * @param List<Recognition> list of recognitions entities.
     *
     */
    internal fun setRecognitions(recognitions: List<Recognition>) {
        this.recognitions = recognitions
        notifyDataSetChanged()
    }


    /**
     * Override function getItemCount from system.
     */
    override fun getItemCount() = recognitions.size
}
