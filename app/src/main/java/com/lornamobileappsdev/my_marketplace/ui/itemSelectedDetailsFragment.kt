package com.lornamobileappsdev.my_marketplace.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.room.TypeConverter
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.lornamobileappsdev.my_marketplace.databinding.FragmentItemSelectedDetailsBinding
import com.lornamobileappsdev.my_marketplace.entity.SignupResponses
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel
import java.text.DecimalFormat
import java.text.NumberFormat
import java.time.LocalDate
import java.util.*


class ItemSelectedDetailsFragment : Fragment() {

    lateinit var _bind: FragmentItemSelectedDetailsBinding
    val bind get() = _bind

    val viewModelClass: MyViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
    }

    val productImagesList = mutableListOf<ByteArray>()

    val outState: MutableList<SignupResponses>? = null

    val outState2: ArrayList<String> = arrayListOf()

    val s = SimpleDateFormat("dd/MM/yyyy") //,hh:mm:ss

    val ss = SimpleDateFormat("hh:mm:ss") //,hh:mm:ss

    val format: String = s.format(Date())
    val timeStamp = format

    lateinit var mAdView: AdView

    val format_time: String = ss.format(Date())
    val timeSt = format_time

    val today = LocalDate.now()

    var user_id:Int? = null
    val product_id:Int?=null
    val rates:Int? = null
    val date_and_time:String?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        _bind = FragmentItemSelectedDetailsBinding.inflate(layoutInflater)

        mAdView = bind.adView
        val adRequest = AdRequest.Builder().build()
        val adRequest1 = AdRequest.Builder().build()
        mAdView.loadAd(adRequest1)

        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedProtectDetails()

        viewModelClass.queryUserDetails().asLiveData().observe(viewLifecycleOwner, Observer {
            if (it == null) {
                //(activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, SignInFragment()).show()
            } else {
                user_id = it.id
            }
        })

        bind.callUs.setOnClickListener {
            val phoneNumber = "0508984777"
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
            startActivity(intent)
        }
    }

    fun selectedProtectDetails(){
        try {
            try {
                val dateString = "${viewModelClass.dateAndtimme.value}"
                val format = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                val date = format.parse(dateString)
                bind.prTitle.text = "${viewModelClass.title.value}"
                bind.prPriceCurrencyTxt.text = "${viewModelClass.currenxy.value}"
                bind.prPrices.text = "${viewModelClass.price.value}"
                bind.prLocation.text = viewModelClass.location.value.toString() + " in " + viewModelClass.paid_product.value
                bind.prTimeAndDate.text = "Date posted: ${formatRelativeDate(date)}"
                bind.prPromoted.text = viewModelClass.type.value.toString()
                bind.visitedView.text = formatViewsCount(viewModelClass.views.value!!.toLong())
                viewModelClass.Itemsratings.value = viewModelClass.ratings.value
                viewModelClass.ratings.value = viewModelClass.ratings.value

            } catch (e: Exception) { }

            viewModelClass.queryUserDetails().asLiveData().observe(viewLifecycleOwner, Observer { t ->
                try {
                    if (viewModelClass.postuserId.value == t.id && viewModelClass.subscription_date_due.value == "$today") {
                        bind.callUs.visibility = View.VISIBLE
                    } else {
                        bind.callUs.visibility = View.GONE
                    }
                } catch (e: Exception) { }
            })

        } catch (e: Exception) { }
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
        requireContext().startActivity(int)

    }

    fun currencyFormat(amount: String): String? {
        val formatter = DecimalFormat("###,###,##0.00")
        return formatter.format(amount.toDouble())
    }

}