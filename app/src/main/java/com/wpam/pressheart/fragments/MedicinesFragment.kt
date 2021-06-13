package com.wpam.pressheart.fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.wpam.pressheart.MedicinesActivity
import com.wpam.pressheart.R
import com.wpam.pressheart.lists_content.SingleMedicine
import kotlinx.android.synthetic.main.fragment_medicines.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class MedicinesFragment: Fragment()  {

    private var db = Firebase.firestore
    private val storageFirebase = FirebaseStorage.getInstance().getReference()
    private lateinit var medicinesRecyclerView: RecyclerView
    private lateinit var medicinesArrayList : ArrayList<SingleMedicine>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_medicines, container, false)
        medicinesRecyclerView = view.findViewById(R.id.medicinesList)
        medicinesRecyclerView.layoutManager = LinearLayoutManager (this.activity as MedicinesActivity)
        medicinesRecyclerView.setHasFixedSize(true)
        medicinesArrayList = arrayListOf<SingleMedicine>()
        getMedicinesData()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AddNewMedicineButton.setOnClickListener {
            findNavController().navigate(R.id.action_MedicinesMain_to_AddMedicine)
        }

    }


    private fun getMedicinesData(){
        val userId : String = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val docRef = db.collection("Medicines").document(userId).collection("Medicines").get()
            docRef.addOnSuccessListener { documents -> for (document in documents)
            {

                val medicine = document.toObject<SingleMedicine>()
                medicine.documentId = document.id
                medicinesArrayList.add(medicine)
            }

                medicinesRecyclerView.adapter = MedicineAdapter(medicinesArrayList)
            }
            .addOnFailureListener{ _ -> Log.w(ContentValues.TAG, "Upsi, dupsi medicine")  }
    }


}