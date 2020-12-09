package com.example.projekt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.activity_my_single_ofert.*
import kotlinx.android.synthetic.main.activity_single_ofert.*
import kotlinx.android.synthetic.main.activity_single_ofert.cityText
import kotlinx.android.synthetic.main.activity_single_ofert.daysText
import kotlinx.android.synthetic.main.activity_single_ofert.descText
import kotlinx.android.synthetic.main.activity_single_ofert.topAppBar

class SingleOfertActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_ofert)

        val appbar = topAppBar
        setSupportActionBar(appbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true);
        supportActionBar!!.setDisplayShowHomeEnabled(true);

        val city = intent.getStringExtra("CITY")
        val days = intent.getStringExtra("DAYS")
        val desc = intent.getStringExtra("DESCRIPTION")
        val id = intent.getStringExtra("DOCNAME")

        cityText.text = city
        daysText.text = days
        descText.text = desc

        acceptText.setOnClickListener {
            db = FirebaseFirestore.getInstance()
            auth = FirebaseAuth.getInstance()

            db.collection("oferty").document(id!!).update("akceptowane", auth.currentUser!!.uid).addOnSuccessListener {
                Toast.makeText(this, "Zaakceptowano ofertę", Toast.LENGTH_LONG).show()
                finish()
            }.addOnFailureListener {
                Log.w("TEST",  it.message.toString())
                Toast.makeText(this, "Błąd podczas akceptowania oferty", Toast.LENGTH_LONG).show()
            }
        }

    }
}