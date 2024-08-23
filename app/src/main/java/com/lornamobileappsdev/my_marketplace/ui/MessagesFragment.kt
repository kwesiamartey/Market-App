package com.lornamobileappsdev.my_marketplace.ui

import android.app.AlertDialog
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.lornamobileappsdev.my_marketplace.R
import com.lornamobileappsdev.my_marketplace.adapter.MessagesListAdapter
import com.lornamobileappsdev.my_marketplace.api.Api
import com.lornamobileappsdev.my_marketplace.constant.test_website
import com.lornamobileappsdev.my_marketplace.constant.token
import com.lornamobileappsdev.my_marketplace.databinding.FragmentMessagesBinding
import com.lornamobileappsdev.my_marketplace.repository.ApiClient
import com.lornamobileappsdev.my_marketplace.responses.ChatDataResponse
import com.lornamobileappsdev.my_marketplace.useCases.MyMessagesList
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class MessagesFragment : AppCompatActivity() {

    lateinit var _bind: FragmentMessagesBinding
    val bind get() = _bind

    val msgData = mutableListOf<ChatDataResponse>()

    val viewModelClass: MyViewModel by lazy {
        ViewModelProvider(this).get(MyViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout for this fragment
        _bind = FragmentMessagesBinding.inflate(layoutInflater)

        setContentView(bind.root)

        bind.noMessagesBanner.visibility = View.GONE

        supportActionBar!!.hide()

        val window: Window = this.window

        // clear FLAG_TRANSLUCENT_STATUS flag:

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this@MessagesFragment, android.R.color.black)

        val layoutManager = LinearLayoutManager(this@MessagesFragment)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        layoutManager.isSmoothScrollbarEnabled = true
        bind.messageRecyclerview.layoutManager = layoutManager

        lifecycleScope.launchWhenStarted {

            viewModelClass.queryUserDetails().asLiveData().observe(this@MessagesFragment, Observer {

                if(it == null){
                    bind.noMessagesBanner.visibility = View.VISIBLE
                }else{
                    CoroutineScope(Dispatchers.IO).launch {
                        updateViewedMessagesNotification(it.id!!)
                    }

                    lifecycleScope.launchWhenStarted {
                        val message = MyMessagesList.my_messages_list(it.id!!)
                        if(message != null){
                            try {
                                Log.i("all_message_1", message.toString())
                                bind.messageShimmer.visibility = View.GONE
                                bind.messageRecyclerview.visibility = View.VISIBLE
                                val adapter = MessagesListAdapter(
                                    this@MessagesFragment,
                                    message as MutableList<ChatDataResponse>,
                                    this@MessagesFragment,
                                    viewModelClass,
                                    it.id!!.toInt()
                                )
                                bind.messageRecyclerview.adapter = adapter
                                bind.noMessagesBanner.visibility = View.GONE
                            }catch (e:Exception){}
                        }else{
                            try {
                                bind.noMessagesBanner.visibility = View.VISIBLE
                            }catch (e:Exception){}
                        }
                    }
                }
            })
        }

        bind.bk.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(android.R.id.content, DashboardFragment()).commit()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                supportFragmentManager.beginTransaction()
                    .replace(android.R.id.content, DashboardFragment()).addToBackStack(null).commit()
            }
            R.id.logout -> {
                val alertDialog = AlertDialog.Builder(this@MessagesFragment)
                alertDialog.setTitle("Logout")
                alertDialog.setMessage("Are you sure you want to logout?")
                alertDialog.setNegativeButton("Cancel") { _, _ ->

                }
                alertDialog.setPositiveButton("Logout") { _, _ ->
                    viewModelClass.deleteProfileDatatbase()
                    viewModelClass.deleteAllproductDatatbase()
                    viewModelClass.deleterecentlyviewdata()
                    viewModelClass.deleteMySavedProductAll()
                    /*  val intent = Intent(requireContext(), MainActivity::class.java)
                     startActivity(intent)*/
                    this@MessagesFragment.finish()
                    this@MessagesFragment.finishAffinity()
                }

                alertDialog.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    suspend fun updateViewedMessagesNotification(id:Int) {
        delay(1000L)
        if (viewModelClass.isNetworkAvailable(test_website)) {
            try {
                try {
                    Log.i("id_calling", id.toString())
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
                        Retrofit.Builder().baseUrl("https://lovicmarketplace.up.railway.app/")
                            .client(okHttpClient)
                            .addConverterFactory(GsonConverterFactory.create())
                    }
                    val retrofitApi: Api by lazy {
                        retrofit.client(okHttpClient)
                        retrofit.build().create(Api::class.java)
                    }
                    val apiClient = ApiClient(retrofitApi)
                    val client = apiClient.api.updateViewedMessagesNotification(id)
                    client.enqueue(object : Callback<Boolean> {
                        override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                            if (response.isSuccessful) {
                                if (response.code() == 200) {
                                    Log.i("retrofit",  response.body()!!.toString())
                                } else {
                                    Log.i("retrofit",  response.body()!!.toString())
                                }
                            } else { }
                        }

                        override fun onFailure(call: Call<Boolean>, t: Throwable) {
                            Log.i("retrofit",  t.message!!.toString())
                        }

                    })

                } catch (e: Exception) { }


            }catch (e:Exception){}


        } else {
            supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, NoSignalWifiFragment()).addToBackStack(null).commit()
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putString("messaging", "messaging")
    }
}
