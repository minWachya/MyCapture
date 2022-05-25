package com.example.mycapture

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
            // 스크린샷 찍기
            val bitmap = takeScreenshot(view)
            binding.imgView.setImageBitmap(bitmap)
            // 갤러리에 저장하기
            savePhoto(bitmap!!)
        }

        // <파일 생성하기> 버튼 클릭
        binding.btnCreateFile.setOnClickListener {
            // 파일에 생성+작성+저장
            createWordFile(binding.editText.text.toString())
        }
    }

    // 권한 묻는 메소드
    private fun requestPermission() {
        ActivityCompat.requestPermissions(this@MainActivity,
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
            PackageManager.PERMISSION_GRANTED)
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
        val folderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/MyCapture/"
        // 폴더 생성(해당 경로의 폴더가 존재하지 않으면 해당 경로에 폴더 생성)
        val folder = File(folderPath)
        if (!folder.isDirectory) folder.mkdir()

        // 폴더 경로에 파일 생성 + 최종 저장
        val timestamp : String  = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date()) // 이미지 파일 이름
        val fileName = "capture_${timestamp}.jpeg"
        val out = FileOutputStream(folderPath + fileName)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)   // 비트맵 압축

        return folderPath + fileName // 저장한 파일 이름
    }

    // 파일 생성하고 저장하는 메소드
    private fun createWordFile(text: String) {
        // 폴더 생성(해당 경로의 폴더가 존재하지 않으면 해당 경로에 폴더 생성)
        val folderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() + "/MyCapture/"
        val folder = File(folderPath)
        if (!folder.isDirectory) folder.mkdir()

        // 폴더 경로에 파일 생성 + 최종 저장
        try {
            val xwpfDocument = XWPFDocument()
            val xwpfParagraph: XWPFParagraph = xwpfDocument.createParagraph()
            val xwpfRun: XWPFRun = xwpfParagraph.createRun()

            xwpfRun.setText(text)
            xwpfRun.fontSize = 24
            xwpfRun.embeddedPictures
            xwpfRun.emphasisMark
            xwpfRun.phonetic
            xwpfRun.pictureText
            xwpfRun.subscript
            xwpfRun.textPosition
//            xwpfRun.addPicture()
            xwpfRun.kerning
            xwpfRun.addTab()

            val fileOutputStream = FileOutputStream(folderPath + "Test.docx")
            xwpfDocument.write(fileOutputStream)

            fileOutputStream.flush()
            fileOutputStream.close()
            xwpfDocument.close()

            Toast.makeText(applicationContext, "파일을 성공적으로 저장했습니다.", Toast.LENGTH_SHORT).show()
        }
        catch (e: Exception){
            e.printStackTrace()
            Toast.makeText(applicationContext, "파일 쓰기/저장 중 문제가 발생했습니다.", Toast.LENGTH_SHORT).show()
            return
        }
    }

}