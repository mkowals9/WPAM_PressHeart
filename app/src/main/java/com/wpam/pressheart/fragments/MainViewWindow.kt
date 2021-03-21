package com.wpam.pressheart.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.wpam.pressheart.MainActivity
import com.wpam.pressheart.R
import kotlinx.android.synthetic.main.fragment_sign_up_window.*
import kotlinx.android.synthetic.main.main_view_window.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MainViewWindow : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.main_view_window, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_log_in.setOnClickListener{
            var email = editTextLogin.text.toString()
            var password = editTextNumberPasswordSignIn.text.toString()
            Log.d(TAG, "email: $email and password: $password")
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this.activity as MainActivity) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = FirebaseAuth.getInstance().currentUser
                        updateUI(user)
                        findNavController().navigate(R.id.action_MainViewFragment_to_MainLoggedFragment)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        updateUI(null)
                    }
                }
        }

        button_sign_up.setOnClickListener{
            findNavController().navigate(R.id.action_MainViewFragment_to_SignUpFragment)
        }
//        view.findViewById<Button>(R.id.button_log_in).setOnClickListener {
//
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//        }
    }

    private fun updateUI(user: FirebaseUser?) {}
}