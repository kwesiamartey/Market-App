package com.lornamobileappsdev.my_marketplace.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.facebook.shimmer.ShimmerFrameLayout
import com.lornamobileappsdev.my_marketplace.R
import com.lornamobileappsdev.my_marketplace.constant.app_name
import com.lornamobileappsdev.my_marketplace.databinding.FragmentCheckUserSignedOrSingedBinding
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel


class CheckUserSignedOrSingedFragment : Fragment() {

    lateinit var _bind:FragmentCheckUserSignedOrSingedBinding

    val bind get() = _bind

    val viewModel: MyViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
    }

    lateinit var shimmerFrameLayout: ShimmerFrameLayout

    val expandIn by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.anim)
    }

    var userDetails:Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        _bind = FragmentCheckUserSignedOrSingedBinding.inflate(layoutInflater)

        val supportActionBar: ActionBar? = (requireActivity() as AppCompatActivity).supportActionBar
        if (supportActionBar != null) supportActionBar.hide()

        val window: Window = requireActivity().window

        // clear FLAG_TRANSLUCENT_STATUS flag:

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(requireContext(), android.R.color.black)

        bind.apply {
            txtTitle.text = "Sign in so we can personalize your ${app_name} experience"
        }

        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try{
            viewModel.queryUserDetails().asLiveData()
                .observe(viewLifecycleOwner, Observer {
                    try {
                        if (it == null ) {
                            bind.credentialCheckerWrapper.visibility = View.VISIBLE
                            bind.loginDash.setOnClickListener {
                                val intent = Intent(requireContext(), SignInFragment::class.java)
                                startActivity(intent)
                            }
                            bind.signupDash.setOnClickListener {
                                val intent = Intent(requireContext(), SignUpFragment::class.java)
                                startActivity(intent)
                            }
                        } else {
                            bind.credentialCheckerWrapper.visibility = View.GONE
                        }
                    } catch (e: Exception) { }
                    Log.i ("user_dey?", "$it")
                })
        }catch (e:Exception){}
    }
}