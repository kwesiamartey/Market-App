package com.lornamobileappsdev.my_marketplace.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.room.TypeConverter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.google.android.gms.ads.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.lornamobileappsdev.my_marketplace.BuildConfig
import com.lornamobileappsdev.my_marketplace.R
import com.lornamobileappsdev.my_marketplace.api.Api
import com.lornamobileappsdev.my_marketplace.constant.token
import com.lornamobileappsdev.my_marketplace.entity.PostProductResponse
import com.lornamobileappsdev.my_marketplace.models.CommentData
import com.lornamobileappsdev.my_marketplace.repository.ApiClient
import com.lornamobileappsdev.my_marketplace.ui.SignUpFragment
import com.lornamobileappsdev.my_marketplace.useCases.SearchProduct
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel
import kotlinx.coroutines.Dispatchers
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


class SelectedProductSingleListItemListAdapter(
    val context: Context,
    val productDataList: MutableList<PostProductResponse>,
    val lifecycleOwner: LifecycleOwner,
    val viewModel: MyViewModel
) : RecyclerView.Adapter<SelectedProductSingleListItemListAdapter.MyViewHolder>() {


    var id: Int? = null

    var phone_number: String? = null

    var userId: Int? = null

    var productId: Int? = null

    var adReportId: String? = null

    var fullname: String? = null

    var product_title: String? = null

    var category: String? = null

    var myUserId: String? = null

    var other_uder_id: Int? = null

    val s = SimpleDateFormat("dd/MM/yyyy") //,hh:mm:ss
    val ss = SimpleDateFormat("hh:mm:ss") //,hh:mm:ss

    val format: String = s.format(Date())
    val timeStamp = format

    val format_time: String = ss.format(Date())
    val timeSt = format_time

    val today = LocalDate.now()

    lateinit var mAdView: AdView

    //lateinit var mAdView1: AdView

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun setData(productDataList: PostProductResponse, position: Int) {
            val productImagesList = mutableListOf<String>()

            MobileAds.initialize(context) {}

            mAdView = itemView.findViewById<AdView>(R.id.adView)
            //mAdView = itemView.findViewById<AdView>(R.id.adViewpp)

            val adRequest = AdRequest.Builder().build()
            //val adRequest1 = AdRequest.Builder().build()
            mAdView.loadAd(adRequest)
            //mAdView.loadAd(adRequest1)

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

            try {
                val dateString = "${productDataList.dateAndtimme}"
                val format = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                val date = format.parse(dateString)

                id = productDataList.id!!.toInt()
                itemView.findViewById<TextView>(R.id.pr_Title).text = productDataList.title.toString()
                itemView.findViewById<TextView>(R.id.pr_prices).text = productDataList.price.toString()
                itemView.findViewById<TextView>(R.id.pr_location).text = productDataList.location.toString()
                itemView.findViewById<TextView>(R.id.sgl_description_product_txt).text = productDataList.desc.toString()
                itemView.findViewById<TextView>(R.id.pr_time_and_date).text = productDataList.dateAndtimme.toString()
                itemView.findViewById<TextView>(R.id.pr_promoted).text = productDataList.type.toString()
                itemView.findViewById<TextView>(R.id.visited_view).text = formatViewsCount(productDataList.views!!.toLong()).toString()

                category = productDataList.category.toString()
                viewModel.category.value = productDataList.category.toString()
                productId = productDataList.id
                product_title = productDataList.title.toString()
                adReportId = productDataList.id.toString()
                phone_number = productDataList.pst_phone_number.toString()


                productImagesList.add(productDataList.image!!)
                productImagesList.add(productDataList.image_two!!)
                productImagesList.add(productDataList.image_three!!)
                productImagesList.add(productDataList.image_four!!)
                productImagesList.add(productDataList.image_five!!)
                productImagesList.add(productDataList.image_six!!)

                for (photo in productImagesList) { //or something like this

                    val circularProgressDrawable =
                        CircularProgressDrawable(context)
                    circularProgressDrawable.strokeWidth = 5f
                    circularProgressDrawable.centerRadius = 30f
                    circularProgressDrawable.start()

                    val image = ImageView(context)
                    image.scaleType = ImageView.ScaleType.CENTER_CROP
                    Glide.with(context)
                        .asBitmap()
                        .load(photo)
                        .placeholder(circularProgressDrawable)
                        .fitCenter()
                        .error(R.drawable.placeholder)
                        .into(image)

                    itemView.findViewById<ViewFlipper>(R.id.single_page_wrapper).visibility = View.VISIBLE
                    itemView.findViewById<ViewFlipper>(R.id.single_item_Sliderr).addView(image)
                }

                itemView.findViewById<ViewFlipper>(R.id.single_item_Sliderr).flipInterval = 5000// 5s intervals
                itemView.findViewById<ViewFlipper>(R.id.single_item_Sliderr).startFlipping()
                itemView.findViewById<LinearLayout>(R.id.prevBtn).setOnClickListener {
                    itemView.findViewById<ViewFlipper>(R.id.single_item_Sliderr).showPrevious()
                }
                itemView.findViewById<LinearLayout>(R.id.nextBtn).setOnClickListener {
                    itemView.findViewById<ViewFlipper>(R.id.single_item_Sliderr).showNext()
                }


                viewModel.queryUserDetails().asLiveData().observe(lifecycleOwner, androidx.lifecycle.Observer {t ->
                    try {
                        if(productDataList.userId == t.id  && productDataList.subscription_date_due == "$today"){
                            itemView.findViewById<ViewFlipper>(R.id.call_us).visibility = View.VISIBLE
                        }else{
                            itemView.findViewById<ViewFlipper>(R.id.call_us).visibility = View.GONE
                        }

                    } catch (e: Exception) {
                    }
                })


            } catch (e: Exception) { }

            itemView.findViewById<TextView>(R.id.call_us).setOnClickListener {
                val phoneNumber = "0508984777"
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
                context.startActivity(intent)
            }

            itemView.findViewById<Button>(R.id.sgl_show_contact).setOnClickListener {
                try{
                    val animationSet = AnimationSet(true)
                    animationSet.addAnimation(
                        AnimationUtils.loadAnimation(
                            context,
                            R.anim.like_button_anim
                        )
                    )
                    it.startAnimation(animationSet)
                    itemView.findViewById<Button>(R.id.sgl_show_contact).text = "0${viewModel.phone_number.value}"
                    it.setOnClickListener {
                        val phoneNumber = "0${viewModel.phone_number.value}"
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
                        context.startActivity(intent)
                    }
                }catch(e:Exception){ }
            }

            itemView.findViewById<Button>(R.id.sgl_send_message).setOnClickListener {
                lifecycleOwner.lifecycleScope.launchWhenStarted {
                    viewModel.queryUserDetails().collect {
                        if (it == null) {
                            val alertDialog = AlertDialog.Builder(context)
                            alertDialog.setMessage("You haven't registered or signed in please do so.")
                            alertDialog.setPositiveButton("Continue") { _, _ ->
                                val intent =
                                    Intent(context, SignUpFragment::class.java)
                                context.startActivity(intent)
                            }
                            alertDialog.show()
                        } else {
                            val bottomSheetDialog = BottomSheetDialog(context)
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
                                            context,
                                            "message shouldn't be empty",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    } else {

                                        lifecycleOwner.lifecycleScope.launchWhenStarted {
                                            viewModel.queryUserDetails()
                                                .asLiveData(Dispatchers.IO)
                                                .observe(lifecycleOwner, androidx.lifecycle.Observer { b ->
                                                    if (b == null) {
                                                        val alertDialog =
                                                            AlertDialog.Builder(context)
                                                        alertDialog.setMessage("You haven't registered or signed in please do so.")
                                                        alertDialog.setPositiveButton("Continue") { _, _ ->
                                                            val intent = Intent(
                                                                context,
                                                                SignUpFragment::class.java
                                                            )
                                                            context.startActivity(intent)
                                                        }
                                                        alertDialog.show()
                                                    } else {

                                                        viewModel.productId.observe(
                                                            lifecycleOwner,
                                                            androidx.lifecycle.Observer { p ->
                                                                lifecycleOwner.lifecycleScope.launchWhenCreated {
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
                                                                            "cxc",
                                                                            chatData.toString()
                                                                        )
                                                                        //commentingOnPost(myUserId!!.toInt(), chatData)
                                                                        val okHttpClient = OkHttpClient().newBuilder()
                                                                            .connectTimeout(3, TimeUnit.MINUTES)
                                                                            .readTimeout(3, TimeUnit.MINUTES)
                                                                            .writeTimeout(3, TimeUnit.MINUTES)
                                                                            .addInterceptor { chain ->
                                                                                val request: Request =
                                                                                    chain.request().newBuilder()
                                                                                        .addHeader("Authorization", "Bearer $token")
                                                                                        .addHeader("Content-Type", "application/text")
                                                                                        .build()
                                                                                chain.proceed(request)
                                                                            }
                                                                            .build()

                                                                        val retrofit: Retrofit.Builder by lazy {
                                                                            Retrofit.Builder().baseUrl(
                                                                                BuildConfig.API_URL)
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
                                                                                    } else {
                                                                                        bottomSheetDialog.findViewById<ProgressBar>(
                                                                                            R.id.submittingrrrprogress
                                                                                        )!!.visibility =
                                                                                            View.GONE
                                                                                        Snackbar.make(
                                                                                            itemView.findViewById<Button>(R.id.sgl_send_message),
                                                                                            "Error Occured",
                                                                                            Snackbar.LENGTH_LONG
                                                                                        ).show()
                                                                                    }
                                                                                } else if (response.code() == 404) {
                                                                                    bottomSheetDialog.findViewById<ProgressBar>(
                                                                                        R.id.submittingrrrprogress
                                                                                    )!!.visibility =
                                                                                        View.GONE
                                                                                    Snackbar.make(
                                                                                        itemView.findViewById<Button>(R.id.sgl_send_message),
                                                                                        "Error Occured, Try again",
                                                                                        Snackbar.LENGTH_LONG
                                                                                    ).show()
                                                                                } else if (response.code() == 500) {

                                                                                    bottomSheetDialog.findViewById<EditText>(
                                                                                        R.id.comment_txt
                                                                                    )!!.setText(
                                                                                        "Error occured, Try again."
                                                                                    )

                                                                                    bottomSheetDialog.findViewById<ProgressBar>(
                                                                                        R.id.submittingrrrprogress
                                                                                    )!!.visibility =
                                                                                        View.GONE
                                                                                    Snackbar.make(
                                                                                        itemView.findViewById<Button>(R.id.sgl_send_message),
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
                                                                                    itemView.findViewById<Button>(R.id.sgl_send_message),
                                                                                    "Error Occured",
                                                                                    Snackbar.LENGTH_LONG
                                                                                ).show()
                                                                            }
                                                                        })
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
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_selected_item_show_single_item_recyclerview_item, parent, false)
        return MyViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val hold = productDataList[position]
        holder.setData(hold, position)

    }

    override fun getItemCount(): Int {
        return productDataList.size
    }

    fun byteArrayToBitmap(data: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(data, 0, data.size)
    }

    fun currencyFormat(amount: String): String? {
        val formatter = DecimalFormat("###,###,##0.00")
        return formatter.format(amount.toDouble())
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

    fun resizePhoto(bitmap: Bitmap): Bitmap {
        val w = bitmap.width
        val h = bitmap.height
        val aspRat = w / h
        val W = 500
        val H = W * aspRat
        val b = Bitmap.createScaledBitmap(bitmap, W, H, false)

        return b
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

    private fun shareTextAndImage(title: String, image: ByteArray) {
        val bitmap = BitmapFactory.decodeByteArray(image, 0, image.size)

        val int = Intent(Intent.ACTION_SEND)
        int.putExtra(Intent.EXTRA_TEXT, title)
        //int.putExtra(Intent.EXTRA_STREAM, bitmap)
        int.putExtra(Intent.EXTRA_TEXT, "Check out this new product from TradeTower")
        int.putExtra(
            Intent.EXTRA_TEXT,
            "https://play.google.com/store/apps/details?id=com.lornamobileappsdev.my_marketplace"
        )
        int.type = "text/plain"
        context.startActivity(int)

    }


}