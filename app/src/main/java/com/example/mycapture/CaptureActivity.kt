package com.example.mycapture

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.akexorcist.screenshotdetection.ScreenshotDetectionDelegate
import com.example.mycapture.custom.CustomToast.showCustomToast
import com.example.mycapture.databinding.ActivityCaptureBinding
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

private lateinit var binding: ActivityCaptureBinding
private const val TAG = "mmmCaptureActivity"

// 캡쳐하기 화면
class CaptureActivity : AppCompatActivity(), ScreenshotDetectionDelegate.ScreenshotDetectionListener {
    // 켑쳐 탐지기
    private val screenshotDetectionDelegate = ScreenshotDetectionDelegate(this@CaptureActivity, this@CaptureActivity)
    // 캡쳐 후 저장에 관한 권한 코드
    companion object { private const val REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION = 3009 }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCaptureBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // 권한 확인
        checkReadExternalStoragePermission()

        // <캡쳐하기> 버튼 클릭
        binding.btnCapture.setOnClickListener {
            // 스크린샷 찍기
            val bitmap = takeScreenshot(view)
            // 갤러리에 저장하기
            savePhoto(bitmap!!)
        }

    }

    // 스크린샷 찍기
    private fun takeScreenshot(view: View): Bitmap? {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    // 갤러리에 저장하는 메소드
    private fun savePhoto(bitmap: Bitmap) : String {
        // 사진 폴더로 저장하기 위한 경로 선언
        val folderPath =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .toString() + "/MyCapture/"
        // 폴더 생성(해당 경로의 폴더가 존재하지 않으면 해당 경로에 폴더 생성)
        val folder = File(folderPath)
        if (!folder.isDirectory) folder.mkdir()

        // 폴더 경로에 파일 생성 + 최종 저장
        val timestamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date()) // 이미지 파일 이름
        val fileName = "capture_${timestamp}.jpeg"
        val out = FileOutputStream(folderPath + fileName)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)   // 비트맵 압축

        return folderPath + fileName // 저장한 파일 이름
    }

    override fun onStart() {
        super.onStart()
        screenshotDetectionDelegate.startScreenshotDetection()
    }

    override fun onStop() {
        super.onStop()
        screenshotDetectionDelegate.stopScreenshotDetection()
    }

    // 화면 캡쳐 시 이벤트
    override fun onScreenCaptured(path: String) {
        // path: /storage/emulated/0/DCIM/Screenshots/Screenshot_20220527-211810_MyCapture.jpg
        val bitmap = BitmapFactory.decodeFile(path)
        Toast(applicationContext).showCustomToast(applicationContext, bitmap, "방금 찍은 화면을 공유해보세요!")
    }
    // 화면 캡쳐를 위한 권한이 설정되지 않았을 때
    override fun onScreenCapturedWithDeniedPermission() {
        Toast.makeText(applicationContext, "캡쳐를 위해 권한을 설정해주세요.", Toast.LENGTH_SHORT).show()
    }
    // 권한 요청에 대한 응답 함수
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION -> {
                // 권한이 거부되었을 때
                if (grantResults.getOrNull(0) == PackageManager.PERMISSION_DENIED)
                    Toast.makeText(this@CaptureActivity,
                        "Read External Storage 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
    // 권한 확인 함수
    private fun checkReadExternalStoragePermission() {
        if (ContextCompat.checkSelfPermission(this@CaptureActivity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestReadExternalStoragePermission()
        }
    }
    // 권한 요청 함수
    private fun requestReadExternalStoragePermission() {
        ActivityCompat.requestPermissions(this@CaptureActivity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION)
    }

}