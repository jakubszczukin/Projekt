package com.example.projekt

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_oferty.*
import kotlinx.android.synthetic.main.fragment_oferty.view.*
import java.util.*

class OfertyFragment : Fragment() {

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
            com.example.projekt.R.layout.fragment_oferty,
            container,
            false
        )

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        viewOfLayout.day_picker.locale = Locale("PL")

        viewOfLayout.addOfertButton.setOnClickListener {
            val days = viewOfLayout.day_picker.selectedDays
            val city = viewOfLayout.addCityText.text.toString()
            if(TextUtils.isEmpty(city) || days.isNullOrEmpty()){
                Toast.makeText(activity, "Dodaj wszystkie informacje", Toast.LENGTH_LONG).show()
            } else {
                val oferta = hashMapOf(
                    "miasto" to city,
                    "dni" to days,
                    "uzytkownik_id" to auth.currentUser!!.uid
                )
                db.collection("oferty").document().set(oferta)
                Toast.makeText(activity, "Pomyślnie dodano ofertę do bazy", Toast.LENGTH_LONG).show()
            }

        }

        return viewOfLayout
    }

}