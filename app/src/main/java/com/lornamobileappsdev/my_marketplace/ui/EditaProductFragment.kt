package com.lornamobileappsdev.my_marketplace.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.telephony.TelephonyManager
import android.util.Base64
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.room.TypeConverter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.lornamobileappsdev.my_marketplace.R
import com.lornamobileappsdev.my_marketplace.api.Api
import com.lornamobileappsdev.my_marketplace.constant.test_website
import com.lornamobileappsdev.my_marketplace.constant.token
import com.lornamobileappsdev.my_marketplace.constant.url_for_uploading
import com.lornamobileappsdev.my_marketplace.constant.url_main
import com.lornamobileappsdev.my_marketplace.databinding.FragmentEditaProductBinding
import com.lornamobileappsdev.my_marketplace.repository.ApiClient
import com.lornamobileappsdev.my_marketplace.utils.FileModel
import com.lornamobileappsdev.my_marketplace.utils.FileUtilss
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel
import com.lornamobileappsdev.my_marketplace.viewModels.PullingDataFromServerViewModel
import kotlinx.coroutines.delay
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.net.URI
import java.text.DecimalFormat
import java.time.LocalDate
import java.util.*
import java.util.concurrent.TimeUnit

class EditaProductFragment : Fragment() {

    lateinit var _bind: FragmentEditaProductBinding
    val bind get() = _bind

    val viewModelClass: MyViewModel by lazy {
        ViewModelProvider(requireActivity())[MyViewModel::class.java]
    }

    val viewModelClassTwo: PullingDataFromServerViewModel by lazy {
        ViewModelProvider(requireActivity())[PullingDataFromServerViewModel::class.java]
    }

    private val imagesUris: MutableList<Uri> = ArrayList()

    var bitmaped = arrayListOf<Bitmap>()

    var uried = arrayListOf<URI>()

    val REQUEST_ID_MULTIPLE_PERMISSIONS = 101

    private val STORAGE_PERMISSION_CODE = 4655

    val productImagesList = mutableListOf<ByteArray>()

