package com.example.projekt

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.fragment_register_one.*
import kotlinx.android.synthetic.main.fragment_register_two.*


class RegisterActivity : AppCompatActivity(), StepperLayout.StepperListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var mStepperLayout : StepperLayout? = null
    private var data: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.projekt.R.layout.activity_register)

        mStepperLayout = findViewById<View>(com.example.projekt.R.id.stepperLayout) as StepperLayout
        mStepperLayout!!.adapter = MyStepperAdapter(supportFragmentManager, this)
        mStepperLayout!!.setListener(this)
    }

    override fun onCompleted(completeButton: View?) {
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val email: String = mailText.text.toString()
        val password: String = passwordText.text.toString()
        val name: String = imieText.text.toString()
        val lastName: String = nazwiskoText.text.toString()
        val city: String = miastoText.text.toString()
        val phoneNumber: Int = telefonText.text.toString().toInt()


        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(city))
            Toast.makeText(this, "Wypełnij wszystkie pola", Toast.LENGTH_LONG).show()
        else{
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this){task ->
                val user = hashMapOf(
                        "email" to email,
                        "imie" to name,
                        "nazwisko" to lastName,
                        "miasto" to city,
                        "numer_telefonu" to phoneNumber
                )
                if(task.isSuccessful){
                    db.collection("uzytkownicy").document(auth.currentUser!!.uid).set(user)
                    Toast.makeText(this, "Pomyślnie zarejestrowano", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else{
                    Toast.makeText(this, "Użytkownik o takim adresie e-mail już istnieje!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onError(verificationError: VerificationError) {
        Toast.makeText(this, "onError! -> " + verificationError.errorMessage, Toast.LENGTH_SHORT)
            .show()
    }

    override fun onStepSelected(newStepPosition: Int) {
        Toast.makeText(this, "onStepSelected! -> $newStepPosition", Toast.LENGTH_SHORT).show()
        if(newStepPosition == 1){
            Toast.makeText(this, mailText.text.toString(), Toast.LENGTH_LONG).show()
        }
    }

    override fun onReturn() {
        finish()
    }
}