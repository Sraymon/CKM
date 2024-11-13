package sam.rayl.ckm

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var selectedImageView: ImageView
    private lateinit var selectPhotoButton: Button
    private lateinit var takePhotoButton: Button
    private lateinit var uploadButton: Button
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private var imageUri: Uri? = null
    private val PICK_IMAGE_REQUEST = 1
    private val REQUEST_IMAGE_CAPTURE = 2
    private val firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()
    private val storageReference: StorageReference = firebaseStorage.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up Toolbar and DrawerLayout
        drawerLayout = findViewById(R.id.drawer_layout)
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Set up NavigationView
        val navigationView: NavigationView = findViewById(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener(this)

        // Initialize UI components
        selectedImageView = findViewById(R.id.selectedImageView)
        selectPhotoButton = findViewById(R.id.selectPhotoButton)
        takePhotoButton = findViewById(R.id.takePhotoButton)
        uploadButton = findViewById(R.id.uploadButton)

        selectPhotoButton.setOnClickListener { openGallery() }
        takePhotoButton.setOnClickListener { openCamera() }
        uploadButton.setOnClickListener {
            imageUri?.let {
                uploadImage(it)
            } ?: Toast.makeText(this, "Please select or take a photo first", Toast.LENGTH_SHORT).show()
        }
    }

    // Handle gallery selection
    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    // Handle camera capture
    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    // Process the result from gallery or camera
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            try {
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                selectedImageView.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK && data != null) {
            val bitmap = data.extras?.get("data") as Bitmap
            selectedImageView.setImageBitmap(bitmap)
            // Convert bitmap to URI if needed (implement this method based on your storage strategy)
            imageUri = getImageUriFromBitmap(bitmap)
        }
    }

    private fun getImageUriFromBitmap(bitmap: Bitmap): Uri? {
        // Placeholder function: implement this to convert the captured image bitmap to a URI if needed
        return null // You may need to save it to a temp file or use a different approach
    }

    // Upload image to Firebase
    private fun uploadImage(filePath: Uri) {
        val ref = storageReference.child("images/${UUID.randomUUID()}")
        ref.putFile(filePath)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { uri ->
                    searchForSimilarFood(uri.toString())
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
    }

    private fun searchForSimilarFood(imageUrl: String) {
        Toast.makeText(this, "Image uploaded! Searching for similar food...", Toast.LENGTH_SHORT).show()
        // Integrate your search functionality with imageUrl here
    }

    // Implement the onNavigationItemSelected method
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_profile -> {
                val intent = Intent(this, UserProfileActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_settings -> {
                Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT).show()
            }
        }
        drawerLayout.closeDrawers()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
