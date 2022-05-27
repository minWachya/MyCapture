package com.example.mycapture

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import com.example.mycapture.databinding.ActivityDocBinding
import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.apache.poi.xwpf.usermodel.XWPFParagraph
import org.apache.poi.xwpf.usermodel.XWPFRun
import java.io.File
import java.io.FileOutputStream

private const val PICK_FROM_ALBUM = 0

private lateinit var binding: ActivityDocBinding
private const val TAG = "mmmDocActivity"

// 파일 미리보기/생성하기 화면
class DocActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDocBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // 이미지뷰 클릭 시 이미지 선택하기
        binding.imgView.setOnClickListener {
            pickImageFromGallery()
        }

        // <파일 생성하기> 버튼 클릭
        binding.btnCreateFile.setOnClickListener {
            // 파일에 생성+작성+저장
            createWordFile(binding.editText.text.toString())
        }
    }

    // 이미지 선택하는 함수
    private fun pickImageFromGallery() {
        val intent = Intent("android.intent.action.GET_CONTENT")
        intent.type = "image/*"     // 모든 이미지
        startActivityForResult(intent, PICK_FROM_ALBUM)
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

    // 읍답 받은 액티비티(선택한 사진 이미지뷰에 띄우기)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode) {
            // 갤러리
            PICK_FROM_ALBUM -> {
                if (resultCode == Activity.RESULT_OK) { // && Build.VERSION.SDK_INT >= 19
                    data?.data.let {
                        // 이미지 파일 읽어와서 설정하기
                        val img = BitmapFactory.decodeStream(contentResolver.openInputStream(data?.data!!))
                        binding.imgView.setImageBitmap(img)
                    }
                }

            }
        }   // end of when
    }   // end of onActivityResult

}