package sam.rayl.ckm

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class PostUploadActivity : AppCompatActivity() {

    private lateinit var imageViewSelected: ImageView
    private lateinit var buttonSelectImage: Button
    private lateinit var buttonUploadPost: Button
    private lateinit var editTextDescription: EditText

    private val storage = FirebaseStorage.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_upload)

        imageViewSelected = findViewById(R.id.imageViewSelected)
        buttonSelectImage = findViewById(R.id.buttonSelectImage)
        buttonUploadPost = findViewById(R.id.buttonUploadPost)
        editTextDescription = findViewById(R.id.editTextDescription)

        // Open gallery to select image
        buttonSelectImage.setOnClickListener { openGallery() }

        // Upload post
        buttonUploadPost.setOnClickListener {
            val description = editTextDescription.text.toString()
            if (selectedImageUri != null && description.isNotEmpty()) {
                uploadImageToStorage(selectedImageUri!!, description)
            } else {
                Toast.makeText(this, "Please select an image and add a description", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_IMAGE_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            imageViewSelected.setImageURI(selectedImageUri) // Display selected image
        }
    }

    private fun uploadImageToStorage(imageUri: Uri, description: String) {
        val storageRef = storage.reference.child("images/${System.currentTimeMillis()}.jpg")

        storageRef.putFile(imageUri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()
                    saveImageUrlToFirestore(downloadUrl, description)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Image upload failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveImageUrlToFirestore(imageUrl: String, description: String) {
        val userId = auth.currentUser?.uid ?: return
        val post = hashMapOf(
            "imageUrl" to imageUrl,
            "description" to description,
            "userId" to userId,
            "timestamp" to System.currentTimeMillis()
        )

        firestore.collection("posts")
            .add(post)
            .addOnSuccessListener {
                Toast.makeText(this, "Post uploaded successfully!", Toast.LENGTH_SHORT).show()
                finish() // Close the activity after successful upload
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to save post: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    companion object {
        private const val REQUEST_CODE_IMAGE_PICK = 100
    }
}
