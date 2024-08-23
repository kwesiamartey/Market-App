package com.lornamobileappsdev.my_marketplace.ui

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.lornamobileappsdev.my_marketplace.R
import com.lornamobileappsdev.my_marketplace.api.Api
import com.lornamobileappsdev.my_marketplace.constant.test_website
import com.lornamobileappsdev.my_marketplace.databinding.FragmentLoginBinding
import com.lornamobileappsdev.my_marketplace.entity.SignInData
import com.lornamobileappsdev.my_marketplace.repository.ApiClient
import com.lornamobileappsdev.my_marketplace.responses.SignupResponses
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel
import kotlinx.coroutines.delay
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class LoginFragment : AppCompatActivity() {

    lateinit var _bind:FragmentLoginBinding

    val bind get() = _bind

    val viewModelClass: MyViewModel by lazy {
        ViewModelProvider(this).get(MyViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout for this fragment
        _bind = FragmentLoginBinding.inflate(layoutInflater)

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
        window.statusBarColor = ContextCompat.getColor(this@LoginFragment, android.R.color.black)

        bind.sEye.setOnClickListener {
            if(bind.sEye.text.toString().equals("Show")){
                bind.sgnPasswordTxt.transformationMethod = HideReturnsTransformationMethod.getInstance()
                bind.sEye.text = "Hide"
            }else{
                bind.sgnPasswordTxt. transformationMethod = PasswordTransformationMethod.getInstance()
                bind.sEye.text = "Show"
            }
        }
        bind.create.setOnClickListener {
            try {
                val intent = Intent(this@LoginFragment, SignUpFragment::class.java)
                startActivity(intent)
            }catch (e:Exception){}
        }
        bind.btnSignin.setOnClickListener {

            if (viewModelClass.isNetworkAvailable(test_website)) {

                try {

                    try {
                        val dialogue = Dialog(this@LoginFragment)
                        dialogue.setContentView(R.layout.activity_progress_loader_3)
                        dialogue.setCancelable(false)
                        dialogue.show()

                        viewModelClass.deleteProfileDatatbase()

                        val signIn = SignInData(
                            email = bind.sgnEmailTxt.text.toString(),
                            password = bind.sgnPasswordTxt.text.toString()
                        )

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

                        apiClient.signInUser(signIn).enqueue(object :
                            Callback<SignupResponses> {
                            override fun onResponse(
                                call: Call<SignupResponses>,
                                response: Response<SignupResponses>
                            ) {
                                val mResponse = response.body()
                                Log.i("account created", mResponse.toString())
                                //Toast.makeText(requireContext(), mResponse.toString(), Toast.LENGTH_LONG).show()
                                Log.i("account created", response.code().toString())
                                if (response.isSuccessful) {
                                    if(response.code() == 202){
                                        mResponse!!.let {
                                            val signUser = com.lornamobileappsdev.my_marketplace.entity.SignupResponses(
                                                id = mResponse.id,
                                                fullName = mResponse.fullName.toString(),
                                                email = mResponse.email,
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
                                            //Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_LONG).show()
                                            try {
                                                saveUserData(signUser)
                                            }catch (e:Exception){}


                                        }
                                        Log.i("account created", mResponse.toString())
                                        dialogue.cancel()
                                        try {
                                            val alertDialog = AlertDialog.Builder(this@LoginFragment)
                                            alertDialog.setCancelable(false)
                                            alertDialog.setTitle("Account found")
                                            alertDialog.setMessage("Welcome back ${mResponse.fullName}")
                                            alertDialog.setPositiveButton("Continue"){_ , _ ->
                                                val intent = Intent(this@LoginFragment, DashboardFragment::class.java)
                                                startActivity(intent)
                                            }
                                            alertDialog.show()
                                        }catch (e:Exception){}
                                    }

                                }else if(response.code() == 500){
                                    dialogue.cancel()
                                    val alertDialog = AlertDialog.Builder(this@LoginFragment)
                                    alertDialog.setCancelable(false)
                                    alertDialog.setTitle("User not found")
                                    alertDialog.setMessage("Username or password not found. Try again")
                                    alertDialog.setPositiveButton("Continue"){_ , _ ->

                                    }
                                    alertDialog.show()
                                }
                            }

                            override fun onFailure(call: Call<SignupResponses>, t: Throwable) {
                                dialogue.cancel()
                                Snackbar.make(bind.imageView2, t.toString(), Snackbar.LENGTH_LONG).show()
                            }
                        })

                    }catch (e:Exception){}

                }catch (e:Exception){}


            } else {
                supportFragmentManager.beginTransaction()
                    .replace(android.R.id.content, NoSignalWifiFragment()).addToBackStack(null).commit()
            }






        }
        bind.icBack.setOnClickListener {
            try {
                val intent = Intent(this@LoginFragment, DashboardFragment::class.java)
                startActivity(intent)
            }catch (e:Exception){}

        }
        bind.forgotPassword.setOnClickListener {
            try {
                val intent = Intent()
            }catch (e:Exception){}
        }

    }

    fun saveUserData(signupResponses: com.lornamobileappsdev.my_marketplace.entity.SignupResponses){
        //viewModelClass.deleteProfileDatatbase()
        lifecycleScope.launchWhenStarted {
            delay(3000L)
        }
        lifecycleScope.launchWhenStarted {
            viewModelClass.storedUserDetails(signupResponses)
        }
    }

}