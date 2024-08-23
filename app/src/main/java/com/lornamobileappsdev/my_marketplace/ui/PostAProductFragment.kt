package com.lornamobileappsdev.my_marketplace.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build.*
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.telephony.TelephonyManager
import android.util.Base64
import android.util.Log
import android.view.*
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.room.TypeConverter
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.lornamobileappsdev.my_marketplace.R
import com.lornamobileappsdev.my_marketplace.api.Api
import com.lornamobileappsdev.my_marketplace.databinding.FragmentAddProductBinding
import com.lornamobileappsdev.my_marketplace.utils.FileUtilss
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel
import com.lornamobileappsdev.my_marketplace.viewModels.PullingDataFromServerViewModel
import kotlinx.coroutines.delay
import okhttp3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.DecimalFormat
import java.time.LocalDate
import java.util.*
import java.util.concurrent.TimeUnit
import androidx.activity.result.PickVisualMediaRequest
import com.lornamobileappsdev.my_marketplace.constant.test_website
import com.lornamobileappsdev.my_marketplace.constant.token
import com.lornamobileappsdev.my_marketplace.constant.url_main
import com.lornamobileappsdev.my_marketplace.responses.PostProductResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.converter.scalars.ScalarsConverterFactory


class PostAProductFragment : AppCompatActivity() {

    lateinit var _bind: FragmentAddProductBinding
    val bind get() = _bind
    val viewModelClass: MyViewModel by lazy {
        ViewModelProvider(this)[MyViewModel::class.java]
    }

    private val PICK_IMAGE_REQUEST = 1

    val viewModelClassTwo: PullingDataFromServerViewModel by lazy {
        ViewModelProvider(this).get(PullingDataFromServerViewModel::class.java)
    }

    val listOFCountryCurrency: MutableMap<String, String> = mutableMapOf()

    val adsType: MutableLiveData<String> by lazy {
        MutableLiveData<String>("Standard")
    }

    val bitmaped: MutableList<Bitmap> = mutableListOf()

    val imagesUris: MutableList<Uri> = mutableListOf()

    lateinit var categorySwitch: Spinner
    var categorySelected: String = ""

    lateinit var pst_RegopmSwitch: Spinner
    var pst_regionSelected: String = ""


    lateinit var currencySwitch: Spinner
    var currencySelected: String = ""

    lateinit var typeSwitch: Spinner
    var typeSelected: String = ""

    val username: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val countrys: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val userId: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    val REQUEST_ID_MULTIPLE_PERMISSIONS = 101

    private val STORAGE_PERMISSION_CODE = 4655

    var bitmaps = arrayListOf<Bitmap>()
    var imageSources = arrayListOf<String>()

    var negotiatable: Int = 0

    val user_country: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val r_code_rgion_spinners = arrayListOf<String>("Select Country")


    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _bind = FragmentAddProductBinding.inflate(layoutInflater)
        setContentView(bind.root)

        viewModelClass.countriesNames.observe(
            this@PostAProductFragment,
            androidx.lifecycle.Observer {
                it.forEach {
                    r_code_rgion_spinners.add(it.name)
                }
            })
        // Inflate the layout for this fragment
        val s = android.icu.text.SimpleDateFormat("dd/MM/yyyy") //,hh:mm:ss
        val format: String = s.format(Date())
        val timeStamp = format

        val ss = android.icu.text.SimpleDateFormat("hh:mm:ss") //,hh:mm:ss
        val format_time: String = ss.format(Date())
        val timeSt = format_time

        val today = LocalDate.now()
        val fiveDaysLater = today.plusDays(5)

        supportActionBar!!.hide()

        val window: Window = this.window

        // clear FLAG_TRANSLUCENT_STATUS flag:

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(
            this@PostAProductFragment,
            android.R.color.black
        )

        onOthers()

        try {
            Handler(Looper.myLooper()!!).postDelayed({
                bind.townProgrBar.visibility = View.GONE
            }, 9000L)
        } catch (e: Exception) {
        }

        bind.standardAds.setOnClickListener {
            adsType.value = ""
            adsType.value = "Standard"
        }
        bind.silverAds.setOnClickListener {
            adsType.value = ""
            adsType.value = "Silver 15 days"
        }
        bind.bronxAds.setOnClickListener {
            adsType.value = ""
            adsType.value = "Bronx 7 days"
        }
        bind.goldAds.setOnClickListener {
            adsType.value = ""
            adsType.value = "Gold 1 month"
        }

        adsType.observe(this@PostAProductFragment, androidx.lifecycle.Observer {
            if (it == "Standard") {
                findViewById<LinearLayout>(R.id.standard_ads).background =
                    ContextCompat.getDrawable(
                        this@PostAProductFragment,
                        R.drawable.selected_ads_background
                    )
                findViewById<LinearLayout>(R.id.silver_ads).background = ContextCompat.getDrawable(
                    this@PostAProductFragment,
                    R.drawable.semi_curve_border
                )
                findViewById<LinearLayout>(R.id.gold_ads).background = ContextCompat.getDrawable(
                    this@PostAProductFragment,
                    R.drawable.semi_curve_border
                )
                findViewById<LinearLayout>(R.id.bronx_ads).background = ContextCompat.getDrawable(
                    this@PostAProductFragment,
                    R.drawable.semi_curve_border
                )
            } else if (it == "Silver 15 days") {
                findViewById<LinearLayout>(R.id.standard_ads).background =
                    ContextCompat.getDrawable(
                        this@PostAProductFragment,
                        R.drawable.semi_curve_border
                    )
                findViewById<LinearLayout>(R.id.silver_ads).background = ContextCompat.getDrawable(
                    this@PostAProductFragment,
                    R.drawable.selected_ads_background
                )
                findViewById<LinearLayout>(R.id.gold_ads).background = ContextCompat.getDrawable(
                    this@PostAProductFragment,
                    R.drawable.semi_curve_border
                )
                findViewById<LinearLayout>(R.id.bronx_ads).background = ContextCompat.getDrawable(
                    this@PostAProductFragment,
                    R.drawable.semi_curve_border
                )
            } else if (it == "Bronx 7 days") {
                findViewById<LinearLayout>(R.id.standard_ads).background =
                    ContextCompat.getDrawable(
                        this@PostAProductFragment,
                        R.drawable.semi_curve_border
                    )
                findViewById<LinearLayout>(R.id.silver_ads).background = ContextCompat.getDrawable(
                    this@PostAProductFragment,
                    R.drawable.semi_curve_border
                )
                findViewById<LinearLayout>(R.id.gold_ads).background = ContextCompat.getDrawable(
                    this@PostAProductFragment,
                    R.drawable.semi_curve_border
                )
                findViewById<LinearLayout>(R.id.bronx_ads).background = ContextCompat.getDrawable(
                    this@PostAProductFragment,
                    R.drawable.selected_ads_background
                )
            } else if (it == "Gold 1 month") {
                findViewById<LinearLayout>(R.id.standard_ads).background =
                    ContextCompat.getDrawable(
                        this@PostAProductFragment,
                        R.drawable.semi_curve_border
                    )
                findViewById<LinearLayout>(R.id.silver_ads).background = ContextCompat.getDrawable(
                    this@PostAProductFragment,
                    R.drawable.semi_curve_border
                )
                findViewById<LinearLayout>(R.id.gold_ads).background = ContextCompat.getDrawable(
                    this@PostAProductFragment,
                    R.drawable.selected_ads_background
                )
                findViewById<LinearLayout>(R.id.bronx_ads).background = ContextCompat.getDrawable(
                    this@PostAProductFragment,
                    R.drawable.semi_curve_border
                )
            }
        })

