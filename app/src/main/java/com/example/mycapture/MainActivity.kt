package com.example.mycapture

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.format.DateFormat
import android.view.PixelCopy
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.mycapture.databinding.ActivityMainBinding
import java.io.File
import java.io.FileOutputStream
import java.util.*

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnCapture.setOnClickListener { takeScreenshot() }
    }

    private fun takeScreenshot() {
        val b: Bitmap = Screenshot.takeScreenshotOfRootView(binding.imgView)
        binding.imgView.setImageBitmap(b)
    }

    companion object Screenshot {
        private fun takeScreenshot(view: View): Bitmap {
            view.isDrawingCacheEnabled = true
            view.buildDrawingCache(true)
            val b = Bitmap.createBitmap(view.drawingCache)
            view.isDrawingCacheEnabled = false
            return b
        }
        fun takeScreenshotOfRootView(v: View): Bitmap {
            return takeScreenshot(v.rootView)
        }
    }

}