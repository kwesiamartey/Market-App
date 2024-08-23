package com.lornamobileappsdev.my_marketplace.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.lornamobileappsdev.my_marketplace.databinding.FragmentSuccessfulSignInBinding
import com.lornamobileappsdev.my_marketplace.viewModels.PullingDataFromServerViewModel
import kotlin.reflect.jvm.jvmName


class SuccessfulSignInFragment : Fragment() {

    lateinit var _bind: FragmentSuccessfulSignInBinding
    val bind get() = _bind

    val viewModelClass: PullingDataFromServerViewModel by lazy {
        ViewModelProvider(requireActivity()).get(PullingDataFromServerViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        _bind = FragmentSuccessfulSignInBinding.inflate(layoutInflater)

        (activity as AppCompatActivity).supportActionBar!!.hide()

        bind.btnRLogin.setOnClickListener {
            (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, DashboardFragment()).commit()
        }

        viewModelClass.globalText.observe(viewLifecycleOwner, Observer {
            bind.textView23.text = "Welcome back " + it
        })

        bind.lottie.setupAnim()


        return bind.root
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
}