package com.wpam.pressheart

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem


class MedicinesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicines)
        setSupportActionBar(findViewById(R.id.toolbar))
    }

    public override fun onStart() {
        super.onStart()

    }

}