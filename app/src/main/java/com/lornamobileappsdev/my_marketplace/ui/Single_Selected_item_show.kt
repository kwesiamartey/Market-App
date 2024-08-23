package com.lornamobileappsdev.my_marketplace.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.*
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.TypeConverter
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.ads.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.lornamobileappsdev.my_marketplace.BuildConfig
import com.lornamobileappsdev.my_marketplace.R
import com.lornamobileappsdev.my_marketplace.adapter.FeedbackListAdapter
import com.lornamobileappsdev.my_marketplace.adsModel.AdsModel
import com.lornamobileappsdev.my_marketplace.api.Api
import com.lornamobileappsdev.my_marketplace.constant.firebase_document_id
import com.lornamobileappsdev.my_marketplace.constant.firebase_id
import com.lornamobileappsdev.my_marketplace.constant.test_website
import com.lornamobileappsdev.my_marketplace.constant.token
import com.lornamobileappsdev.my_marketplace.databinding.FragmentSelectedItemShowSingleItemBinding
import com.lornamobileappsdev.my_marketplace.entity.ChatData
import com.lornamobileappsdev.my_marketplace.entity.ProductMySavedData
import com.lornamobileappsdev.my_marketplace.entity.SignupResponses
import com.lornamobileappsdev.my_marketplace.models.CommentData
import com.lornamobileappsdev.my_marketplace.models.PostLikesData
import com.lornamobileappsdev.my_marketplace.repository.ApiClient
import com.lornamobileappsdev.my_marketplace.responses.ChatDataResponse
import com.lornamobileappsdev.my_marketplace.responses.FeedbackData
import com.lornamobileappsdev.my_marketplace.singletons.Singleton
import com.lornamobileappsdev.my_marketplace.useCases.*
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DecimalFormat
import java.text.NumberFormat
import java.time.LocalDate
import java.util.*
import java.util.concurrent.TimeUnit


