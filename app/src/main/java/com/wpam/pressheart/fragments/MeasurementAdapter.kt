package com.wpam.pressheart.fragments


import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.wpam.pressheart.MeasurementsActivity
import com.wpam.pressheart.R
import com.wpam.pressheart.dialogs.EmptyValuesDialog
import com.wpam.pressheart.lists_content.SingleMeasurement
import java.text.SimpleDateFormat
import java.util.*

class MeasurementAdapter(private val measurementsList: ArrayList<SingleMeasurement>) :
    RecyclerView.Adapter<MeasurementAdapter.MyViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.single_measurement,
        parent, false)
        return MyViewHolder(itemView)
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

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val Date : TextView = itemView.findViewById(R.id.date_Measurement_Browse)
        val DiastolicBP : TextView = itemView.findViewById(R.id.diastolicBP_Measurement_Browse)
        val Mood : TextView = itemView.findViewById(R.id.mood_Measurement_Browse)
        val SystolicBP : TextView = itemView.findViewById(R.id.systolicBP_Measurement_Browse)

        val changeButton : Button = itemView.findViewById(R.id.changeMeasurementButton)
        val deleteButton : Button = itemView.findViewById(R.id.deleteMeasurementButton)

        private var db = Firebase.firestore

            init{
                changeButton.setOnClickListener {
                    //EmptyValuesDialog(itemView.context as MeasurementsActivity).show()
                    Log.d(TAG, "elo pomelo w change")
                }

                deleteButton.setOnClickListener {
                    Log.d(TAG, "elo pomelo w delete")
                    val userId : String = FirebaseAuth.getInstance().currentUser?.uid.toString()
                    val texttext: String = this.Date.text.toString()
                    var formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
                    val temporaryDate : Date = formatter.parse(texttext)
                    val dateStampt = Timestamp(temporaryDate)
                    var docRef = db.collection("Measurements").document(userId).collection("Measurements")
                    var query = docRef.whereEqualTo("Date", dateStampt).
                    whereEqualTo("SystolicBP", this.SystolicBP.text.toString().toLong()).
                    whereEqualTo("DiastolicBP", this.DiastolicBP.text.toString().toLong()).
                    whereEqualTo("Mood", this.Mood.text.toString()).get()
                    if(query != null){
                        Log.d(TAG, "query type = $(query.class.simpleName)")
                        EmptyValuesDialog(itemView.context as MeasurementsActivity).show()
                    }


                }
            }
        }

    }
