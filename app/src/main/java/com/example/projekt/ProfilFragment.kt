package com.example.projekt

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profil.*
import kotlinx.android.synthetic.main.fragment_profil.view.*

class ProfilFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var storageRef: StorageReference
    private lateinit var viewOfLayout: View
    private var TAG = "FIRESTORE TEST"
    private var RCODE = 100

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

        val progressBar = viewOfLayout.progressBar
        val mainContent = viewOfLayout.profileMainContent

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        storageRef = FirebaseStorage.getInstance().reference

        val userData = db.collection("uzytkownicy").document(auth.currentUser!!.uid)
        userData.get().addOnSuccessListener { document ->
            if(document != null){
                Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                val name = document.get("imie").toString()
                Log.d(TAG, "Imie: ${name}")
                val lastname = document.get("nazwisko").toString()
                val city = document.get("miasto").toString()

                viewOfLayout.nameText.text = name
                viewOfLayout.lastNameText.text = lastname
                viewOfLayout.cityText.text = city

                storageRef.child(auth.currentUser!!.uid + "/profilePicture/avatar.jpg").downloadUrl.addOnSuccessListener {
                    Picasso.get().load(it).into(viewOfLayout.update_imageView)
                }.addOnCompleteListener {
                    progressBar.visibility = View.GONE;
                    mainContent.visibility = View.VISIBLE
                }
            }
            else{
                Log.d(TAG, "No such document")
            }
        }.addOnFailureListener{ exception ->
                Log.d(TAG, "get failed with ", exception)
        }

        viewOfLayout.update_imageView.setOnClickListener{
            if(ActivityCompat.checkSelfPermission(
                    context!!,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), RCODE)
            }
            else{
                openGalleryForImage()
            }
        }

        // Inflate the layout for this fragment
        return viewOfLayout
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == RCODE){
            val imageUri = data?.data
            //update_imageView.setImageURI(imageUri) // handle chosen image
            uploadImageToFirebase(imageUri)
        }
    }

    private fun uploadImageToFirebase(imageUri: Uri?){
        val currentUser = auth.currentUser!!.uid
        val fileRef = storageRef.child("$currentUser/profilePicture/avatar.jpg")
        Toast.makeText(activity, "$currentUser/profilePicture/avatar.jpg", Toast.LENGTH_SHORT).show()
        fileRef.putFile(imageUri!!).addOnSuccessListener {
            fileRef.downloadUrl.addOnSuccessListener {
                Picasso.get().load(it).into(update_imageView)
            }
        }.addOnFailureListener {
            Toast.makeText(activity, (it as StorageException).message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun openGalleryForImage(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, RCODE)
    }
}