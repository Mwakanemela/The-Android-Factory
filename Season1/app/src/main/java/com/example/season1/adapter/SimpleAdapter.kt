package com.example.season1.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.season1.R

class SimpleAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val data = ArrayList<String>()
    private var headerText:String? = null

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> R.layout.view_holder_header
            data.size + 1 -> R.layout.view_holder_footer
            else -> R.layout.view_holder_textview
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        return SimpleViewHolder(parent)
        return when(viewType) {
            R.layout.view_holder_header -> HeaderViewHolder(parent)
            R.layout.view_holder_footer -> FooterViewHolder(parent)
           else -> SimpleViewHolder(parent)
        }
    }

    override fun getItemCount(): Int {
        return data.size + 2
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        (holder as SimpleViewHolder).onBind(data[position], position)
        when(holder) {
            is SimpleViewHolder -> holder.onBind(data[position-1], position)
            is HeaderViewHolder -> holder.onBind(headerText)
            is FooterViewHolder -> holder.onBind()
        }
    }

    fun setData(data: List<String>, headerTextView: String?) {
        this.data.clear()
        this.data.addAll(data)
        this.headerText = headerTextView
    }

    inner class SimpleViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.view_holder_textview, parent, false)
    ) {

        private val textView: TextView = itemView.findViewById(R.id.textView)
        fun onBind(textToDisplay: String, position: Int) {

            val color = if (position % 2 == 0) {
                itemView.context.getColor(R.color.light_blue)
            } else {
                itemView.context.getColor(R.color.white)
            }
            textView.text = textToDisplay
            textView.setBackgroundColor(color)
        }
    }

    inner class HeaderViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.view_holder_header, parent, false)
    ) {

        private val headerTextView: TextView = itemView.findViewById(R.id.headerTextView)

        fun onBind(headerTextView: String?) {
            this.headerTextView.text = headerTextView
        }
    }

    inner class FooterViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.view_holder_footer, parent, false)
    ) {

        fun onBind() {

        }
    }
}