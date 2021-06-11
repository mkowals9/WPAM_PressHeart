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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.wpam.pressheart.R
import com.wpam.pressheart.lists_content.SingleMeasurement
import kotlinx.android.synthetic.main.fragment_add_new_measurement.view.*
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



    @ExperimentalTime
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = measurementsList[position]
        var datedate = SimpleDateFormat("yyyy-MM-dd HH:mm").format(currentItem.Date.toDate())
        holder.Date.setText(datedate.toString())
        holder.DiastolicBP.setText(currentItem.DiastolicBP.toString())
        holder.Mood.text = currentItem.Mood
        holder.SystolicBP.setText(currentItem.SystolicBP.toString())
    }

    override fun getItemCount(): Int {
        return measurementsList.size
    }

    fun getItem (pos : Int): SingleMeasurement {
        return measurementsList[pos]
    }

    @ExperimentalTime
    @SuppressLint("ResourceType")
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
        private var saveChanges : Boolean = false

            init{
                changeButton.setOnClickListener {
                    Log.d(TAG, "elo pomelo w change")
                    var currentItem = adapter.getItem(this.adapterPosition)
                    val arrayMoods = itemView.context.resources.getStringArray(R.array.moods)
                    val userId : String = FirebaseAuth.getInstance().currentUser?.uid.toString()
                    var docRef = db.collection("Measurements").document(userId).collection("Measurements")
                    oldMood = this.Mood.text.toString()
                    this.newHourChosen = SimpleDateFormat("HH:mm").format(currentItem.Date.toDate()).toString()
                    this.newDateChosen = SimpleDateFormat("yyyy-MM-dd").format(currentItem.Date.toDate()).toString()
                    val viewDialog = LayoutInflater.from(adapter.parentAdapter.context).inflate(R.layout.dialoge_edit_measurement, null)

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        viewDialog.findViewById<DatePicker>(R.id.spinner_date).init(Calendar.getInstance().get(Calendar.YEAR),
                            Calendar.getInstance().get(Calendar.MONTH),
                            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)){
                                _, year, month, day ->
                            var month_real = month + 1
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

                            Log.d(TAG, "chosen: ${newDateChosen}")
                            Log.d(TAG, "year: ${year} month: ${month+1} day: ${day}")
                        }

                    }
                    viewDialog.findViewById<TimePicker>(R.id.spinner_time).setOnTimeChangedListener { _, hourOfDay, minute ->

                        Log.d(TAG, "bum bum time ${hourOfDay}, ${minute}")

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
                        Log.d(TAG, "ZMIENIAM GODZINE : ${newHourChosen}")
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
                            oldMood = arrayMoods[position]
                            Log.d(TAG, "Chosen mood: ${arrayMoods[position]}")
                        }
                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            Log.d(TAG, "Nothing chosen in months")
                        }

                    }


                    val dialogWindowChange = AlertDialog.Builder(adapter.context)
                        .setView(viewDialog)
                        .setCancelable(false)
                        .setPositiveButton("Save"){ _, _ ->
                            run {
                                saveChanges = true
                                Log.d(TAG, "saveChanges: ${saveChanges}")
                                if(saveChanges){
                                    var currentItemSave = adapter.getItem(this.adapterPosition)
                                    Log.d(TAG, "pokazuj sie mendo ${newHourChosen} , ${newDateChosen}")
                                    var doc = docRef.document(adapter.getItem(this.adapterPosition).documentId.toString())
                                    doc.get()
                                        .addOnSuccessListener { document ->
                                            if(oldMood != document.data?.get("Mood") && oldMood != currentItemSave.Mood){
                                                doc.update("Mood", oldMood)
                                                currentItemSave.Mood = oldMood
                                                adapter.notifyItemChanged(adapterPosition)
                                            }
                                            var newSBPvalue = viewDialog.findViewById<EditText>(R.id.editText_value_SBP_change).text.toString()
                                            if(newSBPvalue != document.data?.get("SystolicBP").toString() && newSBPvalue != currentItemSave.SystolicBP.toString()){
                                                doc.update("SystolicBP", newSBPvalue.toLong())
                                                currentItem.SystolicBP = newSBPvalue.toLong()
                                                adapter.notifyItemChanged(adapterPosition)
                                            }
                                            var newDBPvalue = viewDialog.findViewById<EditText>(R.id.editText_value_DBP_change).text.toString()
                                            if(newDBPvalue != document.data?.get("DiastolicBP") && newDBPvalue != currentItemSave.DiastolicBP.toString()){
                                                doc.update("DiastolicBP", newDBPvalue.toLong())
                                                currentItem.DiastolicBP = newDBPvalue.toLong()
                                                adapter.notifyItemChanged(adapterPosition)
                                            }
                                            var onlyHour = SimpleDateFormat("HH:mm").format(currentItemSave.Date.toDate()).toString()
                                            var onlyDate = SimpleDateFormat("YYYY-MM-dd").format(currentItemSave.Date.toDate()).toString()
                                            if(newDateChosen != onlyDate || newHourChosen != onlyHour){
                                                var timestampToUpdatetext = "${newDateChosen} ${newHourChosen}"
                                                var formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
                                                val temporaryDate : Date = formatter.parse(timestampToUpdatetext)
                                                val timeStampMeasure = Timestamp(temporaryDate)
                                                doc.update("Date", timeStampMeasure)
                                                Log.d(TAG, timestampToUpdatetext)
                                                Log.d(TAG, "${currentItemSave.Date.toDate().toString()}")
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
                    Log.d(TAG, "elo pomelo w delete")
                    val currentPosition = adapter.getItem(this.adapterPosition)
                    val userId : String = FirebaseAuth.getInstance().currentUser?.uid.toString()
                    val texttext: String = this.Date.text.toString()
                    var formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
                    val temporaryDate : Date = formatter.parse(texttext)
                    val dateStampt = Timestamp(temporaryDate)
                    var docRef = db.collection("Measurements").document(userId).collection("Measurements")

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
                                        Log.d(TAG, "jestem w yes")
                                        Log.d(TAG, " date ${dateStampt}, sysBP: ${SystolicBP.text.toString()}, mood ${this.Mood.text}")
                                        docRef.document(adapter.getItem(this.adapterPosition).documentId.toString()).delete()
                                        adapter.measurementsList.remove(currentPosition)
                                        adapter.notifyDataSetChanged()
                                        adapter.notifyItemRemoved(this.adapterPosition)
                                        adapter.notifyItemChanged(this.adapterPosition, adapter.itemCount)
                                    }
                                    "no" -> {
                                        Log.d(TAG, "jestem w no")
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }