package com.lornamobileappsdev.my_marketplace.ui

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.lornamobileappsdev.my_marketplace.databinding.FragmentSuccessFullPageBinding
import kotlin.reflect.jvm.jvmName


class SuccessFullPageFragment : AppCompatActivity() {

    lateinit var _bind:FragmentSuccessFullPageBinding
    val bind get() = _bind

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout for this fragment
        _bind = FragmentSuccessFullPageBinding.inflate(layoutInflater)

        setContentView(bind.root)

        val window: Window = this.window

        // clear FLAG_TRANSLUCENT_STATUS flag:

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        supportActionBar!!.hide()
        bind.btnRLogin.setOnClickListener {
            val intent = Intent(this@SuccessFullPageFragment, SignInFragment::class.java)
            startActivity(intent)
        }

        bind.lottie.setupAnim()
    }

    private fun LottieAnimationView.setupAnim() {
        try {
            val field = javaClass.getDeclaredField("lottieDrawable")
            field.isAccessible = true
            val lottieDrawable = (field.get(this) as LottieDrawable)
            val clazz = Class.forName(LottieDrawable::class.jvmName)
            val systemAnimationsEnabled = clazz.getDeclaredField("systemAnimationsEnabled")
            systemAnimationsEnabled.isAccessible = true
            systemAnimationsEnabled.set(lottieDrawable, true)
        } catch (ex: Exception) { }
    }

    override fun onBackPressed() {
        //Todo
    }
}