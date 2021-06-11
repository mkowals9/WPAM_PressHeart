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
        return viewFragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fromBundle:String = savedInstanceState?.getString("userLogin").toString()

        Log.d("user login bundle", fromBundle)
        //view.findViewById<TextView>(R.id.textview_first).setText("Hello " + fromBundle)
        val userId : String = FirebaseAuth.getInstance().currentUser?.uid.toString()
        Log.d(TAG, "userId: ${userId}")
        val docRef = db.collection("Users_info").document(userId)
        docRef.get()
            .addOnSuccessListener { document ->
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
            val intent = Intent(
                this.activity as MainLoggedMenuActivity,
                MeasurementsActivity::class.java
            )

            startActivity(intent)
            //(this.activity)?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        logOutButton.setOnClickListener{

//
            FirebaseAuth.getInstance().signOut()

            Log.d("Successful log out", "Successful log out")
            updateUI(null)
            //findNavController().navigate(R.id.action_MainLoggedMenu_to_MainViewFragment)
            (this.activity as MainLoggedMenuActivity).onBackPressed()
            val intent = Intent(this.activity as MainLoggedMenuActivity, MainActivity::class.java)
            startActivity(intent)
            //findNavController().navigate(R.id.action_MainLoggedMenu_to_MainViewFragment)

        }

        medicinesButton.setOnClickListener{
            val intent = Intent(
                this.activity as MainLoggedMenuActivity,
                MedicinesActivity::class.java
            )
            startActivity(intent)
            //findNavController().navigate(R.id.action_MainLoggedMenu_to_MedicinesFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        val userId : String = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val docRef = db.collection("Users_info").document(userId)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null){

                    //userLogin= document.data.toString()
                    currentLogin = document.data?.getValue("name").toString()
                }
        if (viewFragmentFragment.findViewById<TextView>(R.id.textview_first).text.toString() != "Hello ${currentLogin}")
        {
            viewFragmentFragment.findViewById<TextView>(R.id.textview_first).setText("Hello ${currentLogin}")
        }

    }
    }

    private fun updateUI(user: FirebaseUser?) {}



}


