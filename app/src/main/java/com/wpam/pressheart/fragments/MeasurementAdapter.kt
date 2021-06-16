package com.wpam.pressheart.fragments


import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.wpam.pressheart.R
import com.wpam.pressheart.lists_content.SingleMeasurement
import kotlinx.android.synthetic.main.fragment_add_new_measurement.*
import kotlinx.android.synthetic.main.fragment_add_new_measurement.view.*
import java.lang.Exception
import java.security.Key
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.time.*

@ExperimentalTime
class MeasurementAdapter(private val measurementsList: ArrayList<SingleMeasurement>, val context : Context) :
    RecyclerView.Adapter<MeasurementAdapter.MyViewHolder>(){

    private lateinit var parentAdapter  : ViewGroup

    @ExperimentalTime
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        parentAdapter = parent
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.single_measurement,
            parent, false
        )
        return MyViewHolder(itemView, this)
    }

    @SuppressLint("SimpleDateFormat")
    @ExperimentalTime
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = measurementsList[position]
        var datedate = SimpleDateFormat("yyyy-MM-dd HH:mm").format(currentItem.Date.toDate())
        holder.Date.text = datedate.toString()
        holder.DiastolicBP.text = currentItem.DiastolicBP.toString()
        holder.Mood.text = currentItem.Mood
        holder.SystolicBP.text = currentItem.SystolicBP.toString()
    }

    override fun getItemCount(): Int {
        return measurementsList.size
    }

    fun getItem (pos : Int): SingleMeasurement {
        return measurementsList[pos]
    }

    @ExperimentalTime
    @SuppressLint("ResourceType", "CutPasteId")
    @RequiresApi(Build.VERSION_CODES.N)
    class MyViewHolder(itemView: View, adapter : MeasurementAdapter) : RecyclerView.ViewHolder(itemView) {
        val Date : TextView = itemView.findViewById(R.id.date_Measurement_Browse)
        val DiastolicBP : TextView = itemView.findViewById(R.id.diastolicBP_Measurement_Browse)
        val Mood : TextView = itemView.findViewById(R.id.mood_Measurement_Browse)
        val SystolicBP : TextView = itemView.findViewById(R.id.systolicBP_Measurement_Browse)
        val changeButton : Button = itemView.findViewById(R.id.changeMeasurementButton)
        val deleteButton : Button = itemView.findViewById(R.id.deleteMeasurementButton)

        private var db = Firebase.firestore
        private var result : String = ""
        private var newDateChosen : String = ""
        private var newHourChosen : String = ""
        private var oldMood = ""
        private var changedMood = false
        private var posSpinner = -1
        private var saveChanges : Boolean = false

            init{
                changeButton.setOnClickListener {
                    var currentItem = adapter.getItem(this.adapterPosition)
                    val arrayMoods = itemView.context.resources.getStringArray(R.array.moods)
                    val userId : String = FirebaseAuth.getInstance().currentUser?.uid.toString()
                    var docRef = db.collection("Measurements").document(userId).collection("Measurements")
                    oldMood = this.Mood.text.toString()
                    try{
                    this.newHourChosen = SimpleDateFormat("HH:mm").format(currentItem.Date.toDate()).toString()
                    this.newDateChosen = SimpleDateFormat("yyyy-MM-dd").format(currentItem.Date.toDate()).toString()}
                    catch (e: Exception){
                        Log.d(TAG, "Failure in parsing")
                        this.newHourChosen = "19:00"
                        this.newDateChosen = "2021-06-16"
                    }
                    val viewDialog = LayoutInflater.from(adapter.parentAdapter.context).inflate(R.layout.dialoge_edit_measurement, null)
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        viewDialog.findViewById<DatePicker>(R.id.spinner_date).init(Calendar.getInstance().get(Calendar.YEAR),
                            Calendar.getInstance().get(Calendar.MONTH),
                            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)){
                                _, year, month, day ->
                            val month_real = month + 1
                            if(month_real<10)
                            {
                                if(day<10){
                                newDateChosen = "${year}-0${month_real}-0${day}"}
                                else{
                                    newDateChosen = "${year}-0${month_real}-${day}"
                                }
                            }
                            else{
                                if(day<10){
                                    newDateChosen = "${year}-${month_real}-0${day}"}
                                else{
                                    newDateChosen = "${year}-${month_real}-${day}"
                                }
                            }
                        }

                    }
                    viewDialog.findViewById<TimePicker>(R.id.spinner_time).setOnTimeChangedListener { _, hourOfDay, minute ->

                        if(hourOfDay<10){
                            if(minute<10){
                                newHourChosen = "0${hourOfDay}:0${minute}"
                            }
                            else{
                                newHourChosen = "$0{hourOfDay}:${minute}"
                            }
                        }
                        else{
                            if(minute<10){
                                newHourChosen = "${hourOfDay}:0${minute}"
                            }
                            else{
                                newHourChosen = "${hourOfDay}:${minute}"
                            }
                        }
                    }

                    val arrayAdapter = ArrayAdapter(itemView.context, android.R.layout.simple_spinner_dropdown_item, arrayMoods)
                    viewDialog.findViewById<Spinner>(R.id.spinner_moods).adapter = arrayAdapter
                    viewDialog.findViewById<Spinner>(R.id.spinner_moods).onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            changedMood = true
                            arrayAdapter.setNotifyOnChange(true)
                            posSpinner = position
                            oldMood = arrayMoods[position]

                        }
                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            changedMood = false
                        }

                    }

                    val dialogWindowChange = AlertDialog.Builder(adapter.context)
                        .setView(viewDialog)
                        .setCancelable(false)
                        .setPositiveButton("Save"){ _, _ ->
                            run {
                                saveChanges = true
                                if(saveChanges){
                                    val currentItemSave = adapter.getItem(this.adapterPosition)
                                    val doc = docRef.document(adapter.getItem(this.adapterPosition).documentId)
                                    doc.get()
                                        .addOnSuccessListener { document ->
                                            if(posSpinner!= -1 && oldMood != document.data?.get("Mood") && oldMood != currentItemSave.Mood && changedMood){
                                                doc.update("Mood", oldMood)
                                                currentItemSave.Mood = oldMood
                                                changedMood = false
                                                posSpinner = -1
                                                adapter.notifyItemChanged(adapterPosition)
                                            }
                                            val newSBPvalue = viewDialog.findViewById<EditText>(R.id.editText_value_SBP_change).text.toString()
                                            val newDBPvalue = viewDialog.findViewById<EditText>(R.id.editText_value_DBP_change).text.toString()
                                            if(newSBPvalue != document.data?.get("SystolicBP").toString() && newSBPvalue != currentItemSave.SystolicBP.toString() &&
                                                    newDBPvalue.matches(Regex("[0-9]+")) && newSBPvalue.matches(Regex("[0-9]+"))){
                                                if(newSBPvalue.toInt()<300 && newSBPvalue>newDBPvalue ){
                                                doc.update("SystolicBP", newSBPvalue.toLong())
                                                currentItem.SystolicBP = newSBPvalue.toLong()
                                                adapter.notifyItemChanged(adapterPosition)}
                                                else{
                                                    viewDialog.findViewById<EditText>(R.id.editText_value_SBP_change).error = "Wrong value"
                                                }
                                            }
                                            else{
                                                if(!newSBPvalue.matches(Regex("[0-9]+"))) { viewDialog.findViewById<EditText>(R.id.editText_value_SBP_change).error = "Only number"}
                                                if(!newDBPvalue.matches(Regex("[0-9]+"))) { viewDialog.findViewById<EditText>(R.id.editText_value_DBP_change).error = "Only number"}
                                            }
                                            if(newDBPvalue != document.data?.get("DiastolicBP") &&
                                                newDBPvalue != currentItemSave.DiastolicBP.toString() &&
                                                newDBPvalue.matches(Regex("[0-9]+")) &&
                                                newSBPvalue.matches(Regex("[0-9]+"))){
                                                if(newDBPvalue.toInt()<300 && newSBPvalue>newDBPvalue){
                                                doc.update("DiastolicBP", newDBPvalue.toLong())
                                                currentItem.DiastolicBP = newDBPvalue.toLong()
                                                adapter.notifyItemChanged(adapterPosition)}
                                                else { viewDialog.findViewById<EditText>(R.id.editText_value_DBP_change).error = "Wrong value"}
                                            }
                                            else {
                                                if(!newSBPvalue.matches(Regex("[0-9]+"))) { viewDialog.findViewById<EditText>(R.id.editText_value_SBP_change).error = "Only number"}
                                                if(!newDBPvalue.matches(Regex("[0-9]+"))) { viewDialog.findViewById<EditText>(R.id.editText_value_DBP_change).error = "Only number"}
                                            }
                                            val onlyHour = SimpleDateFormat("HH:mm").format(currentItemSave.Date.toDate()).toString()
                                            val onlyDate = SimpleDateFormat("YYYY-MM-dd").format(currentItemSave.Date.toDate()).toString()
                                            if(newDateChosen != onlyDate || newHourChosen != onlyHour){
                                                val timestampToUpdatetext = "${newDateChosen} ${newHourChosen}"
                                                val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
                                                val temporaryDate : Date = formatter.parse(timestampToUpdatetext)
                                                val timeStampMeasure = Timestamp(temporaryDate)
                                                doc.update("Date", timeStampMeasure)
                                                Log.d(TAG, timestampToUpdatetext)
                                                currentItemSave.Date = timeStampMeasure
                                                adapter.notifyItemChanged(adapterPosition)
                                            }
                                            adapter.notifyDataSetChanged()
                                        }
                                }
                            }
                        }
                        .setNegativeButton("Cancel"){ dialog, _ ->  dialog.dismiss()}
                        .create()
                    viewDialog.findViewById<EditText>(R.id.editText_value_SBP_change).setText(this.SystolicBP.text.toString())
                    viewDialog.findViewById<EditText>(R.id.editText_value_DBP_change).setText(this.DiastolicBP.text.toString())
                    viewDialog.findViewById<TimePicker>(R.id.spinner_time).setIs24HourView(true)

                    dialogWindowChange.show()

                }

                deleteButton.setOnClickListener {
                    val currentPosition = adapter.getItem(this.adapterPosition)
                    val userId : String = FirebaseAuth.getInstance().currentUser?.uid.toString()
                    val docRef = db.collection("Measurements").document(userId).collection("Measurements")
                    val view = LayoutInflater.from(adapter.parentAdapter.context).inflate(R.layout.dialoge_are_you_sure, null)
                    val dialogWindow = AlertDialog.Builder(adapter.context)
                        .setView(view)
                        .setCancelable(false)
                        .setPositiveButton("Yes") { _, _ ->
                            run {
                                result = "yes"
                            }
                        }
                        .setNegativeButton("No") { _, _ ->
                            run {
                                result = "no"
                            }

                        }
                        .create()
                    dialogWindow.show()
                    dialogWindow.setOnDismissListener{
                        if (result == "yes" || result == "no") {
                            when (result) {
                                    "yes" -> {
                                        docRef.document(adapter.getItem(this.adapterPosition).documentId.toString()).delete()
                                        adapter.measurementsList.remove(currentPosition)
                                        adapter.notifyDataSetChanged()
                                        adapter.notifyItemRemoved(this.adapterPosition)
                                        adapter.notifyItemChanged(this.adapterPosition, adapter.itemCount)
                                    }
                                    "no" -> {
                                    }
                                }
                            }
                        }

                    }

                }
            }
        }