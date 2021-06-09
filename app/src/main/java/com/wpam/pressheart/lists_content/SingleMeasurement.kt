package com.wpam.pressheart.lists_content

import com.google.firebase.Timestamp
import java.time.LocalDateTime

data class SingleMeasurement(var Date:Timestamp = Timestamp.now(), var DiastolicBP:Long = -1, var Mood:String? = null, var SystolicBP:Long = -1, var documentId:String = "") {



}