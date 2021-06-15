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
import java.lang.Exception
import java.text.SimpleDateFormat

import java.util.*


@Suppress("DEPRECATION")
class AddNewMeasurementFragment : Fragment() {

    private var datelbl = ""
    private var timelbl = ""
    private var mood = ""
    private lateinit var temporaryDate : Date
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

        dateButton.setOnClickListener {
            val c = Calendar.getInstance()
            val day = c.get(Calendar.DAY_OF_MONTH)
            val month = c.get(Calendar.MONTH)
            val year = c.get(Calendar.YEAR)
            this.datelbl = SimpleDateFormat("yyyy-MM-dd").format(c.time)

            val dpd = DatePickerDialog(
                this.activity as MeasurementsActivity,
                { _, chosenYear, monthOfYear, dayOfMonth ->

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
            timelbl = SimpleDateFormat("HH:mm").format(c.time)
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
            if(everythingIsFine()){
            val together = datelbl +" "+  timelbl
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
                temporaryDate = Date(2021,6,16)
                try{
                temporaryDate = formatter.parse(together)
                }
                catch(e: Exception){
                    Log.d(TAG, e.toString())
                }
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
                findNavController().navigate(R.id.action_AddMeasurement_to_MainMeasurement)
            }
            else
            {
                Toast.makeText((this.activity as MeasurementsActivity), "Wrong values in Systolic or Diastolic Blood Pressure.",
                    Toast.LENGTH_LONG).show()
                if(SbpPressure> 300 || SbpPressure<DbpPressure){ SbpEditTextNumber.error = "Wrong value" }
                if(DbpPressure>300 || DbpPressure>SbpPressure) {DbpEditTextNumber.error = "Wrong value"}
            }
            }
            else{
                if(datelbl == "") { dateButton.error = "Choose measurement's date"
                    }
                if (timelbl == "") {timeButton.error = "Choose measurement's time"}
                if(mood == "") {Toast.makeText((this.activity as MeasurementsActivity), "Choose your mood",
                    Toast.LENGTH_LONG).show()}
                if(SbpEditTextNumber.getText().toString() == "") { SbpEditTextNumber.error = "Fill the gap"}
                if(DbpEditTextNumber.getText().toString() == "") { DbpEditTextNumber.error = "Fill the gap"}
            }
        }
    }

    private fun everythingIsFine() : Boolean {
        return datelbl != "" &&
                timelbl != "" &&
                this.mood != "" &&
                SbpEditTextNumber.getText().toString() != "" &&
                DbpEditTextNumber.getText().toString() != ""
    }
}