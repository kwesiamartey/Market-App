package com.lornamobileappsdev.my_marketplace.useCases

import com.lornamobileappsdev.my_marketplace.BuildConfig
import com.lornamobileappsdev.my_marketplace.api.Api
import com.lornamobileappsdev.my_marketplace.constant.token
import com.lornamobileappsdev.my_marketplace.models.FeaturedSliderDataResponse
import com.lornamobileappsdev.my_marketplace.models.NewsDataResponse
import com.lornamobileappsdev.my_marketplace.repository.ApiClient
import com.lornamobileappsdev.my_marketplace.responses.CountryListDataResponse


import kotlinx.coroutines.delay
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class GetCountryLocationUseCase {

    suspend fun getLocationStation() : List<CountryListDataResponse>? {
        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(3, TimeUnit.MINUTES)
            .readTimeout(3, TimeUnit.MINUTES)
            .writeTimeout(3, TimeUnit.MINUTES)
            .addInterceptor { chain ->
                val request: Request =
                    chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $token")
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
        var request=  apiClient.getCountryList()
        val mResponse = request.body()
        if (request.isSuccessful){
            if(request.code() == 200){
                delay(3000L)
                return request.body()!!
            }
        }
        return null
    }
}


class GetNewsUseCase {
    suspend fun getGetNewsUseCase(): NewsDataResponse? {
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
        var request = apiClient.getNews()
        val mResponse = request.body()
        if (request.isSuccessful) {
            if (request.code() == 200) {
                delay(3000L)
                if(mResponse == null){
                    return null
                }else{
                    return mResponse
                }
            }
        } else if (request.code() == 500) {
            return null
        }
        return null
    }
}


object GetFeauturedSlider {
    suspend fun feauturedSlider() : List<FeaturedSliderDataResponse>? {
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
            var request=  apiClient.feautured_slider()
            val mResponse = request.body()
            if (request.isSuccessful){
                if(request.code() == 200){
                    delay(3000L)
                    return request.body()!!
                }
            }else if(request.code() == 500){
                return emptyList()
            }else if(request.code() == 404){
                return emptyList()
            }
            return null
        }
}