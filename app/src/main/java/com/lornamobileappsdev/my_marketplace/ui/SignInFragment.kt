package com.lornamobileappsdev.my_marketplace.ui

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.google.android.material.snackbar.Snackbar
import com.lornamobileappsdev.my_marketplace.R
import com.lornamobileappsdev.my_marketplace.constant.test_website
import com.lornamobileappsdev.my_marketplace.databinding.FragmentSignInBinding
import com.lornamobileappsdev.my_marketplace.entity.SignInData
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
import kotlin.reflect.jvm.jvmName


class SignInFragment : AppCompatActivity() {

    lateinit var _bind: FragmentSignInBinding
    val bind get() = _bind

    val code: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val fname: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val delete_account: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val viewModelClass: MyViewModel by lazy {
        ViewModelProvider(this).get(MyViewModel::class.java)
    }
    val viewModelClassTwo: PullingDataFromServerViewModel by lazy {
        ViewModelProvider(this).get(PullingDataFromServerViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _bind = FragmentSignInBinding.inflate(layoutInflater)

        setContentView(bind.root)

        supportActionBar!!.hide()
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        val window: Window = this.window

        // clear FLAG_TRANSLUCENT_STATUS flag:

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.black)

        bind.sgnEmailTxt.clearFocus()

        bind.sgnPasswordTxt.clearFocus()

        bind.sEye.setOnClickListener {
            if (bind.sEye.text.toString().equals("Show")) {
                bind.sgnPasswordTxt.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                bind.sEye.text = "Hide"
            } else {
                bind.sgnPasswordTxt.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                bind.sEye.text = "Show"
            }
        }

        bind.create.setOnClickListener {
            try {
                val intent = Intent(this, SignUpFragment::class.java)
                startActivity(intent)
            } catch (e: Exception) {
            }
        }

        bind.btnSignin.setOnClickListener {
            if (viewModelClass.isNetworkAvailable(test_website)) {
                try {
                    viewModelClass.deleteProfileDatatbase()
                    val signIn = SignInData(
                        email = bind.sgnEmailTxt.text.toString(),
                        password = bind.sgnPasswordTxt.text.toString()
                    )

                    lifecycleScope.launch {
                        signInFunc(signIn)
                    }

                } catch (e: Exception) {
                }
            } else {
                supportFragmentManager.beginTransaction()
                    .replace(android.R.id.content, NoSignalWifiFragment()).addToBackStack(null)
                    .commit()
            }
        }

        bind.icBack.setOnClickListener {
            try {
                supportFragmentManager.beginTransaction()
                    .replace(android.R.id.content, DashboardFragment()).addToBackStack(null)
                    .commit()
            } catch (e: Exception) {
            }

        }

        bind.forgotPassword.setOnClickListener {
            try {
                val intent = Intent(this@SignInFragment, ForgotPasswordFragment::class.java)
                startActivity(intent)
            } catch (e: Exception) { }
        }

        //bind.lottie.setupAnim()

    }

    fun CoroutineScope.signInFunc(signInData: SignInData) {

        try {

            val dialogue = Dialog(this@SignInFragment)
            dialogue.setContentView(R.layout.activity_progress_loader_3)
            dialogue.setCancelable(false)
            dialogue.show()

            launch {
                Singleton.Singleton.apiClient.signInUser(signInData).enqueue(object : Callback<SignupResponses> {
                    override fun onResponse(
                        call: Call<SignupResponses>,
                        response: Response<SignupResponses>
                    ) {
                        val mResponse = response.body()
                        Log.i("account created", mResponse.toString())
                        //Toast.makeText(requireContext(), mResponse.toString(), Toast.LENGTH_LONG).show()
                        Log.i("account created", response.code().toString())
                        if (response.isSuccessful) {
                            if (response.code() == 202) {
                                mResponse!!.let {
                                    if (it.accountStatus == "Verified" && it.delete_account == "0") {
                                        try {
                                            code.value = response.code().toString()
                                            fname.value = mResponse.fullName.toString()
                                            viewModelClassTwo.globalText.value =
                                                it.fullName.toString()
                                            delete_account.value =
                                                mResponse.delete_account.toString()

                                            val signUser =
                                                com.lornamobileappsdev.my_marketplace.entity.SignupResponses(
                                                    id = mResponse.id,
                                                    fullName = mResponse.fullName.toString(),
                                                    email = mResponse.email,
                                                    password = mResponse.password,
                                                    country = mResponse.country,
                                                    phoneNumber = mResponse.phoneNumber!!,
                                                    dateAndTime = mResponse.date_and_time,
                                                    avatar = mResponse.avatar,
                                                    accountStatus = mResponse.accountStatus,
                                                    token = mResponse.token,
                                                    verification_code = mResponse.verification_code,
                                                    description = mResponse.description
                                                )

                                            try {
                                                saveUserData(signUser)
                                            } catch (e: Exception) {
                                            }

                                            Log.i("account created", mResponse.toString())
                                            dialogue.cancel()

                                            lifecycleScope.launchWhenStarted {
                                                delay(1000L)
                                                supportFragmentManager.beginTransaction()
                                                    .replace(
                                                        android.R.id.content,
                                                        SuccessfulSignInFragment()
                                                    ).addToBackStack(null).commit()
                                            }

                                        } catch (e: Exception) {
                                        }

                                    } else if (it.accountStatus == "Not verified" && it.delete_account == "0") {
                                        viewModelClass.email.value = it.email
                                        //startActivity(Intent.createChooser(getEmailIntent(mResponse.email!!, "Verification Code", mResponse.verification_code), "Send mail"))
                                        val alertDialog =
                                            AlertDialog.Builder(this@SignInFragment)
                                        alertDialog.setCancelable(false)
                                        alertDialog.setTitle("Account not verified")
                                        alertDialog.setCancelable(true)
                                        alertDialog.setMessage("Your account is created successfully but not verified, Please verify your account.")
                                        alertDialog.setPositiveButton("Continue") { _, _ ->
                                            val intent = Intent(
                                                this@SignInFragment,
                                                VerifyEmailAdressFragment::class.java
                                            )
                                            startActivity(intent)
                                        }
                                        alertDialog.show()
                                    } else if (it.accountStatus == "Verified" && it.delete_account == "1") {

                                        val alertDialog =
                                            AlertDialog.Builder(this@SignInFragment)
                                        alertDialog.setCancelable(false)
                                        alertDialog.setTitle("Account deleted")
                                        alertDialog.setCancelable(true)
                                        alertDialog.setMessage("Account deleted, When you want to restore contact support by sending us email concerning account restoration(structuredapps@yahoo.com)")
                                        alertDialog.setPositiveButton("Continue") { _, _ ->

                                        }
                                        alertDialog.show()
                                    } else {

                                    }
                                }
                            }

                        } else if (response.code() == 500) {
                            dialogue.cancel()
                            val alertDialog = AlertDialog.Builder(this@SignInFragment)
                            alertDialog.setCancelable(false)
                            alertDialog.setTitle("User not found")
                            alertDialog.setMessage("Username or password not found. Try again")
                            alertDialog.setPositiveButton("Continue") { _, _ ->

                            }
                            alertDialog.show()
                        }
                    }

                    override fun onFailure(call: Call<SignupResponses>, t: Throwable) {
                        dialogue.cancel()
                        Snackbar.make(bind.imageView2, t.toString(), Snackbar.LENGTH_LONG).show()
                    }
                })
            }
        } catch (e: Exception) { }
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
        outState.putString("signin", "signin")
    }

}