        val checkbox = findViewById<Switch>(R.id.negotiate)

        checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                negotiatable = 1
                Log.i("negotiate", bind.negotiate.isChecked.toString())
            } else {
                negotiatable = 0
                Log.i("negotiate", bind.negotiate.isChecked.toString())
            }
        }

        bind.updatePost.setOnClickListener {

            if (bind.edtSglDescriptionProductTxt.text.toString() == "" || bind.edtDtPrTitle.text.toString() == "" || imagesUris.size == 0 || bind.edtPrPrices.text.toString() == "" || currencyFormat(
                    bind.edtPrPrices.text.toString()
                ).isNullOrEmpty() || currencySelected == "" || currencySelected == "Select currency" || categorySelected == "" || categorySelected == "Select Category" || pst_regionSelected == "" || pst_regionSelected == "Select your region" || typeSelected == "" || typeSelected == "Select type of product" || typeSelected == "" || bind.editPhoneNumber.text.toString() == "" || bind.edtPrEnterRegionCity.text.toString() == ""
            ) {
                Snackbar.make(
                    bind.textView16,
                    "Please make sure to fill all fields accordingly",
                    Snackbar.LENGTH_LONG
                ).show()
            } else {

                try {
                    bind.submitProgress.visibility = View.VISIBLE
                    viewModelClass.queryUserDetails().asLiveData()
                        .observe(this, androidx.lifecycle.Observer {
                            lifecycleScope.launch {
                                submit(
                                    userId = it.id.toString(),
                                    user_name = it.fullName!!,
                                    title = bind.edtDtPrTitle.text.toString(),
                                    currenxy = currencySelected,
                                    price = currencyFormat(bind.edtPrPrices.text.toString())!!,
                                    category = categorySelected,
                                    country = getCountryName(this@PostAProductFragment).toUpperCase(),
                                    location = pst_regionSelected,
                                    desc = bind.edtSglDescriptionProductTxt.text.toString(),
                                    dateAndtimme = "${today}",
                                    type = typeSelected,
                                    negotiation = negotiatable.toString(),
                                    paid_product = bind.edtPrEnterRegionCity.text.toString(),
                                    pst_phone_number = bind.editPhoneNumber.text.toString(),
                                    subscription_type = adsType.value!!,
                                    subscription_date_start = "${today}",
                                    subscription_date_due = (if (adsType.value == "Silver 15 days") "${
                                        today.plusDays(
                                            15
                                        )
                                    }" else if (adsType.value == "Bronx 7 days") "${today.plusDays(7)}" else if (adsType.value == "Gold 1 month") "${
                                        today.plusDays(
                                            31
                                        )
                                    }" else "Standard promo"),
                                    views = "1",
                                    approval_status = "pending",
                                    rates = "0",
                                )

                                val products = PostProductResponse(
                                    userId = it.id,
                                    user_name = it.fullName!!,
                                    title = bind.edtDtPrTitle.text.toString(),
                                    currenxy = currencySelected,
                                    price = currencyFormat(bind.edtPrPrices.text.toString())!!,
                                    category = categorySelected,
                                    country = it.country!!,
                                    location = pst_regionSelected,
                                    desc = bind.edtSglDescriptionProductTxt.text.toString(),
                                    dateAndtimme = "${today}",
                                    type = typeSelected,
                                    negotiation = negotiatable.toString(),
                                    paid_product = bind.edtPrEnterRegionCity.text.toString(),
                                    pst_phone_number = bind.editPhoneNumber.text.toString(),
                                    subscription_type = adsType.value!!,
                                    subscription_date_start = "${today}",
                                    subscription_date_due = (if (adsType.value == "Silver 15 days") "${
                                        today.plusDays(15)
                                    }" else if (adsType.value == "Bronx 7 days") "${today.plusDays(7)}" else if (adsType.value == "Gold 1 month") "${
                                        today.plusDays(31)
                                    }" else "Standard promo"),
                                    views = "1".toInt(),
                                    approval_status = "pending",
                                    rates = "0".toInt(),
                                )

                                Log.i("posting_log", products.toString())
                            }
                        })


                } catch (e: Exception) {
                }

            }
        }

        bind.uploadOneo.setImageURI(null)
        bind.uploadTwoo.setImageURI(null)
        bind.uploadThreeo.setImageURI(null)
        bind.uploadTfouro.setImageURI(null)
        bind.uploadFiveo.setImageURI(null)
        bind.uploadSixo.setImageURI(null)
        imagesUris.clear()
        bitmaped.clear()
        imageSources.clear()
        bitmaps.clear()


        try {

            val filePickerLauncher =
                registerForActivityResult(
                    ActivityResultContracts.PickMultipleVisualMedia(10)
                ) { uri ->
                    // Callback is invoked after the user selects a media item or closes the
                    // photo picker.
                    if (uri.isNotEmpty()) {
                        Log.d("PhotoPicker", "Selected URI: $uri")

                        try {
                            uri.forEach {
                                Log.d("PhotoPicker", "Number of items selected: ${it}")
                                val imagePath = FileUtilss.getReadablePathFromUri(this, it)
                                imagesUris.add(Uri.parse(imagePath))
                            }

                        } catch (e: Exception) {
                        }

                        try {

                            bind.uploadOneo.setImageURI(imagesUris[0])
                            bind.uploadTwoo.setImageURI(imagesUris[1])
                            bind.uploadThreeo.setImageURI(imagesUris[2])
                            bind.uploadTfouro.setImageURI(imagesUris[3])
                            bind.uploadFiveo.setImageURI(imagesUris[4])
                            bind.uploadSixo.setImageURI(imagesUris[5])
                        } catch (e: Exception) {
                        }

                    } else {
                        Log.d("PhotoPicker", "No media selected")
                    }
                }


            bind.addPhoto.setOnClickListener {
                //  if (checkAndRequestPermissions(this@PostAProductFragment)) {
                //selectImageFromGallery()
                //pickMultipleMedia.launch("image/*")
                // }

                // If you have access to the external storage, do whatever you need
                // If you have access to the external storage, do whatever you need

                filePickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))


            }

        } catch (e: Exception) {
        }

        bind.bk.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, DashboardFragment()).commit()
        }

        lifecycleScope.launchWhenStarted {
            getSpecificCountryCurrency()
        }

        viewModelClassTwo.uploadProductText.observe(
            this@PostAProductFragment,
            androidx.lifecycle.Observer {

                Log.i("uploadTag", it)
                if (it.toString() == "Error occurred while updating item. Check your internet connection and try again") {

                    try {
                        bind.submitProgress.visibility = View.GONE
                    } catch (e: Exception) {
                    }

                    try {
                        bind.submitProgress.visibility = View.GONE
                    } catch (e: Exception) {
                    }

                    val alertDialog = AlertDialog.Builder(this@PostAProductFragment)
                    alertDialog.setCancelable(false)
                    alertDialog.setTitle("Error")
                    alertDialog.setMessage("${it}")
                    alertDialog.setPositiveButton("Continue") { _, _ ->

                    }
                    alertDialog.show()
                } else {
                    try {
                        bind.submitProgress.visibility = View.GONE
                    } catch (e: Exception) {
                    }


                    lifecycleScope.launchWhenStarted {
                        delay(1000L)
                    }

                    supportFragmentManager.beginTransaction().replace(
                        android.R.id.content,
                        UploadedProductSuccessfullyFragment()
                    ).commit()

                }
            })
    }

    @RequiresApi(VERSION_CODES.N)
    fun onOthers() {

        //currency switch
        currencySwitch = bind.currency
        //rSwitch_text = findViewById(R.id.rSwitch_text) as TextView
        var currencySpinner: MutableList<Int> = mutableListOf<Int>()
        val currency_code_spinners = arrayListOf(
            "Select currency",
            "AFN",
            "ALL",
            "DZD",
            "EUR",
            "AOA",
            "XCD",
            "ARS",
            "AMD",
            "AWG",
            "AUD",
            "AZN",
            "BSD",
            "BHD",
            "BDT",
            "BBD",
            "BYN",
            "BZD",
            "XOF",
            "BMD",
            "BTN",
            "INR",
            "BOB",
            "BOV",
            "USD",
            "BAM",
            "BWP",
            "NOK",
            "BRL",
            "BND",
            "BGN",
            "BIF",
            "CVE",
            "KHR",
            "XAF",
            "CAD",
            "KYD",
            "CLF",
            "CLP",
            "CNY",
            "KMF",
            "CDF",
            "XPF",
            "GMD",
            "GEL",
            "GHS",
            "GIP",
            "DKK",
            "DJF",
            "DOP",
            "EGP",
            "SVC",
            "ERN",
            "ETB",
            "FKP",
            "FJD",
            "GIP",
            "HNL",
            "HKD",
            "HUF",
            "ISK",
            "JPY",
            "JOD",
            "KZT",
            "KES",
            "KPW",
            "KRW",
            "KWD",
            "KGS",
            "LAK",
            "LBP",
            "LSL",
            "ZAR",
            "LRD",
            "LYD",
            "CHF",
            "MOP",
            "MGA",
            "MWK",
            "MYR",
            "MVR",
            "WST",
            "STN",
            "SAR",
            "RSD",
            "SCR",
            "SLL",
            "SGD",
            "ANG",
            "SBD",
            "SOS",
            "GBP",
            "SSP",
            "LKR",
            "SDG",
            "SRD",
            "NOK",
            "SEK",
            "CHE",
            "CHF",
            "CHW",
            "SYP",
            "TWD",
            "TJS",
            "TZS",
            "THB",
            "UGX",
            "UAH",
            "AED",
            "UYI",
            "UYU",
            "UZS",
            "VUV",
            "VES",
            "VND",
            "YER",
            "ZMW",
            "ZWL"
        )

        currencySwitch.adapter = ArrayAdapter(
            this@PostAProductFragment,
            R.layout.support_simple_spinner_dropdown_item,
            currency_code_spinners
        )

        currencySwitch.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {
                bind.currencyCategoryPrTxt.text = "Select currency"
            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                bind.currencyCategoryPrTxt.text = currency_code_spinners[position]
                currencySpinner.removeAll(currencySpinner)
                currencySpinner.add(currency_code_spinners[position].length)

                if (currency_code_spinners[position] != "Select currency") {
                    currencySelected = currency_code_spinners[position]
                } else if (currency_code_spinners[position] == "Select currency") {
                    Snackbar.make(
                        bind.textView16, "Select currency", Snackbar.LENGTH_LONG
                    ).show()
                    currencySelected = "Select currency"
                }
            }
        }


        //category switch
        categorySwitch = bind.edtPrCategory
        //rSwitch_text = findViewById(R.id.rSwitch_text) as TextView
        var selectedSpinner: MutableList<Int> = mutableListOf<Int>()
        val r_code_spinners = arrayListOf(
            "Select category",
            "Frozen Foods",
            "Restaurant",
            "Vegetables/Farm",
            "AutoMobile",
            "Appliance",
            "Health",
            "Education",
            "Business",
            "Grocery",
            "Adult",
            "Beauty",
            "Real Estate",
            "Accessories",
            "HomeWare",
            "Clothing/Fashion",
            "Electronic",
            "Service",
            "Furniture",
            "Apartment",
            "Arts",
            "Movies/Tv",
            "Radio",
            "Animals/Pets",
            "Pharmacy",
            "Kids/Babies",
            "Smartphones",
            "Wristwatches",
            "Fishing",
            "Sneakers",
            "Everything Else"
        )

        categorySwitch.adapter = ArrayAdapter(
            this@PostAProductFragment,
            R.layout.support_simple_spinner_dropdown_item,
            r_code_spinners
        )

        categorySwitch.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {
                bind.edtCategoryPrTxt.text = "Select Category"
            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                bind.edtCategoryPrTxt.text = r_code_spinners[position]
                selectedSpinner.removeAll(selectedSpinner)
                selectedSpinner.add(r_code_spinners[position].length)

                if (r_code_spinners[position] != "Select Category") {
                    categorySelected = r_code_spinners[position]
                } else if (r_code_spinners[position] == "Select Category") {
                    Snackbar.make(
                        bind.textView16, "Select Category", Snackbar.LENGTH_LONG
                    ).show()
                    categorySelected = "Select Category"
                }
            }
        }

        //type of product spinner section
        typeSwitch = bind.edtTypeProduct
        //rSwitch_text = findViewById(R.id.rSwitch_text) as TextView
        var selecteTypedSpinner: MutableList<Int> = mutableListOf<Int>()
        val r_type_spinners = arrayListOf(
            "Select type",
            "Home Use",
            "Used Abroad",
            "Slightly Used",
            "Brand New",
            "Fresh",
            "Live",
            "Prepared",
            "Healthy",
            "String",
            "Unhealthy",
            "Faulty",
            "Refurbished",
            "Repaired",
            "Frozen",
            "Hygienic",
            "Under good condition",
            "Needs a little maintenance"
        )

        typeSwitch.adapter = ArrayAdapter(
            this@PostAProductFragment,
            R.layout.support_simple_spinner_dropdown_item,
            r_type_spinners
        )

        typeSwitch.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {
                bind.edtCategoryPrTxt.text = "Select type of product"
            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                bind.edtCategoryPrTxt.text = r_type_spinners[position]
                selecteTypedSpinner.removeAll(selecteTypedSpinner)
                selecteTypedSpinner.add(r_type_spinners[position].length)

                if (r_type_spinners[position] != "Select type") {
                    typeSelected = r_type_spinners[position]
                } else if (r_type_spinners[position] == "Select type") {
                    Snackbar.make(
                        bind.textView16, "Select type of product", Snackbar.LENGTH_LONG
                    ).show()
                    typeSelected = "Select type of product"
                }
            }
        }

        //reqion switch
        pst_RegopmSwitch = bind.editLocationSpinner
        //rSwitch_text = findViewById(R.id.rSwitch_text) as TextView
        var selectedREgionSpinner: MutableList<Int> = mutableListOf<Int>()


        pst_RegopmSwitch.adapter = ArrayAdapter(
            this@PostAProductFragment,
            R.layout.support_simple_spinner_dropdown_item,
            r_code_rgion_spinners
        )
        pst_RegopmSwitch.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {
                bind.editLocationSpinnerText.text = "Select your country"
            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                bind.editLocationSpinnerText.text = r_code_rgion_spinners[position]
                selectedREgionSpinner.removeAll(selectedREgionSpinner)
                selectedREgionSpinner.add(r_code_rgion_spinners[position].length)

                if (r_code_rgion_spinners[position] != "Select your country") {
                    pst_regionSelected = r_code_rgion_spinners[position]
                } else if (r_code_rgion_spinners[position] == "Select your country") {
                    Snackbar.make(
                        bind.textView16,
                        "Select your country",
                        Snackbar.LENGTH_LONG
                    ).show()
                    pst_regionSelected = "Select your country"
                }
            }
        }

        val s = android.icu.text.SimpleDateFormat("dd/MM/yyyy") //,hh:mm:ss
        val format: String = s.format(Date())
        val timeStamp = format

        val ss = android.icu.text.SimpleDateFormat("hh:mm:ss") //,hh:mm:ss
        val format_time: String = ss.format(Date())
        val timeSt = format_time

        bind.edtPrTimeAndDate.setText(timeSt)
    }

    fun CoroutineScope.submit(
        userId: String,
        user_name: String,
        title: String,
        currenxy: String,
        price: String,
        category: String,
        country: String,
        location: String,
        desc: String,
        dateAndtimme: String,
        type: String,
        negotiation: String,
        paid_product: String,
        pst_phone_number: String,
        subscription_type: String,
        subscription_date_start: String,
        subscription_date_due: String,
        views: String,
        approval_status: String,
        rates: String
    ) {
        try {
            bind.submitProgress.visibility = View.VISIBLE
        } catch (e: Exception) {
        }


        if (viewModelClass.isNetworkAvailable(test_website)) {

            try {
                launch {
                    val list: MutableList<MultipartBody.Part> = arrayListOf()
                    for (uri in imagesUris) {
                        prepairFiles("file[]", uri)?.let { list.add(it) }
                    }

                    // val body = MultipartBody.Part.createFormData("picture", list.getName(), requestFile)

                    // add another part within the multipart request

                    // add another part within the multipart request

                    val userIds = userId.toRequestBody(MultipartBody.FORM)
                    val user_names = user_name.toRequestBody(MultipartBody.FORM)
                    val titles = title.toRequestBody(MultipartBody.FORM)
                    val currenxys = currenxy.toRequestBody(MultipartBody.FORM)
                    val prices = price.toRequestBody(MultipartBody.FORM)
                    val categorys = category.toRequestBody(MultipartBody.FORM)
                    val countrys = country.toRequestBody(MultipartBody.FORM)
                    val locations = location.toRequestBody(MultipartBody.FORM)
                    val descs = desc.toRequestBody(MultipartBody.FORM)
                    val dateAndtimmes = dateAndtimme.toRequestBody(MultipartBody.FORM)
                    val types = type.toRequestBody(MultipartBody.FORM)
                    val negotiations = negotiation.toRequestBody(MultipartBody.FORM)
                    val paid_products = paid_product.toRequestBody(MultipartBody.FORM)
                    val pst_phone_numbers = pst_phone_number.toRequestBody(MultipartBody.FORM)
                    val subscription_types =
                        subscription_type.toRequestBody(MultipartBody.FORM)
                    val subscription_date_starts =
                        subscription_date_start.toRequestBody(MultipartBody.FORM)
                    val subscription_date_dues =
                        subscription_date_due.toRequestBody(MultipartBody.FORM)
                    val viewss = views.toRequestBody(MultipartBody.FORM)
                    val approval_statuss = approval_status.toRequestBody(MultipartBody.FORM)
                    val ratess = rates.toRequestBody(MultipartBody.FORM)


                    Log.i(
                        "posting_log",
                        "${userIds} + ${user_names} +${titles} + ${currenxys} +${prices} +${categorys} +${countrys} +${locations} +${descs} +${dateAndtimmes} +${types} +${negotiations} +${paid_products} +${pst_phone_numbers} +${subscription_types} +${subscription_date_starts} +${subscription_date_dues} +${viewss} +${approval_statuss} +${ratess}"
                    )

                    try {
                        val gson = GsonBuilder()
                            .setLenient()
                            .create()
                        val okHttpClient = OkHttpClient().newBuilder()
                            .connectTimeout(3, TimeUnit.MINUTES)
                            .readTimeout(3, TimeUnit.MINUTES)
                            .writeTimeout(3, TimeUnit.MINUTES)
                            .addInterceptor { chain ->
                                val request: Request =
                                    chain.request().newBuilder()
                                        .addHeader("Authorization", "Bearer ${token}")
                                        .addHeader("Content-Type", "application/json")
                                        .build()
                                chain.proceed(request)
                            }
                            .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                            .build()

                        val retrofit: Retrofit.Builder by lazy {
                            Retrofit.Builder().baseUrl(url_main)
                                .client(okHttpClient)
                                .addConverterFactory(ScalarsConverterFactory.create()) //important
                                .addConverterFactory(GsonConverterFactory.create(gson))
                        }
                        val retrofitApi: Api by lazy {
                            retrofit.client(okHttpClient)
                            retrofit.build().create(Api::class.java)
                        }
                        val call = retrofitApi.posting(
                            userIds,
                            user_names,
                            titles,
                            currenxys,
                            prices,
                            categorys,
                            countrys,
                            locations,
                            descs,
                            dateAndtimmes,
                            types,
                            negotiations,
                            list,
                            /* image_two,
                                   image_three,
                                   image_four,
                                   image_five,
                                   image_six,*/
                            paid_products,
                            pst_phone_numbers,
                            subscription_types,
                            subscription_date_starts,
                            subscription_date_dues,
                            viewss,
                            approval_statuss,
                            ratess
                        )


                        call.enqueue(object : Callback<String> {
                            override fun onResponse(
                                call: Call<String>,
                                response: Response<String>
                            ) {
                                val model = response.body().toString()
                                viewModelClassTwo.uploadProductText.value = model

                                Log.i("error_posting", "${model}")
                            }

                            override fun onFailure(call: Call<String>, t: Throwable) {
                                bind.submitProgress.visibility = View.GONE
                                Toast.makeText(
                                    this@PostAProductFragment,
                                    "Error, Something happened please wait for some minute ad try again.${t.message}",
                                    Toast.LENGTH_LONG
                                ).show()

                                Log.i("error_posting", "${t.message}")
                            }

                        })
                    } catch (e: Exception) {
                    }

                }
            } catch (e: Exception) {
            }

        } else {
            supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, NoSignalWifiFragment())
                .addToBackStack(null).commit()
        }
    }

    private fun prepairFiles(partName: String, fileUri: Uri): MultipartBody.Part {
        val file = File(fileUri.path)
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, file.name, requestBody)
    }

    @RequiresApi(VERSION_CODES.O)
    @TypeConverter
    fun byteArrayToBitmap(data: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(data, 0, data.size)
    }

    fun currencyFormat(amount: String): String? {
        val formatter = DecimalFormat("###,###,##0.00")
        return formatter.format(amount.toDouble())
    }

    @RequiresApi(VERSION_CODES.O)
    @TypeConverter
    fun stringToBitMap(encodedString: ByteArray?): Bitmap? {
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
        val W = 400
        val H = W * aspRat
        val b = Bitmap.createScaledBitmap(bitmap, W, H, false)

        return b
    }

    fun resizeImageUri(context: Context, uri: Uri, maxWidth: Int, maxHeight: Int): Uri {
        val resolver = context.contentResolver
        val inputStream = resolver.openInputStream(uri)

        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeStream(inputStream, null, options)
        inputStream?.close()

        var width = options.outWidth
        var height = options.outHeight
        var scale = 1

        while (width / scale > maxWidth || height / scale > maxHeight) {
            scale *= 2
        }

        val options2 = BitmapFactory.Options()
        options2.inSampleSize = scale
        val bitmap = BitmapFactory.decodeStream(resolver.openInputStream(uri), null, options2)

        val outputStream = ByteArrayOutputStream()
        bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

        val bytes = outputStream.toByteArray()

        val filename = UUID.randomUUID().toString() + ".jpg"
        val fos = context.openFileOutput(filename, Context.MODE_PRIVATE)
        fos.write(bytes)
        fos.close()

        return Uri.fromFile(context.getFileStreamPath(filename))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(this@PostAProductFragment, DashboardFragment::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun convertToAfricanCurrencies(amount: Double, currency: String): Double {
        val exchangeRates = mapOf(

            "CAD" to 0.12,
            "INR" to 1.0,
            "SGD" to 0.12,
            "AED" to 0.32,
            "NGN" to 0.015,   // Nigerian Naira
            "ZAR" to 0.064,   // South African Rand
            "KES" to 0.053,   // Kenyan Shilling
            "EGP" to 0.39,    // Egyptian Pound
            "USD" to 0.0881,
            "GBP" to 0.07106,
            "DZD" to 12.013,
            "AOA" to 50.099,
            "XOF" to 54.0823,
            "BWP" to 1.20004,
            "BIF" to 245.998,
            "XAF" to 54.0823,
            "CVE" to 9.09112,
            "KMF" to 40.5617,
            "CDF" to 203.854,
            "DJF" to 15.6555,
            "EGP" to 2.71794,
            "ERN" to 1.32143,
            "ETB" to 4.78217,
            "GMD" to 5.22056,
            "GHS" to 10.00,
            "GNF" to 751.002,
            "CFA" to 751.002,
            "KES" to 12.1302,
            "LSL" to 1.73967,
            "LRD" to 14.7649,
            "LYD" to 0.42267,
            "MGA" to 384.119,
            "MWK" to 89.6368,
            "MRO" to 29.9853,
            "MUR" to 3.9229,
            "MAD" to 0.88635,
            "MZN" to 5.56764,
            "NAD" to 1.73967,
            "NGN" to 40.5509,
            "EUR" to 0.08245,
            "RWF" to 98.1838,
            "STD" to 2.001,
            "SCR" to 1.10725,
            "SLL" to 1.739,
            "SOS" to 48.7857,
            "ZAR" to 1.73967,
            "SSP" to 52.7692,
            "SDG" to 52.7692,
            "SZL" to 1.73967,
            "TZS" to 207.636,
            "TND" to 0.27154,
            "UGX" to 330.348,
            "ZMW" to 1.71119,
            "ZWD" to 32.8861
            // Add more currencies and their exchange rates here
        )

        exchangeRates.filter {
            it.key == currency
        }.map {
            return amount * it.value
        }

        return 0.0
    }

    suspend fun getSpecificCountryCurrency() {
        delay(100L)
        try {
            listOFCountryCurrency!!.put("AFGHANISTAN", "AFN")
            listOFCountryCurrency!!.put("ALBANIA", "ALL")
            listOFCountryCurrency!!.put("ALGERIA", "DZD")
            listOFCountryCurrency!!.put("ANDORRA", "EUR")
            listOFCountryCurrency!!.put("ANGOLA", "AOA")
            listOFCountryCurrency!!.put("ANGUILLA", "XCD")
            listOFCountryCurrency!!.put("ANTARCTICA", "")
            listOFCountryCurrency!!.put("ANTIGUA AND BARBUDA", "XCD")
            listOFCountryCurrency!!.put("ARGENTINA", "ARS")
            listOFCountryCurrency!!.put("ARMENIA", "AMD")
            listOFCountryCurrency!!.put("ARUBA", "AWG")
            listOFCountryCurrency!!.put("AUSTRALIA", "AUD")
            listOFCountryCurrency!!.put("AUSTRIA", "EUR")
            listOFCountryCurrency!!.put("AZERBAIJAN", "AZN")
            listOFCountryCurrency!!.put("BAHAMAS", "BSD")
            listOFCountryCurrency!!.put("BAHRAIN", "BHD")
            listOFCountryCurrency!!.put("BANGLADESH", "BDT")
            listOFCountryCurrency!!.put("BARBADOS", "BBD")
            listOFCountryCurrency!!.put("BELARUS", "BYN")
            listOFCountryCurrency!!.put("BELGIUM", "EUR")
            listOFCountryCurrency!!.put("BELIZE", "BZD")
            listOFCountryCurrency!!.put("BENIN", "XOF")
            listOFCountryCurrency!!.put("BERMUDA", "BMD")
            listOFCountryCurrency!!.put("BHUTAN", "BTN")
            listOFCountryCurrency!!.put("BHUTAN", "INR")
            listOFCountryCurrency!!.put("BOLIVIA", "BOB")
            listOFCountryCurrency!!.put("BOLIVIA", "BOV")
            listOFCountryCurrency!!.put("BONAIRE", "USD")
            listOFCountryCurrency!!.put("BOSNIA AND HERZEGOVINA", "BAM")
            listOFCountryCurrency!!.put("BOTSWANA", "BWP")
            listOFCountryCurrency!!.put("BOUVET ISLAND", "NOK")
            listOFCountryCurrency!!.put("BRAZIL", "BRL")
            listOFCountryCurrency!!.put("BRITISH INDIAN OCEAN TERRITORY (THE)", "USD")
            listOFCountryCurrency!!.put("BRUNEI DARUSSALAM", "BND")
            listOFCountryCurrency!!.put("BULGARIA", "BGN")
            listOFCountryCurrency!!.put("BURKINA FASO", "XOF")
            listOFCountryCurrency!!.put("BURUNDI", "BIF")
            listOFCountryCurrency!!.put("CABO VERDE", "CVE")
            listOFCountryCurrency!!.put("CAMBODIA", "KHR")
            listOFCountryCurrency!!.put("CAMEROON", "XAF")
            listOFCountryCurrency!!.put("CANADA", "CAD")
            listOFCountryCurrency!!.put("CAYMAN ISLANDS", "KYD")
            listOFCountryCurrency!!.put("CENTRAL AFRICAN REPUBLIC", "XAF")
            listOFCountryCurrency!!.put("CHAD", "XAF")
            listOFCountryCurrency!!.put("CHILE", "CLF")
            listOFCountryCurrency!!.put("CHILE", "CLP")
            listOFCountryCurrency!!.put("CHINA", "CNY")
            listOFCountryCurrency!!.put("CHRISTMAS ISLAND", "AUD")
            listOFCountryCurrency!!.put("COCOS (KEELING) ISLANDS", "AUD")
            listOFCountryCurrency!!.put("COLOMBIA", "COP")
            listOFCountryCurrency!!.put("COLOMBIA", "COU")
            listOFCountryCurrency!!.put("COMOROS (THE)", "KMF")
            listOFCountryCurrency!!.put("CONGO (THE DEMOCRATIC REPUBLIC OF THE)", "CDF")
            listOFCountryCurrency!!.put("CONGO", "XAF")
            listOFCountryCurrency!!.put("COOK ISLANDS", "NZD")
            listOFCountryCurrency!!.put("COSTA RICA", "CRC")
            listOFCountryCurrency!!.put("CROATIA", "HRK")
            listOFCountryCurrency!!.put("CUBA", "CUC")
            listOFCountryCurrency!!.put("CUBA", "CUP")
            listOFCountryCurrency!!.put("CURAÇAO", "ANG")
            listOFCountryCurrency!!.put("CYPRUS", "EUR")
            listOFCountryCurrency!!.put("CZECH REPUBLIC (THE)", "CZK")
            listOFCountryCurrency!!.put("CÔTE D'IVOIRE", "XOF")
            listOFCountryCurrency!!.put("DENMARK", "DKK")
            listOFCountryCurrency!!.put("DJIBOUTI", "DJF")
            listOFCountryCurrency!!.put("DOMINICA", "XCD")
            listOFCountryCurrency!!.put("DOMINICAN REPUBLIC", "DOP")
            listOFCountryCurrency!!.put("ECUADOR", "USD")
            listOFCountryCurrency!!.put("EGYPT", "EGP")
            listOFCountryCurrency!!.put("EL SALVADOR", "SVC")
            listOFCountryCurrency!!.put("EL SALVADOR", "USD")
            listOFCountryCurrency!!.put("EQUATORIAL GUINEA", "XAF")
            listOFCountryCurrency!!.put("ERITREA", "ERN")
            listOFCountryCurrency!!.put("ESTONIA", "EUR")
            listOFCountryCurrency!!.put("ETHIOPIA", "ETB")
            listOFCountryCurrency!!.put("EUROPEAN UNION", "EUR")
            listOFCountryCurrency!!.put("FALKLAND ISLANDS (THE) [MALVINAS]", "FKP")
            listOFCountryCurrency!!.put("FAROE ISLANDS (THE)", "DKK")
            listOFCountryCurrency!!.put("FIJI", "FJD")
            listOFCountryCurrency!!.put("FINLAND", "EUR")
            listOFCountryCurrency!!.put("FRANCE", "EUR")
            listOFCountryCurrency!!.put("FRENCH GUIANA", "EUR")
            listOFCountryCurrency!!.put("FRENCH POLYNESIA", "XPF")
            listOFCountryCurrency!!.put("FRENCH SOUTHERN TERRITORIES (THE)", "EUR")
            listOFCountryCurrency!!.put("GABON", "XAF")
            listOFCountryCurrency!!.put("GAMBIA (THE)", "GMD")
            listOFCountryCurrency!!.put("GEORGIA", "GEL")
            listOFCountryCurrency!!.put("GERMANY", "EUR")
            listOFCountryCurrency!!.put("GHANA", "GHS")
            listOFCountryCurrency!!.put("GIBRALTAR", "GIP")
            listOFCountryCurrency!!.put("GREECE", "EUR")
            listOFCountryCurrency!!.put("GREENLAND", "DKK")
            listOFCountryCurrency!!.put("GRENADA", "XCD")
            listOFCountryCurrency!!.put("GUADELOUPE", "EUR")
            listOFCountryCurrency!!.put("GUAM", "USD")
            listOFCountryCurrency!!.put("GUATEMALA", "GTQ")
            listOFCountryCurrency!!.put("GUERNSEY", "GBP")
            listOFCountryCurrency!!.put("GUINEA", "GNF")
            listOFCountryCurrency!!.put("GUINEA-BISSAU", "XOF")
            listOFCountryCurrency!!.put("GUYANA", "GYD")
            listOFCountryCurrency!!.put("HAITI", "HTG")
            listOFCountryCurrency!!.put("HAITI", "USD")
            listOFCountryCurrency!!.put("HEARD ISLAND AND McDONALD ISLANDS", "AUD")
            listOFCountryCurrency!!.put("HOLY SEE (THE)", "EUR")
            listOFCountryCurrency!!.put("HONDURAS", "HNL")
            listOFCountryCurrency!!.put("HONG KONG", "HKD")
            listOFCountryCurrency!!.put("HUNGARY", "HUF")
            listOFCountryCurrency!!.put("ICELAND", "ISK")
            listOFCountryCurrency!!.put("INDIA", "INR")
            listOFCountryCurrency!!.put("INDONESIA", "IDR")
            listOFCountryCurrency!!.put("INTERNATIONAL MONETARY FUND (IMF)", "XDR")
            listOFCountryCurrency!!.put("IRAN (ISLAMIC REPUBLIC OF)", "IRR")
            listOFCountryCurrency!!.put("IRAQ", "IQD")
            listOFCountryCurrency!!.put("IRELAND", "EUR")
            listOFCountryCurrency!!.put("ISLE OF MAN", "GBP")
            listOFCountryCurrency!!.put("ISRAEL", "ILS")
            listOFCountryCurrency!!.put("ITALY", "EUR")
            listOFCountryCurrency!!.put("JAMAICA", "JMD")
            listOFCountryCurrency!!.put("JAPAN", "JPY")
            listOFCountryCurrency!!.put("JERSEY", "GBP")
            listOFCountryCurrency!!.put("JORDAN", "JOD")
            listOFCountryCurrency!!.put("KAZAKHSTAN", "KZT")
            listOFCountryCurrency!!.put("KENYA", "KES")
            listOFCountryCurrency!!.put("KIRIBATI", "AUD")
            listOFCountryCurrency!!.put("KOREA (THE DEMOCRATIC PEOPLE’S REPUBLIC OF)", "KPW")
            listOFCountryCurrency!!.put("KOREA (THE REPUBLIC OF)", "KRW")
            listOFCountryCurrency!!.put("KUWAIT", "KWD")
            listOFCountryCurrency!!.put("KYRGYZSTAN", "KGS")
            listOFCountryCurrency!!.put("LAO PEOPLE’S DEMOCRATIC REPUBLIC (THE)", "LAK")
            listOFCountryCurrency!!.put("LATVIA", "EUR")
            listOFCountryCurrency!!.put("LEBANON", "LBP")
            listOFCountryCurrency!!.put("LESOTHO", "LSL")
            listOFCountryCurrency!!.put("LESOTHO", "ZAR")
            listOFCountryCurrency!!.put("LIBERIA", "LRD")
            listOFCountryCurrency!!.put("LIBYA", "LYD")
            listOFCountryCurrency!!.put("LIECHTENSTEIN", "CHF")
            listOFCountryCurrency!!.put("LITHUANIA", "EUR")
            listOFCountryCurrency!!.put("LUXEMBOURG", "EUR")
            listOFCountryCurrency!!.put("MACAO", "MOP")
            listOFCountryCurrency!!.put("MADAGASCAR", "MGA")
            listOFCountryCurrency!!.put("MALAWI", "MWK")
            listOFCountryCurrency!!.put("MALAYSIA", "MYR")
            listOFCountryCurrency!!.put("MALDIVES", "MVR")
            listOFCountryCurrency!!.put("MALI", "XOF")
            listOFCountryCurrency!!.put("MALTA", "EUR")
            listOFCountryCurrency!!.put("MARSHALL ISLANDS (THE)", "USD")
            listOFCountryCurrency!!.put("MARTINIQUE", "EUR")
            listOFCountryCurrency!!.put("MAURITANIA", "MRU")
            listOFCountryCurrency!!.put("MAURITIUS", "MUR")
            listOFCountryCurrency!!.put("MAYOTTE", "EUR")
            listOFCountryCurrency!!.put(
                "MEMBER COUNTRIES OF THE AFRICAN DEVELOPMENT BANK GROUP",
                "XUA"
            )
            listOFCountryCurrency!!.put("MEXICO", "MXN")
            listOFCountryCurrency!!.put("MEXICO", "MXV")
            listOFCountryCurrency!!.put("MICRONESIA (FEDERATED STATES OF)", "USD")
            listOFCountryCurrency!!.put("MOLDOVA (THE REPUBLIC OF)", "MDL")
            listOFCountryCurrency!!.put("MONACO", "EUR")
            listOFCountryCurrency!!.put("MONGOLIA", "MNT")
            listOFCountryCurrency!!.put("MONTENEGRO", "EUR")
            listOFCountryCurrency!!.put("MONTSERRAT", "XCD")
            listOFCountryCurrency!!.put("MOROCCO", "MAD")
            listOFCountryCurrency!!.put("MOZAMBIQUE", "MZN")
            listOFCountryCurrency!!.put("MYANMAR", "MMK")
            listOFCountryCurrency!!.put("NAMIBIA", "NAD")
            listOFCountryCurrency!!.put("NAMIBIA", "ZAR")
            listOFCountryCurrency!!.put("NAURU", "AUD")
            listOFCountryCurrency!!.put("NEPAL", "NPR")
            listOFCountryCurrency!!.put("NETHERLANDS (THE)", "EUR")
            listOFCountryCurrency!!.put("NEW CALEDONIA", "XPF")
            listOFCountryCurrency!!.put("NEW ZEALAND", "NZD")
            listOFCountryCurrency!!.put("NICARAGUA", "NIO")
            listOFCountryCurrency!!.put("NIGER (THE)", "XOF")
            listOFCountryCurrency!!.put("NIGERIA", "NGN")
            listOFCountryCurrency!!.put("NIUE", "NZD")
            listOFCountryCurrency!!.put("NORFOLK ISLAND", "AUD")
            listOFCountryCurrency!!.put("NORTHERN MARIANA ISLANDS (THE)", "USD")
            listOFCountryCurrency!!.put("NORWAY", "NOK")
            listOFCountryCurrency!!.put("OMAN", "OMR")
            listOFCountryCurrency!!.put("PAKISTAN", "PKR")
            listOFCountryCurrency!!.put("PALAU", "USD")
            listOFCountryCurrency!!.put("PALESTINE, STATE OF", "ILS")
            listOFCountryCurrency!!.put("PANAMA", "PAB")
            listOFCountryCurrency!!.put("PANAMA", "USD")
            listOFCountryCurrency!!.put("PAPUA NEW GUINEA", "PGK")
            listOFCountryCurrency!!.put("PARAGUAY", "PYG")
            listOFCountryCurrency!!.put("PERU", "PEN")
            listOFCountryCurrency!!.put("PHILIPPINES (THE)", "PHP")
            listOFCountryCurrency!!.put("PITCAIRN", "NZD")
            listOFCountryCurrency!!.put("POLAND", "PLN")
            listOFCountryCurrency!!.put("PORTUGAL", "EUR")
            listOFCountryCurrency!!.put("PUERTO RICO", "USD")
            listOFCountryCurrency!!.put("QATAR", "QAR")
            listOFCountryCurrency!!.put("REPUBLIC OF NORTH MACEDONIA", "MKD")
            listOFCountryCurrency!!.put("ROMANIA", "RON")
            listOFCountryCurrency!!.put("RUSSIAN FEDERATION (THE)", "RUB")
            listOFCountryCurrency!!.put("RWANDA", "RWF")
            listOFCountryCurrency!!.put("RÉUNION", "EUR")
            listOFCountryCurrency!!.put("SAINT BARTHÉLEMY", "EUR")
            listOFCountryCurrency!!.put("SAINT HELENA, ASCENSION AND TRISTAN DA CUNHA", "SHP")
            listOFCountryCurrency!!.put("SAINT KITTS AND NEVIS", "XCD")
            listOFCountryCurrency!!.put("SAINT LUCIA", "XCD")
            listOFCountryCurrency!!.put("SAINT MARTIN (FRENCH PART)", "EUR")
            listOFCountryCurrency!!.put("SAINT PIERRE AND MIQUELON", "EUR")
            listOFCountryCurrency!!.put("SAINT VINCENT AND THE GRENADINES", "XCD")
            listOFCountryCurrency!!.put("SAMOA", "WST")
            listOFCountryCurrency!!.put("SAN MARINO", "EUR")
            listOFCountryCurrency!!.put("SAO TOME AND PRINCIPE", "STN")
            listOFCountryCurrency!!.put("SAUDI ARABIA", "SAR")
            listOFCountryCurrency!!.put("SENEGAL", "XOF")
            listOFCountryCurrency!!.put("SERBIA", "RSD")
            listOFCountryCurrency!!.put("SEYCHELLES", "SCR")
            listOFCountryCurrency!!.put("SIERRA LEONE", "SLL")
            listOFCountryCurrency!!.put("SINGAPORE", "SGD")
            listOFCountryCurrency!!.put("SINT MAARTEN (DUTCH PART)", "ANG")
            listOFCountryCurrency!!.put("SLOVAKIA", "EUR")
            listOFCountryCurrency!!.put("SLOVENIA", "EUR")
            listOFCountryCurrency!!.put("SOLOMON ISLANDS", "SBD")
            listOFCountryCurrency!!.put("SOMALIA", "SOS")
            listOFCountryCurrency!!.put("SOUTH AFRICA", "ZAR")
            listOFCountryCurrency!!.put("SOUTH GEORGIA AND THE SOUTH SANDWICH ISLANDS", "GBP")
            listOFCountryCurrency!!.put("SOUTH SUDAN", "SSP")
            listOFCountryCurrency!!.put("SPAIN", "EUR")
            listOFCountryCurrency!!.put("SRI LANKA", "LKR")
            listOFCountryCurrency!!.put("SUDAN (THE)", "SDG")
            listOFCountryCurrency!!.put("SURINAME", "SRD")
            listOFCountryCurrency!!.put("SVALBARD AND JAN MAYEN", "NOK")
            listOFCountryCurrency!!.put("SWEDEN", "SEK")
            listOFCountryCurrency!!.put("SWITZERLAND", "CHE")
            listOFCountryCurrency!!.put("SWITZERLAND", "CHF")
            listOFCountryCurrency!!.put("SWITZERLAND", "CHW")
            listOFCountryCurrency!!.put("SYRIAN ARAB REPUBLIC (THE)", "SYP")
            listOFCountryCurrency!!.put("TAIWAN (PROVINCE OF CHINA)", "TWD")
            listOFCountryCurrency!!.put("TAJIKISTAN", "TJS")
            listOFCountryCurrency!!.put("TANZANIA, THE UNITED REPUBLIC OF", "TZS")
            listOFCountryCurrency!!.put("THAILAND", "THB")
            listOFCountryCurrency!!.put("TIMOR-LESTE", "USD")
            listOFCountryCurrency!!.put("TOGO", "XOF")
            listOFCountryCurrency!!.put("TOKELAU", "NZD")
            listOFCountryCurrency!!.put("TONGA", "TOP")
            listOFCountryCurrency!!.put("TRINIDAD AND TOBAGO", "TTD")
            listOFCountryCurrency!!.put("TUNISIA", "TND")
            listOFCountryCurrency!!.put("TURKEY", "TRY")
            listOFCountryCurrency!!.put("TURKMENISTAN", "TMT")
            listOFCountryCurrency!!.put("TURKS AND CAICOS ISLANDS (THE)", "USD")
            listOFCountryCurrency!!.put("TUVALU", "AUD")
            listOFCountryCurrency!!.put("UGANDA", "UGX")
            listOFCountryCurrency!!.put("UKRAINE", "UAH")
            listOFCountryCurrency!!.put("UNITED ARAB EMIRATES (THE)", "AED")
            listOFCountryCurrency!!.put(
                "UNITED KINGDOM OF GREAT BRITAIN AND NORTHERN IRELAND (THE)",
                "GBP"
            )
            listOFCountryCurrency!!.put("UNITED STATES MINOR OUTLYING ISLANDS (THE)", "USD")
            listOFCountryCurrency!!.put("UNITED STATES OF AMERICA (THE)", "USD")
            listOFCountryCurrency!!.put("UNITED STATES OF AMERICA (THE)", "USN")
            listOFCountryCurrency!!.put("UNITED STATES OF AMERICA (THE)", "USS")
            listOFCountryCurrency!!.put("URUGUAY", "UYI")
            listOFCountryCurrency!!.put("URUGUAY", "UYU")
            listOFCountryCurrency!!.put("UZBEKISTAN", "UZS")
            listOFCountryCurrency!!.put("VANUATU", "VUV")
            listOFCountryCurrency!!.put("VENEZUELA (BOLIVARIAN REPUBLIC OF)", "VES")
            listOFCountryCurrency!!.put("VENEZUELA (BOLIVARIAN REPUBLIC OF)", "VUV")
            listOFCountryCurrency!!.put("VIET NAM", "VND")
            listOFCountryCurrency!!.put("VIRGIN ISLANDS (BRITISH)", "USD")
            listOFCountryCurrency!!.put("VIRGIN ISLANDS (U.S.)", "USD")
            listOFCountryCurrency!!.put("WALLIS AND FUTUNA", "XPF")
            listOFCountryCurrency!!.put("WESTERN SAHARA", "MAD")
            listOFCountryCurrency!!.put("YEMEN", "YER")
            listOFCountryCurrency!!.put("ZAMBIA", "ZMW")
            listOFCountryCurrency!!.put("ZIMBABWE", "ZWL")

            listOFCountryCurrency.filter { map ->
                map.key == getCountryName(this@PostAProductFragment).toUpperCase()
            }.map {
                when {
                    true -> {
                        //Toast.makeText(this@PostAProductFragment, it.value, Toast.LENGTH_LONG).show()
                        try {
                            bind.silverAdsCurrency.text = it.value + " "
                            bind.bronxAdsCurrency.text = it.value + " "
                            bind.goldAdsCurrency.text = it.value + " "
                            bind.bronxAdsAmount.text = String.format(
                                "%.2f", convertToAfricanCurrencies("3".toDouble(), it.value)
                            )
                            bind.silverAdsAmount.text = String.format(
                                "%.2f", convertToAfricanCurrencies("9".toDouble(), it.value)
                            )
                            bind.goldAdsAmount.text = String.format(
                                "%.2f", convertToAfricanCurrencies("15".toDouble(), it.value)
                            )
                        } catch (e: Exception) {
                        }
                    }

                    else -> {
                        // Toast.makeText(this@PostAProductFragment, "Not found ${it.key} == ${p.country}", Toast.LENGTH_LONG).show()
                    }
                }
            }

        } catch (e: Exception) {
        }
    }

    fun getCountryName(context: Context): String {
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val countryCode = telephonyManager.simCountryIso
        val locale = Locale("", countryCode)
        return locale.displayCountry
    }

}

