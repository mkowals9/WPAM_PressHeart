package com.wpam.pressheart.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues.TAG
import android.icu.util.Calendar
import android.os.Build.VERSION_CODES.O
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.wpam.pressheart.MeasurementsActivity
import com.wpam.pressheart.R
import kotlinx.android.synthetic.main.fragment_add_new_measurement.*
import java.text.SimpleDateFormat

import java.util.*


@Suppress("DEPRECATION")
class AddNewMeasurementFragment : Fragment() {

    private var datelbl = ""
    private var timelbl = ""
    private var mood = ""
    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_new_measurement, container, false)
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //datepicker
        dateButton.setOnClickListener {
            val c = Calendar.getInstance()
            val day = c.get(Calendar.DAY_OF_MONTH)
            val month = c.get(Calendar.MONTH)
            val year = c.get(Calendar.YEAR)

            val dpd = DatePickerDialog(
                this.activity as MeasurementsActivity,
                DatePickerDialog.OnDateSetListener { _, chosenYear, monthOfYear, dayOfMonth ->

                    // Display Selected date in textbox
                    c.set(chosenYear, monthOfYear, dayOfMonth)
                    this.datelbl = SimpleDateFormat("yyyy-MM-dd").format(c.time)

                },
                year,
                month,
                day
            )


            dpd.show()

        }

        timeButton.setOnClickListener {
            val c = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                c.set(Calendar.HOUR_OF_DAY, hour)
                c.set(Calendar.MINUTE, minute)

                timelbl = SimpleDateFormat("HH:mm").format(c.time)

            }
            TimePickerDialog(
                this.activity as MeasurementsActivity,
                timeSetListener,
                c.get(Calendar.HOUR_OF_DAY),
                c.get(
                    Calendar.MINUTE
                ),
                true
            ).show()
        }

        happyButton.setOnClickListener{
            this.mood = "happy"
        }

        angryButton.setOnClickListener {
            this.mood = "angry"
        }

        sadButton.setOnClickListener {
            this.mood = "sad"
        }

        tiredButton.setOnClickListener {
            this.mood = "tired"
        }

        saveMeasurementButton.setOnClickListener {
            val together = datelbl +" "+  timelbl
            var formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
            val temporaryDate : Date = formatter.parse(together)
            val timeStampMeasure = Timestamp(temporaryDate)
            val SbpPressure = SbpEditTextNumber.getText().toString().toInt()
            val DbpPressure = DbpEditTextNumber.getText().toString().toInt()

            if(SbpPressure<300 && DbpPressure<SbpPressure && DbpPressure<300){
            val newMeasurement = hashMapOf(
                "DiastolicBP" to DbpPressure,
                "SystolicBP" to SbpPressure,
                "Mood" to this.mood,
                "Date" to timeStampMeasure
            )

            val newId = FirebaseAuth.getInstance().currentUser.uid
            db.collection("Measurements").document(newId).collection("Measurements").add(newMeasurement)
            Log.d(TAG, "DONE ADDDING")
                findNavController().navigate(R.id.action_AddMeasurement_to_MainMeasurement)
            }
            else
            {
                Toast.makeText((this.activity as MeasurementsActivity), "Wrong values.",
                    Toast.LENGTH_LONG).show()
            }
        }
    }
}