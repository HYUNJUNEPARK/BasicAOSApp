package com.june.pictureframe

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.june.pictureframe.databinding.ActivityMainBinding

class MainActivity : Permission() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val photoUriList: MutableList<Uri> = mutableListOf()
    private val permissionArray: Array<String> = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)

    private val imageViewList: List<ImageView> by lazy {
        mutableListOf<ImageView>().apply {
            add(binding.imageView1Right)
            add(binding.imageView1Middle)
            add(binding.imageView1Left)
            add(binding.imageView2Right)
            add(binding.imageView2Middle)
            add(binding.imageView2Left)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //추상화 : onCreate 내부에 코드를 모두 적으면 지저분해 보이기 때문에 깔끔하게 보이게 하는 과정
        initAddPhotoButton()
        initSlideShowButton()
    }

    private fun initAddPhotoButton() {
        binding.addPhotoButton.setOnClickListener {
            requirePermissions(permissionArray, 999)
        }
    }

    private fun initSlideShowButton() {
        binding.slideShowButton.setOnClickListener {
            val intent = Intent(this, SubFrameActivity::class.java)
            photoUriList.forEachIndexed { index, uri ->
                intent.putExtra("photo$index", uri.toString())
            }
            intent.putExtra("photoListSize", photoUriList.size)
            startActivity(intent)
        }
    }

    override fun permissionGranted(requestCode: Int) {
        takePhotos()
    }

    override fun permissionDenied(requestCode: Int) {
        showPopup()
    }

    private fun showPopup() {
        AlertDialog.Builder(this)
            .setTitle("권한이 필요합니다")
            .setMessage("사진을 불러오기 위해 권한이 필요합니다")
            .setPositiveButton("동의하기") { _, _ ->
                requestPermissions(permissionArray, 999)
            }
            .setNegativeButton("취소하기") { _, _ ->
                Toast.makeText(this,"권한 승인을 취소하셨습니다.", Toast.LENGTH_SHORT).show()
            }
            .create()
            .show()
    }

    //TODO: startActivityForResult deprecated
    private fun takePhotos() {
        //SAF 기능 이용
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, 2000)
    }

    //TODO: startActivityForResult deprecated
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {
            2000 -> {
                val selectedImageUri: Uri? = data?.data
                if (selectedImageUri != null) {
                    if (photoUriList.size >= imageViewList.size) {
                        Toast.makeText(this, "더 이상 사진을 추가할 수 없습니다.", Toast.LENGTH_SHORT).show()
                        return
                    }
                    photoUriList.add(selectedImageUri)
                    imageViewList[photoUriList.size - 1].setImageURI(selectedImageUri)
                } else {
                    Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}