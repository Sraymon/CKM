package sam.rayl.ckm

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class UserProfileActivity : AppCompatActivity() {

    // Firebase instances
    private lateinit var auth: FirebaseAuth
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    // UI elements
    private lateinit var emailTextView: TextView
    private lateinit var userNameTextView: TextView
    private lateinit var profilePicture: ImageView
    private lateinit var editProfilePictureIcon: ImageView
    private lateinit var editTextDescription: EditText
    private lateinit var buttonSaveProfile: Button
    private lateinit var menuButton: ImageButton

    // Image URI
    private var selectedImageUri: Uri? = null

    companion object {
        private const val REQUEST_CODE_IMAGE_PICK = 100
        private const val REQUEST_CODE_IMAGE_CAPTURE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_profile_page)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize UI elements
        emailTextView = findViewById(R.id.emailTextView)
        userNameTextView = findViewById(R.id.userNameTextView)
        profilePicture = findViewById(R.id.profilePicture)
        editProfilePictureIcon = findViewById(R.id.editProfilePictureIcon)
        editTextDescription = findViewById(R.id.editTextDescription)
        buttonSaveProfile = findViewById(R.id.buttonSaveProfile)
        menuButton = findViewById(R.id.menuButton)

        // Display email passed from the LoginActivity
        val email = intent.getStringExtra("email")
        emailTextView.text = "Logged in as: $email"

        // Load user profile information
        loadUserProfile()

        // Set up the popup menu
        menuButton.setOnClickListener { view ->
            showPopupMenu(view)
        }

        // Handle profile picture edit icon click
        editProfilePictureIcon.setOnClickListener { selectImage() }

        // Handle Save Profile button click
        buttonSaveProfile.setOnClickListener {
            val description = editTextDescription.text.toString()
            if (selectedImageUri != null) {
                uploadProfilePictureAndSave(description)
            } else {
                saveDescriptionOnly(description)
            }
        }
    }

    private fun showPopupMenu(view: View) {
        // Create a PopupMenu
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.profile_menu, popupMenu.menu)

        // Set click listener for menu items
        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.action_capture_food -> {
                    captureFood()
                    true
                }
                R.id.action_search_food_nearby -> {
                    searchFoodNearby()
                    true
                }
                else -> false
            }
        }

        // Show the PopupMenu
        popupMenu.show()
    }

    private fun captureFood() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_CODE_IMAGE_CAPTURE)
    }

    private fun searchFoodNearby() {
        Toast.makeText(this, "Searching for food nearby...", Toast.LENGTH_SHORT).show()
        // Implement your search functionality here
    }

    private fun loadUserProfile() {
        val userId = auth.currentUser?.uid ?: return
        firestore.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val userName = document.getString("name") ?: "User"
                    val profilePicUrl = document.getString("profilePicUrl") ?: ""
                    val description = document.getString("description") ?: ""

                    userNameTextView.text = userName
                    editTextDescription.setText(description)
                    if (profilePicUrl.isNotEmpty()) {
                        Glide.with(this).load(profilePicUrl).into(profilePicture)
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load profile", Toast.LENGTH_SHORT).show()
            }
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_IMAGE_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE_PICK && resultCode == RESULT_OK) {
            selectedImageUri = data?.data
            profilePicture.setImageURI(selectedImageUri)
        } else if (requestCode == REQUEST_CODE_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            profilePicture.setImageBitmap(imageBitmap)
            // Optionally, upload or process the captured image
        }
    }

    private fun uploadProfilePictureAndSave(description: String) {
        val userId = auth.currentUser?.uid ?: return
        val profilePicRef = storage.reference.child("profile_pics/$userId")

        selectedImageUri?.let { uri ->
            profilePicRef.putFile(uri)
                .addOnSuccessListener {
                    profilePicRef.downloadUrl.addOnSuccessListener { downloadUri ->
                        saveProfileToFirestore(downloadUri.toString(), description)
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to upload profile picture", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun saveProfileToFirestore(profilePicUrl: String, description: String) {
        val userId = auth.currentUser?.uid ?: return
        val userProfile = hashMapOf(
            "profilePicUrl" to profilePicUrl,
            "description" to description
        )
        firestore.collection("users").document(userId)
            .update(userProfile as Map<String, Any>)
            .addOnSuccessListener {
                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveDescriptionOnly(description: String) {
        val userId = auth.currentUser?.uid ?: return
        firestore.collection("users").document(userId)
            .update("description", description)
            .addOnSuccessListener {
                Toast.makeText(this, "Description updated successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to update description", Toast.LENGTH_SHORT).show()
            }
    }
}
