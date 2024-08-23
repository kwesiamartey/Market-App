package com.lornamobileappsdev.my_marketplace.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.ext.SdkExtensions
import android.util.Base64
import android.util.Log
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.room.TypeConverter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.imageproject.RetrofitBuilder
import com.google.android.material.snackbar.Snackbar
import com.lornamobileappsdev.my_marketplace.R
import com.lornamobileappsdev.my_marketplace.api.Api
import com.lornamobileappsdev.my_marketplace.constant.test_website
import com.lornamobileappsdev.my_marketplace.databinding.FragmentProfileUpdateBinding
import com.lornamobileappsdev.my_marketplace.entity.SignUpDAta
import com.lornamobileappsdev.my_marketplace.repository.ApiClient
import com.lornamobileappsdev.my_marketplace.responses.SignupResponses
import com.lornamobileappsdev.my_marketplace.utils.FileModel
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel
import kotlinx.coroutines.delay
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit


class ProfileUpdateFragment : AppCompatActivity() {

    lateinit var _bind: FragmentProfileUpdateBinding

    val bind get() = _bind

    val viewModelClass: MyViewModel by lazy {
        ViewModelProvider(this).get(MyViewModel::class.java)
    }

    private var userId: Int? = null

    private var fullName: String? = null

    private var description: String? = null

    lateinit var countrySwitch: Spinner

    var countrySelected: String = ""

    var bitmaps = arrayListOf<Bitmap>()

    var imageSources = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _bind = FragmentProfileUpdateBinding.inflate(layoutInflater)
        setContentView(bind.root)

        supportActionBar!!.hide()

