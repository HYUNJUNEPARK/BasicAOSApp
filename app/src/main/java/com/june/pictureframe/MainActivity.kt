package com.june.pictureframe

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.june.pictureframe.databinding.ActivityMainBinding

class MainActivity : Permission() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val selectedPhotoUriList: MutableList<Uri> = mutableListOf()
    private val permissionArray: Array<String> = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    private lateinit var resultListener: ActivityResultLauncher<Intent>
    private val imageViewList: List<ImageView> by lazy {
        mutableListOf<ImageView>().apply {
            add(binding.imageView1Right)
            add(binding.imageView1Middle)
            add(binding.imageView1Left)
            add(binding.imageView2Right)
            add(binding.imageView2Middle)
            add(binding.imageView2Left)
            add(binding.imageView3Right)
            add(binding.imageView3Middle)
            add(binding.imageView3Left)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //추상화 : onCreate 내부에 코드를 모두 적으면 지저분해 보이기 때문에 깔끔하게 보이게 하는 과정
        initAddPhotoButton()
        initSlideShowButton()
        initInitializeButton()
        initResultListener()
    }

    private fun initAddPhotoButton() {
        binding.addPhotoButton.setOnClickListener {
            requirePermissions(permissionArray, 999)
        }
    }

    private fun initSlideShowButton() {
        binding.slideShowButton.setOnClickListener {
            if (selectedPhotoUriList.isNotEmpty()) {
                val intent = Intent(this, SubFrameActivity::class.java)
                selectedPhotoUriList.forEachIndexed { index, uri ->
                    intent.putExtra("photo$index", uri.toString())
                }
                intent.putExtra("photoListSize", selectedPhotoUriList.size)
                startActivity(intent)
            } else {
                Toast.makeText(this, getString(R.string.choice_photo), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initInitializeButton() {
        binding.initializeButton.setOnClickListener {
            val occupiedImageViewIdx = selectedPhotoUriList.size-1
            for(i in 0..occupiedImageViewIdx) {
                imageViewList[i].setImageURI(null)
            }
            selectedPhotoUriList.clear()
        }
    }

    private fun initResultListener() {
        resultListener = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ selectResult ->
            if (selectResult.resultCode == Activity.RESULT_OK) {
                val selectedPhotoUri: Uri? = selectResult.data?.data
                if (selectedPhotoUri != null) {
                    if (selectedPhotoUriList.size >= imageViewList.size){
                        Toast.makeText(this,  getString(R.string.not_add_photo), Toast.LENGTH_SHORT).show()
                        return@registerForActivityResult
                    }
                    selectedPhotoUriList.add(selectedPhotoUri)
                    val emptyImageViewIdx: Int = selectedPhotoUriList.size-1
                    imageViewList[emptyImageViewIdx].setImageURI(selectedPhotoUri)
                } else {
                    Toast.makeText(this, getString(R.string.not_find_photo), Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, getString(R.string.not_find_photo), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun permissionGranted(requestCode: Int) {
        takePhotosSAF()
    }

    override fun permissionDenied(requestCode: Int) {
        showDenyPopup()
    }

    private fun showDenyPopup() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.popup_title))
            .setMessage(getString(R.string.popup_content))
            .setPositiveButton(getString(R.string.popup_agree)) { _, _ ->
                requestPermissions(permissionArray, 999)
            }
            .setNegativeButton(getString(R.string.popup_cancel)) { _, _ ->
                Toast.makeText(this,getString(R.string.cancel_auth), Toast.LENGTH_SHORT).show()
            }
            .create()
            .show()
    }

    private fun takePhotosSAF() {//SAF(Storage Access Framework)
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        resultListener.launch(intent)
    }
}