package com.wpam.pressheart.fragments

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.wpam.pressheart.MainActivity
import com.wpam.pressheart.MainLoggedMenu
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


            //if (TextUtils.isEmpty(editTextTextEmailAddress.text.toString()) || TextUtils.isEmpty(editTextNumberPasswordSignIn.text.toString()))

                var email = editTextTextEmailAddress.text.toString()
                //var passwordFromEdit = editTextNumberPasswordSignIn.text.toString()
                val password = "123456"
                Log.d(TAG, "email: $email")
                //Log.d(TAG,"passwordfromEdit : $passwordFromEdit")
                if(!email.isEmpty() && !password.isEmpty())
                {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this.activity as MainActivity) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success")
                                Log.d(TAG, "success email: $email")
                                Log.d(TAG, "success password: $password")
                                val user = FirebaseAuth.getInstance().currentUser
                                updateUI(user)
                                val intent = Intent(this.activity as MainActivity, MainLoggedMenu::class.java)
                                startActivity(intent)
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                                //Toast.makeText(baseContext, "Authentication failed.",
                                //  Toast.LENGTH_SHORT).show()
                                Log.d(TAG, "ble email: $email")
                                Log.d(TAG, "ble password: $password")
                                updateUI(null)
                            }
                        }
                    //findNavController().navigate(R.id.action_SignUpFragment_to_MainLoggedFragment)

                }
                else {
                    Log.d(TAG, "Something went wrong honey")
                }
//            } else
//            {
//                //TODO a dialogue window with a communcate of wrong value
//                editTextTextEmailAddress.setError("E-mail can not be empty")
//                Log.d(TAG, "Upsi pupsi nie przejdziesz dalej skarbie")
//            }

        }

    }

    private fun updateUI(user: FirebaseUser?) {}
}
