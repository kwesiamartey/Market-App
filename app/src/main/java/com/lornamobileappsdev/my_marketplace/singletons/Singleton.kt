package com.lornamobileappsdev.my_marketplace.singletons

import com.google.gson.GsonBuilder
import com.lornamobileappsdev.my_marketplace.BuildConfig
import com.lornamobileappsdev.my_marketplace.api.Api
import com.lornamobileappsdev.my_marketplace.constant.token
import com.lornamobileappsdev.my_marketplace.repository.ApiClient
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit


object Singleton {

    object Singleton {
        var okHttpClient = OkHttpClient.Builder()
            .callTimeout(5,TimeUnit.MINUTES)
            .connectTimeout(5, TimeUnit.MINUTES)
            .readTimeout(4, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)
            .retryOnConnectionFailure(true)
            .addInterceptor { chain ->
                val request: Request =
                    chain.request().newBuilder()
                        .addHeader( "Authorization","Bearer ${token}")
                        .addHeader( "Content-Type","application/text")
                        .build()
                chain.proceed(request)
            } //This is used to add NetworkInterceptor.
            .protocols( Collections.singletonList(Protocol.HTTP_1_1) )
            .build()
        var gson = GsonBuilder().setLenient().create()
        val retrofit: Retrofit.Builder by lazy { // geny http://10.0.3.2:8080/  :  android http://10.0.2.2:8081/ nox 172.17.100.2:8080   https://yupee-restapi.herokuapp.com
            Retrofit.Builder().baseUrl(BuildConfig.API_URL)
            .addConverterFactory(ScalarsConverterFactory.create()) //important
            .addConverterFactory(GsonConverterFactory.create(gson))
        }
        val retrofitApi: Api by lazy {
            retrofit.client(okHttpClient)
            retrofit.build()
                .create(Api::class.java)
        }
        val apiClient = ApiClient(retrofitApi)

    }
}
