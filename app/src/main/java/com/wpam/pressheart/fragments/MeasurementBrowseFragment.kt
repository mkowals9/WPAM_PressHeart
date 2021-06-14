package com.wpam.pressheart.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.wpam.pressheart.MeasurementsActivity
import com.wpam.pressheart.R
import com.wpam.pressheart.lists_content.SingleMeasurement
import kotlinx.android.synthetic.main.fragment_measurement_browse.*
import kotlin.time.ExperimentalTime


class MeasurementBrowseFragment : Fragment() {

    private var db = Firebase.firestore
    private lateinit var measurementsRecyclerView: RecyclerView
    private lateinit var measurementsArrayList : ArrayList<SingleMeasurement>
    private lateinit var viewViewView : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    @ExperimentalTime
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_measurement_browse, container, false)
        viewViewView = view
        measurementsRecyclerView = view.findViewById(R.id.MeasurementsBrowseList)
        measurementsRecyclerView.layoutManager = LinearLayoutManager (this.activity as MeasurementsActivity)
        measurementsRecyclerView.setHasFixedSize(true)
        measurementsArrayList = arrayListOf<SingleMeasurement>()
        getMeasurementData()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        floatingAddingMesButton.setOnClickListener {
            findNavController().navigate(R.id.action_MeasurementMain_to_AddMeasurement)
        }

    }

    @ExperimentalTime
    private fun getMeasurementData(){
        val userId : String = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val docRef = db.collection("Measurements").document(userId).collection("Measurements").get()
            docRef.addOnSuccessListener { documents -> for (document in documents)
            {
                val measurement = document.toObject<SingleMeasurement>()
                measurement.documentId = document.id
                measurementsArrayList.add(measurement)
            }

                measurementsRecyclerView.adapter = MeasurementAdapter(measurementsArrayList, this.activity as MeasurementsActivity)

            }
            .addOnFailureListener{ _ -> Log.w(TAG, "Upsi, dupsi")  }
    }


}