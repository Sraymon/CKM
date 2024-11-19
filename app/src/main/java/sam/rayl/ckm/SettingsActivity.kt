package sam.rayl.ckm

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class SettingsActivity : AppCompatActivity() {

    private lateinit var themeSwitch: Switch
    private lateinit var notificationsSwitch: Switch
    private lateinit var sharedPreferences: SharedPreferences

    // Variables for swipe detection
    private var startX: Float = 0.0f
    private val SWIPE_THRESHOLD = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load theme based on saved preference
        sharedPreferences = getSharedPreferences("AppSettingsPrefs", MODE_PRIVATE)
        if (sharedPreferences.getBoolean("DarkTheme", false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        setContentView(R.layout.activity_settings)

        // Initialize SharedPreferences editor
        val editor = sharedPreferences.edit()

        // Initialize switches
        themeSwitch = findViewById(R.id.themeSwitch)
        notificationsSwitch = findViewById(R.id.notificationsSwitch)

        // Set initial switch states
        themeSwitch.isChecked = sharedPreferences.getBoolean("DarkTheme", false)
        notificationsSwitch.isChecked = sharedPreferences.getBoolean("NotificationsEnabled", true)

        // Dark mode toggle
        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            editor.putBoolean("DarkTheme", isChecked)
            editor.apply()

            // Apply dark mode setting
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            Toast.makeText(this, "Theme updated", Toast.LENGTH_SHORT).show()
        }

        // Notifications toggle
        notificationsSwitch.setOnCheckedChangeListener { _, isChecked ->
            editor.putBoolean("NotificationsEnabled", isChecked)
            editor.apply()
            Toast.makeText(this, "Notifications updated", Toast.LENGTH_SHORT).show()
        }

        // Set up the swipe listener
        setSwipeListener()
    }

    private fun setSwipeListener() {
        val rootLayout: View = findViewById(R.id.rootLayout) // Root layout ID in `activity_settings.xml`

        rootLayout.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> startX = event.x
                MotionEvent.ACTION_UP -> {
                    val endX = event.x
                    val deltaX = endX - startX

                    if (deltaX > SWIPE_THRESHOLD) {
                        navigateBackToMain()
                    }
                }
            }
            true
        }
    }

    private fun navigateBackToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
