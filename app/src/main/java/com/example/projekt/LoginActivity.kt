package com.example.projekt

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.multidex.MultiDex
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*


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

            if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this@LoginActivity, "Wypełnij wszystkie pola", Toast.LENGTH_LONG).show()
            } else{
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this)
                { task ->
                    if(task.isSuccessful) {
                        Toast.makeText(this, "Pomyślnie zalogowano", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else {
                        Toast.makeText(
                            this,
                            "Adres e-mail lub hasło niepoprawne",
                            Toast.LENGTH_LONG
                        ).show()
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