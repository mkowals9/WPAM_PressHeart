package com.wpam.pressheart.fragments

import android.app.ActivityOptions
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.wpam.pressheart.MainActivity
import com.wpam.pressheart.MainLoggedMenuActivity
import com.wpam.pressheart.MeasurementsActivity
import com.wpam.pressheart.R
import kotlinx.android.synthetic.main.fragment_main_logged_menu.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */

class MainLoggedMenuFragment : Fragment() {

    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_logged_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val userId : String = FirebaseAuth.getInstance().currentUser?.uid.toString()
        Log.d(TAG, "userId: ${userId}")
        val docRef = db.collection("Users_info").document(userId)
        docRef.get()
            .addOnSuccessListener {
                document ->
                if (document != null){

                    //userLogin= document.data.toString()
                    val userLogin = document.data?.getValue("name")
                    //Log.d(TAG, "${document.toString()}")
                    Log.d(TAG, "Login: ${document.data?.getValue("name")}")
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    view.findViewById<TextView>(R.id.textview_first).setText("Hello " + userLogin)

                }
                else
                {
                    view.findViewById<TextView>(R.id.textview_first).setText("Hello User")
                }
            }

        //view.findViewById<Button>(R.id.measurementsButton).setOnClickListener {
          //  findNavController().navigate(R.id.action_MainLoggedMenu_to_MeasurementsFragment)
        //}
        measurementsButton.setOnClickListener{
            //findNavController().navigate(R.id.action_MainLoggedMenu_to_MeasurementsFragment)
            val intent = Intent(this.activity as MainLoggedMenuActivity, MeasurementsActivity::class.java)

            startActivity(intent)
            //(this.activity)?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        logOutButton.setOnClickListener{

//
            FirebaseAuth.getInstance().signOut()

            Log.d("Successful log out" , "Successful log out")
            updateUI(null)
            //findNavController().navigate(R.id.action_MainLoggedMenu_to_MainViewFragment)
            (this.activity as MainLoggedMenuActivity).onBackPressed()
            val intent = Intent(this.activity as MainLoggedMenuActivity, MainActivity::class.java)
            startActivity(intent)
            //findNavController().navigate(R.id.action_MainLoggedMenu_to_MainViewFragment)

        }

        medicinesButton.setOnClickListener{
            //findNavController().navigate(R.id.action_MainLoggedMenu_to_MedicinesFragment)
        }
    }

    private fun updateUI(user: FirebaseUser?) {}



}


