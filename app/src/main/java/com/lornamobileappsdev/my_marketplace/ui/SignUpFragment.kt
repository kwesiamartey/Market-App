package com.lornamobileappsdev.my_marketplace.ui


import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Base64
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.google.android.material.snackbar.Snackbar
import com.lornamobileappsdev.my_marketplace.R
import com.lornamobileappsdev.my_marketplace.constant.test_website
import com.lornamobileappsdev.my_marketplace.databinding.FragmentSignUpBinding
import com.lornamobileappsdev.my_marketplace.entity.SignUpDAta
import com.lornamobileappsdev.my_marketplace.responses.SignupResponses
import com.lornamobileappsdev.my_marketplace.singletons.Singleton
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel
import com.lornamobileappsdev.my_marketplace.viewModels.PullingDataFromServerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.reflect.jvm.jvmName


class SignUpFragment : AppCompatActivity() {

    lateinit var _bind: FragmentSignUpBinding
    val bind get() = _bind

    lateinit var viewModelClass: MyViewModel

    lateinit var viewModelClassTwo: PullingDataFromServerViewModel

    lateinit var pst_RegopmSwitch: Spinner
    var pst_regionSelected: String = ""

    var list = kotlin.random.Random

    var countryName: String = ""

    val r_code_rgion_spinners = arrayListOf<String>("Select Country")

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout for this fragment
        _bind = FragmentSignUpBinding.inflate(layoutInflater)

        setContentView(bind.root)


        viewModelClass = ViewModelProvider(this)[MyViewModel::class.java]

        viewModelClassTwo = ViewModelProvider(this)[PullingDataFromServerViewModel::class.java]


        supportActionBar!!.hide()
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        viewModelClass.countriesNames.observe(this@SignUpFragment, androidx.lifecycle.Observer {
            it.forEach {
                r_code_rgion_spinners.add(it.name)
            }
        })

        try{
            Handler(Looper.myLooper()!!).postDelayed({
                bind.townProgrBar.visibility = View.GONE
            }, 9000L)
        }catch (e:Exception){}


        val window: Window = this.window

