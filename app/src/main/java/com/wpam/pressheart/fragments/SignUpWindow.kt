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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.wpam.pressheart.MainActivity
import com.wpam.pressheart.MainLoggedMenuActivity
import com.wpam.pressheart.R
import com.wpam.pressheart.dialogs.EmptyValuesDialog
import kotlinx.android.synthetic.main.fragment_sign_up_window.*
import java.util.regex.Pattern


class SignUpWindow : Fragment(){

    private val db = Firebase.firestore
    private var gender = ""
    val EMAIL_ADDRESS_PATTERN = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return  inflater.inflate(R.layout.fragment_sign_up_window, container, false)
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

            if(!email.isEmpty() && !passwordFromEdit.isEmpty() && !this.gender.isEmpty() && !login.isEmpty() && isValidString(email))
            {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    email,
                    passwordFromEdit
                )
                    .addOnCompleteListener(this.activity as MainActivity) { task ->
                        if (task.isSuccessful) {
                            if(task.result?.additionalUserInfo?.isNewUser == true){
                            val user = FirebaseAuth.getInstance().currentUser
                            val user2 = Firebase.auth.currentUser
                            val profileUpdates = userProfileChangeRequest { displayName = login.toString() }
                            user2!!.updateProfile(profileUpdates)
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
                            intent.putExtra("userName", login)
                            startActivity(intent)
                            (this.activity as MainActivity).finish()
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            EmptyValuesDialog(this.activity as MainActivity).show()
                            updateUI(null)
                        }
                        }
                        else{
                            if(passwordFromEdit.length<6){
                                editPasswordSignUp.error = "Insert longer password"
                            }
                            
                            else {
                                editTextTextEmailAddress.error = "There's an user created with such e-mail, choose another"
                            }
                                }
                    }

            }
            else {
                if(email.isEmpty()){
                    editTextTextEmailAddress.error = "Fill the gap"
                }
                if(passwordFromEdit.isEmpty()){
                    editPasswordSignUp.error = "Fill the gap"
                }
                if(login.isEmpty()){
                    editNameSignUp.error = "Fill the gap"
                }
                if(!isValidString(email)){
                    editTextTextEmailAddress.error = "Wrong e-mail format"
                }
                if(this.gender.isEmpty()){
                    Toast.makeText(
                        (this.activity as MainActivity), "Choose your gender.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }

    private fun updateUI(user: FirebaseUser?) {}

    private fun isValidString(str: String): Boolean{
        return EMAIL_ADDRESS_PATTERN.matcher(str).matches()
    }

}

