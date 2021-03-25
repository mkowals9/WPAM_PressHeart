package com.wpam.pressheart.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import com.wpam.pressheart.R
import kotlinx.android.synthetic.main.dialoge_empty_values.*

class EmptyValuesDialog(context: Context) : Dialog(context) {

    init {
        setCancelable(false)
    }

    //EmptyValuesDialog(this.activity as MainActivity).show()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialoge_empty_values)

        btn_ok.setOnClickListener { this.dismiss() }

    }


}