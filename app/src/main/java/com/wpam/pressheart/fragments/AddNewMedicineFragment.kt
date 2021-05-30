package com.wpam.pressheart.fragments

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.wpam.pressheart.R
import kotlinx.android.synthetic.main.fragment_add_new_medicine.*


class AddNewMedicineFragment : Fragment() {

    private val db = Firebase.firestore
    private val storageFirebase = FirebaseStorage.getInstance().getReference()
    private var imageViewUri = ""
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
            var nameMedicine = editTextNameMedicine.text.toString()
            var amountOfPills = editTextNumberleftPills.text.toString().toInt()
            var desc = editTextDescriptionMedicine.text.toString()

            val bitmap = (new_medicine_imageView.getDrawable() as BitmapDrawable).bitmap



//            val path: String = Uri.parse(new_medicine_imageView.getTag().toString())
//
//            val pathInStorage = "medicines/" + userId + "/" + path
//            storageFirebase.child(pathInStorage)

            val newMedicineToAdd = hashMapOf(
                "Name" to nameMedicine,
                "LeftPills" to amountOfPills,
                "Description" to desc
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
                    Log.w(ContentValues.TAG, "data : ")
                    Log.w(ContentValues.TAG, data.toString())

                    Log.w(ContentValues.TAG, "data extra : ")
                    Log.w(ContentValues.TAG, data?.extras.toString())
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