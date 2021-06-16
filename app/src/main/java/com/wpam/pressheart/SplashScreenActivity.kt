package com.wpam.pressheart


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.content.ContentValues.TAG
import androidx.core.os.HandlerCompat.postDelayed
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SplashScreenActivity : AppCompatActivity() {

    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val currentUser = FirebaseAuth.getInstance().currentUser
        val currentUser2 = Firebase.auth.currentUser
        if(currentUser != null && currentUser2 != null){

            val intent = Intent(this@SplashScreenActivity, MainLoggedMenuActivity::class.java)
            val docRef = db.collection("Users_info").document(currentUser.uid)
            docRef.get()
                .addOnSuccessListener {
                        document ->
                    if (document != null){
                        val userLogin:String = document.data?.getValue("name").toString()
                        Log.d(TAG, "succes ${userLogin}")
                    }
                    else
                    {
                    }
                }
        Handler().postDelayed({

            startActivity(intent)
            finish()
        }, 2000)

        }
        else{

            val intent2 = Intent(this@SplashScreenActivity, MainActivity::class.java)
            Handler().postDelayed({
                startActivity(intent2)
            }, 2000)
            startActivity(intent2)
            finish()
        }

    }
}