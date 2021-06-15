package com.wpam.pressheart.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.wpam.pressheart.MainActivity
import com.wpam.pressheart.MainLoggedMenuActivity
import com.wpam.pressheart.R
import com.wpam.pressheart.dialogs.EmptyValuesDialog
import kotlinx.android.synthetic.main.main_view_window.*


class MainViewWindow : Fragment() {

    private val db = Firebase.firestore
    private var login = ""

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.main_view_window, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_log_in.setOnClickListener {
            var email = editTextLogin.text.toString()
            var password = (editTextPasswordSignIn.getText().toString())
            if (!email.isEmpty() && !password.isEmpty() && SignUpWindow.isValidString(email)) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this.activity as MainActivity) { task ->
                        if (task.isSuccessful) {
                            val user = FirebaseAuth.getInstance().currentUser
                            updateUI(user)
                            val docRef = db.collection("Users_info").document(user.uid)
                            docRef.get()
                                .addOnSuccessListener { document ->
                                    if (document != null) {
                                        this.login = document.data?.getValue("name").toString()
                                    } else {
                                        this.login = "user"
                                    }
                                    val intent = Intent(
                                        this.activity as MainActivity,
                                        MainLoggedMenuActivity::class.java
                                    )
                                    intent.putExtra("userName", this.login)
                                    startActivity(intent)
                                    (this.activity as MainActivity).finish()
                                }
                        }
                        else {
                            Toast.makeText(this.context, "Can't log in", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this.context, "Can't log in, check your credentials", Toast.LENGTH_LONG).show()
                    }
            } else {
                if(!SignUpWindow.isValidString(email)){
                    editTextLogin.error = "Bad e-mail format"
                }
                if(email.isEmpty()) {editTextLogin.error = "Insert your e-mail"}
                if(editTextPasswordSignIn.text.isEmpty()){  editTextPasswordSignIn.error = "Insert your password"}
                else{
                EmptyValuesDialog(this.activity as MainActivity).show()}
            }
        }
        button_sign_up.setOnClickListener{
            findNavController().navigate(R.id.action_MainViewFragment_to_SignUpFragment)
        }
    }

    private fun updateUI(user: FirebaseUser?) {}
}
