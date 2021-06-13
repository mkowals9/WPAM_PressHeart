package com.wpam.pressheart.fragments


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wpam.pressheart.R
import com.wpam.pressheart.lists_content.SingleMedicine
import java.util.ArrayList

class MedicineAdapter(private val medicinesList: ArrayList<SingleMedicine>) :
    RecyclerView.Adapter<MedicineAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MedicineAdapter.MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.single_medicine,
            parent, false
        )
        return MedicineAdapter.MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MedicineAdapter.MyViewHolder, position: Int) {
        val currentItem = medicinesList[position]
        holder.Name.setText(currentItem.Name.toString())
        holder.LeftPills.setText(currentItem.LeftPills.toString())
        holder.Description.text = currentItem.Description
        Glide.with(holder.ImageUri).load(currentItem.ImageUri.toString()).into(holder.ImageUri)
    }

    override fun getItemCount(): Int {
        return medicinesList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ImageUri: ImageView = itemView.findViewById(R.id.imageViewMedicine)
        val Name: TextView = itemView.findViewById(R.id.name_Medicine)
        val LeftPills: TextView = itemView.findViewById(R.id.leftPills_Medicine)
        val Description: TextView = itemView.findViewById(R.id.description_Medicine)
    }
}
