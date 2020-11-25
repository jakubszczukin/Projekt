package com.example.projekt

import ca.antonious.materialdaypicker.MaterialDayPicker
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class MyOfert(
    var miasto: String = "",
    var dni: List<String> = emptyList(),
    var opis: String = "",
    var uzytkownik_id: String = ""

)