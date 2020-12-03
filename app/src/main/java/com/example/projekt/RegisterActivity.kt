package com.example.projekt

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError
import kotlinx.android.synthetic.main.fragment_register_one.*
import kotlinx.android.synthetic.main.fragment_register_two.*
import java.lang.Integer.parseInt


class RegisterActivity : AppCompatActivity(), StepperLayout.StepperListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var mStepperLayout : StepperLayout? = null
    private var data: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mStepperLayout = findViewById<View>(R.id.stepperLayout) as StepperLayout
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
        val phoneNumber: String = telefonText.text.toString()


        when {
            TextUtils.isEmpty(name) -> {
                imieText.error = "Podaj swoje imię"
                imieText.requestFocus()
            }
            TextUtils.isEmpty(lastName) -> {
                nazwiskoText.error = "Podaj swoje nazwisko"
                nazwiskoText.requestFocus()
            }
            TextUtils.isEmpty(city) -> {
                miastoText.error = "Podaj miejsce zamieszkania"
                miastoText.requestFocus()
            }
            TextUtils.isEmpty(phoneNumber) -> {
                telefonText.error = "Podaj numer telefonu"
                telefonText.requestFocus()
            }
            else -> {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this){task ->
                    val user = hashMapOf(
                        "email" to email,
                        "imie" to name,
                        "nazwisko" to lastName,
                        "miasto" to city,
                        "numer_telefonu" to phoneNumber,
                        "admin" to "false"
                    )
                    if(task.isSuccessful){
                        db.collection("uzytkownicy").document(auth.currentUser!!.uid).set(user)
                        Toast.makeText(this, "Pomyślnie zarejestrowano", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else{
                        try{
                            throw task.exception!!
                        } catch(e : FirebaseAuthWeakPasswordException){
                            passwordText.error = "Użyj mocniejszego hasła"
                            passwordText.requestFocus()
                            mStepperLayout!!.currentStepPosition = 0
                        } catch(e : FirebaseAuthInvalidCredentialsException){
                            mailText.error = "Nieprawidłowy adres e-mail"
                            mailText.requestFocus()
                            mStepperLayout!!.currentStepPosition = 0
                        } catch(e : FirebaseAuthUserCollisionException){
                            mailText.error = "Podany adres e-mail jest zajęty"
                            mailText.requestFocus()
                            mStepperLayout!!.currentStepPosition = 0
                        } catch(e : Exception){
                            Log.e("EXCEPTION", e.message!!)
                        }
                    }
                }
            }
        }
    }

    override fun onError(verificationError: VerificationError) {}

    override fun onStepSelected(newStepPosition: Int) {}

    override fun onReturn() {
        finish()
    }
}