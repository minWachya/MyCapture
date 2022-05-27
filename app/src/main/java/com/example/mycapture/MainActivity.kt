package com.example.mycapture

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.mycapture.databinding.ActivityMainBinding
import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.apache.poi.xwpf.usermodel.XWPFParagraph
import org.apache.poi.xwpf.usermodel.XWPFRun
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

private lateinit var binding: ActivityMainBinding
private const val TAG = "mmmMainActivity"

// 메인 화면
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // 권한 허용 묻기
        requestPermission()

        // <캡쳐하기> 버튼 클릭
        binding.btnCapture.setOnClickListener {
            val intent = Intent(this@MainActivity, CaptureActivity::class.java)
            startActivity(intent)
        }

        // <파일 생성하기> 버튼 클릭
        binding.btnCreateFile.setOnClickListener {
            val intent = Intent(this@MainActivity, DocActivity::class.java)
            startActivity(intent)
        }
    }

    // 권한 묻는 메소드
    private fun requestPermission() {
        ActivityCompat.requestPermissions(this@MainActivity,
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
            PackageManager.PERMISSION_GRANTED)
    }

}