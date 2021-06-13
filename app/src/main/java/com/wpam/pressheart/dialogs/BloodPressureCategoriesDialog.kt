package com.wpam.pressheart.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import com.wpam.pressheart.R
import kotlinx.android.synthetic.main.dialoge_blood_pressures_categories.*
import kotlinx.android.synthetic.main.dialoge_empty_values.*

class BloodPressureCategoriesDialog(context: Context) : Dialog(context)  {

    init {
        setCancelable(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialoge_blood_pressures_categories)

        ok_btn.setOnClickListener { this.dismiss() }

    }
}