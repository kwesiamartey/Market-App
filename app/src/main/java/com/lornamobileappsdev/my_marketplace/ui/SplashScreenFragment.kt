package com.lornamobileappsdev.my_marketplace.ui

import android.R
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import androidx.room.TypeConverter
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.lornamobileappsdev.my_marketplace.databinding.FragmentSplashScreenBinding
import com.lornamobileappsdev.my_marketplace.internet_connectivity.IsInternetConnectedWithData
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel
import com.lornamobileappsdev.my_marketplace.viewModels.PullingDataFromServerViewModel
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.*

import kotlin.reflect.jvm.jvmName


class SplashScreenFragment : Fragment() {

    lateinit var _bind: FragmentSplashScreenBinding
    val bind get() = _bind
    val viewModelClass: MyViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
    }
    val viewModelClassTwo: PullingDataFromServerViewModel by lazy {
        ViewModelProvider(requireActivity()).get(PullingDataFromServerViewModel::class.java)
    }

    val IsInternetConnectedWithData = IsInternetConnectedWithData()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _bind = FragmentSplashScreenBinding.inflate(layoutInflater)

         /*CoroutineScope(Dispatchers.IO).launch {
             delay(1000L)
             viewModelClass.deleteAllproductDatatbase()
         }*/


        val window: Window = requireActivity().window

        // clear FLAG_TRANSLUCENT_STATUS flag:

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.black)



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


    @TypeConverter
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar!!.hide()
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)


        GlobalScope.launch(Dispatchers.IO) {
            val isConnectedWithData =  IsInternetConnectedWithData.isInternetConnectedWithData(requireContext())
            requireActivity().runOnUiThread {
                if (isConnectedWithData) {

                    lifecycleScope.launch {
                        delay(2000L)
                        (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                            .replace(R.id.content, DashboardFragment()).addToBackStack(null)
                            .commit()           
                    }


                }else {
                    Log.i("internet_connectivity", "internet_connectivity " +isConnectedWithData.toString())
                    // Internet is connected, but there is no data access
                    (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                        .replace(R.id.content, NoSignalWifiFragment()).addToBackStack(null).commit()
                }
            }
        }


        bind.lottie.setupAnim()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("splash", "splashScreen")

    }
}

