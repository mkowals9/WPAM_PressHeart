package com.wpam.pressheart


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity(){

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        auth = FirebaseAuth.getInstance()

    }

    public override fun onStart() {
        super.onStart()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }





}
