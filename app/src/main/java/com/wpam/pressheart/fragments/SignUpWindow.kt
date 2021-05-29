package com.wpam.pressheart.fragments

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.wpam.pressheart.MainActivity
import com.wpam.pressheart.MainLoggedMenuActivity
import com.wpam.pressheart.R
import com.wpam.pressheart.dialogs.EmptyValuesDialog
import kotlinx.android.synthetic.main.fragment_sign_up_window.*


class SignUpWindow : Fragment(){

    private val db = Firebase.firestore
    private var gender = ""

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

        femaleImageButton.setOnClickListener {
            this.gender = "female"
        }

        maleImageButton.setOnClickListener {
            this.gender = "male"
        }

        sign_up_buttonSignUp.setOnClickListener{
            var login = editNameSignUp.text.toString()
            var email = editTextTextEmailAddress.text.toString()
            var passwordFromEdit = editPasswordSignUp.getText().toString()

            if(!email.isEmpty() && !passwordFromEdit.isEmpty() && !this.gender.isEmpty() && !login.isEmpty())
            {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    email,
                    passwordFromEdit
                )
                    .addOnCompleteListener(this.activity as MainActivity) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val user = FirebaseAuth.getInstance().currentUser
                            updateUI(user)
                            val intent = Intent(
                                this.activity as MainActivity,
                                MainLoggedMenuActivity::class.java
                            )

                            val userNewInfo = hashMapOf(
                                "email" to email,
                                "name" to login,
                                "gender" to this.gender
                            )
                            val newId = FirebaseAuth.getInstance().currentUser.uid
                            db.collection("Users_info").document(newId).set(userNewInfo)
                            startActivity(intent)
                            (this.activity as MainActivity).finish()
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            // TO DO DIALOG WINDOW WITH ERROR IN SINGING UP
                            EmptyValuesDialog(this.activity as MainActivity).show()
                            updateUI(null)
                        }
                    }

            }
            else {
                if (email.isEmpty() && passwordFromEdit.isEmpty()){
                    Toast.makeText((this.activity as MainActivity), "E-mail and password can't be empty.",
                        Toast.LENGTH_LONG).show()
                }
                if(email.isEmpty()){
                    Toast.makeText((this.activity as MainActivity), "E-mail is empty. Fill the gap.",
                        Toast.LENGTH_SHORT).show()
                }
                if(passwordFromEdit.isEmpty()){
                    Toast.makeText((this.activity as MainActivity), "Password is empty. Fill the gap.",
                        Toast.LENGTH_SHORT).show()
                }

                if(login.isEmpty()){
                    Toast.makeText((this.activity as MainActivity), "Login is empty. Fill the gap.",
                        Toast.LENGTH_SHORT).show()
                }

                if(this.gender.isEmpty()){
                    Toast.makeText((this.activity as MainActivity), "Choose your gender.",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun updateUI(user: FirebaseUser?) {}
}

