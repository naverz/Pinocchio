/**
 * Pinocchio
 * Copyright (c) 2022-present NAVER Z Corp.
 * Apache-2.0
 */

package io.github.naverz.pinocchio.hsvpicker.sample

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import io.github.naverz.hsvpicker.sample.R

class LaunchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)
        findViewById<View>(R.id.hsv_picker_composable).setOnClickListener {
            launchMainActivityCompose()
        }
        findViewById<View>(R.id.hsv_picker_view_with_java).setOnClickListener {
            launchMainActivityByJava()
        }
        findViewById<View>(R.id.hsv_picker_view_with_kotlin).setOnClickListener {
            launchMainActivityByKotlin()
        }
    }

    private fun launchMainActivityByKotlin() {
        startActivity(Intent(this, MainActivityByKotlin::class.java))
    }

    private fun launchMainActivityByJava() {
        startActivity(Intent(this, MainActivityByJava::class.java))
    }

    private fun launchMainActivityCompose() {
        startActivity(Intent(this, MainActivityCompose::class.java))
    }
}