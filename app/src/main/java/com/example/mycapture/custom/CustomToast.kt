package com.example.mycapture.custom

import android.content.Context
import android.graphics.Bitmap
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import com.example.mycapture.R
import com.example.mycapture.databinding.CustomToastBinding

// 커스텀 토스트
object CustomToast {
    fun Toast.showCustomToast(context: Context, image: Bitmap, message: String) {
        val layout = LayoutInflater.from(context).inflate(R.layout.custom_toast,null)
        val binding = CustomToastBinding.bind(layout)
        // 이미지 설정
        binding.imgView.setImageBitmap(image)
        // 텍스트 설정
        binding.textView.text = message
        // show
        this.apply {
            setGravity(Gravity.TOP, 0, 80)
            duration = Toast.LENGTH_SHORT
            view = layout
            show()
        }
    }
}