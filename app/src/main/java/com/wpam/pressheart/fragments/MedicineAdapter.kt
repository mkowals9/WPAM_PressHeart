package com.wpam.pressheart.fragments


import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.wpam.pressheart.AppNameGlideModule
import com.wpam.pressheart.GlideApp
import com.wpam.pressheart.R
import com.wpam.pressheart.lists_content.SingleMedicine
import java.util.*


class MedicineAdapter(private val medicinesList: ArrayList<SingleMedicine>) :
    RecyclerView.Adapter<MedicineAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.single_medicine,
            parent, false
        )
        return MyViewHolder(itemView)
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

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ImageUri: ImageView = itemView.findViewById(R.id.imageViewMedicine_single)
        val Name: TextView = itemView.findViewById(R.id.name_Medicine)
        val LeftPills: TextView = itemView.findViewById(R.id.leftPills_Medicine)
        val Description: TextView = itemView.findViewById(R.id.description_Medicine)

        private var db = Firebase.firestore

        init {

        }
    }



}
