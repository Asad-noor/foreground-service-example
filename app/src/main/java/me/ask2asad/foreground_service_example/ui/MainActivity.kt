package me.ask2asad.foreground_service_example.ui

import android.app.TimePickerDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import me.ask2asad.foreground_service_example.R
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        init()
    }

    private fun init() {
        btnSetAlarm.setOnClickListener {
            showDateTimePicker()
        }
    }

    private fun showDateTimePicker() {
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
        val minute = mcurrentTime.get(Calendar.MINUTE)
        val mTimePicker: TimePickerDialog
        mTimePicker = TimePickerDialog(this,
            TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                edtDateTime.setText(
                    "$selectedHour:$selectedMinute"
                )
                edtDateTime.setSelection(edtDateTime.text!!.length)
            }, hour, minute, true
        )//Yes 24 hour time
        mTimePicker.setTitle("Select Time")
        mTimePicker.show()
    }
}
