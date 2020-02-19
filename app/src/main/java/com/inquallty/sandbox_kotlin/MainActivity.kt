package com.inquallty.sandbox_kotlin

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.transition.Scene
import androidx.transition.TransitionInflater
import androidx.transition.TransitionManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_main)
        val s1 = Scene.getSceneForLayout(findViewById(R.id.scene_root), R.layout.s_main, this)
        val s2 = Scene.getSceneForLayout(findViewById(R.id.scene_root), R.layout.s_secondary, this)
        TransitionManager.go(s1)
        s1.sceneRoot.findViewById<View>(R.id.ok).setOnClickListener {
            val transition = TransitionInflater.from(this).inflateTransition(R.transition.s_tran)
            TransitionManager.go(s2, transition)
        }
    }
}
