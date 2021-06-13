package com.wpam.pressheart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import com.wpam.pressheart.dialogs.BloodPressureCategoriesDialog
import com.wpam.pressheart.dialogs.EmptyValuesDialog

class MeasurementsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_measurements_activity)
        setSupportActionBar(findViewById(R.id.toolbar))
    }

    public override fun onStart() {
        super.onStart()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_mesurements, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_BP_categories -> {
                val viewDialogView = LayoutInflater.from(this.applicationContext).inflate(R.layout.dialoge_edit_measurement, null)
                BloodPressureCategoriesDialog(this as MeasurementsActivity).show()

                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}