package com.example.projekt

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_profil.*
import kotlinx.android.synthetic.main.fragment_profil.view.*

class ProfilFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var viewOfLayout: View
    private var TAG = "FIRESTORE TEST"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewOfLayout = inflater.inflate(
            com.example.projekt.R.layout.fragment_profil,
            container,
            false
        )

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()



        val userData = db.collection("uzytkownicy").document(auth.currentUser!!.uid)
        userData.get().addOnSuccessListener { document ->
            if(document != null){
                Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                val name = document.get("imie").toString()
                Log.d(TAG, "Imie: ${name}")
                val lastname = document.get("nazwisko").toString()
                val city = document.get("miasto").toString()
                val uid = document.id.toString()

                viewOfLayout.nameText.text = name
                viewOfLayout.lastNameText.text = lastname
                viewOfLayout.cityText.text = city
                viewOfLayout.uIdText.text = uid
            }
            else{
                Log.d(TAG, "No such document")
            }
        }
            .addOnFailureListener{exception ->
                Log.d(TAG, "get failed with ", exception)
            }

        // Inflate the layout for this fragment
        return viewOfLayout
    }
}