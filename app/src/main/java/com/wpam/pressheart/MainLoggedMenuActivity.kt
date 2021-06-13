package com.wpam.pressheart

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_add_new_medicine.*

class MainLoggedMenuActivity : AppCompatActivity() {

    private var db = Firebase.firestore
    private lateinit var contextApp : View
    private var currentLogin = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_logged_menu)
        setSupportActionBar(findViewById(R.id.toolbar))

        
    }

    public override fun onStart() {
        super.onStart()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val userId: String = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val docRef = db.collection("Users_info").document(userId)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {

                    currentLogin = document.data?.getValue("name").toString()
                }
                contextApp.findViewById<TextView>(R.id.textview_first)
                    .setText("Hello ${currentLogin}")
            }
    }



}