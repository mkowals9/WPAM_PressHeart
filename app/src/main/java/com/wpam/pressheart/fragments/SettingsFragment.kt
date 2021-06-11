package com.wpam.pressheart.fragments

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.media.audiofx.BassBoost
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.wpam.pressheart.MainActivity
import com.wpam.pressheart.MainLoggedMenuActivity
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
    private var newPassword= ""
    private var deleteUser : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        val userId : String = FirebaseAuth.getInstance().currentUser?.uid.toString()
        refUser.get()
            .addOnSuccessListener { document ->
                if (document != null){

                    //userLogin= document.data.toString()
                    userLog = document.data?.getValue("name").toString()
                    newUserLog = userLog
                    newEmail = document.data?.getValue("email").toString()
                    oldEmail = newEmail
                    view.findViewById<EditText>(R.id.editTextPersonLogin).setHint(userLog)
                    view.findViewById<EditText>(R.id.editTextPersonLogin).setText(userLog)
                    view.findViewById<EditText>(R.id.editTextNewEmail).setHint(newEmail)
                    view.findViewById<EditText>(R.id.editTextNewEmail).setText(newEmail)
                    view.findViewById<EditText>(R.id.editTextPassformConfirm).setText("hehPassword")
                    view.findViewById<EditText>(R.id.editTextNewPasswordUpdate).setText("NewPassword")
                }
                else
                {
                    Toast.makeText(this.context, "Something went wrong while fetching user's info", Toast.LENGTH_LONG)
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
                //view.findViewById<TextView>(R.id.textview_first).setText("Hello ${newUserLog}")
            }
            if(newEmail != oldEmail){
                refUser.update("email", newEmail)
                Firebase.auth.currentUser!!.updateEmail(newEmail)
            }
            view.findViewById<EditText>(R.id.editTextNewPasswordUpdate).addTextChangedListener {
                if(view.findViewById<EditText>(R.id.editTextPassformConfirm).text.toString() == view.findViewById<EditText>(R.id.editTextNewPasswordUpdate).text.toString()){
                    Firebase.auth.currentUser!!.updatePassword(view.findViewById<EditText>(R.id.editTextPassformConfirm).text.toString())
                }
                else{
                    Toast.makeText(this.context, "Something went wrong, check new password", Toast.LENGTH_LONG)
                }
            }

            Toast.makeText(this.context, "Updated user's info", Toast.LENGTH_LONG)
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
                            Log.d(TAG, "Yes yes yes")
                            Firebase.auth.currentUser!!.delete().addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d(TAG, "User account deleted.")
                                    db.collection("Measurements").document(userId)
                                    db.collection("Medicines").document(userId)
                                    refUser.delete()
                                    updateUI(null)
                                    val intent = Intent(this.activity as SettingsActivity, MainActivity::class.java)
                                    startActivity(intent)
                                    (this.activity as SettingsActivity).finish()
                                }
                                else {
                                    Toast.makeText(this.context, "Something wnet wrong with deleting user", Toast.LENGTH_LONG)
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