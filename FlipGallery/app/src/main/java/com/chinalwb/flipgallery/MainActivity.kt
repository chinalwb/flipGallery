package com.chinalwb.flipgallery

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.chinalwb.flipgallery.library.FlipGallery

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var flipGallery = findViewById<FlipGallery>(R.id.flip_gallery)
        flipGallery.setFlipDuration(300).setFlipIndex(0)

        flipGallery.postDelayed({
            flipGallery.smoothFlipToEnd(6000)
        }, 1000)
    }
}
