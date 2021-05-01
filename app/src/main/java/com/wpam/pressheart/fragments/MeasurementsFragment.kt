package com.wpam.pressheart.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.wpam.pressheart.MainActivity
import com.wpam.pressheart.MainLoggedMenuActivity
import com.wpam.pressheart.MeasurementsActivity
import com.wpam.pressheart.R
import kotlinx.android.synthetic.main.fragment_measurements.*

class MeasurementsFragment : Fragment()  {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_measurements, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        BackToMainFromMeasureButton.setOnClickListener(){
            (this.activity as MeasurementsActivity).onBackPressed()
            val intent = Intent(this.activity as MeasurementsActivity, MainLoggedMenuActivity::class.java)
            startActivity(intent)
        }

        }
}
