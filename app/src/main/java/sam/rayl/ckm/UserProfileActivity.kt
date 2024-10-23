package sam.rayl.ckm

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class UserProfileActivity : AppCompatActivity() {

    // Firebase Authentication instance
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_profile_page)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Get the email passed from the LoginActivity
        val email = intent.getStringExtra("email")

        // Reference to the TextView and set the email text
        val emailTextView: TextView = findViewById(R.id.emailTextView)
        emailTextView.text = "Logged in as: $email"

        // Handle Sign-Out Button
        val signOutButton: Button = findViewById(R.id.signOutButton)
        signOutButton.setOnClickListener {
            auth.signOut()  // Sign out the user
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)  // Redirect to LoginActivity
            finish()  // Close the AccountActivity
        }

        // Handle Back Button
        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish()  // Go back to the previous activity
        }

        // Handle Next Button
        val nextButton: Button = findViewById(R.id.nextButton)
        nextButton.setOnClickListener {
            // Navigate to the next page (replace NextActivity with your actual activity)
            val intent = Intent(this, NextActivity::class.java)
            startActivity(intent)
        }
    }
}
