package com.wpam.pressheart.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.wpam.pressheart.MainLoggedMenuActivity
import com.wpam.pressheart.MeasurementsActivity
import com.wpam.pressheart.R
import kotlinx.android.synthetic.main.fragment_add_new_measurement.*
import kotlinx.android.synthetic.main.fragment_measurements.*
import java.text.SimpleDateFormat
import java.time.YearMonth
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS


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
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //datepicker
        dateButton.setOnClickListener {
            val c = Calendar.getInstance()
            val day = c.get(Calendar.DAY_OF_MONTH)
            val month = c.get(Calendar.MONTH)
            val year = c.get(Calendar.YEAR)

            val dpd = DatePickerDialog(this.activity as MeasurementsActivity, DatePickerDialog.OnDateSetListener { view, chosenYear, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox

                this.datelbl = ("${monthOfYear+1}/${dayOfMonth}/${chosenYear}")
                Log.d(TAG, "lblDate : ${this.datelbl}")
                Log.d(TAG, "day: ${day} month: ${month} year: ${year}")
            }, year, month, day)


            dpd.show()

            Log.d(TAG, "lblDate po  : ${this.datelbl}")
            Log.d(TAG, "day po : ${day} month: ${month} year: ${year}")
        }

        timeButton.setOnClickListener {

            val c = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                c.set(Calendar.HOUR_OF_DAY, hour)
                c.set(Calendar.MINUTE, minute)

                timelbl = SimpleDateFormat("HH:mm").format(c.time)
            }
            TimePickerDialog(this.activity as MeasurementsActivity, timeSetListener, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show()
            Log.d(TAG, "lbltime po  : ${this.timelbl}")
        }

    }
}