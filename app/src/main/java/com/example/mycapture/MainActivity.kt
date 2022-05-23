package com.example.mycapture

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mycapture.databinding.ActivityMainBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnCapture.setOnClickListener {
            // 스크린샷 찍기
            val bitmap = takeScreenshot(view)
            binding.imgView.setImageBitmap(bitmap)
            // 갤러리에 저장하기
            savePhoto(bitmap!!)
        }
    }

    private fun takeScreenshot(view: View): Bitmap? {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    // 갤러리에 저장하는 메소드
    private fun savePhoto(bitmap: Bitmap) : String {
        // 사진 폴더로 저장하기 위한 경로 선언
        val folderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/MyCapture/"
        val timestamp : String  = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date()) // 이미지 파일 이름
        val fileName = "capture_${timestamp}.jpeg"
        // 폴더 생성(해당 경로의 폴더가 존재하지 않으면 해당 경로에 폴더 생성)
        val folder = File(folderPath)
        if (!folder.isDirectory) folder.mkdir()

        // 최종 저장
        val out = FileOutputStream(folderPath + fileName)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)   // 비트맵 압축

        return folderPath + fileName // 저장한 파일 이름
    }

}