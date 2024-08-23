package com.lornamobileappsdev.my_marketplace.ui

import android.R
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.lornamobileappsdev.my_marketplace.BuildConfig
import com.lornamobileappsdev.my_marketplace.api.Api
import com.lornamobileappsdev.my_marketplace.constant.test_website
import com.lornamobileappsdev.my_marketplace.constant.token
import com.lornamobileappsdev.my_marketplace.databinding.FragmentVerifyEmailAdressBinding
import com.lornamobileappsdev.my_marketplace.repository.ApiClient
import com.lornamobileappsdev.my_marketplace.responses.SignupResponses
import com.lornamobileappsdev.my_marketplace.useCases.Resend_code
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel
import com.lornamobileappsdev.my_marketplace.viewModels.PullingDataFromServerViewModel
import kotlinx.coroutines.delay
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class VerifyEmailAdressFragment : AppCompatActivity() {

    lateinit var _bind: FragmentVerifyEmailAdressBinding
    val bind get() = _bind

    val viewModelClass: MyViewModel by lazy {
        ViewModelProvider(this).get(MyViewModel::class.java)
    }
    val viewModelClassTwo: PullingDataFromServerViewModel by lazy {
        ViewModelProvider(this).get(PullingDataFromServerViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout for this fragment
        _bind = FragmentVerifyEmailAdressBinding.inflate(layoutInflater)
        setContentView(bind.root)

        val window: Window = this.window

        // clear FLAG_TRANSLUCENT_STATUS flag:

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        bind.btnValidate.setOnClickListener {

            if (viewModelClass.isNetworkAvailable(test_website)) {

                try {


                    val code = bind.vfyEmailEdt.text.toString()
                    bind.vfyProgressBar.visibility = View.VISIBLE
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
                    apiClient.verifyUser(code).enqueue(object : Callback<SignupResponses> {
                        override fun onResponse(call: Call<SignupResponses>, response: Response<SignupResponses> ) {
                            val mResponse = response.body()

                            if (response.isSuccessful) {
                                Log.i("verify", mResponse.toString())
                                viewModelClass.deleteProfileDatatbase()

                                if(response.code() == 200){
                                    /*val signUser = com.yupee.yupee.entity.SignupResponses(
                                        id = mResponse!!.id,
                                        fullName =mResponse!!.fullName.toString(),
                                        email =mResponse!!.email,
                                        password =mResponse.password,
                                        country =mResponse.country,
                                        phoneNumber = mResponse.phoneNumber!!,
                                        dateAndTime = mResponse.date_and_time,
                                        avatar = mResponse.avatar,
                                        accountStatus =mResponse.accountStatus,
                                        token = mResponse.token,
                                        verification_code = mResponse.verification_code,
                                        description = mResponse.description
                                    )
                                    lifecycleScope.launchWhenStarted { viewModelClass.storedUserDetails(signUser) }
                                    lifecycleScope.launchWhenStarted { delay(3000L) }*/


                                    val intent = Intent(this@VerifyEmailAdressFragment, SuccessFullPageFragment::class.java)
                                    startActivity(intent)
                                    bind.vfyProgressBar.visibility = View.GONE

                                }
                            } else if(response.code() == 500) {
                                Snackbar.make(
                                    bind.imageView3,
                                    "Error found, Try again",
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<SignupResponses>, t: Throwable) {
                            Snackbar.make(
                                bind.imageView3,
                                t.message.toString(),
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    })

                }catch (e:Exception){}


            } else {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.content, NoSignalWifiFragment()).addToBackStack(null).commit()
            }

        }

        bind.vfyBack.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.content, DashboardFragment()).addToBackStack(null).commit()
        }

        bind.btnResendCode.setOnClickListener {
            lifecycleScope.launchWhenStarted {
                viewModelClass.email.observe(this@VerifyEmailAdressFragment, Observer {
                    lifecycleScope.launchWhenStarted {
                        resent_code(it)
                    }
                })
            }
        }

    }

    override fun onBackPressed() {
        //Todo
    }

    suspend fun resent_code(email:String){
        delay(1000L)
        bind.txtCountdown.visibility = View.VISIBLE
        val d =  Resend_code.resend_code(email)
        if(d == true){
            bind.txtCountdown.visibility = View.GONE
             Snackbar.make(bind.imageView3, "OTP has been sent successfully check your email", Snackbar.LENGTH_LONG).show()
        }else{
            bind.txtCountdown.visibility = View.GONE
            Snackbar.make(bind.imageView3, "Error occured try again..", Snackbar.LENGTH_LONG).show()

        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)

        outState.putString("verifyfragment", "verifyfragment")
    }

}