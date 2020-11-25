package com.example.projekt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_single_ofert.*

class SingleOfertActivity : AppCompatActivity() {
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

        cityText.text = city
        daysText.text = days
        descText.text = desc
    }
}