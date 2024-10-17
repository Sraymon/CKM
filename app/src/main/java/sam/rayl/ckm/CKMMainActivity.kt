package sam.rayl.ckm

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class CKMMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ckmmainactivity)  // Ensure this matches your layout file name

        // Get reference to the "Get Started" button
        val getStartedButton: Button = findViewById(R.id.getStartedButton)

        // Set click listener to navigate to LoginActivity
        getStartedButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
