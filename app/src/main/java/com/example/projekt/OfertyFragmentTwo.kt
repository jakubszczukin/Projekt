package com.example.projekt

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_oferty.view.*
import kotlinx.android.synthetic.main.fragment_oferty_two.view.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class OfertyFragmentTwo : Fragment() {

    private lateinit var viewOfLayout: View
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        viewOfLayout = inflater.inflate(
            com.example.projekt.R.layout.fragment_oferty_two,
            container,
            false
        )

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        viewOfLayout.day_picker.locale = Locale("PL")

        viewOfLayout.addOfertButton.setOnClickListener {
            val days = viewOfLayout.day_picker.selectedDays
            val city = viewOfLayout.addCityText.text.toString()
            val description = viewOfLayout.addDescriptionText.text.toString()
            val date = getCurrentDateTime()
            if(TextUtils.isEmpty(city) || days.isNullOrEmpty()){
                Toast.makeText(activity, "Dodaj wszystkie informacje", Toast.LENGTH_LONG).show()
            } else {
                val oferta = hashMapOf(
                    "miasto" to city,
                    "dni" to days,
                    "opis" to description,
                    "uzytkownik_id" to auth.currentUser!!.uid,
                    "data_start" to date.toString("yyyy/MM/dd HH:mm:ss")
                )
                db.collection("oferty").document().set(oferta)
                Toast.makeText(activity, "Pomyślnie dodano ofertę do bazy", Toast.LENGTH_LONG).show()
            }

        }
        return viewOfLayout
    }

    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    private fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

}