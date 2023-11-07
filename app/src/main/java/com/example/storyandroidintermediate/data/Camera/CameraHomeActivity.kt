package com.example.storyandroidintermediate.data.Camera

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.storyandroidintermediate.R
import com.example.storyandroidintermediate.data.retrofit.Result
import com.example.storyandroidintermediate.data.edittext.ButtonEditText
import com.example.storyandroidintermediate.data.main.MainActivity
import com.example.storyandroidintermediate.data.view.ViewModelFactoryStory
import com.example.storyandroidintermediate.databinding.ActivityCamHomeBinding
import com.example.storyandroidintermediate.utils.getImageUri
import com.example.storyandroidintermediate.utils.reduceFileImage
import com.example.storyandroidintermediate.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class CameraHomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCamHomeBinding
    private val viewModel by viewModels<CameraHomeViewModel>{
        ViewModelFactoryStory.getInstance(this)
    }

    private lateinit var myButton: ButtonEditText

    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCamHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)



        myButton = binding.submitbuttonCameraHome

        val result = binding.descriptionEditTextCameraHome.text
        myButton.isEnabled = result != null && result.toString().isNotEmpty()

        binding.descriptionEditTextCameraHome.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val result = binding.descriptionEditTextCameraHome.text
                myButton.isEnabled = result != null && result.toString().isNotEmpty()
            }
            override fun afterTextChanged(s: Editable) {
            }
        })
        myButton.setOnClickListener { Toast.makeText(this@CameraHomeActivity, binding.descriptionEditTextCameraHome.text, Toast.LENGTH_SHORT).show() }


        binding.descriptionEditTextCameraHome.movementMethod = ScrollingMovementMethod();


        binding.gallerybutton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.submitbuttonCameraHome.setOnClickListener { uploadImage() }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewimageBagianstory.setImageURI(it)
        }
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")
            val descriptionText = binding.descriptionEditTextCameraHome.text.toString()

            showLoading(true)

            val requestBody = descriptionText.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )

            val title = getString(R.string.head_notif)
            val message = getString(R.string.upload_succes_notif)
            val next = getString(R.string.next_notif)


            viewModel.postStory(multipartBody,requestBody).observe(this) {upload ->
                if(upload != null){
                    when(upload){
                        is Result.Loading ->{
                            showLoading(true)
                        }
                        is Result.Success -> {
                            showLoading(false)
                            AlertDialog.Builder(this).apply {
                                setTitle(title)
                                setMessage(message)
                                setPositiveButton(next) { _, _ ->
                                    val intent = Intent(context, MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                    finish()
                                }
                                create()
                                show()
                            }
                        }
                        is Result.Error -> {
                            showLoading(false)
                            showToast(upload.error)
                        }
                    }
                }

            }
        } ?: showToast(getString(R.string.error))
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicatorBagianstory.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }

}