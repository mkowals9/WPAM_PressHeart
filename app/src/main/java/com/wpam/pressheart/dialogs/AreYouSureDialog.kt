package com.wpam.pressheart.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.Button
import com.wpam.pressheart.MeasurementsActivity
import com.wpam.pressheart.R
import kotlinx.android.synthetic.main.dialoge_are_you_sure.*
import kotlinx.android.synthetic.main.dialoge_are_you_sure.view.*
import kotlinx.android.synthetic.main.fragment_add_new_medicine.*

class AreYouSureDialog(context: Context) : Dialog(context) {


    var mDialogResult: String = ""

    init {
        setCancelable(false)

    }

    public fun getResult(): String {
        return mDialogResult
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialoge_are_you_sure)

//
//        btn_yes_delete_please.setOnClickListener {
//            this.mDialogResult = "yes"
//            //listener.onDialogClick(this, "yes")
//            this.dismiss()
//            }
//        button_no_delete.setOnClickListener {
//            this.mDialogResult = "no"
//            //listener.onDialogClick(this, "no")
//            this.dismiss() }
//    }

//
//    interface NoticeDialogListener {
//        fun onDialogClick(dialog: Dialog, result : String) : String
    }

}