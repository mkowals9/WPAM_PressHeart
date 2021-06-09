package com.wpam.pressheart.fragments


import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.DialogInterface
import android.text.style.DynamicDrawableSpan.ALIGN_CENTER
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
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