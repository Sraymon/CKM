package sam.rayl.ckm

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    // Firebase Authentication instance
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up_page_activity)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Get references to the EditTexts and Button
        val emailEditText: EditText = findViewById(R.id.emailEditText)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val createAccountButton: Button = findViewById(R.id.createAccountButton)

        // Handle Create Account Button click
        createAccountButton.setOnClickListener {
            val email = emailEditText.text.toString().trim() // trim to remove unnecessary spaces
            val password = passwordEditText.text.toString().trim()

            // Validate input
            if (email.isEmpty()) {
                emailEditText.error = "Email is required"
                emailEditText.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                passwordEditText.error = "Password is required"
                passwordEditText.requestFocus()
                return@setOnClickListener
            }

            if (password.length < 6) {
                passwordEditText.error = "Password must be at least 6 characters long"
                passwordEditText.requestFocus()
                return@setOnClickListener
            }

            // Call Firebase sign-up method
            createNewAccount(email, password)
        }
    }

    private fun createNewAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Account creation successful, navigate to AccountActivity
                    val intent = Intent(this, AccountActivity::class.java)
                    intent.putExtra("email", email) // Optionally pass user email to next activity
                    startActivity(intent)
                    finish() // Close SignUpActivity to prevent going back
                } else {
                    // If sign-up fails, display a message to the user
                    Toast.makeText(this, "Sign-up failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
