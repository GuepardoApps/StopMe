package guepardoapps.stopme.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Switch
import guepardoapps.stopme.R
import guepardoapps.stopme.common.Constants
import guepardoapps.stopme.controller.SharedPreferenceController
import guepardoapps.stopme.controller.SystemInfoController
import guepardoapps.stopme.service.FloatingService

class ActivitySettings : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.side_settings)

        val systemInfoController = SystemInfoController(this)
        val sharedPreferenceController = SharedPreferenceController(this)

        val bubbleStateSwitch: Switch = findViewById(R.id.switch_bubble_state)
        bubbleStateSwitch.isChecked = sharedPreferenceController.load(Constants.bubbleState, false) as Boolean
        bubbleStateSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferenceController.save(Constants.bubbleState, isChecked)
            if (isChecked) {
                if (systemInfoController.canDrawOverlay()) {
                    systemInfoController.checkAPI23SystemPermission(Constants.systemPermissionId)
                } else {
                    startService(Intent(this, FloatingService::class.java))
                }
            } else {
                if (systemInfoController.isServiceRunning(FloatingService::class.java)) {
                    stopService(Intent(this, FloatingService::class.java))
                }
            }
        }
    }
}