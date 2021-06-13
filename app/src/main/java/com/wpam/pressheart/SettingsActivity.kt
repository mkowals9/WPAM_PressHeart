package com.wpam.pressheart

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(findViewById(R.id.toolbar))
    }

    public override fun onStart() {
        super.onStart()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.getItemId() === android.R.id.home) {
            this.onBackPressed()
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

}