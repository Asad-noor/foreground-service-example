package me.ask2asad.foreground_service_example.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.WorkSource
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import me.ask2asad.foreground_service_example.R
import me.ask2asad.foreground_service_example.data.WorkerService
import java.util.*

class MainActivity : AppCompatActivity() {

    private val ALARM_ID = 1
    private var selectHour = 0
    private var selectMinute = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        init()
    }

    private fun init() {
        btnSetAlarm.setOnClickListener {
            if (!btnSetAlarm.text.equals("Set Alarm")) {
                showDateTimePicker()
            } else {
                setAlarm(selectHour, selectMinute)
                Toast.makeText(this, "Alarm set successfully.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDateTimePicker() {
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
        val minute = mcurrentTime.get(Calendar.MINUTE)
        val mTimePicker: TimePickerDialog
        mTimePicker = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                edtDateTime.setText(
                    "$selectedHour:$selectedMinute"
                )
                edtDateTime.setSelection(edtDateTime.text!!.length)
                selectHour = selectedHour
                selectMinute = selectedMinute

                if (selectedHour > 0 && selectedMinute > 0) {
                    btnSetAlarm.setText("Set Alarm")
                }

            }, hour, minute, true
        )//Yes 24 hour time
        mTimePicker.setTitle("Select Time")
        mTimePicker.show()
    }

    private fun setAlarm(selectedHour: Int, selectedMinute: Int) {
        var millis = selectedHour * 60 * 1000
        millis += selectedMinute * 1000

        val intent = Intent(this, WorkerService::class.java)
        val bundle = Bundle()
        bundle.putInt("hour", selectedHour)
        bundle.putInt("minute", selectedMinute)
        intent.putExtra("toto", bundle)

        startServicePerfectly(millis.toLong(), intent)
    }

    private fun startServicePerfectly(millis: Long, intent: Intent) {
        val pendingIntent: PendingIntent
        pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PendingIntent.getForegroundService(
                this,
                ALARM_ID,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        } else {
            PendingIntent.getService(
                this,
                ALARM_ID,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        val alarm =
            getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarm.set(
            AlarmManager.RTC_WAKEUP,
            millis,
            pendingIntent
        )
    }
}
