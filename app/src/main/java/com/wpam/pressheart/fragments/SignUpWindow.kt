package com.wpam.pressheart.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.wpam.pressheart.MainActivity
import com.wpam.pressheart.R
import kotlinx.android.synthetic.main.fragment_sign_up_window.*
import kotlinx.android.synthetic.main.main_view_window.*


class SignUpWindow : Fragment(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up_window, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sign_up_buttonSignUp.setOnClickListener{

            var email = editTextTextEmailAddress.text.toString()
            var password = editTextNumberPassword.text.toString()

            if(!email.isEmpty() && !password.isEmpty()){
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this.activity as MainActivity) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        //Log.d(TAG, "createUserWithEmail:success")
                        val user = FirebaseAuth.getInstance().currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        //Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        //Toast.makeText(baseContext, "Authentication failed.",
                          //  Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }
                }
            findNavController().navigate(R.id.action_SignUpFragment_to_MainLoggedFragment)
            }
        }

    }
    private fun updateUI(user: FirebaseUser?) {

    }
}

