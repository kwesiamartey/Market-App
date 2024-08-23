package com.lornamobileappsdev.my_marketplace.ui

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.ads.*
import com.google.firebase.firestore.FirebaseFirestore
import com.lornamobileappsdev.my_marketplace.adsModel.AdsModel
import com.lornamobileappsdev.my_marketplace.api.Api
import com.lornamobileappsdev.my_marketplace.constant.firebase_document_id
import com.lornamobileappsdev.my_marketplace.constant.firebase_id
import com.lornamobileappsdev.my_marketplace.constant.test_website
import com.lornamobileappsdev.my_marketplace.constant.url_for_news
import com.lornamobileappsdev.my_marketplace.databinding.ActivityNewsLayoutBinding
import com.lornamobileappsdev.my_marketplace.models.IResponse
import com.lornamobileappsdev.my_marketplace.repository.ApiClient
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class NewsFragment : Fragment() {

    lateinit var _bind: ActivityNewsLayoutBinding
    val bind get() = _bind

    val viewModelClass: MyViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
    }

    lateinit var mAdView: AdView

    lateinit var firestore : FirebaseFirestore


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _bind = ActivityNewsLayoutBinding.inflate(layoutInflater)
        firestore = FirebaseFirestore.getInstance()

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


        MobileAds.initialize(requireContext()) {}

        mAdView = bind.adView
        firestore.collection(firebase_document_id).document(firebase_id).addSnapshotListener { value, error ->
            if (error != null) {
                // Handle the error here
                Log.e("iAds", "Error fetching Firestore document: ${error.message}")
                return@addSnapshotListener  // Exit the function to prevent further execution
            }

            if (value != null && value.exists()) {
                // Convert the Firestore document to your AdsModel class
                val adsModel = value.toObject(AdsModel::class.java)

                if (adsModel != null) {
                    // Now you can access the properties of adsModel
                    Log.i("iAds", "Document data: ${adsModel.status}")
                    if (adsModel.status){
                        val adRequest = AdRequest.Builder().build()
                        mAdView.loadAd(adRequest)
                    }

                } else {
                    Log.e("iAds", "Failed to convert Firestore document to AdsModel")
                }
            } else {
                Log.e("iAds", "Firestore document does not exist or is null")
            }
        }


        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar!!.show()
        (activity as AppCompatActivity).supportActionBar!!.title = "News"
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        mAdView.adListener = object : AdListener() {
            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                // Code to be executed when an ad request fails.
            }

            override fun onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
        }

        if (viewModelClass.isNetworkAvailable(test_website)) {
            lifecycleScope.launch {
                val it = getNews()
                if (it == null) {
                    bind.txtNewsWrapper.visibility = View.GONE
                } else {
                    it.let {
                        it.forEach { data ->
                            if (data.status == 0 && data.title.isNullOrEmpty()
                                && data.body.isNullOrEmpty() && data.Images.isNullOrEmpty()
                                && data.contact.isNullOrEmpty() && data.link
                                    .isNullOrEmpty()) {
                                bind.txtNewsWrapper.visibility = View.GONE
                            } else {
                                Log.i("news_feed", data.toString())

                                try {
                                    bind.txtNewsWrapper.visibility = View.VISIBLE
                                    var st = bind.txtNews
                                    var st1 = bind.txtLink
                                    var st2 = bind.txtTilte
                                    bind.txtTilte.setTextColor(Color.parseColor("${data.button_color}"))
                                    bind.txtNews.setTextColor(Color.parseColor("${data.button_color}"))


                                    bind.txtLink.text = "${data.button_text}"
                                    st.text = "${data.body}"
                                    st2.text = "${data.title}"
                                    st.isSelected = true

                                    if (data.contact.isNullOrEmpty()) {

                                        try {
                                            bind.txtLink.visibility = View.GONE
                                        } catch (e: Exception) {
                                        }

                                    } else {
                                        bind.txtLink.backgroundTintList = ColorStateList.valueOf(Color.parseColor("${data.button_color}"))
                                        bind.txtLink.setTextColor(Color.parseColor("${data.link}"))

                                        bind.txtLink.setOnClickListener { r ->
                                            viewModelClass.categoryName.value = "${data.contact}"
                                            viewModelClass.category.value = "${data.contact}"
                                            Toast.makeText(requireContext(), data.contact, Toast.LENGTH_LONG).show()
                                            (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, CategoryFragment()).addToBackStack(null).commit()
                                        }
                                    }

                                    if (data.Images.isNullOrEmpty()) {
                                        try {
                                            bind.img.visibility = View.GONE
                                        } catch (e: Exception) {
                                        }
                                    } else {
                                        val circularProgressDrawable = CircularProgressDrawable(requireContext())
                                        circularProgressDrawable.strokeWidth = 5f
                                        circularProgressDrawable.centerRadius = 30f
                                        circularProgressDrawable.start()
                                        val requestOptions = RequestOptions()
                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        Glide.with(requireContext())
                                            .load(data.Images)
                                            .apply(requestOptions)
                                            .placeholder(circularProgressDrawable)
                                            .dontAnimate()
                                            .into(bind.img)
                                        bind.newsOneWrapper.setBackgroundColor(Color.parseColor("${data.link}"))
                                    }
                                } catch (e: Exception) { }
                            }
                        }
                    }
                }
            }
        } else {
            (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, NoSignalWifiFragment()).addToBackStack(null).commit()
        }
    }

    @SuppressLint("SuspiciousIndentation")
    suspend fun getNews(): List<IResponse> {
        delay(1000L)
        if (viewModelClass.isNetworkAvailable(test_website)) {
            try {
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
                val client = apiClient.api.getData()
                if (client.isSuccessful) {
                    if (client.code() == 200) {
                        return client.body()!!
                    } else {
                        return emptyList()
                    }
                } else {
                    return emptyList()
                }
            } catch (e: Exception) { }

        } else {
            (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, NoSignalWifiFragment()).addToBackStack(null).commit()
        }
        return emptyList()
    }
}