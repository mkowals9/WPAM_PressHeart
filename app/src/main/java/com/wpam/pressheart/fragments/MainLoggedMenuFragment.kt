package com.wpam.pressheart.fragments

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.wpam.pressheart.MainActivity
import com.wpam.pressheart.MainLoggedMenu
import com.wpam.pressheart.R
import kotlinx.android.synthetic.main.main_logged_menu_fragment.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MainLoggedMenuFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.main_logged_menu_fragment, container, false)
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

//            FirebaseAuth.getInstance()
//                .signOut().addOnCompleteListener(this.activity as MainLoggedMenu) { task ->
//                    if (task.isSuccessful) {
//                        updateUI(null)
//                        findNavController().navigate(R.id.action_MainLoggedMenu_to_MainViewFragment) }
//                    else {
//                        Log.d(ContentValues.TAG, "UPS SOMETHING WENT WRONG WITH LOGGING OUT")}
//                }
            FirebaseAuth.getInstance().signOut()
            if(FirebaseAuth.getInstance().currentUser != null)
            {
                Log.d("Successful log out" , "Successful log out")
                //findNavController().navigate(R.id.action_MainLoggedMenu_to_MainViewFragment)
                val intent = Intent(this.activity as MainLoggedMenu, MainActivity::class.java)
                startActivity(intent)
            }
            else{
                Log.d("Something went wrong", "Something went wrong")
            }

        }
    }

    private fun updateUI(user: FirebaseUser?) {}
}