        // clear FLAG_TRANSLUCENT_STATUS flag:
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this@SignUpFragment, android.R.color.black)

        pst_RegopmSwitch = bind.country
        //rSwitch_text = findViewById(R.id.rSwitch_text) as TextView
        var selectedREgionSpinner: MutableList<Int> = mutableListOf<Int>()

        pst_RegopmSwitch.adapter = ArrayAdapter(
            this@SignUpFragment, R.layout.axtivirty_spinner_text, r_code_rgion_spinners
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
                val selectedText = pst_RegopmSwitch.selectedItem.toString()

                if (r_code_rgion_spinners[position] == "Select Town") {
                    Toast.makeText(this@SignUpFragment, "Select Town", Toast.LENGTH_LONG).show()
                    pst_regionSelected = "Select Town"
                } else {
                    pst_regionSelected = r_code_rgion_spinners[position]
                }

            }
        }

        viewModelClass.selectedCountryName.observe(
            this@SignUpFragment,
            androidx.lifecycle.Observer {
                countryName = it
            })

        bind.pEye.setOnClickListener {
            if (bind.pEye.text.toString().equals("Show")) {
                bind.password.transformationMethod = HideReturnsTransformationMethod.getInstance()
                bind.pEye.text = "Hide"
            } else {
                bind.password.transformationMethod = PasswordTransformationMethod.getInstance()
                bind.pEye.text = "Show"
            }
        }

        val s = SimpleDateFormat("dd/MM/yyyy") //,hh:mm:ss
        val format: String = s.format(Date())
        val timeStamp = format

        val ss = SimpleDateFormat("hh:mm:ss") //,hh:mm:ss
        val format_time: String = ss.format(Date())
        val timeSt = format_time

        val myLogo = (resources.getDrawable(R.drawable.user_image_avatar) as BitmapDrawable).bitmap

        bind.btnSubmit.setOnClickListener {
            if (bind.sgnFullnameTxt.text.toString() == "") {

                Toast.makeText(
                    this@SignUpFragment,
                    "Full name must not be empty",
                    Toast.LENGTH_LONG
                ).show()
            } else if (bind.sgnupEmailTxt.text.toString() == "") {

                Toast.makeText(this@SignUpFragment, "Enter a valid email", Toast.LENGTH_LONG).show()
            } else if (bind.password.text.toString() == "") {

                Toast.makeText(
                    this@SignUpFragment,
                    "You have not set a password",
                    Toast.LENGTH_LONG
                ).show()
            } else if (bind.mobileNumber.text.toString() == "") {

                Toast.makeText(this@SignUpFragment, "Provide mobile number", Toast.LENGTH_LONG)
                    .show()
            } else if (bind.signDescriptionTxt.text.toString() == "") {

                Toast.makeText(this@SignUpFragment, "Provide description", Toast.LENGTH_LONG).show()
            } else if (pst_regionSelected.toString() == "Select country") {

                Toast.makeText(this@SignUpFragment, "Select country", Toast.LENGTH_LONG).show()
            } else {
                try {
                    val alertDialog = AlertDialog.Builder(this@SignUpFragment)
                    alertDialog.setCancelable(false)
                    alertDialog.setTitle("New Account Creation")
                    alertDialog.setMessage("Make sure you provided a valid email, We will send a verification code to your email")
                    alertDialog.setNegativeButton("Cancel") { _, _ ->

                    }

                    alertDialog.setPositiveButton("Continue") { _, _ ->
                        lifecycleScope.launchWhenStarted {
                            val signUpDAta = SignUpDAta(
                                fullName = bind.sgnFullnameTxt.text.toString(),
                                email = bind.sgnupEmailTxt.text.toString(),
                                password = bind.password.text.toString(),
                                country = pst_regionSelected,
                                date_and_time = "$timeStamp: $timeSt",
                                phoneNumber = bind.mobileNumber.text.toString().toInt(),
                                avatar = myLogo.toByteArray(50),
                                accountStatus = "Not verified",
                                verification_code = list.nextLong(654828, 859624).toString(),
                                description = bind.signDescriptionTxt.text.toString()
                            )
                            Log.i("signup", signUpDAta.toString())
                            submit(signUpDAta)
                        }
                    }
                    alertDialog.show()

                } catch (e: Exception) {
                }

            }
        }

        bind.txtSignIn.setOnClickListener {
            val intent = Intent(this@SignUpFragment, SignInFragment::class.java)
            startActivity(intent)
        }

        bind.icBback.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, DashboardFragment()).addToBackStack(null).commit()
        }

        //bind.lottie.setupAnim()

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
        } catch (ex: Exception) {
        }
    }


    fun CoroutineScope.submit(signUpDAta: SignUpDAta) {
        launch {
            if (viewModelClass.isNetworkAvailable(test_website)) {

                try {
                    bind.progressBarSignup.visibility = View.VISIBLE
                    viewModelClass.deleteProfileDatatbase()

                    Singleton.Singleton.apiClient.singUpUser(signUpDAta).enqueue(object :
                        Callback<SignupResponses> {
                        override fun onResponse(
                            call: Call<SignupResponses>,
                            response: Response<SignupResponses>
                        ) {
                            val mResponse = response.body()
                            Log.i("SignupLog", mResponse.toString())
                            if (response.isSuccessful) {
                                if (response.code() == 200) {
                                    try {
                                        bind.progressBarSignup.visibility = View.VISIBLE
                                    } catch (e: Exception) {
                                    }
                                    if (mResponse!!.email == signUpDAta.email && mResponse.token == null && mResponse.verification_code!! != null && mResponse.accountStatus != "Not verified") {
                                        val signUser =
                                            com.lornamobileappsdev.my_marketplace.entity.SignupResponses(
                                                id = mResponse.id,
                                                fullName = mResponse.fullName.toString(),
                                                email = mResponse.email,
                                                password = mResponse.password,
                                                country = mResponse.country,
                                                dateAndTime = mResponse.date_and_time,
                                                phoneNumber = mResponse.phoneNumber!!,
                                                avatar = mResponse.avatar,
                                                accountStatus = mResponse.accountStatus,
                                                token = mResponse.token,
                                                verification_code = mResponse.verification_code,
                                                description = mResponse.description
                                            )
                                        lifecycleScope.launchWhenStarted {
                                            saveUserData(signUser)
                                        }

                                        val alertDialog = AlertDialog.Builder(this@SignUpFragment)
                                        alertDialog.setCancelable(false)
                                        alertDialog.setTitle("Welcome back")
                                        alertDialog.setMessage("You have an account already please login")
                                        alertDialog.setPositiveButton("Continue") { _, _ ->
                                            val intent =
                                                Intent(
                                                    this@SignUpFragment,
                                                    SignInFragment::class.java
                                                )
                                            startActivity(intent)
                                        }
                                        alertDialog.show()
                                    } else if (mResponse.accountStatus == "Not verified") {

                                        //startActivity(Intent.createChooser(getEmailIntent(mResponse.email!!, "Verification Code", mResponse.verification_code), "Send mail"))
                                        val alertDialog = AlertDialog.Builder(this@SignUpFragment)
                                        alertDialog.setCancelable(false)
                                        alertDialog.setTitle("New Account")
                                        alertDialog.setMessage("Your account is created successfully, Please verify your account.")
                                        alertDialog.setPositiveButton("Continue") { _, _ ->
                                            val intent = Intent(
                                                this@SignUpFragment,
                                                VerifyEmailAdressFragment::class.java
                                            )
                                            startActivity(intent)
                                        }
                                        alertDialog.show()
                                    } else if (mResponse == null) {

                                        //startActivity(Intent.createChooser(getEmailIntent(mResponse.email!!, "Verification Code", mResponse.verification_code), "Send mail"))
                                        val alertDialog = AlertDialog.Builder(this@SignUpFragment)
                                        alertDialog.setCancelable(false)
                                        alertDialog.setTitle("Error Occurred")
                                        alertDialog.setMessage("We are sorry an error occurred, Please check your internet connection and make sure you provide the right information's.Please try again.")
                                        alertDialog.setPositiveButton("Continue") { _, _ ->
                                            val intent = Intent(
                                                this@SignUpFragment,
                                                VerifyEmailAdressFragment::class.java
                                            )
                                            startActivity(intent)
                                        }
                                        alertDialog.show()
                                    }

                                }
                            } else if (response.code() == 500) {
                                try {
                                    bind.progressBarSignup.visibility = View.GONE
                                } catch (e: Exception) {
                                }
                                val alertDialog = AlertDialog.Builder(this@SignUpFragment)
                                alertDialog.setCancelable(false)
                                alertDialog.setTitle("ERROR")
                                alertDialog.setMessage("ERROR signing up. Try again")
                                alertDialog.setPositiveButton("Continue") { _, _ ->
                                    val intent =
                                        Intent(this@SignUpFragment, SignUpFragment::class.java)
                                    startActivity(intent)
                                }
                                alertDialog.show()
                            } else if (response.code() == 400) {
                                try {
                                    bind.progressBarSignup.visibility = View.GONE
                                } catch (e: Exception) {
                                }
                                val alertDialog = AlertDialog.Builder(this@SignUpFragment)
                                alertDialog.setCancelable(false)
                                alertDialog.setTitle("ERROR")
                                alertDialog.setMessage("ERROR signing up. Try again")
                                alertDialog.setPositiveButton("Continue") { _, _ ->
                                    val intent =
                                        Intent(this@SignUpFragment, SignUpFragment::class.java)
                                    startActivity(intent)
                                }
                                alertDialog.show()
                            }
                        }

                        override fun onFailure(call: Call<SignupResponses>, t: Throwable) {
                            //dialogue.cancel()
                            Snackbar.make(bind.textView3, t.toString(), Snackbar.LENGTH_LONG).show()
                        }
                    })


                } catch (e: Exception) {
                }
            } else {
                supportFragmentManager.beginTransaction()
                    .replace(android.R.id.content, NoSignalWifiFragment()).addToBackStack(null)
                    .commit()
            }
        }
    }

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

    private fun getEmailIntent(email: String, subject: String?, content: String?): Intent? {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "message/rfc822"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, content)
        return getIntent(intent, true)
    }

    private fun getIntent(intent: Intent, isNewTask: Boolean): Intent {
        return if (isNewTask) intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) else intent
    }

    fun saveUserData(signupResponses: com.lornamobileappsdev.my_marketplace.entity.SignupResponses) {
        //viewModelClass.deleteProfileDatatbase()
        lifecycleScope.launchWhenStarted {
            delay(3000L)

        }
        lifecycleScope.launchWhenStarted {
            viewModelClass.storedUserDetails(signupResponses)
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putString("signup", "signup")
    }


  /*  suspend fun getlistofTown(){
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
                            .addHeader("Authorization", "Bearer $token")
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
                if (rees.isNotEmpty()){
                    rees.forEach {
                        r_code_rgion_spinners.add(it.name)
                        Log.i("town_list", it.toString())

                    }

                }
            }
        }catch (e:Exception){ }
    }*/
}