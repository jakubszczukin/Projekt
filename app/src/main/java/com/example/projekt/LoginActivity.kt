package com.example.projekt

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.multidex.MultiDex
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.mailText
import kotlinx.android.synthetic.main.activity_login.passwordText
import kotlinx.android.synthetic.main.fragment_register_one.*


class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null) { // if null not logged in
            val intent = Intent(this, MainActivity::class.java);
            startActivity(intent);
            finish();
       }

        loginButton.setOnClickListener{
            val email: String = mailText.text.toString()
            val password: String = passwordText.text.toString()

            when{
                TextUtils.isEmpty(email) -> {
                    mailText.error = "Podaj adres e-mail"
                    mailText.requestFocus()
                }
                TextUtils.isEmpty(password) -> {
                    passwordText.error = "Podaj hasło"
                    passwordText.requestFocus()
                }
                else -> {
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                        if(task.isSuccessful) {
                            Toast.makeText(this, "Pomyślnie zalogowano", Toast.LENGTH_LONG).show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        else {
                            try {
                                throw task.exception!!
                            } catch(e : FirebaseAuthInvalidCredentialsException){
                                mailText.error = "Nieprawidłowy adres e-mail lub hasło"
                                passwordText.error = "Nieprawidłowy adres e-mail lub hasło"
                                mailText.requestFocus()
                            } catch(e : Exception){
                                Log.e("EXCEPTION", e.message!!)
                            }
                        }
                    }
                }
            }
        }

        registerText.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java).apply{}
            startActivity(intent)
        }

        forgotPasswordText.setOnClickListener{
            val intent = Intent(this, ResetPasswordActivity::class.java).apply{}
            startActivity(intent)
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

}