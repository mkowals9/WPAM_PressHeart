package com.wpam.pressheart.fragments


import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.wpam.pressheart.AppNameGlideModule
import com.wpam.pressheart.GlideApp
import com.wpam.pressheart.R
import com.wpam.pressheart.lists_content.SingleMeasurement
import com.wpam.pressheart.lists_content.SingleMedicine
import java.util.*


class MedicineAdapter(private val medicinesList: ArrayList<SingleMedicine>) :
    RecyclerView.Adapter<MedicineAdapter.MyViewHolder>() {

    private lateinit var parentAdapter  : ViewGroup

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        parentAdapter = parent
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.single_medicine,
            parent, false
        )
        return MyViewHolder(itemView, this)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = medicinesList[position]
        holder.Name.setText(currentItem.Name.toString())
        holder.LeftPills.setText(currentItem.LeftPills.toString())
        holder.Description.text = currentItem.Description
        Log.d(TAG, "MDEDICNE URL : ${currentItem.ImageUri.toString()}")
        Glide.with(holder.itemView).load(currentItem.ImageUri.toString()).into(
            holder.ImageUri)
    }

    override fun getItemCount(): Int {
        return medicinesList.size
    }

    fun getItem (pos : Int): SingleMedicine {
        return medicinesList[pos]
    }

    class MyViewHolder(itemView: View, adapter : MedicineAdapter) : RecyclerView.ViewHolder(itemView) {
        val ImageUri: ImageView = itemView.findViewById(R.id.imageViewMedicine_single)
        val Name: TextView = itemView.findViewById(R.id.name_Medicine)
        val LeftPills: TextView = itemView.findViewById(R.id.leftPills_Medicine)
        val Description: TextView = itemView.findViewById(R.id.description_Medicine)

        private var db = Firebase.firestore
        private val storageFirebase = FirebaseStorage.getInstance().getReference()
        private var result : String = ""
        private val userId : String = FirebaseAuth.getInstance().currentUser?.uid.toString()

        init {
            itemView.findViewById<Button>(R.id.deleteMeasurementButton).setOnClickListener {
                Log.d(TAG, "elo delete medycyne")
                val currentPosition = adapter.getItem(this.adapterPosition)
                var docRef = db.collection("Medicines").document(userId).collection("Medicines")
                val view = LayoutInflater.from(adapter.parentAdapter.context).inflate(R.layout.dialoge_are_you_sure, null)
                val dialogWindow = AlertDialog.Builder(adapter.parentAdapter.context)
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
                                Log.d(TAG, "image uri : ${currentPosition.ImageUri.toString()}")
                                storageFirebase.storage.getReferenceFromUrl(currentPosition.ImageUri.toString()).delete()
                                docRef.document(adapter.getItem(this.adapterPosition).documentId.toString()).delete()
                                adapter.medicinesList.remove(currentPosition)
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

            itemView.findViewById<Button>(R.id.changeMeasurementButton).setOnClickListener {
                    
            }

            itemView.findViewById<Button>(R.id.TakePillButton).setOnClickListener {
                val currentPos = adapter.getItem(this.adapterPosition)
                db.collection("Medicines").document(userId).collection("Medicines").document(currentPos.documentId).update("LeftPills", FieldValue.increment(-1))
                currentPos.LeftPills = (currentPos.LeftPills - 1).toLong()
                if(currentPos.LeftPills < 10){
                    Toast.makeText(adapter.parentAdapter.context, "You should buy more pills!", Toast.LENGTH_SHORT)
                }
                adapter.notifyDataSetChanged()
                adapter.notifyItemChanged(this.adapterPosition)

            }
        }
    }



}
