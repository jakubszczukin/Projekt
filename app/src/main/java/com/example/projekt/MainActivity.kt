package com.example.projekt

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    var bottomNavigation: BottomNavigationView? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.projekt.R.layout.activity_main)

        loadFragment(OfertyFragment())
        auth = FirebaseAuth.getInstance()

        val bottomNavigation = bottom_navigation
        bottomNavigation.setOnNavigationItemSelectedListener(this);

        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                com.example.projekt.R.id.settings -> {
                    Toast.makeText(this, "Ustawienia", Toast.LENGTH_SHORT).show()
                    auth.signOut()
                    val intent = Intent(this, LoginActivity::class.java);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent);
                    finish();
                    true
                }
                else -> false
            }
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var fragment: Fragment? = null
        when (item.itemId) {
            com.example.projekt.R.id.nav_oferty-> fragment = OfertyFragment()
            com.example.projekt.R.id.nav_wiadomosci -> fragment = WiadomosciFragment()
            com.example.projekt.R.id.nav_profil -> fragment = ProfilFragment()
        }
        return loadFragment(fragment)
    }

    private fun loadFragment(fragment: Fragment?): Boolean {
        if (fragment != null) {
            supportFragmentManager
                .beginTransaction()
                .replace(com.example.projekt.R.id.container, fragment)
                .commit()
            return true
        }
        return false
    }
}