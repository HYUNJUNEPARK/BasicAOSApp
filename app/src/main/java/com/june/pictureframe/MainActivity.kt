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
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //추상화 : onCreate 내부에 코드를 모두 적으면 지저분해 보이기 때문에 깔끔하게 보이게 하는 과정
        initAddPhotoButton()
        initSlideShowButton()
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
                val intent_ = Intent(this, SubFrameActivity::class.java)
                selectedPhotoUriList.forEachIndexed { index, uri ->
                    intent_.putExtra("photo$index", uri.toString())
                }
                intent_.putExtra("photoListSize", selectedPhotoUriList.size)
                startActivity(intent_)
            } else {
                Toast.makeText(this, "사진을 선택 후 시작할 수 있습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initResultListener(){
        resultListener = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ selectResult ->
            if (selectResult.resultCode == Activity.RESULT_OK) {
                val selectedPhotoUri: Uri? = selectResult.data?.data
                if (selectedPhotoUri != null) {
                    if (selectedPhotoUriList.size >= imageViewList.size){
                        Toast.makeText(this,  "더 이상 사진을 추가할 수 없습니다.", Toast.LENGTH_SHORT).show()
                        return@registerForActivityResult
                    }
                    selectedPhotoUriList.add(selectedPhotoUri)
                    imageViewList[selectedPhotoUriList.size-1].setImageURI(selectedPhotoUri)
                } else {
                    Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
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

    private fun takePhotosSAF() {//SAF(Storage Access Framework)
        val intent_ = Intent(Intent.ACTION_GET_CONTENT)
        intent_.type = "image/*"
        resultListener.launch(intent_)
    }
}