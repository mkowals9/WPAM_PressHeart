package com.wpam.pressheart.fragments


import android.content.ContentValues.TAG
import android.content.Intent
import android.content.Intent.getIntent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.wpam.pressheart.*
import kotlinx.android.synthetic.main.fragment_main_logged_menu.*


class MainLoggedMenuFragment : Fragment() {

    private val db = Firebase.firestore
    private lateinit var viewFragmentFragment : View
    private var currentLogin : String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        var viewFragment = inflater.inflate(R.layout.fragment_main_logged_menu, container, false)
        viewFragmentFragment = viewFragment
        db.collection("Users_info").document(Firebase.auth.uid.toString()).get().addOnSuccessListener {
            documentSnapshot ->
            viewFragment.findViewById<TextView>(R.id.textview_first).setText("Hello ${documentSnapshot.data?.get("name").toString()}, nice to see you again ❤")
            Log.d(TAG, "snapshot: ${documentSnapshot.data?.get("name").toString()}")
        }
        currentLogin = viewFragment.findViewById<TextView>(R.id.textview_first).text.toString()
        val user2 = Firebase.auth.currentUser
        val profileUpdates = userProfileChangeRequest { displayName = currentLogin.toString() }
        user2!!.updateProfile(profileUpdates).addOnSuccessListener {
        Firebase.auth.currentUser?.let{for(profile in it.providerData) Log.d(TAG, "user LOGIN : ${profile.displayName}")}
        }
        return viewFragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = Firebase.auth.currentUser
        user?.let {
            for (profile in it.providerData) {
                val name = profile.displayName
                view.findViewById<TextView>(R.id.textview_first).setText("Hello ${name}, nice to see you again ❤")
            }
        }

        measurementsButton.setOnClickListener{
            val intent = Intent(
                this.activity as MainLoggedMenuActivity,
                MeasurementsActivity::class.java
            )

            startActivity(intent)
        }

        logOutButton.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            updateUI(null)
            (this.activity as MainLoggedMenuActivity).onBackPressed()
            val intent = Intent(this.activity as MainLoggedMenuActivity, MainActivity::class.java)
            startActivity(intent)

        }

        doctorButton.setOnClickListener {
            val intent = Intent(
                this.activity as MainLoggedMenuActivity,
                AppointmentsActivity::class.java
            )
            startActivity(intent)
        }

        medicinesButton.setOnClickListener{
            val intent = Intent(
                this.activity as MainLoggedMenuActivity,
                MedicinesActivity::class.java
            )
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val user = Firebase.auth.currentUser
        user?.let {
            for (profile in it.providerData) {
                val name = profile.displayName
                viewFragmentFragment.findViewById<TextView>(R.id.textview_first).setText("Hello ${name}, nice to see you again ❤")
            }
        }
    }

    private fun updateUI(user: FirebaseUser?) {}

}


