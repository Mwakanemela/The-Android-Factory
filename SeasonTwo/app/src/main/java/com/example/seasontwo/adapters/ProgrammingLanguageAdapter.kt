package com.example.seasontwo.adapters

import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.seasontwo.R
import com.example.seasontwo.dataclass.ProgrammingLanguagesTile

class ProgrammingLanguageTileAdapter(
 private val data: ArrayList<ProgrammingLanguagesTile>
): RecyclerView.Adapter<ProgrammingLanguageTileAdapter.ProgrammingLanguageTileViewHolder>() {

    inner class ProgrammingLanguageTileViewHolder(parent: ViewGroup): RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.programming_languages_tile_view_holder, parent, false)
    ){

        fun onBind(programmingLanguagesTile: ProgrammingLanguagesTile) {

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProgrammingLanguageTileViewHolder {
        return ProgrammingLanguageTileViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ProgrammingLanguageTileViewHolder, position: Int) {
        holder.onBind(data[position])
    }
}