package com.example.storyappdicoding.ui.addStory

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.storyappdicoding.data.model.UserPreference
import com.example.storyappdicoding.ui.Utils.bitmapToFile
import com.example.storyappdicoding.ui.Utils.dataStore
import com.example.storyappdicoding.ui.Utils.rotateBitmap
import com.example.storyappdicoding.ui.Utils.uriToFile
import com.example.storyappdicoding.ui.ViewModelFactory
import com.example.storyappdicoding.ui.addStory.CameraActivity.Companion.IS_BACK_CAMERA_EXTRA
import com.example.storyappdicoding.ui.addStory.CameraActivity.Companion.PICTURE_EXTRA
import com.example.storyappdicoding.ui.main.MainActivity
import com.example.storydicoding.R
import com.example.storydicoding.databinding.ActivityAddStoryBinding
import java.io.File

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var addStoryViewModel: AddStoryViewModel

    private var getFile: File? = null
    private var token: String? = null

    @Suppress("DEPRECATION")
    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val file = it.data?.getSerializableExtra(PICTURE_EXTRA) as File
            val isBackCamera = it.data?.getBooleanExtra(IS_BACK_CAMERA_EXTRA, true) as Boolean
            val bmp = rotateBitmap(
                BitmapFactory.decodeFile(file.path),
                isBackCamera
            )
            val result = bitmapToFile(this, bmp)
            getFile = result
            addStoryViewModel.setFile(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val file = uriToFile(selectedImg, this)
            getFile = file
            addStoryViewModel.setFile(file)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Add Story"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        setupViewModel()
        setupAction()
        setButtonEnable()
    }

    override fun onResume() {
        super.onResume()
        setButtonEnable()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    getString(R.string.unable_to_obtain_permission),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun setupViewModel() {
        addStoryViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[AddStoryViewModel::class.java]

        addStoryViewModel.apply {
            isLoading.observe(this@AddStoryActivity) {
                showLoading(it)
            }

            success.observe(this@AddStoryActivity) {
                if (it == true) {
                    val intent = Intent(this@AddStoryActivity, MainActivity::class.java)
                    startActivity(intent,
                        ActivityOptionsCompat.makeSceneTransitionAnimation(this@AddStoryActivity).toBundle()
                    )
                }
            }

            getUser().observe(this@AddStoryActivity) { user ->
                token = "Bearer ${user.token}"
            }

            file.observe(this@AddStoryActivity) { file ->
                val bitmap = BitmapFactory.decodeFile(file.path)
                binding.imgPreview.setImageBitmap(bitmap)
            }
        }
    }

    private fun setupAction() {
        binding.apply {
            btnAdd.setOnClickListener {
                addStoryViewModel.addStory(
                    token.toString(),
                    binding.edAddDescription.text.toString()
                )
            }
            btnGallery.setOnClickListener { startGallery() }
            btnCamera.setOnClickListener { startCameraX() }

            edAddDescription.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    c: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(c: CharSequence?, start: Int, before: Int, count: Int) {
                    setButtonEnable()
                }

                override fun afterTextChanged(s: Editable?) {
                    edAddDescription.error = if (edAddDescription.text.toString().isEmpty()) {
                        getString(R.string.this_field_cannot_be_blank)
                    } else null
                }
            })
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.choose_a_picture))
        launcherIntentGallery.launch(chooser)
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun setButtonEnable() {
        val storyImage = binding.imgPreview.drawable
        val description = binding.edAddDescription.text

        binding.btnAdd.isEnabled =
            storyImage != null && description != null && description.toString().isNotEmpty()
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}