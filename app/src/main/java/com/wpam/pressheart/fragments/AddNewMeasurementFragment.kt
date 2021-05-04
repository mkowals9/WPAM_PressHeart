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
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.firebase.Timestamp
import com.wpam.pressheart.MeasurementsActivity
import com.wpam.pressheart.R
import kotlinx.android.synthetic.main.fragment_add_new_measurement.*
import kotlinx.android.synthetic.main.fragment_measurements.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ofPattern
import java.time.temporal.TemporalQueries.localDate
import java.util.*


@Suppress("DEPRECATION")
class AddNewMeasurementFragment : Fragment() {

    private var datelbl = ""
    private var timelbl = ""


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
                DatePickerDialog.OnDateSetListener { view, chosenYear, monthOfYear, dayOfMonth ->

                    // Display Selected date in textbox
                    c.set(chosenYear, monthOfYear, dayOfMonth)
                    this.datelbl = SimpleDateFormat("yyyy-MM-dd").format(c.time)
                    Log.d(TAG, "lblDate : ${this.datelbl}")
                    Log.d(TAG, "day: ${day} month: ${month} year: ${year}")
                },
                year,
                month,
                day
            )


            dpd.show()

        }

        timeButton.setOnClickListener {
            val c = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                c.set(Calendar.HOUR_OF_DAY, hour)
                c.set(Calendar.MINUTE, minute)

                timelbl = SimpleDateFormat("HH:mm").format(c.time)

                Log.d(TAG, "timelbl: ${timelbl}")
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


        saveMeasurementButton.setOnClickListener {
            Log.d(TAG, "data przed timestamp: ${datelbl}, time przed timestamp: ${timelbl}")

            val together = datelbl +" "+  timelbl
            var formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
            val temporaryDate : Date = formatter.parse(together)
            val temporaryDateString = temporaryDate.toString()
            val timeStampMeasure = Timestamp(temporaryDate)
            Log.d(TAG, "tempDate: ${temporaryDateString} ,  timeStaml: ${timeStampMeasure.toDate().toString()}")



        }
    }
}