        bind.bk.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(android.R.id.content, MyProfile()).commit()
        }

        val window: Window = this.window

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        window.statusBarColor = ContextCompat.getColor(
            this@ProfileUpdateFragment,
            android.R.color.black
        )


        viewModelClass.queryUserDetails().asLiveData()
            .observe(this@ProfileUpdateFragment, Observer {
                // Toast.makeText(requireContext(), "${it}", Toast.LENGTH_LONG).show()
                try {

                    bind.sgnFullnameTxt.setText(it.fullName)
                    fullName = it.fullName
                    description = it.description
                    userId = it.id


                    val circularProgressDrawable = CircularProgressDrawable(this@ProfileUpdateFragment)
                    circularProgressDrawable.strokeWidth = 5f
                    circularProgressDrawable.centerRadius = 30f
                    circularProgressDrawable.start()

                    if (it.avatar!!.isEmpty()) {
                        Glide.with(this@ProfileUpdateFragment)
                            .asBitmap()
                            .load(it.avatar!!)
                            .transform(CircleCrop())
                            .placeholder(R.drawable.ic_avatar)
                            .fitCenter()
                            .into(bind.avater)
                    } else {
                        Glide.with(this@ProfileUpdateFragment)
                            .asBitmap()
                            .circleCrop()
                            .load(it.avatar!!)
                            .transform(CircleCrop())
                            .placeholder(circularProgressDrawable)
                            .fitCenter()
                            .into(bind.avater)
                    }



                    bind.signDescriptionTxt.setText(it.description)

                } catch (e: Exception) { }
            })


        //val myLogo = (resources.getDrawable(com.lornamobileappsdev.my_marketplace.R.drawable.ic_avatar) as BitmapDrawable).bitmap

        //type of product spinner section........................................................................
        countrySwitch = bind.countryediSpinner
        //rSwitch_text = findViewById(R.id.rSwitch_text) as TextView
        var selecteTypedSpinner: MutableList<Int> = mutableListOf<Int>()
        val r_type_spinners = arrayListOf(
            "Select country",
            "Burkina Faso",
            "Benin",
            "Brazil",
            "Chile",
            "Cote d Ivoire",
            "Congo",
            "Cameroon",
            "D.R.Congo",
            "Ethiopia",
            "Gabon",
            "Ghana",
            "Kenya",
            "Ghana",
            "Mexico",
            "Mali",
            "Nigeria",
            "Rwanda",
            "South Africa",
            "Senegal",
            "Togo",
            "Tanzania",
            "Uganda",
            "Zambia",

            )
        countrySwitch.adapter = ArrayAdapter(
            this@ProfileUpdateFragment,
            android.R.layout.simple_spinner_dropdown_item,
            r_type_spinners
        )

        countrySwitch.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {
                bind.spinnerText.text = "Select Type =>"
            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {

                bind.spinnerText.text = r_type_spinners[position]
                selecteTypedSpinner.removeAll(selecteTypedSpinner)
                selecteTypedSpinner.add(r_type_spinners[position].length)

                if (r_type_spinners[position] == "Burkina Faso") {
                    countrySelected = "Burkina Faso"
                } else if (r_type_spinners[position] == "Benin") {
                    countrySelected = "Benin"
                } else if (r_type_spinners[position] == "Brazil") {
                    countrySelected = "Brazil"
                } else if (r_type_spinners[position] == "Chile") {
                    countrySelected = "Chile"
                } else if (r_type_spinners[position] == "Cote d Ivoire") {
                    countrySelected = "Cote d Ivoire"
                } else if (r_type_spinners[position] == "Congo") {
                    countrySelected = "Congo"
                } else if (r_type_spinners[position] == "Cameroon") {
                    countrySelected = "Cameroon"
                } else if (r_type_spinners[position] == "D.R.Congo") {
                    countrySelected = "D.R.Congo"
                } else if (r_type_spinners[position] == "Ethiopia") {
                    countrySelected = "Ethiopia"
                } else if (r_type_spinners[position] == "Gabon") {
                    countrySelected = "Gabon"
                } else if (r_type_spinners[position] == "Ghana") {
                    countrySelected = "Ghana"
                } else if (r_type_spinners[position] == "Mexico") {
                    countrySelected = "Mexico"
                } else if (r_type_spinners[position] == "Mali") {
                    countrySelected = "Mali"
                } else if (r_type_spinners[position] == "Nigeria") {
                    countrySelected = "Nigeria"
                } else if (r_type_spinners[position] == "Rwanda") {
                    countrySelected = "Rwanda"
                } else if (r_type_spinners[position] == "South Africa") {
                    countrySelected = "South Africa"
                } else if (r_type_spinners[position] == "Senegal") {
                    countrySelected = "Senegal"
                } else if (r_type_spinners[position] == "Togo") {
                    countrySelected = "Togo"
                } else if (r_type_spinners[position] == "Tanzania") {
                    countrySelected = "Tanzania"
                } else if (r_type_spinners[position] == "Uganda") {
                    countrySelected = "Uganda"
                } else if (r_type_spinners[position] == "Mexico") {
                    countrySelected = "Mexico"
                } else if (r_type_spinners[position] == "Zambia") {
                    countrySelected = "Zambia"
                } else if (r_type_spinners[position] == "Select Type =>") {
                    Snackbar.make(
                        bind.sgnFullnameTxt, "Tap to select your country", Snackbar.LENGTH_LONG
                    ).show()
                    countrySelected = "Select Type"
                }
            }
        }

        bind.btnUpdate.setOnClickListener {
            if (countrySelected.isEmpty()) {
                Snackbar.make(
                    bind.sgnFullnameTxt,
                    "Country shouldn't be empty",
                    Snackbar.LENGTH_LONG
                ).show()
            } else {

                uploadToServer(
                    userId!!,
                    fullName = if (bind.sgnFullnameTxt.text.toString().isEmpty()) fullName!!
                    else bind.sgnFullnameTxt.text.toString(),
                    country = countrySelected,
                    description = if (bind.signDescriptionTxt.text.toString().isEmpty()) description!! else bind.signDescriptionTxt.text.toString()
                )
                Log.i("user1", "${userId!!} == ${description!!} == ${countrySelected}")
            }
        }
    }

    fun uploadToServer(id: Int, fullName: String, country: String, description: String) {

        if (viewModelClass.isNetworkAvailable(test_website)) {

            try {

                val id = MultipartBody.Part.createFormData("userId", "$id")
                val fullName = MultipartBody.Part.createFormData("fullName", "$fullName")
                val country = MultipartBody.Part.createFormData("country", "$country")
                val description = MultipartBody.Part.createFormData("description", "$description")

                val dialogue = Dialog(this)
                dialogue.setContentView(R.layout.activity_progress_loader_2)
                dialogue.setCancelable(true)
                dialogue.show()

                val service = RetrofitBuilder.Singleton
                val call = service.retrofitApii.updateUpdateUserDetails(id, fullName, country, description)
                call!!.enqueue(object : Callback<FileModel?> {

                    override fun onResponse(call: Call<FileModel?>, response: Response<FileModel?>) {

                        val model = response.body()

                        if (response.isSuccessful) {
                            if (model!!.status == true) {
                                dialogue.cancel()
                                val alertDialog = AlertDialog.Builder(this@ProfileUpdateFragment)
                                alertDialog.setCancelable(false)
                                alertDialog.setTitle("Profile Update")
                                alertDialog.setMessage("You have successfully submitted your details. Please login now to update records.")
                                alertDialog.setPositiveButton("Continue") { _, _ ->
                                    viewModelClass.deleteProfileDatatbase()
                                    val intent = Intent(this@ProfileUpdateFragment, SignInFragment::class.java)
                                    startActivity(intent)
                                }
                                alertDialog.show()
                            } else if (model.status == false) {
                                dialogue.cancel()
                                val alertDialog = AlertDialog.Builder(this@ProfileUpdateFragment)
                                alertDialog.setCancelable(false)
                                alertDialog.setTitle("Error")
                                alertDialog.setMessage("${model.message}.")
                                alertDialog.setPositiveButton("Continue") { _, _ ->
                                    val intent =
                                        Intent(
                                            this@ProfileUpdateFragment,
                                            ProfileUpdateFragment::class.java
                                        )
                                    startActivity(intent)
                                }
                                alertDialog.show()
                            }
                        } else if (response.code() == 500) {
                            dialogue.cancel()
                            val alertDialog = AlertDialog.Builder(this@ProfileUpdateFragment)
                            alertDialog.setCancelable(false)
                            alertDialog.setTitle("Http Error")
                            alertDialog.setMessage("Sorry we could not process your Ad, Please try again.")
                            alertDialog.setPositiveButton("Continue") { _, _ ->
                                val intent =
                                    Intent(this@ProfileUpdateFragment, ProfileUpdateFragment::class.java)
                                startActivity(intent)
                            }
                            alertDialog.show()
                        }
                    }

                    override fun onFailure(call: Call<FileModel?>, t: Throwable) {
                        dialogue.dismiss()
                        Toast.makeText(this@ProfileUpdateFragment, t.message, Toast.LENGTH_LONG).show()
                    }
                })


            }catch (e:Exception){}


        } else {
            supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, NoSignalWifiFragment()).addToBackStack(null).commit()
        }

    }

    private fun prepairFiles(partName: String, fileUri: Uri): MultipartBody.Part {
        val file = File(fileUri.path)
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, file.name, requestBody)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun isPhotoPickerAvailable(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            true
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            SdkExtensions.getExtensionVersion(Build.VERSION_CODES.R) >= 2
        } else {
            false
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun handlePhotoPickerLaunch() {
        if (isPhotoPickerAvailable()) {
            // To launch the system photo picker, invoke an intent that includes the
            // ACTION_PICK_IMAGES action. Consider adding support for the
            // EXTRA_PICK_IMAGES_MAX intent extra.
        } else {
            // Consider implementing fallback functionality so that users can still
            // select images and videos.
        }
    }

    fun submit(userId: Int, signUpDAta: SignUpDAta) {
        try {
            bind.progressBarUpdatingUser.visibility = View.VISIBLE
        } catch (e: Exception) {
        }
        try {

            val okHttpClient = OkHttpClient().newBuilder()
                .connectTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES)
                .writeTimeout(3, TimeUnit.MINUTES)
                .build()

            val retrofit: Retrofit.Builder by lazy {
                Retrofit.Builder().baseUrl("https://lovicmarketplace.up.railway.app/")
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
            }
            val retrofitApi: Api by lazy {
                retrofit.client(okHttpClient)
                retrofit.build().create(Api::class.java)
            }
            val apiClient = ApiClient(retrofitApi)

            apiClient.updateProfileDetails(userId, signUpDAta)
                .enqueue(object : Callback<List<SignupResponses>> {
                    override fun onResponse(
                        call: Call<List<SignupResponses>>, response: Response<List<SignupResponses>>
                    ) {
                        val mResponse = response.body()
                        Log.i("account created", mResponse.toString())
                        //Toast.makeText(requireContext(), mResponse.toString(), Toast.LENGTH_LONG).show()
                        Log.i("account created", response.code().toString())
                        if (response.isSuccessful) {
                            if (response.code() == 200) {
                                viewModelClass.deleteProfileDatatbase()

                                try {
                                    lifecycleScope.launchWhenStarted {
                                        delay(4000L)
                                    }
                                    bind.progressBarUpdatingUser.visibility = View.GONE
                                } catch (e: Exception) {
                                }

                                // Toast.makeText(requireContext(), b.toString(), Toast.LENGTH_LONG).show()
                                try {
                                    /*val signUser = SignupResponses(
                                        id = mResponse?.get(0)!!.id,
                                        fullName = mResponse?.get(0)!!!!.fullName.toString(),
                                        email = mResponse?.get(0)!!!!.email,
                                        password = mResponse?.get(0)!!.password,
                                        country = mResponse?.get(0)!!.country,
                                        phoneNumber = mResponse?.get(0)!!.phoneNumber!!,
                                        dateAndTime = mResponse?.get(0)!!.dateAndTime,
                                        //image = resizePhoto(myLogo!!).toByteArray(50),
                                        accountStatus = mResponse?.get(0)!!.accountStatus,
                                        token = mResponse?.get(0)!!.token,
                                        verification_code = mResponse?.get(0)!!.verification_code,
                                        description = mResponse?.get(0)!!.description
                                    )*/
                                    try {
                                        Snackbar.make(
                                            bind.sgnFullnameTxt,
                                            "Account details updated successfully",
                                            Snackbar.LENGTH_LONG
                                        ).show()

                                        val intent = Intent(
                                            this@ProfileUpdateFragment,
                                            SuccessFullPageFragment::class.java
                                        )
                                        startActivity(intent)

                                    } catch (e: Exception) {
                                    }


                                } catch (e: Exception) {
                                }

                            }

                        } else if (response.code() == 500) {


                            try {
                                Snackbar.make(
                                    bind.btnUpdate,
                                    "Error Occured try again in few minute..",
                                    Snackbar.LENGTH_LONG
                                ).show()
                                val intent =
                                    Intent(this@ProfileUpdateFragment, MyProfile::class.java)
                                startActivity(intent)

                            } catch (e: Exception) {
                            }

                        }
                    }

                    override fun onFailure(call: Call<List<SignupResponses>>, t: Throwable) {
                        //dialogue.cancel()
                        try {
                            Snackbar.make(bind.sgnFullnameTxt, t.toString(), Snackbar.LENGTH_LONG)
                                .show()
                        } catch (e: Exception) {
                        }
                    }
                })
        } catch (e: Exception) {
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                try {
                    supportFragmentManager.beginTransaction().replace(android.R.id.content, MyProfile()).commit()
                } catch (e: Exception) {
                }
            }
        }
        return super.onOptionsItemSelected(item)
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

    fun Bitmap.toByteArray(quality: Int = 50): ByteArray {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, quality, stream)
        return stream.toByteArray()
    }

    fun byteArrayToBitmap(data: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(data, 0, data.size)
    }

    fun currencyFormat(amount: String): String? {
        val formatter = DecimalFormat("###,###,##0.00")
        return formatter.format(amount.toDouble())
    }

}