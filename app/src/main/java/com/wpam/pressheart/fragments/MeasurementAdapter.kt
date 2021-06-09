package com.wpam.pressheart.fragments


import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.DialogInterface
import android.icu.util.Calendar
import android.os.Build
import android.text.style.DynamicDrawableSpan.ALIGN_CENTER
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.wpam.pressheart.MeasurementsActivity
import com.wpam.pressheart.R
import com.wpam.pressheart.dialogs.AreYouSureDialog
import com.wpam.pressheart.lists_content.SingleMeasurement
import java.text.SimpleDateFormat
import java.util.*

class MeasurementAdapter(private val measurementsList: ArrayList<SingleMeasurement>, val context : Context) :
    RecyclerView.Adapter<MeasurementAdapter.MyViewHolder>(){

    private lateinit var parentAdapter  : ViewGroup

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        parentAdapter = parent
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.single_measurement,
            parent, false
        )

        return MyViewHolder(itemView, this)
    }



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

            init{
                changeButton.setOnClickListener {
                    Log.d(TAG, "elo pomelo w change")
                    val currentPosition = adapter.getItem((this as MyViewHolder).adapterPosition)
                    val texttext: String = this.Date.text.toString()
                    var formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
                    var saveChanges : Boolean = false
                    val temporaryDate : Date = formatter.parse(texttext)
                    val dateStampt = Timestamp(temporaryDate)
                    val oldSystolicBP = this.SystolicBP.text
                    val oldDiastolicBP = this.DiastolicBP.text
                    val oldMood = this.Mood.text
                    var newDatelbl = ""
                    val view = LayoutInflater.from(adapter.parentAdapter.context).inflate(R.layout.dialoge_edit_measurement, null)
                    val dialogWindowChange = AlertDialog.Builder(adapter.context)
                        .setView(view)
                        .setCancelable(false)
                        .setPositiveButton("Save"){dialog, which ->
                            run {
                                saveChanges = true
                            }
                        }
                        .setNegativeButton("Cancel"){dialog, which ->  dialog.dismiss()}
                        .create()
                    view.findViewById<EditText>(R.id.editText_value_SBP_change).setText(oldSystolicBP.toString())
                    view.findViewById<EditText>(R.id.editText_value_DBP_change).setText(oldDiastolicBP.toString())



                    dialogWindowChange.show()
                    view.findViewById<Button>(R.id.button_new_date_change_measure).setOnClickListener {
                        Log.d(TAG, "Here Iam stupid bitch")
                    }


                }

                deleteButton.setOnClickListener {
                    Log.d(TAG, "elo pomelo w delete")
                    val currentPosition = adapter.getItem((this as MyViewHolder).adapterPosition)
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
                        .setPositiveButton("Yes") { dialog, which ->
                            run {
                                result = "yes"
                            }
                        }
                        .setNegativeButton("No") { dialog, which ->
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
                                        var query = docRef.whereEqualTo("Date", dateStampt).
                                        whereEqualTo("SystolicBP", this.SystolicBP.text.toString().toLong()).
                                        whereEqualTo("DiastolicBP", this.DiastolicBP.text.toString().toLong()).
                                        whereEqualTo("Mood", this.Mood.text.toString()).get()
                                            .addOnSuccessListener {
                                                documents -> for (document in documents){
                                                    Log.d(TAG, " bubu doc ${document.id} => ${document.data}")
                                                    docRef.document(document.id).delete()
                                                    adapter.measurementsList.remove(currentPosition)
                                                    adapter.notifyDataSetChanged()
                                                    adapter.notifyItemRemoved(this.adapterPosition)
                                                    adapter.notifyItemChanged(this.adapterPosition, adapter.itemCount)
                                                }

                                            }
                                            .addOnFailureListener {
                                                Toast.makeText(itemView.context.applicationContext, "Something went wrong, honey! Try again", Toast.LENGTH_LONG)
                                            }

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