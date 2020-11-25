package com.example.projekt

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView


class OfertViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val cityView: TextView = itemView.findViewById(R.id.ofert_mCity)
    private val daysView: TextView = itemView.findViewById(R.id.ofert_mDays)
    //private val descView: TextView = itemView.findViewById(R.id.ofert_mDesc)

    fun bind(ofert: MyOfert) {
        cityView.text = ofert.miasto
        daysView.text = ofert.dni.toString()
        //descView.text = ofert.opis

        itemView.setOnClickListener{
            val intent = Intent(itemView.context, SingleOfertActivity::class.java)
            intent.putExtra("CITY", ofert.miasto)
            intent.putExtra("DAYS", ofert.dni.toString())
            intent.putExtra("DESCRIPTION", ofert.opis)
            itemView.context.startActivity(intent)
        }
    }
}