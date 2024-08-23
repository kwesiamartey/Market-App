package com.lornamobileappsdev.my_marketplace.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import com.bumptech.glide.Glide
import com.google.android.gms.ads.*
import com.lornamobileappsdev.my_marketplace.api.Api
import com.lornamobileappsdev.my_marketplace.constant.test_website
import com.lornamobileappsdev.my_marketplace.constant.url_for_news
import com.lornamobileappsdev.my_marketplace.databinding.ActivityNewsTwoLayoutBinding
import com.lornamobileappsdev.my_marketplace.models.IResponse
import com.lornamobileappsdev.my_marketplace.repository.ApiClient
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.delay
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class NewsTwoFragment : Fragment() {

    lateinit var _bind: ActivityNewsTwoLayoutBinding
    val bind get() = _bind

    val viewModelClass: MyViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _bind = ActivityNewsTwoLayoutBinding.inflate(layoutInflater)

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

        if (viewModelClass.isNetworkAvailable(test_website)) {
            lifecycleScope.launchWhenStarted {
                val it = getNews()
                if(it == null){
                    bind.txtNewsWrapper.visibility = View.GONE
                }else{
                    it.let {
                        it.forEach {data->
                            if(data.status == 0){
                                bind.txtNewsWrapper.visibility = View.GONE
                            }else{
                                Log.i("news_feed", data.toString())
                                try {
                                    var st = bind.txtNews
                                    var st1 = bind.txtLink
                                    var st2 = bind.txtTilte


                                    st.text = "${data.body}"
                                    st2.text = "${data.title}"
                                    st.isSelected = true

                                    bind.txtNewsWrapper.visibility = View.VISIBLE

                                    if(data.link.isNullOrEmpty()){
                                        bind.txtLink.visibility = View.GONE
                                    }else{
                                        bind.txtLink.setOnClickListener { r ->
                                            viewModelClass.link.value = data.link
                                            requireActivity().supportFragmentManager.beginTransaction()
                                                .replace(android.R.id.content, WebFragment()).addToBackStack(null).commit()
                                        }
                                    }

                                    if(data.Images.isNullOrEmpty()){
                                        bind.img.visibility = View.GONE
                                    }else{
                                        Glide.with(requireContext()).asBitmap().load(data.Images).into(bind.img)
                                    }

                                } catch (e: Exception) { }
                            }
                        }
                    }
                }
            }
        }else{
            (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, NoSignalWifiFragment()).addToBackStack(null).commit()
        }

        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar!!.show()
        (activity as AppCompatActivity).supportActionBar!!.title = "News"
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)



    }

    @SuppressLint("SuspiciousIndentation")
    suspend fun getNews() :List<IResponse> {
        delay(1000L)

        if (viewModelClass.isNetworkAvailable(test_website)) {
            try{
                val okHttpClient = OkHttpClient().newBuilder()
                    .connectTimeout(3, TimeUnit.MINUTES)
                    .readTimeout(3, TimeUnit.MINUTES)
                    .writeTimeout(3, TimeUnit.MINUTES)
                    .build()

                val retrofit: Retrofit.Builder by lazy {
                    Retrofit.Builder().baseUrl(url_for_news)
                        .client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create())
                }
                val retrofitApi: Api by lazy {
                    retrofit.client(okHttpClient)
                    retrofit.build().create(Api::class.java)
                }
                val apiClient = ApiClient(retrofitApi)
                val client = apiClient.api.getDataTwo()
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