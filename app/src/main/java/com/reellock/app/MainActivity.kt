package com.reellock.app

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.reellock.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
    }

    override fun onResume() {
        super.onResume()
        updateServiceStatus()
        updateBlockedCount()
    }

    private fun setupUI() {
        binding.btnEnableAccessibility.setOnClickListener {
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            startActivity(intent)
        }

        binding.btnGrantOverlay.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                startActivity(intent)
            }
        }
    }

    private fun updateServiceStatus() {
        val isRunning = ReelBlockerService.isServiceRunning
        if (isRunning) {
            binding.tvStatus.text = getString(R.string.service_status_active)
            binding.tvStatus.setTextColor(resources.getColor(R.color.status_green, theme))
        } else {
            binding.tvStatus.text = getString(R.string.service_status_inactive)
            binding.tvStatus.setTextColor(resources.getColor(R.color.status_red, theme))
        }
    }

    private fun updateBlockedCount() {
        val prefs = getSharedPreferences(ReelBlockerService.PREFS_NAME, Context.MODE_PRIVATE)
        val count = prefs.getInt(ReelBlockerService.KEY_BLOCKED_COUNT, 0)
        binding.tvBlockedCount.text = "$count times"
    }
}
