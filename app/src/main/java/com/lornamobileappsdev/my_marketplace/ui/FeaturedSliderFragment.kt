package com.lornamobileappsdev.my_marketplace.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.lornamobileappsdev.my_marketplace.R
import com.lornamobileappsdev.my_marketplace.api.Api
import com.lornamobileappsdev.my_marketplace.constant.test_website
import com.lornamobileappsdev.my_marketplace.databinding.FragmentFeaturedSliderBinding
import com.lornamobileappsdev.my_marketplace.models.FeaturedSliderDataResponse
import com.lornamobileappsdev.my_marketplace.repository.ApiClient
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class FeaturedSliderFragment : Fragment() {

    lateinit var _bind: FragmentFeaturedSliderBinding
    val bind get() = _bind

    val viewModelClass: MyViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
    }

    val productImagesList = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _bind = FragmentFeaturedSliderBinding.inflate(layoutInflater)


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

        when (viewModelClass.isNetworkAvailable(test_website)) {
            true -> {
                CoroutineScope(Dispatchers.IO).launch {
                }
            }
            else -> {
                try {
                    (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                        .replace(android.R.id.content, NoSignalWifiFragment()).addToBackStack(null).commit()
                } catch (e: Exception) {
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            if(getProductsItems().size == 0){
                bind.fSliderWrapper.visibility = View.GONE
            }else{
                bind.fSliderWrapper.visibility = View.VISIBLE
                getProductsItems().forEach {
                    productImagesList.add(it.images!!)

                    val dotViews = mutableListOf<View>()

                    for (i in 0 until bind.dashViewPager.childCount) {
                        val dotView = ImageView(requireContext())
                        dotView.setImageResource(R.drawable.ic_baseline_circle_24)
                        dotView.setPadding(8, 8, 8, 8)
                        bind.dotsLayout.addView(dotView)
                        dotViews.add(dotView)
                    }

                    dotViews[0].isSelected = true


                    for (photo in productImagesList) { //or something like this

                        val circularProgressDrawable = CircularProgressDrawable(requireContext())
                        circularProgressDrawable.strokeWidth = 5f
                        circularProgressDrawable.centerRadius = 30f
                        circularProgressDrawable.start()

                        val image = ImageView(requireContext())
                        image.scaleType = ImageView.ScaleType.FIT_XY
                        Glide.with(requireActivity())
                            .asBitmap()
                            .load(photo)
                            .placeholder(circularProgressDrawable)
                            .fitCenter()
                            .error(R.drawable.placeholder)
                            .into(image)
                        bind.dashViewPager.visibility = View.VISIBLE
                        bind.dashViewPager.addView(image)
                    }

                    bind.dashViewPager.flipInterval = 5000//5s intervals
                    bind.dashViewPager.startFlipping()
                    bind.prevBtn.setOnClickListener {
                        bind.dashViewPager.showPrevious()
                    }
                    bind.nextBtn.setOnClickListener {
                        bind.dashViewPager.showNext()
                    }

                    bind.dashViewPager.setInAnimation(requireContext(), android.R.anim.slide_in_left)
                    bind.dashViewPager.setOutAnimation(requireContext(), android.R.anim.slide_out_right)

                   /* bind.dashViewPager.setOn { viewFlipper ->
                        val index = viewFlipper.displayedChild
                        for (i in 0 until bind.dotsLayout.childCount) {
                            dotViews[i].isSelected = i == index
                        }
                    }*/
                }
            }

        }

        return bind.root
    }

    suspend fun getProductsItems(): List<FeaturedSliderDataResponse> {
        delay(1000L)

        if (viewModelClass.isNetworkAvailable(test_website)) {

            try {

                val okHttpClient = OkHttpClient().newBuilder()
                    .connectTimeout(3, TimeUnit.MINUTES)
                    .readTimeout(3, TimeUnit.MINUTES)
                    .writeTimeout(3, TimeUnit.MINUTES)
                    .build()

                val retrofit: Retrofit.Builder by lazy {
                    Retrofit.Builder().baseUrl("https://structuredappsstreaming.win/")
                        .client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create())
                }
                val retrofitApi: Api by lazy {
                    //retrofit.client(okHttpClient)
                    retrofit.build().create(Api::class.java)
                }
                val apiClient = ApiClient(retrofitApi)
                val client = apiClient.api.getFeaturedSliderData()
                if (client.isSuccessful){
                    if (client.code() == 200){
                        return client.body()!!
                    }else{
                        return emptyList()
                    }
                }else{
                    return emptyList()
                }


            }catch (e:Exception){}


        } else {
            (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, NoSignalWifiFragment()).addToBackStack(null).commit()
        }
        return emptyList()
    }
}