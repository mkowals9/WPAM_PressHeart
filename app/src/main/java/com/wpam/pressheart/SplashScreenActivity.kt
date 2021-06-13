package com.wpam.pressheart

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.core.os.HandlerCompat.postDelayed
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SplashScreenActivity : AppCompatActivity() {

    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val currentUser = FirebaseAuth.getInstance().currentUser
        if(currentUser != null){

            val intent = Intent(this@SplashScreenActivity, MainLoggedMenuActivity::class.java)

            val docRef = db.collection("Users_info").document(currentUser.uid)
            docRef.get()
                .addOnSuccessListener {
                        document ->
                    if (document != null){
                        val userLogin:String = document.data?.getValue("name").toString()
                        intent.putExtra("userName", userLogin)
                    }
                    else
                    {
                        val userLogin = "No Name User"
                        intent.putExtra("userName", userLogin)
                    }
                }
        Handler().postDelayed({
            startActivity(intent)
        }, 2000)

        }
        else{
            val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}