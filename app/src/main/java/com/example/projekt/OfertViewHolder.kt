package com.example.projekt

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class OfertViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val cityView: TextView = itemView.findViewById(R.id.ofert_mCity)
    private val daysView: TextView = itemView.findViewById(R.id.ofert_mDays)
    //private val descView: TextView = itemView.findViewById(R.id.ofert_mDesc)

    fun bind(ofert: MyOfert) {
        cityView.text = ofert.miasto
        daysView.text = translateDays(ofert.dni)
        //descView.text = ofert.opis

        itemView.setOnClickListener{
            val intent = Intent(itemView.context, SingleOfertActivity::class.java)
            intent.putExtra("CITY", ofert.miasto)
            intent.putExtra("DAYS", ofert.dni.toString())
            intent.putExtra("DESCRIPTION", ofert.opis)
            itemView.context.startActivity(intent)
        }
    }

    private fun translateDays(list: List<String>): String{
        var tempDays = list.joinToString()
        if("MONDAY" in tempDays)    tempDays = tempDays.replace("MONDAY", "Pon")
        if("TUESDAY" in tempDays)    tempDays = tempDays.replace("TUESDAY", "Wt")
        if("WEDNESDAY" in tempDays)    tempDays = tempDays.replace("WEDNESDAY", "Śr")
        if("THURSDAY" in tempDays)    tempDays = tempDays.replace("THURSDAY", "Czw")
        if("FRIDAY" in tempDays)    tempDays = tempDays.replace("FRIDAY", "Pt")
        if("SATURDAY" in tempDays)    tempDays = tempDays.replace("SATURDAY", "Sob")
        if("SUNDAY" in tempDays)    tempDays = tempDays.replace("SUNDAY", "Niedz")
        return tempDays
    }
}