class Single_Selected_item_show : Fragment(), OnClickListernersRecyclervire,

    OnCommentLikeListernersRecyclervire, OnReplyLikeListernersRecyclervire, CommentLikesCounts, ReplyLikesCounts {

    lateinit var _bind: FragmentSelectedItemShowSingleItemBinding
    val bind get() = _bind

    val viewModelClass: MyViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
    }

    val viewModelClass1: MyViewModel by lazy {
        ViewModelProvider(this).get(MyViewModel::class.java)
    }

    val expandIn by lazy {
        AnimationUtils.loadAnimation(context, R.anim.anim)
    }

    val obs: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>(0)
    }

    var id: Int? = null

    var phone_number: String? = null

    var userId: Int? = null

    var productId: Int? = null

    var adReportId: String? = null

    var fullname: String? = null

    var product_title: String? = null

    var category: String? = null

    var desc:String? =null

    var myUserId: String? = null

    var other_uder_id: Int? = null

    lateinit var layoutManager1: LinearLayoutManager

    lateinit var shimmerFrameLayout: ShimmerFrameLayout

    lateinit var mAdView1: AdView

    val outState:MutableList<SignupResponses>? = null

    val outState2: ArrayList<String> = arrayListOf()

    val s = SimpleDateFormat("dd/MM/yyyy") //,hh:mm:ss
    val ss = SimpleDateFormat("hh:mm:ss") //,hh:mm:ss

    val format: String = s.format(Date())
    val timeStamp = format

    val format_time: String = ss.format(Date())
    val timeSt = format_time

    val today = LocalDate.now()

    val productImagesList = mutableListOf<ByteArray>()

    lateinit var mAdView: AdView
    lateinit var mAdView11: AdView

    lateinit var firestore : FirebaseFirestore

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _bind = FragmentSelectedItemShowSingleItemBinding.inflate(layoutInflater)

        firestore = FirebaseFirestore.getInstance()
        
        bind.bottomAppBar.setBackgroundColor(resources.getColor(R.color.white))

        if(savedInstanceState != null){

            lifecycleScope.launch {
                //Toast.makeText(requireContext(), "${viewModelClass.productId.value}", Toast.LENGTH_LONG).show()
                viewModelClass.queryUserDetails().asLiveData().observe(viewLifecycleOwner, Observer {
                    try {
                        Log.i("uuuuuuTT", "${it.id} == ${viewModelClass.postuserId.value}")
                        // Toast.makeText(requireContext(), "${it}", Toast.LENGTH_LONG).show()
                        myUserId = it.id.toString()
                        fullname = it.fullName
                        other_uder_id = it.id

                    } catch (e: Exception) { }

                    try {
                        if (it.id == viewModelClass.postuserId.value) {
                            bind.btnEditPr.visibility = View.VISIBLE

                        } else {
                            bind.btnEditPr.visibility = View.GONE
                        }

                    } catch (e: Exception) {
                    }
                })
            }

            val s = SimpleDateFormat("dd/MM/yyyy") //,hh:mm:ss
            val format: String = s.format(Date())
            val timeStamp = format

            val ss = SimpleDateFormat("hh:mm:ss") //,hh:mm:ss
            val format_time: String = ss.format(Date())
            val timeSt = format_time

            val today = LocalDate.now()

            bind.bk.setOnClickListener {
                (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(android.R.id.content, DashboardFragment()).addToBackStack(null).commit()
            }

            bind.seeFullDecription.setOnClickListener {
                (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                    .replace(android.R.id.content, SeeFullDescriptionFragment()).addToBackStack(null).commit()
            }

            //Edit button
            bind.btnEditPr.setOnClickListener {
                val animationSet = AnimationSet(true)
                animationSet.addAnimation(
                    AnimationUtils.loadAnimation(
                        context,
                        R.anim.like_button_anim
                    )
                )
                it.startAnimation(animationSet)
                //viewModelClass.productId.value = productId.toString()
                //viewModelClass1.id.value = id.toString()
                (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                    .replace(android.R.id.content, EditaProductFragment()).addToBackStack(null).commit()
            }

            bind.sglShowContact.setOnClickListener {o->
                lifecycleScope.launchWhenCreated {
                    viewModelClass.queryUserDetails().asLiveData().observe(viewLifecycleOwner, Observer {
                        if (it == null) {
                            Snackbar.make(
                                bind.bk,
                                "You must login first before you can save",
                                Snackbar.LENGTH_LONG
                            ).show()

                            lifecycleScope.launchWhenStarted {
                                delay(2000L)
                                val intent =
                                    Intent(requireContext(), SignInFragment::class.java)
                                startActivity(intent)
                            }

                            //(activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, SignInFragment()).show()

                        } else {
                            try{
                                val animationSet = AnimationSet(true)
                                animationSet.addAnimation(
                                    AnimationUtils.loadAnimation(
                                        context,
                                        R.anim.like_button_anim
                                    )
                                )
                                o.startAnimation(animationSet)
                                bind.sglShowContactText.text = "${viewModelClass.phone_number.value}"
                                o.setOnClickListener {
                                    val phoneNumber = "${viewModelClass.phone_number.value}"
                                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
                                    startActivity(intent)
                                }
                            }catch(e:Exception){ }
                        }
                    })
                }
            }

            bind.save.setOnClickListener {
                lifecycleScope.launchWhenCreated {

                    viewModelClass.queryUserDetails().asLiveData().observe(viewLifecycleOwner, Observer {
                        if (it == null) {
                            Snackbar.make(
                                bind.bk,
                                "You must login first before you can save",
                                Snackbar.LENGTH_LONG
                            ).show()

                            lifecycleScope.launchWhenStarted {
                                delay(2000L)
                                val intent =
                                    Intent(requireContext(), SignInFragment::class.java)
                                startActivity(intent)
                            }

                            //(activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, SignInFragment()).show()

                        } else {

                            try {

                                viewModelClass.getSingleProductItemDatabase(viewModelClass.productId.value!!).observe(viewLifecycleOwner, Observer {
                                    try {
                                        Log.i("getSingleProduct", it[0].rates.toString())
                                        try {
                                            val products = ProductMySavedData(
                                                id = it[0].id,
                                                userId = it[0].userId,
                                                user_name = it[0].user_name,
                                                title = it[0].title,
                                                currenxy =it[0].currenxy,
                                                price = it[0].price.toString(),
                                                category = it[0].category,
                                                country = it[0].country,
                                                location = it[0].location,
                                                desc = it[0].desc,
                                                dateAndtimme = it[0].dateAndtimme,
                                                type = it[0].type,
                                                negotiation = it[0].negotiation,
                                                image = it[0].image,
                                                image_two = it[0].image_two,
                                                image_three = it[0].image_three,
                                                image_four = it[0].image_four,
                                                image_five = it[0].image_five,
                                                image_six = it[0].image_six,
                                                paid_product = it[0].paid_product,
                                                pst_phone_number = it[0].pst_phone_number.toString(),
                                                subscription_type = it[0].subscription_type,
                                                subscription_date_start = it[0].subscription_date_start,
                                                subscription_date_due = it[0].subscription_date_due,
                                                views = it[0].views,
                                                approval_status = it[0].approval_status,
                                                rates = viewModelClass.Itemsratings.value
                                            )

                                            lifecycleScope.launchWhenStarted {
                                                viewModelClass1.insertIntoMySavedProductDatatbase(
                                                    products
                                                )
                                            }

                                        } catch (e: Exception) { }
                                    } catch (e: Exception) {
                                    }
                                })

                                Snackbar.make(
                                    bind.bk,
                                    "Item successfully saved",
                                    Snackbar.LENGTH_LONG
                                ).show()

                            } catch (e: Exception) { }
                        }
                    })
                }
            }

            when (viewModelClass.isNetworkAvailable(test_website)) {
                true -> {

                    lifecycleScope.launch {
                        getProductFeedback()
                    }

                    obs.observe(viewLifecycleOwner, Observer {
                        if (it == 1) {
                            lifecycleScope.launch {
                                getProductFeedback()
                            }
                        } else { }
                    })

                    bind.sglSendMessage.setOnClickListener {
                        lifecycleScope.launchWhenStarted {
                            viewModelClass.queryUserDetails().collect {
                                if (it == null) {
                                    val alertDialog = AlertDialog.Builder(requireContext())
                                    alertDialog.setMessage("You haven't registered or signed in please do so.")
                                    alertDialog.setPositiveButton("Continue") { _, _ ->
                                        val intent =
                                            Intent(requireContext(), SignUpFragment::class.java)
                                        startActivity(intent)
                                    }
                                    alertDialog.show()
                                } else {
                                    val bottomSheetDialog = BottomSheetDialog(requireContext())
                                    bottomSheetDialog.setContentView(R.layout.activity_comment_reply_box)
                                    bottomSheetDialog.findViewById<TextView>(R.id.send_a_comment)!!
                                        .setOnClickListener {

                                            bottomSheetDialog.findViewById<ProgressBar>(R.id.submittingrrrprogress)!!.visibility =
                                                View.VISIBLE
                                            val comment =
                                                bottomSheetDialog.findViewById<EditText>(R.id.comment_txt)!!.text
                                            if (comment!!.isEmpty()) {
                                                bottomSheetDialog.findViewById<ProgressBar>(R.id.submittingrrrprogress)!!.visibility =
                                                    View.GONE
                                                Toast.makeText(
                                                    requireContext(),
                                                    "message shouldn't be empty",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            } else {

                                                lifecycleScope.launchWhenStarted {
                                                    viewModelClass.queryUserDetails()
                                                        .asLiveData(Dispatchers.IO)
                                                        .observe(viewLifecycleOwner, Observer { b ->
                                                            if (b == null) {
                                                                val alertDialog =
                                                                    AlertDialog.Builder(requireContext())
                                                                alertDialog.setMessage("You haven't registered or signed in please do so.")
                                                                alertDialog.setPositiveButton("Continue") { _, _ ->
                                                                    val intent = Intent(
                                                                        requireContext(),
                                                                        SignUpFragment::class.java
                                                                    )
                                                                    startActivity(intent)
                                                                }
                                                                alertDialog.show()
                                                            } else {

                                                                viewModelClass.productId.observe(
                                                                    viewLifecycleOwner,
                                                                    Observer { p ->
                                                                        lifecycleScope.launchWhenCreated {
                                                                            val data =
                                                                                SearchProduct.searchProduct(
                                                                                    "approved",
                                                                                    p.toInt()
                                                                                )
                                                                            data.forEach { i ->
                                                                                val chatData =
                                                                                    CommentData(
                                                                                        user_id = b.id,
                                                                                        product_id = i.id,
                                                                                        comment = comment.toString(),
                                                                                        liked = 0,
                                                                                        date_and_time = "$timeStamp: $timeSt",
                                                                                        comment_id = "${b.id!!}${i.id!!}".toInt(),
                                                                                        type = "comments"
                                                                                    )
                                                                                Log.i(
                                                                                    "cxc", chatData.toString() )
                                                                                //commentingOnPost(myUserId!!.toInt(), chatData)
                                                                                try{
                                                                                    val okHttpClient = OkHttpClient().newBuilder()
                                                                                        .connectTimeout(3, TimeUnit.MINUTES)
                                                                                        .readTimeout(3, TimeUnit.MINUTES)
                                                                                        .writeTimeout(3, TimeUnit.MINUTES)
                                                                                        .addInterceptor { chain ->
                                                                                            val request: Request =
                                                                                                chain.request().newBuilder()
                                                                                                    .addHeader("Authorization", "Bearer ${token}")
                                                                                                    .addHeader("Content-Type", "application/text")
                                                                                                    .build()
                                                                                            chain.proceed(request)
                                                                                        }
                                                                                        .build()

                                                                                    val retrofit: Retrofit.Builder by lazy {
                                                                                        Retrofit.Builder().baseUrl(BuildConfig.API_URL)
                                                                                            .client(okHttpClient)
                                                                                            .addConverterFactory(GsonConverterFactory.create())
                                                                                    }
                                                                                    val retrofitApi: Api by lazy {
                                                                                        retrofit.client(okHttpClient)
                                                                                        retrofit.build().create(Api::class.java)
                                                                                    }
                                                                                    val apiClient = ApiClient(retrofitApi)
                                                                                    apiClient.comment(
                                                                                        b.id!!.toInt(),
                                                                                        chatData
                                                                                    ).enqueue(object :
                                                                                        Callback<Boolean> {
                                                                                        override fun onResponse(
                                                                                            call: Call<Boolean>,
                                                                                            response: Response<Boolean>
                                                                                        ) {
                                                                                            val mResponse =
                                                                                                response.body()
                                                                                            if (response.isSuccessful) {
                                                                                                if (response.code() == 200) {

                                                                                                    bottomSheetDialog.findViewById<ProgressBar>(
                                                                                                        R.id.submittingrrrprogress
                                                                                                    )!!.visibility =
                                                                                                        View.GONE
                                                                                                    bottomSheetDialog.findViewById<EditText>(
                                                                                                        R.id.comment_txt
                                                                                                    )!!
                                                                                                        .setText(
                                                                                                            "Comment sent."
                                                                                                        )
                                                                                                    obs.value =
                                                                                                        1
                                                                                                } else {
                                                                                                    lifecycleScope.launchWhenStarted {
                                                                                                        getProductFeedback()
                                                                                                    }
                                                                                                    bottomSheetDialog.findViewById<ProgressBar>(
                                                                                                        R.id.submittingrrrprogress
                                                                                                    )!!.visibility =
                                                                                                        View.GONE
                                                                                                    Snackbar.make(
                                                                                                        bind.bk,
                                                                                                        "Error Occured",
                                                                                                        Snackbar.LENGTH_LONG
                                                                                                    ).show()
                                                                                                }
                                                                                            } else if (response.code() == 404) {
                                                                                                lifecycleScope.launchWhenStarted {
                                                                                                    getProductFeedback()
                                                                                                }
                                                                                                bottomSheetDialog.findViewById<ProgressBar>(
                                                                                                    R.id.submittingrrrprogress
                                                                                                )!!.visibility =
                                                                                                    View.GONE
                                                                                                Snackbar.make(
                                                                                                    bind.bk,
                                                                                                    "Error Occured, Try again",
                                                                                                    Snackbar.LENGTH_LONG
                                                                                                ).show()
                                                                                            } else if (response.code() == 500) {

                                                                                                bottomSheetDialog.findViewById<EditText>(
                                                                                                    R.id.comment_txt
                                                                                                )!!.setText(
                                                                                                    "Error occured, Try again."
                                                                                                )

                                                                                                lifecycleScope.launchWhenStarted {
                                                                                                    getProductFeedback()
                                                                                                }

                                                                                                bottomSheetDialog.findViewById<ProgressBar>(
                                                                                                    R.id.submittingrrrprogress
                                                                                                )!!.visibility =
                                                                                                    View.GONE
                                                                                                Snackbar.make(
                                                                                                    bind.bk,
                                                                                                    "Error Occured",
                                                                                                    Snackbar.LENGTH_LONG
                                                                                                ).show()
                                                                                            }
                                                                                        }

                                                                                        override fun onFailure(
                                                                                            call: Call<Boolean>,
                                                                                            t: Throwable
                                                                                        ) {
                                                                                            Snackbar.make(
                                                                                                bind.bk,
                                                                                                "Error Occured",
                                                                                                Snackbar.LENGTH_LONG
                                                                                            ).show()
                                                                                        }
                                                                                    })
                                                                                }catch(e:Exception){}
                                                                            }
                                                                        }
                                                                    })
                                                            }
                                                        })
                                                }
                                            }
                                        }
                                    bottomSheetDialog.setCancelable(true)
                                    bottomSheetDialog.show()
                                }
                            }
                        }
                    }

                    //send message to store
                    bind.submitChat.setOnClickListener  {
                        val animationSet = AnimationSet(true)
                        animationSet.addAnimation(
                            AnimationUtils.loadAnimation(
                                context,
                                R.anim.like_button_anim
                            )
                        )
                        it.startAnimation(animationSet)
                        Log.i("other_user_id", "${myUserId} ==== ${viewModelClass.postuserId.value!!}")

                        try {
                            bind.submittingChatProgress.visibility = View.VISIBLE
                        } catch (e: Exception) {
                        }

                        lifecycleScope.launchWhenStarted {
                            viewModelClass.queryUserDetails().asLiveData(Dispatchers.IO).observe(viewLifecycleOwner, Observer { b ->
                                if (b == null) {
                                    val alertDialog = AlertDialog.Builder(requireContext())
                                    alertDialog.setMessage("You haven't registered or signed in please do so.")
                                    alertDialog.setPositiveButton("Continue") { _, _ ->
                                        val intent =
                                            Intent(requireContext(), SignUpFragment::class.java)
                                        startActivity(intent)
                                    }
                                    alertDialog.show()
                                } else {
                                    val message = bind.chatTxt.text.toString()
                                    if (message.isEmpty()) {
                                        try {
                                            bind.submittingChatProgress.visibility = View.GONE
                                        } catch (e: Exception) {
                                        }
                                        Snackbar.make(
                                            bind.bk,
                                            "You didnt provide any message..",
                                            Snackbar.LENGTH_LONG
                                        ).show()
                                    } else {
                                        lifecycleScope.launchWhenStarted {
                                            val chatData = ChatData(
                                                user_id = b.id,
                                                other_id = viewModelClass.postuserId.value!!,
                                                user_name = b.fullName,
                                                product_id = viewModelClass.productId.value!!.toInt(),
                                                product_title = product_title,
                                                message = message,
                                                viewed = 0,
                                                date_and_time = "$timeStamp: $timeSt",
                                            )
                                            Log.i("cccccc", chatData.toString())
                                            sendMessageToStores(
                                                b.id!!,
                                                chatData
                                            )
                                        }
                                    }
                                }
                            })
                        }
                    }

                    bind.flag.setOnClickListener {
                        sendEmail(BuildConfig.APP_Email, "Ad Report", "{$adReportId}")
                    }
                }
                else -> {
                    lifecycleScope.launch {
                        delay(3000L)
                        try {
                            requireActivity()
                                .supportFragmentManager
                                .beginTransaction()
                                .replace(android.R.id.content, NoSignalWifiFragment())
                                .commit()
                        } catch (e: Exception) { }
                    }
                }
            }

            CoroutineScope(Dispatchers.IO).launch {
                loadSelectedProductDetails()
            }
        }

        MobileAds.initialize(requireContext()) {}

        val window: Window = requireActivity().window

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        // finally change the color
        window.statusBarColor = ContextCompat.getColor(requireContext(), android.R.color.black)

        adReportId = viewModelClass.productId.value

        product_title = viewModelClass.title.value

        lifecycleScope.launch {
            //Toast.makeText(requireContext(), "${viewModelClass.productId.value}", Toast.LENGTH_LONG).show()
            // Get users details from room
            viewModelClass.queryUserDetails().asLiveData().observe(viewLifecycleOwner, Observer {
                try {
                    outState!!.add(it)
                    Log.i("uuuuuuTT", "${it.id} == ${viewModelClass.postuserId.value}")
                    // Toast.makeText(requireContext(), "${it}", Toast.LENGTH_LONG).show()
                    myUserId = it.id.toString()
                    fullname = it.fullName
                    other_uder_id = it.id

                } catch (e: Exception) { }


                try {
                    if (it.id == viewModelClass.postuserId.value) {
                        bind.btnEditPr.visibility = View.VISIBLE

                    } else {
                        bind.btnEditPr.visibility = View.GONE
                    }

                } catch (e: Exception) {
                }
            })
        }

        mAdView = bind.adViewpp
        mAdView11 = bind.adViewppp

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
                        val adRequest1 = AdRequest.Builder().build()
                        mAdView.loadAd(adRequest)
                        mAdView11.loadAd(adRequest1)
                    }

                } else {
                    Log.e("iAds", "Failed to convert Firestore document to AdsModel")
                }
            } else {
                Log.e("iAds", "Firestore document does not exist or is null")
            }
        }



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
        mAdView11.adListener = object : AdListener() {
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

        bind.fdbackViewAll.setOnClickListener {
            (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(android.R.id.content, ViewAllFeedBacksOnPost()).addToBackStack(null).commit()

        }

        CoroutineScope(Dispatchers.IO).launch {
            viewModelClass.desc.asFlow().collect {
                bind.sglDescriptionProductTxt.text =  it.toString()
            }
        }

        return bind.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(false)
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar!!.hide()
        (activity as AppCompatActivity).supportActionBar!!.title = "TradeTower"
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        bind.seeFullDecription.setOnClickListener {
            (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, SeeFullDescriptionFragment()).addToBackStack(null).commit()
        }

        bind.bk.setOnClickListener {
            (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(android.R.id.content, DashboardFragment()).addToBackStack(null).commit()
        }

        try {
            bind.shareItem.setOnClickListener { b ->
                viewModelClass.title.observe(viewLifecycleOwner, Observer { t->
                    viewModelClass.image.observe(viewLifecycleOwner, Observer { i->
                        shareTextAndImage(t,i)
                    })
                })
            }
        } catch (e: Exception) { }

        bind.shareThisItem.setOnClickListener {j ->
            bind.shareThisItem.setOnClickListener { b ->
                viewModelClass.title.observe(viewLifecycleOwner, Observer { t->
                    viewModelClass.image.observe(viewLifecycleOwner, Observer { i->
                        shareTextAndImage(t,i)
                    })
                })
            }
        }

        //Edit button
        bind.btnEditPr.setOnClickListener {
            val animationSet = AnimationSet(true)
            animationSet.addAnimation(
                AnimationUtils.loadAnimation(
                    context,
                    R.anim.like_button_anim
                )
            )
            it.startAnimation(animationSet)
            //viewModelClass.productId.value = productId.toString()
            //viewModelClass1.id.value = id.toString()
            (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, EditaProductFragment()).addToBackStack(null).commit()
        }

        bind.sglShowContact.setOnClickListener {o->
            lifecycleScope.launchWhenCreated {
                viewModelClass.queryUserDetails().asLiveData().observe(viewLifecycleOwner, Observer {
                    if (it == null) {
                        Snackbar.make(
                            bind.bk,
                            "You must login first before you can save",
                            Snackbar.LENGTH_LONG
                        ).show()

                        lifecycleScope.launchWhenStarted {
                            delay(2000L)
                            val intent =
                                Intent(requireContext(), SignInFragment::class.java)
                            startActivity(intent)
                        }

                        //(activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, SignInFragment()).show()

                    }
                    else {
                        try{
                            val animationSet = AnimationSet(true)
                            animationSet.addAnimation(
                                AnimationUtils.loadAnimation(
                                    context,
                                    R.anim.like_button_anim
                                )
                            )
                            o.startAnimation(animationSet)
                            bind.sglShowContactText.text = "${viewModelClass.phone_number.value}"
                            o.setOnClickListener {
                                val phoneNumber = "${viewModelClass.phone_number.value}"
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
                                startActivity(intent)
                            }
                        }catch(e:Exception){ }
                    }
                })
            }
        }

        bind.save.setOnClickListener {
            lifecycleScope.launchWhenCreated {
                viewModelClass.queryUserDetails().asLiveData().observe(viewLifecycleOwner, Observer {
                    if (it == null) {
                        Snackbar.make(
                            bind.bk,
                            "You must login first before you can save",
                            Snackbar.LENGTH_LONG
                        ).show()

                        lifecycleScope.launchWhenStarted {
                            delay(2000L)
                            val intent =
                                Intent(requireContext(), SignInFragment::class.java)
                            startActivity(intent)
                        }

                        //(activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, SignInFragment()).show()

                    } else {

                        try {

                            viewModelClass.getSingleProductItemDatabase(viewModelClass.productId.value!!).observe(viewLifecycleOwner, Observer {
                                try {
                                    Log.i("getSingleProduct", it[0].rates.toString())
                                    try {
                                        val products = ProductMySavedData(
                                            id = it[0].id,
                                            userId = it[0].userId,
                                            user_name = it[0].user_name,
                                            title = it[0].title,
                                            currenxy =it[0].currenxy,
                                            price = it[0].price.toString(),
                                            category = it[0].category,
                                            country = it[0].country,
                                            location = it[0].location,
                                            desc = it[0].desc,
                                            dateAndtimme = it[0].dateAndtimme,
                                            type = it[0].type,
                                            negotiation = it[0].negotiation,
                                            image = it[0].image,
                                            image_two = it[0].image_two,
                                            image_three = it[0].image_three,
                                            image_four = it[0].image_four,
                                            image_five = it[0].image_five,
                                            image_six = it[0].image_six,
                                            paid_product = it[0].paid_product,
                                            pst_phone_number = it[0].pst_phone_number.toString(),
                                            subscription_type = it[0].subscription_type,
                                            subscription_date_start = it[0].subscription_date_start,
                                            subscription_date_due = it[0].subscription_date_due,
                                            views = it[0].views,
                                            rates = viewModelClass.Itemsratings.value,
                                            approval_status = it[0].approval_status,
                                        )

                                        lifecycleScope.launchWhenStarted {
                                            viewModelClass1.insertIntoMySavedProductDatatbase(
                                                products
                                            )
                                        }

                                    } catch (e: Exception) { }
                                } catch (e: Exception) {
                                }

                            })

                            Snackbar.make(
                                bind.bk,
                                "Item successfully saved",
                                Snackbar.LENGTH_LONG
                            ).show()

                        } catch (e: Exception) {
                        }
                    }
                })
            }
        }

        when (viewModelClass.isNetworkAvailable(test_website)) {
            true -> {
                lifecycleScope.launch {
                    getProductFeedback()
                }

                CoroutineScope(Dispatchers.IO).launch {
                    loadSelectedProductDetails()
                }


                obs.observe(viewLifecycleOwner, Observer {
                    if (it == 1) {
                        lifecycleScope.launch {
                            getProductFeedback()
                        }
                    } else { }
                })

                bind.sglSendMessage.setOnClickListener {
                    lifecycleScope.launchWhenStarted {
                        viewModelClass.queryUserDetails().collect {
                            if (it == null) {
                                val alertDialog = AlertDialog.Builder(requireContext())
                                alertDialog.setMessage("You haven't registered or signed in please do so.")
                                alertDialog.setPositiveButton("Continue") { _, _ ->
                                    val intent =
                                        Intent(requireContext(), SignUpFragment::class.java)
                                    startActivity(intent)
                                }
                                alertDialog.show()
                            } else {
                                val bottomSheetDialog = BottomSheetDialog(requireContext())
                                bottomSheetDialog.setContentView(R.layout.activity_comment_reply_box)
                                bottomSheetDialog.findViewById<TextView>(R.id.send_a_comment)!!
                                    .setOnClickListener {

                                        bottomSheetDialog.findViewById<ProgressBar>(R.id.submittingrrrprogress)!!.visibility =
                                            View.VISIBLE
                                        val comment =
                                            bottomSheetDialog.findViewById<EditText>(R.id.comment_txt)!!.text
                                        if (comment!!.isEmpty()) {
                                            bottomSheetDialog.findViewById<ProgressBar>(R.id.submittingrrrprogress)!!.visibility =
                                                View.GONE
                                            Toast.makeText(
                                                requireContext(),
                                                "message shouldn't be empty",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        } else {

                                            lifecycleScope.launchWhenStarted {
                                                viewModelClass.queryUserDetails()
                                                    .asLiveData(Dispatchers.IO)
                                                    .observe(viewLifecycleOwner, Observer { b ->
                                                        if (b == null) {
                                                            val alertDialog =
                                                                AlertDialog.Builder(requireContext())
                                                            alertDialog.setMessage("You haven't registered or signed in please do so.")
                                                            alertDialog.setPositiveButton("Continue") { _, _ ->
                                                                val intent = Intent(
                                                                    requireContext(),
                                                                    SignUpFragment::class.java
                                                                )
                                                                startActivity(intent)
                                                            }
                                                            alertDialog.show()
                                                        } else {

                                                            viewModelClass.productId.observe(
                                                                viewLifecycleOwner,
                                                                Observer { p ->
                                                                    lifecycleScope.launchWhenCreated {
                                                                        try {
                                                                            val data =
                                                                                SearchProduct.searchProduct(
                                                                                    "approved",
                                                                                    p.toInt())
                                                                            data.forEach { i ->
                                                                                val chatData =
                                                                                    CommentData(
                                                                                        user_id = b.id,
                                                                                        product_id = i.id,
                                                                                        comment = comment.toString(),
                                                                                        liked = 0,
                                                                                        date_and_time = "$timeStamp: $timeSt",
                                                                                        comment_id = "${b.id!!}${i.id!!}".toInt(),
                                                                                        type = "comments"
                                                                                    )
                                                                                Log.i(
                                                                                    "cxc",
                                                                                    chatData.toString()
                                                                                )
                                                                                //commentingOnPost(myUserId!!.toInt(), chatData)
                                                                                try{

                                                                                    Singleton.Singleton.apiClient.comment(
                                                                                        b.id!!.toInt(),
                                                                                        chatData
                                                                                    ).enqueue(object :
                                                                                        Callback<Boolean> {
                                                                                        override fun onResponse(
                                                                                            call: Call<Boolean>,
                                                                                            response: Response<Boolean>
                                                                                        ) {
                                                                                            val mResponse = response.body()
                                                                                            if (response.isSuccessful) {
                                                                                                if (response.code() == 200) {

                                                                                                    bottomSheetDialog.findViewById<ProgressBar>(
                                                                                                        R.id.submittingrrrprogress
                                                                                                    )!!.visibility =
                                                                                                        View.GONE
                                                                                                    bottomSheetDialog.findViewById<EditText>(
                                                                                                        R.id.comment_txt
                                                                                                    )!!
                                                                                                        .setText(
                                                                                                            "Comment sent."
                                                                                                        )
                                                                                                    obs.value =
                                                                                                        1
                                                                                                } else {
                                                                                                    lifecycleScope.launchWhenStarted {
                                                                                                        getProductFeedback()
                                                                                                    }
                                                                                                    bottomSheetDialog.findViewById<ProgressBar>(
                                                                                                        R.id.submittingrrrprogress
                                                                                                    )!!.visibility =
                                                                                                        View.GONE
                                                                                                    Snackbar.make(
                                                                                                        bind.bk,
                                                                                                        "Error Occured",
                                                                                                        Snackbar.LENGTH_LONG
                                                                                                    ).show()
                                                                                                }
                                                                                            } else if (response.code() == 404) {
                                                                                                lifecycleScope.launchWhenStarted {
                                                                                                    getProductFeedback()
                                                                                                }
                                                                                                bottomSheetDialog.findViewById<ProgressBar>(
                                                                                                    R.id.submittingrrrprogress
                                                                                                )!!.visibility =
                                                                                                    View.GONE
                                                                                                Snackbar.make(
                                                                                                    bind.bk,
                                                                                                    "Error Occured, Try again",
                                                                                                    Snackbar.LENGTH_LONG
                                                                                                ).show()
                                                                                            } else if (response.code() == 500) {

                                                                                                bottomSheetDialog.findViewById<EditText>(
                                                                                                    R.id.comment_txt
                                                                                                )!!.setText(
                                                                                                    "Error occured, Try again."
                                                                                                )

                                                                                                lifecycleScope.launchWhenStarted {
                                                                                                    getProductFeedback()
                                                                                                }

                                                                                                bottomSheetDialog.findViewById<ProgressBar>(
                                                                                                    R.id.submittingrrrprogress
                                                                                                )!!.visibility =
                                                                                                    View.GONE
                                                                                                Snackbar.make(
                                                                                                    bind.bk,
                                                                                                    "Error Occured",
                                                                                                    Snackbar.LENGTH_LONG
                                                                                                ).show()
                                                                                            }
                                                                                        }

                                                                                        override fun onFailure(
                                                                                            call: Call<Boolean>,
                                                                                            t: Throwable
                                                                                        ) {
                                                                                            Snackbar.make(
                                                                                                bind.bk,
                                                                                                "Error Occured",
                                                                                                Snackbar.LENGTH_LONG
                                                                                            ).show()
                                                                                        }
                                                                                    })
                                                                                }catch(e:Exception){}
                                                                            }
                                                                        }catch (e:Exception){}
                                                                    }
                                                                })
                                                        }
                                                    })
                                            }
                                        }
                                    }
                                bottomSheetDialog.setCancelable(true)
                                bottomSheetDialog.show()
                            }
                        }
                    }
                }

                //send message to store
                bind.submitChat.setOnClickListener  {
                    val animationSet = AnimationSet(true)
                    animationSet.addAnimation(
                        AnimationUtils.loadAnimation(
                            context,
                            R.anim.like_button_anim
                        )
                    )
                    it.startAnimation(animationSet)
                    Log.i("other_user_id", "${myUserId} ==== ${viewModelClass.postuserId.value!!}")

                    try {
                        bind.submittingChatProgress.visibility = View.VISIBLE
                    } catch (e: Exception) {
                    }

                    lifecycleScope.launchWhenStarted {
                        viewModelClass.queryUserDetails().asLiveData(Dispatchers.IO).observe(viewLifecycleOwner, Observer { b ->
                            if (b == null) {
                                val alertDialog = AlertDialog.Builder(requireContext())
                                alertDialog.setMessage("You haven't registered or signed in please do so.")
                                alertDialog.setPositiveButton("Continue") { _, _ ->
                                    val intent =
                                        Intent(requireContext(), SignUpFragment::class.java)
                                    startActivity(intent)
                                }
                                alertDialog.show()
                            } else {
                                val message = bind.chatTxt.text.toString()
                                if (message.isEmpty()) {
                                    try {
                                        bind.submittingChatProgress.visibility = View.GONE
                                    } catch (e: Exception) {
                                    }
                                    Snackbar.make(
                                        bind.bk,
                                        "You didnt provide any message..",
                                        Snackbar.LENGTH_LONG
                                    ).show()
                                } else {
                                    lifecycleScope.launchWhenStarted {
                                        val chatData = ChatData(
                                            user_id = b.id,
                                            other_id = viewModelClass.postuserId.value!!,
                                            user_name = b.fullName,
                                            product_id = viewModelClass.productId.value!!.toInt(),
                                            product_title = product_title,
                                            message = message,
                                            viewed = 0,
                                            date_and_time = "$timeStamp: $timeSt",
                                        )
                                        Log.i("cccccc", chatData.toString())
                                        sendMessageToStores(
                                            b.id!!,
                                            chatData
                                        )
                                    }
                                }
                            }
                        })
                    }
                }

                bind.flag.setOnClickListener {
                    sendEmail(BuildConfig.APP_Email, "Ad Report", "{$adReportId}")
                }
                
                bind.reportThisItem.setOnClickListener {
                    sendEmail(BuildConfig.APP_Email, "Ad Report", "{$adReportId}")
                }


            }
            else -> {
                lifecycleScope.launchWhenStarted {
                    launch {
                        delay(3000L)
                        try {
                            requireActivity()
                                .supportFragmentManager
                                .beginTransaction()
                                .replace(android.R.id.content, NoSignalWifiFragment())
                                .commit()
                        } catch (e: Exception) { }
                    }
                }
            }
        }
    }

    fun sendEmail(recipient: String, subject: String, message: String) {
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle("Reporting")
        alertDialog.setMessage("Email us to know why you are reporting this Ad.")
        alertDialog.setCancelable(true)
        alertDialog.setNegativeButton("Cancel") { _, _ ->

        }
        alertDialog.setPositiveButton("Continue") { _, _ ->
            /*ACTION_SEND action to launch an email client installed on your Android device.*/
            val mIntent = Intent(Intent.ACTION_SEND)
            /*To send an email you need to specify mailto: as URI using setData() method
            and data type will be to text/plain using setType() method*/
            mIntent.data = Uri.parse("mailto:")
            mIntent.type = "text/plain"
            // put recipient email in intent
            /* recipient is put as array because you may wanna send email to multiple emails
               so enter comma(,) separated emails, it will be stored in array*/
            mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
            //put the Subject in the intent
            mIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
            //put the message in the intent
            mIntent.putExtra(Intent.EXTRA_TEXT, "I am reporting this Ads with details ( $message : {${requireActivity().packageName}} $product_title ) please check it and do the necessary needful" )


            try {
                //start email intent
                startActivity(Intent.createChooser(mIntent, "Choose Email Client..."))
            } catch (e: Exception) {
                //if any thing goes wrong for example no email client application or any exception
                //get and show exception message
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
            }
        }
        alertDialog.show()
    }

    fun byteArrayToBitmap(data: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(data, 0, data.size)
    }

    fun currencyFormat(amount: String): String? {
        val formatter = DecimalFormat("###,###,##0.00")
        return formatter.format(amount.toDouble())
    }

    suspend fun sendMessageToStores(userId: Int, chatData: ChatData) {
        try {
            bind.submittingChatProgress.visibility = View.VISIBLE
        } catch (e: Exception) {
        }

        try {
            Singleton.Singleton.apiClient.chat(userId, chatData).enqueue(object : Callback<ChatDataResponse> {
                    @SuppressLint("SuspiciousIndentation")
                    override fun onResponse(
                        call: Call<ChatDataResponse>,
                        response: Response<ChatDataResponse>
                    ) {
                        val mResponse = response.body()
                        if (response.isSuccessful) {
                            if (response.code() == 200) {
                                val d = bind.chatTxt
                                d.setText("Message Sent to the store.")
                                d.gravity = Gravity.CENTER
                                d.setTextColor(resources.getColor(R.color.black))
                                try {
                                    bind.submittingChatProgress.visibility = View.GONE
                                    bind.submitChat.visibility = View.GONE
                                } catch (e: Exception) {
                                }

                            } else {
                                try {
                                    bind.submittingChatProgress.visibility = View.GONE
                                } catch (e: Exception) {
                                }
                                Toast.makeText(
                                    requireContext(),
                                    mResponse.toString(),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } else {
                            try {
                                bind.submittingChatProgress.visibility = View.GONE
                            } catch (e: Exception) {
                            }
                            Toast.makeText(requireContext(), mResponse.toString(), Toast.LENGTH_LONG)
                                .show()
                        }
                    }

                    override fun onFailure(call: Call<ChatDataResponse>, t: Throwable) {
                        try {
                            bind.submittingChatProgress.visibility = View.GONE
                        } catch (e: Exception) {
                        }
                        Toast.makeText(requireContext(), t.message.toString(), Toast.LENGTH_LONG).show()
                    }

                })
        }catch (e:Exception){}
    }

    suspend fun getProductFeedback() {
        bind.feedbackLoading.visibility = View.VISIBLE
        viewModelClass.id.asFlow().collect{
            Log.i("thisId", it.toString())
            layoutManager1 = LinearLayoutManager(requireContext())
            layoutManager1.orientation = LinearLayoutManager.VERTICAL
            layoutManager1.isSmoothScrollbarEnabled = true
            bind.feedbackRecyclerview.layoutManager = layoutManager1
            try {

                val client = Singleton.Singleton.apiClient.api.getProductFeedback(it!!.toInt())
                if (client.isSuccessful) {
                    if (client.code() == 200) {
                        client.body()?.let { y ->
                            if (y.size == 0) {false
                                bind.feedWrapper.visibility = View.GONE
                            } else if (y.isNullOrEmpty()) {
                                bind.feedWrapper.visibility = View.GONE
                            } else {
                                bind.feedWrapper.visibility = View.VISIBLE
                                bind.feedbackLoading.visibility = View.VISIBLE
                                try {

                                    val adapter = FeedbackListAdapter(
                                        requireContext(), y, requireActivity(),
                                        viewModelClass,
                                        this@Single_Selected_item_show,
                                        this@Single_Selected_item_show,
                                        this@Single_Selected_item_show,
                                        this@Single_Selected_item_show,
                                        this@Single_Selected_item_show
                                    )
                                    bind.feedbackRecyclerview.adapter = adapter
                                    bind.feedbackLoading.visibility = View.GONE
                                } catch (e: Exception) {
                                }
                            }
                        }
                    }
                }

            } catch (e: Exception) {
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun stringToBitMap(encodedString: String?): Bitmap? {
        return try {
            val encodeByte: ByteArray = Base64.decode(encodedString, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
            bitmap
        } catch (e: java.lang.Exception) {
            e.message
            null
        }
    }

    fun formatViewsCount(viewsCount: Long): String {
        val suffixes = arrayOf("K", "M", "B", "T")
        val formatter: NumberFormat = NumberFormat.getInstance(Locale.US)
        formatter.maximumFractionDigits = 1

        var value = viewsCount.toDouble()
        var suffix = ""
        for (i in suffixes.indices) {
            if (value < 1000.0) {
                break
            }
            value /= 1000.0
            suffix = suffixes[i]
        }
        return formatter.format(value) + suffix + " views"
    }

    fun formatRelativeDate(date: Date): String {
        val now = Calendar.getInstance().timeInMillis
        val diffInMillis = now - date.time
        val diffInDays = diffInMillis / (24 * 60 * 60 * 1000)
        val diffInWeeks = diffInDays / 7

        return when {
            diffInDays < 1 -> "today"
            diffInDays == 1L -> "1 day ago"
            diffInDays < 7 -> "$diffInDays days ago"
            diffInWeeks == 1L -> "1 week ago"
            diffInWeeks < 52 -> "$diffInWeeks weeks ago"
            else -> "over a year ago"
        }
    }

    private fun shareTextAndImage(title: String, image: String) {
        val bitmap = BitmapFactory.decodeByteArray(image.toByteArray(), 0, image.length)

        val int = Intent(Intent.ACTION_SEND)
        int.putExtra(Intent.EXTRA_TEXT, title)
        //int.putExtra(Intent.EXTRA_STREAM, bitmap)
        int.putExtra(Intent.EXTRA_TEXT, "Check out this new product from TradeTower")
        int.putExtra(
            Intent.EXTRA_TEXT,
            "https://play.google.com/store/apps/details?id=${requireActivity().packageName}"
        )
        int.type = "text/plain"
        requireContext().startActivity(int)

    }

    //reply on comment
    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onReplyCellClickListener(feed: FeedbackData) {

        val s = SimpleDateFormat("dd/MM/yyyy") //,hh:mm:ss
        val format: String = s.format(Date())
        val timeStamp = format

        val ss = SimpleDateFormat("hh:mm:ss") //,hh:mm:ss
        val format_time: String = ss.format(Date())
        val timeSt = format_time
        lifecycleScope.launchWhenStarted {
            viewModelClass.queryUserDetails().collect {
                if (it == null) {
                    val alertDialog = AlertDialog.Builder(requireContext())
                    alertDialog.setMessage("You haven't registered or signed in please do so.")
                    alertDialog.setPositiveButton("Continue") { _, _ ->
                        val intent = Intent(requireContext(), SignUpFragment::class.java)
                        startActivity(intent)
                    }
                    alertDialog.show()
                } else {
                    val bottomSheetDialog = BottomSheetDialog(requireContext())
                    bottomSheetDialog.setContentView(R.layout.activity_comment_reply_box)
                    bottomSheetDialog.findViewById<TextView>(R.id.send_a_comment)!!
                        .setOnClickListener {
                            it.startAnimation(expandIn)
                            bottomSheetDialog.findViewById<ProgressBar>(R.id.submittingrrrprogress)!!.visibility =
                                View.VISIBLE
                            val comment =
                                bottomSheetDialog.findViewById<EditText>(R.id.comment_txt)!!.text
                            if (comment!!.isEmpty()) {
                                bottomSheetDialog.findViewById<ProgressBar>(R.id.submittingrrrprogress)!!.visibility =
                                    View.GONE
                                Toast.makeText(
                                    requireContext(),
                                    "Reply shouldn't be empty",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                it.startAnimation(expandIn)
                                lifecycleScope.launchWhenStarted {
                                    viewModelClass.queryUserDetails().asLiveData(Dispatchers.IO)
                                        .observe(viewLifecycleOwner, Observer { b ->
                                            if (b == null) {
                                                val alertDialog =
                                                    AlertDialog.Builder(requireContext())
                                                alertDialog.setMessage("You haven't registered or signed in please do so.")
                                                alertDialog.setPositiveButton("Continue") { _, _ ->
                                                    val intent = Intent(
                                                        requireContext(),
                                                        SignUpFragment::class.java
                                                    )
                                                    startActivity(intent)
                                                }
                                                alertDialog.show()
                                            } else {

                                                viewModelClass.productId.observe(
                                                    viewLifecycleOwner,
                                                    Observer { p ->
                                                        lifecycleScope.launchWhenCreated {
                                                            val data = SearchProduct.searchProduct(
                                                                "approved",
                                                                p.toInt()
                                                            )
                                                            data.forEach { i ->
                                                               try{
                                                                   val replyData = CommentData(
                                                                       user_id = b.id,
                                                                       product_id = feed.product_id,
                                                                       comment = comment.toString(),
                                                                       liked = 0,
                                                                       date_and_time = "$timeStamp: $timeSt",
                                                                       comment_id = feed.id,
                                                                       type = "reply"
                                                                   )
                                                                   Log.i(
                                                                       "cb",
                                                                       replyData.toString()
                                                                   )
                                                                   //commentingOnPost(myUserId!!.toInt(), chatData)
                                                                   Singleton.Singleton.apiClient.comment(
                                                                       b.id!!.toInt(),
                                                                       replyData
                                                                   ).enqueue(object :
                                                                       Callback<Boolean> {
                                                                       override fun onResponse(
                                                                           call: Call<Boolean>,
                                                                           response: Response<Boolean>
                                                                       ) {
                                                                           val mResponse =
                                                                               response.body()
                                                                           if (response.isSuccessful) {
                                                                               if (response.code() == 200) {
                                                                                   lifecycleScope.launchWhenStarted {
                                                                                       GlobalScope.launch(
                                                                                           Dispatchers.IO
                                                                                       ) {
                                                                                           getProductFeedback()
                                                                                       }
                                                                                   }
                                                                                   bottomSheetDialog.findViewById<ProgressBar>(
                                                                                       R.id.submittingrrrprogress
                                                                                   )!!.visibility =
                                                                                       View.GONE
                                                                                   bottomSheetDialog.findViewById<EditText>(
                                                                                       R.id.comment_txt
                                                                                   )!!
                                                                                       .setText("Reply sent.")
                                                                               } else {
                                                                                   lifecycleScope.launchWhenStarted {
                                                                                       getProductFeedback()
                                                                                   }
                                                                                   bottomSheetDialog.findViewById<ProgressBar>(
                                                                                       R.id.submittingrrrprogress
                                                                                   )!!.visibility =
                                                                                       View.GONE
                                                                                   Snackbar.make(
                                                                                       bind.bk,
                                                                                       "Error Occured",
                                                                                       Snackbar.LENGTH_LONG
                                                                                   ).show()
                                                                               }
                                                                           } else if (response.code() == 404) {
                                                                               lifecycleScope.launchWhenStarted {
                                                                                   getProductFeedback()
                                                                               }
                                                                               bottomSheetDialog.findViewById<ProgressBar>(
                                                                                   R.id.submittingrrrprogress
                                                                               )!!.visibility =
                                                                                   View.GONE
                                                                               Snackbar.make(
                                                                                   bind.bk,
                                                                                   "Error Occured",
                                                                                   Snackbar.LENGTH_LONG
                                                                               ).show()
                                                                           } else if (response.code() == 500) {
                                                                               lifecycleScope.launchWhenStarted {
                                                                                   getProductFeedback()
                                                                               }
                                                                               bottomSheetDialog.findViewById<ProgressBar>(
                                                                                   R.id.submittingrrrprogress
                                                                               )!!.visibility =
                                                                                   View.GONE
                                                                               Snackbar.make(
                                                                                   bind.bk,
                                                                                   "Error Occured",
                                                                                   Snackbar.LENGTH_LONG
                                                                               ).show()
                                                                           }
                                                                       }

                                                                       override fun onFailure(
                                                                           call: Call<Boolean>,
                                                                           t: Throwable
                                                                       ) {
                                                                           Snackbar.make(
                                                                               bind.bk,
                                                                               "Error Occured",
                                                                               Snackbar.LENGTH_LONG
                                                                           ).show()
                                                                       }
                                                                   })
                                                               }catch (e:Exception){}
                                                            }
                                                        }
                                                    })
                                            }
                                        })
                                }
                            }
                        }
                    bottomSheetDialog.setCancelable(true)
                    bottomSheetDialog.show()
                }
            }
        }
    }

    override suspend fun onCommentLikeListernersRecyclervire(comment_id: Int, userId: Int,comment_user_id: Int,my_id:Int , like: Int) : Long{
        //Toast.makeText(requireContext(), "$comment_id/$userId/$like", Toast.LENGTH_LONG).show()

        try {
            val client = Singleton.Singleton.apiClient.api.comment_like(comment_id, userId,comment_user_id,my_id, like)
            if (client.isSuccessful) {
                if (client.code() == 200) {
                    return client.body()!!
                    Log.i("retrofit", client.body()!!.toString())
                } else {
                    0
                    Log.i("retrofit", client.body()!!.toString())
                }
            } else {
                0
                Log.i("retrofit", client.body()!!.toString())
            }

        } catch (e: Exception) { }


        return 0
    }

    override suspend fun onReplyLikeListernersRecyclervire(reply_id: Int, user_id: Int,comment_user_id: Int,my_id:Int ,like:Int) : Long{

        //Toast.makeText(requireContext(), "$reply_id/$user_id/$comment_user_id/$my_id /$like", Toast.LENGTH_LONG).show()

        try {
            val client = Singleton.Singleton.apiClient.api.reply_like(reply_id,
                user_id,comment_user_id,my_id, like)
            if (client.isSuccessful) {
                if (client.code() == 200) {
                    return client.body()!!
                    Log.i("retrofit", client.body()!!.toString())
                } else {
                    0
                    Log.i("retrofit", client.body()!!.toString())
                }
            } else {
                0
                Log.i("retrofit", client.body()!!.toString())
            }

        } catch (e: Exception) { }

        return 0
    }

    override suspend fun commentLikesCounts(post_id: Int, comment_id_user_id: Int, user_id: Int): List<PostLikesData> {
        try {
            val client = Singleton.Singleton.apiClient.api.commentLikesCounts(post_id, comment_id_user_id, user_id)
            if (client.isSuccessful) {
                if (client.code() == 200) {
                    return client.body()!!
                    Log.i("commentCount", client.body()!!.toString())
                } else {
                    return emptyList()
                    Log.i("commentCount", client.body()!!.toString())
                }
            } else {
                return emptyList()
                Log.i("commentCount", client.body()!!.toString())
            }

        } catch (e: Exception) { }

        return emptyList()
    }

    override suspend fun replyLikesCounts(post_id: Int, comment_id_user_id: Int, user_id: Int): List<PostLikesData> {
        try {
            val client = Singleton.Singleton.apiClient.api.replyLikesCounts(post_id, comment_id_user_id, user_id)
            if (client.isSuccessful) {
                if (client.code() == 200) {
                    return client.body()!!
                    Log.i("commentCount", client.body()!!.toString())
                } else {
                    return emptyList()
                    Log.i("commentCount", client.body()!!.toString())
                }
            } else {
                return emptyList()
                Log.i("commentCount", client.body()!!.toString())
            }

        } catch (e: Exception) { }

        return emptyList()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("singleSelected", "singleSelected")

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true)
            {
                override fun handleOnBackPressed() {
                    // Leave empty do disable back press or
                    // write your code which you want
                    (activity as AppCompatActivity)
                        .supportFragmentManager
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(android.R.id.content, DashboardFragment())
                        .addToBackStack(null).commit()

                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }

    suspend fun loadSelectedProductDetails(){
        delay(2000L)
        try {
            viewModelClass.id.asFlow().collect {
                id = it.toInt()
            }

            viewModelClass.category.asFlow().collect {
                category = it.toString()
            }
            viewModelClass.category.asFlow().collect {
                category = it.toString()
            }
            viewModelClass.id.asFlow().collect {
                productId = it.toInt()
            }
            viewModelClass.title.asFlow().collect {
                product_title = it
            }
            viewModelClass.id.asFlow().collect {
                adReportId = it.toString()
            }
            viewModelClass.pst_phone_number.asFlow().collect {
                phone_number = it.toString()
            }
        } catch (e: Exception) { }
    }
}
