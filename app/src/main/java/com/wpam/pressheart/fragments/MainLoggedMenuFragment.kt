package com.wpam.pressheart.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.wpam.pressheart.MainLoggedMenu
import com.wpam.pressheart.R
import kotlinx.android.synthetic.main.fragment_main_logged_menu.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */

class MainLoggedMenuFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_logged_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //view.findViewById<Button>(R.id.measurementsButton).setOnClickListener {
          //  findNavController().navigate(R.id.action_MainLoggedMenu_to_MeasurementsFragment)
        //}
        measurementsButton.setOnClickListener{
            findNavController().navigate(R.id.action_MainLoggedMenu_to_MeasurementsFragment)
        }

        logOutButton.setOnClickListener{

//
            FirebaseAuth.getInstance().signOut()

            Log.d("Successful log out" , "Successful log out")
            updateUI(null)
            //findNavController().navigate(R.id.action_MainLoggedMenu_to_MainViewFragment)
            (this.activity as MainLoggedMenu).onBackPressed()
            //findNavController().navigate(R.id.action_MainLoggedMenu_to_MainViewFragment)

        }

        medicinesButton.setOnClickListener{
            findNavController().navigate(R.id.action_MainLoggedMenu_to_MedicinesFragment)
        }
    }

    private fun updateUI(user: FirebaseUser?) {}



}


