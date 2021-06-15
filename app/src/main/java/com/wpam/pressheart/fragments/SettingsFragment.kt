package com.wpam.pressheart.fragments

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.wpam.pressheart.MainActivity
import com.wpam.pressheart.R
import com.wpam.pressheart.SettingsActivity
import kotlinx.android.synthetic.main.fragment_medicines.*
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment: Fragment() {

    private val db = Firebase.firestore
    private val userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
    private val refUser = db.collection("Users_info").document(userId)
    private var newUserLog = ""
    private var userLog = ""
    private var newEmail = ""
    private var oldEmail = ""
    private var deleteUser : String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        refUser.get()
            .addOnSuccessListener { document ->
                if (document != null){
                    userLog = document.data?.getValue("name").toString()
                    newUserLog = userLog
                    newEmail = document.data?.getValue("email").toString()
                    oldEmail = newEmail
                    view.findViewById<EditText>(R.id.editTextPersonLogin).hint = userLog
                    view.findViewById<EditText>(R.id.editTextPersonLogin).setText(userLog)
                    view.findViewById<EditText>(R.id.editTextNewEmail).hint = newEmail
                    view.findViewById<EditText>(R.id.editTextNewEmail).setText(newEmail)
                    view.findViewById<EditText>(R.id.editTextPasswordConfirm).setText("hehPassword")
                    view.findViewById<EditText>(R.id.editTextNewPasswordUpdate).setText("NewPassword")
                }
                else
                {
                    Toast.makeText(this.context, "Something went wrong while fetching user's info", Toast.LENGTH_LONG).show()
                }
            }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        buttonUpdate.setOnClickListener {
            newUserLog = view.findViewById<EditText>(R.id.editTextPersonLogin).text.toString()
            newEmail = view.findViewById<EditText>(R.id.editTextNewEmail).text.toString()
            if(newUserLog != userLog){
                refUser.update("name", newUserLog)
                Firebase.auth.currentUser.updateProfile(userProfileChangeRequest{
                    displayName = newUserLog
                }).addOnSuccessListener {  Toast.makeText(view.context, "Successfully changed user's login", Toast.LENGTH_SHORT).show() }

            }
            if(newEmail != oldEmail){
                if(SignUpWindow.isValidString(newEmail)){
                refUser.update("email", newEmail).addOnSuccessListener { Log.d(TAG, "Done in data") }
                Firebase.auth.currentUser!!.updateEmail(newEmail).addOnSuccessListener { }
                    Toast.makeText(view.context, "Successfully changed user's e-mail", Toast.LENGTH_SHORT).show()
                }
                else{
                    view.findViewById<EditText>(R.id.editTextNewEmail).error = "Bad e-mail format"
                }
            }
                if(view.findViewById<EditText>(R.id.editTextPasswordConfirm).text.toString() == view.findViewById<EditText>(R.id.editTextNewPasswordUpdate).text.toString()){
                    Log.d(TAG, "jestem tuuu")
                    val neewPas = view.findViewById<EditText>(R.id.editTextPasswordConfirm).text.toString()
                    if(neewPas.length >= 6){
                    Firebase.auth.currentUser!!.updatePassword(neewPas).addOnSuccessListener { Log.d(TAG, "CHANGED THIS") }
                    Toast.makeText(view.context, "Successfully changes user's password", Toast.LENGTH_SHORT).show()}
                    else{ view.findViewById<EditText>(R.id.editTextPasswordConfirm).error = "Insert longer password (min. 6 letters)"
                    view.findViewById<EditText>(R.id.editTextNewPasswordUpdate).error = "Insert longer password (min. 6 letters)"}
                }
                else{
                    Toast.makeText(this.context, "Something went wrong, check new password", Toast.LENGTH_LONG).show()
                }

            Toast.makeText(this.context, "Updated user's info", Toast.LENGTH_LONG).show()
        }

        buttonDeleteUser.setOnClickListener {

            val view = LayoutInflater.from(this.context).inflate(R.layout.dialoge_are_you_sure, null)
            val dialogWindow = this.context?.let { it1 ->
                AlertDialog.Builder(it1)
                    .setView(view)
                    .setCancelable(false)
                    .setPositiveButton("Yes") { _, _ ->
                        run {
                            deleteUser = "yes"
                            val userid = Firebase.auth.currentUser.uid
                            db.collection("Measurements").document(userid)
                            db.collection("Medicines").document(userid)
                            refUser.delete()
                            Firebase.auth.currentUser!!.delete().addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    updateUI(null)
                                    val intent = Intent(this.activity as SettingsActivity, MainActivity::class.java)
                                    startActivity(intent)
                                    (this.activity as SettingsActivity).finish()
                                }
                                else {
                                    Toast.makeText(this.context, "Something went wrong with deleting user", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }
                    .setNegativeButton("No") { _, _ ->
                        run {
                            deleteUser = "no"
                        }
                    }
                    .create()
            }
            if (dialogWindow != null) {
                dialogWindow.show()
            }
        }


    }
    private fun updateUI(user: FirebaseUser?) {}
}