package com.wpam.pressheart.fragments


import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.wpam.pressheart.R
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
        private val CAMERA_REQUEST = 1888
        private val GALLERY_REQUEST = 1889
        private var saveChanges = false

        init {
            itemView.findViewById<Button>(R.id.deleteMeasurementButton).setOnClickListener {
                val currentPosition = adapter.getItem(this.adapterPosition)
                val docRef = db.collection("Medicines").document(userId).collection("Medicines")
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
                                storageFirebase.storage.getReferenceFromUrl(currentPosition.ImageUri.toString()).delete()
                                docRef.document(adapter.getItem(this.adapterPosition).documentId).delete()
                                adapter.medicinesList.remove(currentPosition)
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

            itemView.findViewById<Button>(R.id.changeMeasurementButton).setOnClickListener {
                val currentItem = adapter.getItem(this.adapterPosition)
                val userId : String = FirebaseAuth.getInstance().currentUser?.uid.toString()
                val docRef = db.collection("Medicines").document(userId).collection("Medicines")
                val viewDialog = LayoutInflater.from(adapter.parentAdapter.context).inflate(R.layout.dialoge_edit_medicine, null)

                val dialogWindowChange = AlertDialog.Builder(adapter.parentAdapter.context)
                    .setView(viewDialog)
                    .setCancelable(false)
                    .setPositiveButton("Save"){ _, _ ->
                        saveChanges = true
                        if(saveChanges){
                            val newName = viewDialog.findViewById<EditText>(R.id.editTextTextMedicineNameChange).text.toString()
                            if(newName != currentItem.Name){
                                docRef.document(currentItem.documentId).update("Name", newName)
                                currentItem.Name = newName
                                adapter.notifyItemChanged(adapterPosition)
                            }
                            val newPills = viewDialog.findViewById<EditText>(R.id.editTextLeftPillsChange).text.toString()
                            if(newPills != currentItem.LeftPills.toString()){
                                docRef.document(currentItem.documentId).update("LeftPills", newPills.toLong())
                                currentItem.LeftPills = newPills.toLong()
                                adapter.notifyItemChanged(adapterPosition)
                            }
                            val newDesc = viewDialog.findViewById<EditText>(R.id.editText_Description).text.toString()
                            if(newDesc != currentItem.Description.toString())
                            {
                                docRef.document(currentItem.documentId).update("Description", newDesc)
                                currentItem.Description = newDesc
                                adapter.notifyItemChanged(adapterPosition)
                            }
                            Toast.makeText(adapter.parentAdapter.context, "Successfully changed medicine's info", Toast.LENGTH_SHORT).show()

                        }
                    }
                    .setNegativeButton("No"){
                            dialog, _ ->  dialog.dismiss()
                    }
                    .create()
                viewDialog.findViewById<EditText>(R.id.editTextTextMedicineNameChange).setText(currentItem.Name)
                viewDialog.findViewById<EditText>(R.id.editTextLeftPillsChange).setText(currentItem.LeftPills.toString())
                viewDialog.findViewById<EditText>(R.id.editText_Description).setText(currentItem.Description.toString())
                dialogWindowChange.show()
            }

            itemView.findViewById<Button>(R.id.TakePillButton).setOnClickListener {
                val currentPos = adapter.getItem(this.adapterPosition)
                if(currentPos.LeftPills > 0) {
                    db.collection("Medicines").document(userId).collection("Medicines")
                        .document(currentPos.documentId)
                        .update("LeftPills", FieldValue.increment(-1))
                    currentPos.LeftPills = (currentPos.LeftPills - 1)
                    if (currentPos.LeftPills < 10) {
                        Toast.makeText(
                            itemView.context,
                            "You should buy more pills!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    adapter.notifyDataSetChanged()
                    adapter.notifyItemChanged(this.adapterPosition)
                }
                else{
                    Toast.makeText(itemView.context, "You ran out of pills", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }


}