    val expandIn by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.anim)
    }

    lateinit var currencySwitch: Spinner
    var currencySelected: String = ""

    lateinit var categorySwitch: Spinner
    var categorySelected: String = ""

    lateinit var pst_RegopmSwitch: Spinner
    var pst_regionSelected: String = ""

    lateinit var typeSwitch: Spinner
    var typeSelected: String = ""

    var bitmaps = arrayListOf<Bitmap>()
    var imageSources = arrayListOf<String>()

    var negotiatable: Int = 0

    val adsType: MutableLiveData<String> by lazy {
        MutableLiveData<String>("Standard")
    }

    val today = LocalDate.now()

    val r_code_rgion_spinners = arrayListOf<String>("Select Country")


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        _bind = FragmentEditaProductBinding.inflate(layoutInflater)

        (activity as AppCompatActivity).supportActionBar!!.hide()

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

        viewModelClass.countriesNames.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it.forEach {
                r_code_rgion_spinners.add(it.name)
            }
        })

        try {
            Handler(Looper.myLooper()!!).postDelayed({
                bind.townProgrBar.visibility = View.GONE
            }, 9000L)
        } catch (e: Exception) {
        }


        val pickMultipleMedia =
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
                            val imagePath = FileUtilss.getReadablePathFromUri(requireContext(), it)
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



        bind.addImagees.setOnClickListener {

            pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        val checkbox = bind.negotiate
        checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                negotiatable = 1
                Log.i("negotiate", bind.negotiate.isChecked.toString())
            } else {
                negotiatable = 0
                Log.i("negotiate", bind.negotiate.isChecked.toString())
            }
        }

        try {

            val prz = viewModelClass.price.value.toString().replace("[,. ]".toRegex(), "")
                .lowercase(Locale.getDefault())

            val circularProgressDrawable = CircularProgressDrawable(requireContext())
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            Glide.with(this@EditaProductFragment)
                .load(url_for_uploading + viewModelClass.image.value)
                .placeholder(circularProgressDrawable)
                .fitCenter()
                .into(bind.uploadOneo)
            Glide.with(this@EditaProductFragment)
                .asBitmap()
                .load(url_for_uploading + viewModelClass.image_two.value)
                .placeholder(circularProgressDrawable)
                .fitCenter()
                .into(bind.uploadTwoo)
            Glide.with(this@EditaProductFragment)
                .asBitmap()
                .load(url_for_uploading + viewModelClass.image_three.value!!)
                .placeholder(circularProgressDrawable)
                .fitCenter()
                .into(bind.uploadThreeo)
            Glide.with(this@EditaProductFragment)
                .asBitmap()
                .load(url_for_uploading + viewModelClass.image_four.value)
                .placeholder(circularProgressDrawable)
                .fitCenter()
                .into(bind.uploadTfouro)
            Glide.with(this@EditaProductFragment)
                .asBitmap()
                .load(url_for_uploading + viewModelClass.image_five.value)
                .placeholder(circularProgressDrawable)
                .fitCenter()
                .into(bind.uploadFiveo)
            Glide.with(this@EditaProductFragment)
                .asBitmap()
                .load(url_for_uploading + viewModelClass.image_six.value)
                .placeholder(circularProgressDrawable)
                .fitCenter()
                .into(bind.uploadSixo)

            /*bind.uploadOne.setImageBitmap(byteArrayToBitmap(data.image!!))
            bind.uploadTwo.setImageBitmap(byteArrayToBitmap(data.image_two!!))
            bind.uploadThree.setImageBitmap(byteArrayToBitmap(data.image_three!!))
            bind.uploadTfour.setImageBitmap(byteArrayToBitmap(data.image_four!!))
            bind.uploadFive.setImageBitmap(byteArrayToBitmap(data.image_five!!))
            bind.uploadSix.setImageBitmap(byteArrayToBitmap(data.image_six!!))*/

            bind.edtDtPrTitle.setText(viewModelClass.title.value)
            bind.edtPrPrices.setText(prz)
            bind.editLocationSpinnerText.text = viewModelClass.location.value
            bind.edtSglDescriptionProductTxt.setText(viewModelClass.desc.value!!)
            bind.edtPrTimeAndDate.setText(viewModelClass.dateAndtimme.value!!)
            bind.editPhoneNumber.setText(viewModelClass.phone_number.value!!)
            bind.editLocationSpinnerText.text = viewModelClass.location.value!!
            bind.edtCategoryPrTxt.text = viewModelClass.category.value!!
            bind.edtTypePrTxt.text = viewModelClass.type.value!!
            bind.editPhoneNumber.setText(viewModelClass.phone_number.value!!)
            bind.edtPrEnterRegionCity.setText(viewModelClass.paid_product.value!!)

        } catch (e: Exception) {
        }

        lifecycleScope.launchWhenStarted {
            requestStoragePermission()
        }

        viewModelClass.productId.observe(viewLifecycleOwner, Observer {

        })

        viewModelClass.title.observe(viewLifecycleOwner, Observer {

        })

        viewModelClass.price.observe(viewLifecycleOwner, Observer {

        })

        viewModelClass.location.observe(viewLifecycleOwner, Observer {

        })

        viewModelClass.desc.observe(viewLifecycleOwner, Observer {
            bind.edtSglDescriptionProductTxt.setText(it)
        })

        viewModelClass.dateAndtimme.observe(viewLifecycleOwner, Observer {
            bind.edtPrTimeAndDate.setText(it)
        })

        viewModelClass.image.observe(viewLifecycleOwner, Observer {

        })

        onOthers()

        bind.updatePost.setOnClickListener {

            try {

                bind.submitProgress.visibility = View.VISIBLE
            } catch (e: Exception) {
            }

            if (bind.edtSglDescriptionProductTxt.text.toString() == "" || bind.edtDtPrTitle.text.toString() == "" || bind.edtPrPrices.text.toString() == "" || currencyFormat(
                    bind.edtPrPrices.text.toString()
                ).isNullOrEmpty() || currencySelected == "" || currencySelected == "Select currency" || categorySelected == "" || categorySelected == "Select Category" || pst_regionSelected == "" || pst_regionSelected == "Select your region" || typeSelected == "" || typeSelected == "Select type of product" || typeSelected == "" || bind.editPhoneNumber.text.toString() == "" || bind.edtPrEnterRegionCity.text.toString() == ""
            ) {
                try {
                    bind.submitProgress.visibility = View.VISIBLE
                } catch (e: Exception) {
                }
                Snackbar.make(
                    bind.textView16,
                    "Please make sure to fill all fields accordingly",
                    Snackbar.LENGTH_LONG
                ).show()
            } else {
                try {

                    bind.submitProgress.visibility = View.VISIBLE
                } catch (e: Exception) {
                }
                submit()
            }

        }

        bind.bk.setOnClickListener {
            (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, Single_Selected_item_show()).addToBackStack(null)
                .commit()
        }

        //Toast.makeText(requireContext(), viewModelClass.productId.value!!.toInt().toString(), Toast.LENGTH_LONG).show()

        return bind.root
    }


    fun checkAndRequestPermissions(context: Activity?): Boolean {
        val WExtstorePermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val cameraPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        )
        val listPermissionsNeeded: MutableList<String> = ArrayList()
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (WExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                .add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                requireActivity(), listPermissionsNeeded.toTypedArray(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.N)
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
            requireContext(),
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
            "Grocery",
            "Adult",
            "Beauty",
            "Real Estate",
            "Accessories",
            "HomeWare",
            "Appliance",
            "Health",
            "Education",
            "Business",
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
            requireContext(), R.layout.support_simple_spinner_dropdown_item, r_code_spinners
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
            requireContext(), R.layout.support_simple_spinner_dropdown_item, r_type_spinners
        )

        typeSwitch.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {
                bind.edtCategoryPrTxt.text = "Select type of product"
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
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
            requireContext(), R.layout.support_simple_spinner_dropdown_item, r_code_rgion_spinners
        )

        pst_RegopmSwitch.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {
                bind.editLocationSpinnerText.text = "Select your region"
            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                bind.editLocationSpinnerText.text = r_code_rgion_spinners[position]
                selectedREgionSpinner.removeAll(selectedREgionSpinner)
                selectedREgionSpinner.add(r_code_rgion_spinners[position].length)

                if (r_code_rgion_spinners[position] != "Select country") {
                    pst_regionSelected = r_code_rgion_spinners[position]
                } else if (r_code_rgion_spinners[position] == "Select country") {
                    Snackbar.make(
                        bind.textView16,
                        "Select country",
                        Snackbar.LENGTH_LONG
                    ).show()
                    pst_regionSelected = "Select country"
                }
            }
        }
        val s = SimpleDateFormat("dd/MM/yyyy") //,hh:mm:ss
        val format: String = s.format(Date())
        val timeStamp = format

        val ss = SimpleDateFormat("hh:mm:ss") //,hh:mm:ss
        val format_time: String = ss.format(Date())
        val timeSt = format_time


    }

    private fun requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) else return


        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            STORAGE_PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(
                    requireContext(),
                    "Permission granted now you can read the storage",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Oops you just denied the permission you will not be able to post an Ads until you grant it",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @TypeConverter
    fun submit() {

        // Inflate the layout for this fragment
        val s = SimpleDateFormat("dd/MM/yyyy") //,hh:mm:ss
        val format: String = s.format(Date())
        val timeStamp = format

        val ss = SimpleDateFormat("hh:mm:ss") //,hh:mm:ss
        val format_time: String = ss.format(Date())
        val timeSt = format_time

        val today = LocalDate.now()
        val fiveDaysLater = today.plusDays(5)

        submitUpdaete(
            id = viewModelClass.productId.value!!.toInt().toString(),
            userId = viewModelClass.postuserId.value!!.toString(),
            rates = viewModelClass.ratings.value.toString(),
            user_name = viewModelClass.user_name.value!!,
            title = bind.edtDtPrTitle.text.toString(),
            currenxy = currencySelected,
            price = currencyFormat(bind.edtPrPrices.text.toString())!!,
            category = categorySelected,
            country = getCountryName(requireContext()).toUpperCase(),
            location = pst_regionSelected,
            desc = bind.edtSglDescriptionProductTxt.text.toString(),
            dateAndtimme = "${today}",
            type = typeSelected,
            negotiation = negotiatable.toString(),
            paid_product = bind.edtPrEnterRegionCity.text.toString(),
            pst_phone_number = bind.editPhoneNumber.text.toString(),
            subscription_type = viewModelClass.subscription_type.value!!,
            subscription_date_start = "${today}",
            subscription_date_due = (if (viewModelClass.subscription_type.value!! == "Silver 15 days") "${
                today.plusDays(
                    15
                )
            }" else if (viewModelClass.subscription_type.value!! == "Bronx 7 days") "${
                today.plusDays(
                    7
                )
            }" else if (viewModelClass.subscription_type.value!! == "Gold 1 month") "${
                today.plusDays(
                    31
                )
            }" else "Standard promo"),
            views = viewModelClass.views.value!!.toInt().toString(),
            approval_status = "pending",
            /*image = (if (uried.size == 0) viewModelClass.image!!.value else uried[0]) as String,
            image_two = (if (uried.size == 0) viewModelClass.image_two!!.value else uried[1]) as String,
            image_three = (if (uried.size == 0) viewModelClass.image_three!!.value else uried[2]) as String,
            image_four = (if (uried.size == 0) viewModelClass.image_four!!.value else uried[3]) as String,
            image_five = (if (uried.size == 0) viewModelClass.image_five!!.value else uried[4]) as String,
            image_six = (if (uried.size == 0) viewModelClass.image_six!!.value else uried[5]) as String,*/
        )


        Log.i(
            "updateData",
            "${viewModelClass.productId.value!!.toInt()} \n ${viewModelClass.postuserId.value!!} \n ${viewModelClass.ratings.value.toString()} \n ${viewModelClass.user_name.value!!} \n ${bind.edtDtPrTitle.text} \n ${currencySelected} \n ${
                currencyFormat(bind.edtPrPrices.text.toString())!!
            } \n ${categorySelected} \n ${viewModelClass.country.value!!} \n ${pst_regionSelected} \n ${bind.edtSglDescriptionProductTxt.text} \n ${today} \n ${typeSelected} \n $negotiatable \n ${bind.edtPrEnterRegionCity.text} \n  ${bind.editPhoneNumber.text} \n ${viewModelClass.subscription_type.value!!} \n ${today} \n ${viewModelClass.views.value!!.toInt()} "
        )


    }


    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun bitMapToString(bitmap: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        val temp: String = Base64.encodeToString(b, Base64.DEFAULT)
        return if (temp == null) {
            null
        } else temp
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun byteArrayToBitmap(data: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(data, 0, data.size)
    }

    fun submitUpdaete(

        id: String,
        userId: String,
        rates: String,
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
    ) {

        val progressDialog = Dialog(requireContext())
        try {

            bind.submitProgress.visibility = View.VISIBLE
        } catch (e: Exception) {
        }

        if (viewModelClass.isNetworkAvailable(test_website)) {

            try {

                val list: MutableList<MultipartBody.Part> = arrayListOf()
                for (uri in imagesUris) {
                    prepairFiles("file[]", uri).let { list.add(it) }
                }

                val ids = id.toRequestBody(MultipartBody.FORM)
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
                val subscription_types = subscription_type.toRequestBody(MultipartBody.FORM)
                val subscription_date_starts =
                    subscription_date_start.toRequestBody(MultipartBody.FORM)
                val subscription_date_dues =
                    subscription_date_due.toRequestBody(MultipartBody.FORM)
                val viewss = views.toRequestBody(MultipartBody.FORM)
                val approval_statuss = approval_status.toRequestBody(MultipartBody.FORM)
                val ratess = rates.toRequestBody(MultipartBody.FORM)

                Log.i(
                    "posting_log",
                    "${ids} + ${userIds} + ${user_names} +${titles} + ${currenxys} +${prices} +${categorys} +${countrys} +${locations} +${descs} +${dateAndtimmes} +${types} +${negotiations} +${paid_products} +${pst_phone_numbers} +${subscription_types} +${subscription_date_starts} +${subscription_date_dues} +${viewss} +${approval_statuss} +${ratess} "
                )


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
                    .build()

                val retrofit: Retrofit.Builder by lazy {
                    Retrofit.Builder().baseUrl(url_main)
                        .client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                }
                val retrofitApi: Api by lazy {
                    retrofit.client(okHttpClient)
                    retrofit.build().create(Api::class.java)
                }
                val apiClient = ApiClient(retrofitApi)

                retrofitApi.UpdatingPosting(
                    ids,
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
                    paid_products,
                    pst_phone_numbers,
                    subscription_types,
                    subscription_date_starts,
                    subscription_date_dues,
                    viewss,
                    approval_statuss,
                    ratess,
                )
                    .enqueue(object : Callback<FileModel> {

                        override fun onResponse(
                            call: Call<FileModel>,
                            response: Response<FileModel>
                        ) {
                            val model = response.body()
                            if (response.isSuccessful) {
                                if (model!!.status!!) {
                                    try {
                                        bind.submitProgress.visibility = View.GONE
                                    } catch (e: Exception) {
                                    }
                                    lifecycleScope.launchWhenStarted {
                                        viewModelClass.deleteproductDatatbase(viewModelClass.productId.value!!.toInt())
                                        viewModelClassTwo.deleteRecentViewedProduct(viewModelClass.productId.value!!.toInt())
                                    }

                                    viewModelClassTwo.uploadProductText.value = model.message

                                    lifecycleScope.launchWhenStarted {
                                        delay(1000L)
                                    }

                                    (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                                        .replace(
                                            android.R.id.content,
                                            UploadedProductSuccessfullyFragment()
                                        )
                                        .commit()


                                } else if (model.status == false) {
                                    try {
                                        bind.submitProgress.visibility = View.GONE
                                    } catch (e: Exception) {
                                    }
                                    val alertDialog = AlertDialog.Builder(requireContext())
                                    alertDialog.setCancelable(false)
                                    alertDialog.setTitle("Error")
                                    alertDialog.setMessage("${model.message}")
                                    alertDialog.setPositiveButton("Continue") { _, _ ->

                                    }
                                    alertDialog.show()
                                }
                            } else {
                                try {
                                    bind.submitProgress.visibility = View.GONE
                                } catch (e: Exception) {
                                }
                                val alertDialog = AlertDialog.Builder(requireContext())
                                alertDialog.setCancelable(false)
                                alertDialog.setTitle("Http Error")
                                alertDialog.setMessage("Sorry we could not process your Ad, Please try again ${response.message()}.")
                                alertDialog.setPositiveButton("Continue") { _, _ ->

                                }
                                alertDialog.show()
                            }
                        }

                        override fun onFailure(call: Call<FileModel>, t: Throwable) {
                            bind.submitProgress.visibility = View.GONE
                            Toast.makeText(
                                requireContext(),
                                "Error reaching server, check your internet connection ${t.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    })


            } catch (e: Exception) {
            }


        } else {
            (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, NoSignalWifiFragment()).addToBackStack(null).commit()
        }


    }


    /*fun submitUpdaete(userId:Int, postingItemData : ProductData) {

        *//* val multipartBody = MultipartBody.Builder()
             .setType(MultipartBody.FORM)
             .addFormDataPart("userId", "$userId")
             .addFormDataPart("user_name", "$user_name")
             .addFormDataPart("title", "$title")
             .addFormDataPart("price", "$price")
             .addFormDataPart("category", "$category")
             .addFormDataPart("country", "$country")
             .addFormDataPart("location", "$location")
             .addFormDataPart("descp", "$desc")
             .addFormDataPart("dateAndtimme", "$dateAndtimme")
             .addFormDataPart("type", "$type")
             .addFormDataPart("negotiation", "$negotiation")
             .addFormDataPart("paid_product", "$paid_product")
             .addFormDataPart("pst_phone_number", "$pst_phone_number")
             .addFormDataPart("subscription_type", "$subscription_type")
             .addFormDataPart("subscription_date_start", "$subscription_date_start")
             .addFormDataPart("subscription_date_due", "$subscription_date_due")
             .addFormDataPart("views", "$views")
             .addFormDataPart("approval_status", "$approval_status")
             .addFormDataPart("image", "$image")
             .addFormDataPart("image_two", "$image_two")
             .addFormDataPart("image_three", "$image_three")
             .addFormDataPart("image_four", "$image_four")
             .addFormDataPart("image_five", "$image_five")
             .addFormDataPart("image_six", "$image_six")
             .build()*//*

        Log.i("postingAds", "$postingItemData" )


        val service = Singleton.Singleton
        val call = service.retrofitApi.updateProductDetailsWithImages(id = viewModelClass.productId.value!!.toInt(), productData = postingItemData)

        call!!.enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                val model = response.body()
                Log.i("bool", model.toString())
                if (response.isSuccessful) {
                    if(response.code() == 200){
                        if (model!!) {
                            try {
                                bind.submitProgress.visibility = View.GONE
                            }catch (e:Exception){}
                            CoroutineScope(Dispatchers.IO).launch {
                                viewModelClass.deleteproductDatatbase(viewModelClass.id.value!!.toInt())
                            }
                            val alertDialog = AlertDialog.Builder(requireContext())
                            alertDialog.setCancelable(false)
                            alertDialog.setTitle("New Post")
                            alertDialog.setMessage("Your post is successfully updated and awaiting approval. It usually takes a day or two depending on your subscription or Whatsapp only on this line 0508984777 to get it checked.")
                            alertDialog.setPositiveButton("Continue") { _, _ ->
                                val intent =
                                    Intent(requireContext(), DashboardFragment::class.java)
                                startActivity(intent)
                            }
                            alertDialog.show()
                        } else if (model!! == false) {
                            try {
                                bind.submitProgress.visibility = View.GONE
                            }catch (e:Exception){}
                            val alertDialog = AlertDialog.Builder(requireContext())
                            alertDialog.setCancelable(false)
                            alertDialog.setTitle("Error")
                            alertDialog.setMessage("Something went wrong.")
                            alertDialog.setPositiveButton("Continue") { _, _ ->
                                val intent =
                                    Intent(requireContext(), EditaProductFragment::class.java)
                                startActivity(intent)
                            }
                            alertDialog.show()
                        }

                    }else if (response.code() == 500) {
                        try {
                            bind.submitProgress.visibility = View.GONE
                        }catch (e:Exception){}
                        val alertDialog = AlertDialog.Builder(requireContext())
                        alertDialog.setCancelable(false)
                        alertDialog.setTitle("Http Error")
                        alertDialog.setMessage("Sorry we could not process your Ad, Please try again.")
                        alertDialog.setPositiveButton("Continue") { _, _ ->
                            val intent =
                                Intent(requireContext(), EditaProductFragment::class.java)
                            startActivity(intent)
                        }
                        alertDialog.show()
                    } else if (response.code() == 400) {
                        try {
                            bind.submitProgress.visibility = View.GONE
                        }catch (e:Exception){}
                        val alertDialog = AlertDialog.Builder(requireContext())
                        alertDialog.setCancelable(false)
                        alertDialog.setTitle("Http Error")
                        alertDialog.setMessage("Sorry we could not process your Ad, Please try again.")
                        alertDialog.setPositiveButton("Continue") { _, _ ->
                            val intent =
                                Intent(requireContext(), EditaProductFragment::class.java)
                            startActivity(intent)
                        }
                        alertDialog.show()
                    }



                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                bind.submitProgress.visibility = View.GONE
                Toast.makeText(requireContext(), t.message, Toast.LENGTH_LONG).show()
            }
        })
    }*/

    private fun prepairFiles(partName: String, fileUri: Uri): MultipartBody.Part {
        val file = File(fileUri.path)
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, file.name, requestBody)
    }

    @SuppressLint("Range")
    private fun getPath(uri: Uri): String? {
        var cursor = requireContext().contentResolver.query(uri, null, null, null, null)
        if (cursor!!.moveToFirst() && cursor.count > 0) {
            var document_id = cursor.getString(0)
            document_id = document_id.substring(document_id.lastIndexOf(":") + 1)
            cursor = requireContext().contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null,
                MediaStore.Images.Media._ID + "=?",
                arrayOf(document_id),
                null
            )
            cursor!!.moveToFirst()
            val path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            cursor.close()
            return path
        }
        return null
    }

    private fun Bitmap.toByteArray(quality: Int = 50): ByteArray {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, quality, stream)
        return stream.toByteArray()
    }

    private fun stringToBitMap(encodedString: String?): Bitmap? {
        return try {
            val encodeByte: ByteArray = Base64.decode(encodedString, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
            bitmap
        } catch (e: java.lang.Exception) {
            e.message
            null
        }
    }

    fun currencyFormat(amount: String): String? {
        val formatter = DecimalFormat("###,###,##0.00")
        return formatter.format(amount.toDouble())
    }

    @RequiresApi(Build.VERSION_CODES.O)
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

    fun convertUriToBitmap(uri: Uri) {
        val `is`: InputStream? = requireContext().contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(`is`)
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
        val btmap = Bitmap.createScaledBitmap(bitmap, 400, 400, true)
        bitmaped.add(btmap)
    }

    suspend fun getlistofTown() {
        delay(2000L)
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

            val rees = retrofitApi.get_town_list().body()
            if (rees != null) {
                if (rees.isNotEmpty()) {
                    rees.forEach {
                        r_code_rgion_spinners.add(it.name)
                        Log.i("town_list", it.toString())

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
