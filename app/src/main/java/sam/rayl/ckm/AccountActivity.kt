package sam.rayl.ckm

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        // Get the email passed from the LoginActivity
        val email = intent.getStringExtra("email")

        // Reference to the TextView and set the email text
        val emailTextView: TextView = findViewById(R.id.emailTextView)
        emailTextView.text = "Logged in as: $email"
    }
}
