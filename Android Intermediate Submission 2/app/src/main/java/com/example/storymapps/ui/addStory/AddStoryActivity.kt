package com.example.storymapps.ui.addStory

import android.annotation.SuppressLint
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.storymapps.R
import com.example.storymapps.data.Result
import com.example.storymapps.data.model.UserModel
import com.example.storymapps.databinding.ActivityAddStoryBinding
import com.example.storymapps.ui.Utils.rotateFile
import com.example.storymapps.ui.Utils.uriToFile
import com.example.storymapps.ui.ViewModelFactory
import com.example.storymapps.ui.main.MainActivity
import com.example.storymapps.ui.main.MainActivity.Companion.TOKEN_MAIN
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.File

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var viewModel: AddStoryViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var userModel = UserModel("","","")
    private var getFile: File? = null
    private var lat: Double? = null
    private var lon: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.add_page)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        @Suppress("DEPRECATION")
        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(TOKEN_MAIN, UserModel::class.java)
        } else {
            intent.getParcelableExtra(TOKEN_MAIN)
        }
        if (data != null) {
            userModel = UserModel(data.name, data.email, data.token)
        }

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setupViewModel()
        setupAction()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this, ViewModelFactory.getInstance(this)
        )[AddStoryViewModel::class.java]
    }

    private fun setupAction() {
        enableButton()
        binding.apply {
            btnCamera.setOnClickListener {
                if (!allPermissionsGranted()) {
                    ActivityCompat.requestPermissions(
                        this@AddStoryActivity,
                        REQUIRED_PERMISSIONS,
                        REQUEST_CODE_PERMISSIONS
                    )
                } else { startCameraX() }
            }
            btnGallery.setOnClickListener { startGallery() }

            switchLocation.setOnCheckedChangeListener { _, isSwitched ->
                if (isSwitched) {
                   getMyLastLocation()
                }
            }

            edDescription.addTextChangedListener ( object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    //Do Nothing
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.toString().isBlank()) {
                        edDescription.error = getString(R.string.no_empty)
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                    enableButton()
                }

            } )

            btnUpload.setOnClickListener {
                val token = "Bearer ${userModel.token}"
                val file = getFile as File
                val desc = edDescription.text.toString()
                showLoading(true)

                viewModel.addStory(token, file, desc, lat, lon).observe(this@AddStoryActivity) { result ->
                    when (result) {
                        is Result.Loading -> {
                            showLoading(true)
                        }
                        is Result.Success -> {
                            showLoading(false)
                            successUpload()
                        }
                        is Result.Error -> {
                            showLoading(false)
                            errorUpload(result.error)
                            Log.d(TAG, result.error)
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun enableButton() {
        val image = binding.imgStory.drawable
        val description = binding.edDescription.text
        (image != null && description != null && description.toString().isNotBlank()).also {
            binding.btnUpload.isEnabled = it
        }
    }

    private fun successUpload() {
        AlertDialog.Builder(this, R.style.MyAlertDialog).apply {
            setTitle(getString(R.string.congrats))
            setMessage(getString(R.string.success_upload))
            setPositiveButton(getString(R.string.ok)) { _, _ ->
                val intent = Intent(this@AddStoryActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            create()
            show()
        }
    }

    private fun errorUpload(message: String?) {
        AlertDialog.Builder(this, R.style.MyAlertDialog).apply {
            setTitle(getString(R.string.error_upload))
            setMessage(message)
            setPositiveButton(getString(R.string.ok)) { ok, _ ->
                ok.cancel()
            }
            create()
            show()
        }
    }

    //Camera and Gallery
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(this, getString(R.string.no_permission), Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.choose_picture))
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri

            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@AddStoryActivity)
                getFile = myFile
                binding.imgStory.setImageURI(uri)
            }
        }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == CAMERA_X_RESULT) {
            val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                result.data?.getSerializableExtra("picture", File::class.java)
            } else {
                @Suppress("DEPRECATION")
                result.data?.getSerializableExtra("picture")
            } as? File

            val isBackCamera = result.data?.getBooleanExtra("isBackCamera", true) as Boolean

            myFile?.let { file ->
                rotateFile(file, isBackCamera)
                getFile = file
                binding.imgStory.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    //Location
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result ->
            when {
                result[android.Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }
                result[android.Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }
                else -> {}
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation() {
        if (checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    lat = location.latitude
                    lon = location.longitude
                } else {
                    Toast.makeText(this, getString(R.string.no_location), Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION)
            )
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        const val TAG = "AddStoryActivity"
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}