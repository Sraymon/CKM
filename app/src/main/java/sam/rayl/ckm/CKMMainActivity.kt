package sam.rayl.ckm

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class CKMMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ckmmainactivity)  // Make sure this layout file exists

        // Reference the Get Started button
        val getStartedButton: Button = findViewById(R.id.getStartedButton)

        // Set click listener for the Get Started button
        getStartedButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
