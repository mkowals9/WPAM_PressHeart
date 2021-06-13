package com.wpam.pressheart.fragments

import android.app.Activity
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.wpam.pressheart.R
import kotlinx.android.synthetic.main.fragment_add_new_medicine.*
import java.io.ByteArrayOutputStream


class AddNewMedicineFragment : Fragment() {

    private val db = Firebase.firestore
    private val storageFirebase = FirebaseStorage.getInstance().getReference()
    private var imageViewUri = ""
    private var medicineName = ""
    private var downloadUri = ""
    private var  desc = ""
    private var amountOfPills = 0
    private val CAMERA_REQUEST = 1888
    private val GALLERY_REQUEST = 1889

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_new_medicine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        add_new_photo_camera_Button.setOnClickListener {

            var cameraIntent: Intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, CAMERA_REQUEST)
        }

        add_new_photo_gallery_Button.setOnClickListener {
            val pickPhoto = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(pickPhoto, GALLERY_REQUEST)
        }

        addCompleteMedicineButton.setOnClickListener {
            val userId: String = FirebaseAuth.getInstance().currentUser?.uid.toString()
            this.medicineName = editTextNameMedicine.text.toString()
            if(editTextNumberleftPills.text.toString() != ""){
            this.amountOfPills = editTextNumberleftPills.text.toString().toInt()
            }
            else{
                Toast.makeText(this.context, "You left empty amount of pills", Toast.LENGTH_SHORT).show()
            }
            desc = editTextDescriptionMedicine.text.toString()
            new_medicine_imageView.isDrawingCacheEnabled = true
            new_medicine_imageView.buildDrawingCache()
            val bitmapFireBase = (new_medicine_imageView.drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmapFireBase.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val data = baos.toByteArray()
            var uploadTask = storageFirebase.child("${userId}/medicines/${this.medicineName}").putBytes(data)
            uploadTask.addOnFailureListener {
                Toast.makeText(this.context,"Cannot upload a photo to Database", Toast.LENGTH_LONG).show()
            }
            val urlTask = uploadTask.continueWithTask { task ->
                if(!task.isSuccessful){task.exception?.let {throw it}}
                storageFirebase.child("${userId}/medicines/${this.medicineName}").downloadUrl}
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        this.downloadUri = task.result.toString()
                    }
                    else
                    {
                        Toast.makeText(this.context,"Cannot get photo's uri", Toast.LENGTH_LONG).show()
                    }
                }

            Log.d(TAG, " UPLOAD PHOTO ITS URI: ${downloadUri}")

            val newMedicineToAdd = hashMapOf(
                "Name" to medicineName,
                "LeftPills" to amountOfPills,
                "Description" to desc,
                "ImageUri" to downloadUri
            )

        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            CAMERA_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    val photo = data?.extras!!["data"] as Bitmap?
                    //this.imageViewUri = data?.extras!!["data"] as String?
                    new_medicine_imageView.setImageBitmap(photo)
                    //var uriUri = getImageUri(this.activity as MedicinesActivity, data?.extras!!["data"] as Bitmap, this.medicineName)
                    Log.w(ContentValues.TAG, "data : ")
                    Log.w(ContentValues.TAG, data.toString())

                    Log.w(ContentValues.TAG, "data extra : ")
                    Log.w(ContentValues.TAG, data.extras.toString())

                    Log.w(ContentValues.TAG, "data extra extra")
                    Log.w(ContentValues.TAG, data.extras!!["data"].toString())
                }
            }
            GALLERY_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    new_medicine_imageView.setImageURI(data?.data)
                    this.imageViewUri = data?.data.toString()
                }
            }
        }
    }




}