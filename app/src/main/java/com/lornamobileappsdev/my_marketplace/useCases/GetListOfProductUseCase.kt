package com.lornamobileappsdev.my_marketplace.useCases


import com.lornamobileappsdev.my_marketplace.BuildConfig
import com.lornamobileappsdev.my_marketplace.api.Api
import com.lornamobileappsdev.my_marketplace.constant.token
import com.lornamobileappsdev.my_marketplace.repository.ApiClient
import com.lornamobileappsdev.my_marketplace.responses.*
import com.lornamobileappsdev.my_marketplace.singletons.Singleton
import kotlinx.coroutines.CoroutineScope

import kotlinx.coroutines.delay
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object GetAllListOfProductUseCase {
    suspend fun getAllProductApi(): List<PostProductResponse> {

        try {
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
            val request = apiClient.getAllProducts()
            if (request.isSuccessful) {
                if (request.code() == 200) {
                    delay(3000L)
                    return request.body()!!
                } else if (request.code() == 404) {
                    return null!!
                } else {
                    return null!!
                }
            }
        }catch (e:Exception){}


        return null!!
    }
}

object GetuserAllProductlist {
    suspend fun getuserAllProductlist(status: String, id: Int): List<PostProductResponse> {

        try {
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
            val request = apiClient.getuserAllProductlist(status, id)
            if (request.isSuccessful) {
                if (request.code() == 200) {
                    delay(3000L)
                    return request.body()!!
                } else if (request.code() == 404) {
                    return null!!
                } else {
                    return null!!
                }
            }

        }catch (e:Exception){}

        return null!!
    }
}

object GetFeaturedProduct {
    suspend fun getFeaturedProduct(
        status: String,
        category: String
    ): MutableList<PostProductResponse> {


        try {
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
            val request = apiClient.getFeaturedProduct(status, category)
            if (request.isSuccessful) {
                if (request.code() == 200) {
                    delay(3000L)
                    return request.body()!!
                } else if (request.code() == 404) {
                    return null!!
                } else {
                    return null!!
                }
            }
        }catch (e:Exception){}

        return null!!
    }
}

object MyMessagesList {

    suspend fun my_messages_list(
        staoredUserId:Int
    ): List<ChatDataResponse> {

        try {
            val request = Singleton.Singleton.apiClient.api.my_messages_list(staoredUserId)
            if (request.isSuccessful) {
                if (request.code() == 200) {
                    delay(3000L)
                    return request.body()!!
                } else if (request.code() == 404) {
                    return emptyList()
                } else if (request.code() == 500) {
                    return emptyList()
                } else {
                    return emptyList()
                }
            }
        }catch (e:Exception){}

        return emptyList()
    }
}

object GetProductFeedback {

    suspend fun CoroutineScope.getProductFeedback(
        product_id: Int,
    ): List<FeedbackData> {

        try {
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
            val request = apiClient.getProductFeedback(product_id)
            if (request.isSuccessful) {
                if (request.code() == 200) {
                    delay(3000L)
                    return request.body()!!
                } else if (request.code() == 404) {
                    return emptyList()
                } else if (request.code() == 500) {
                    return emptyList()
                } else {
                    return emptyList()
                }
            } else if (request.code() == 404) {
                return emptyList()
            } else if (request.code() == 500) {
                return emptyList()
            } else {
                return emptyList()
            }

        }catch (e:Exception){}




        return emptyList()
    }
}

object Messages_list {

    suspend fun messages_list(
        chat_id: Int
    ): List<MessagingChatData> {

        try {
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
            val request = apiClient.messages_list(chat_id)
            if (request.isSuccessful) {
                if (request.code() == 200) {
                    delay(3000L)
                    return request.body()!!
                } else if (request.code() == 404) {
                    return null!!
                } else if (request.code() == 500) {
                    return null!!
                } else {
                    return null!!
                }
            }
        }catch (e:Exception){}

        return null!!
    }
}

object SearchProduct {
    suspend fun searchProduct(status: String, id: Int): List<PostProductResponse> {

        try {
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
            val request = apiClient.searchProduct(status, id)
            if (request.isSuccessful) {
                if (request.code() == 200) {
                    delay(3000L)
                    return request.body()!!
                } else if (request.code() == 404) {
                    return null!!
                } else {
                    return null!!
                }
            }
        }catch (e:Exception){}

        return null!!
    }
}

/*object CommentProduct {

    suspend fun commentProduct(status: String, id: Int): List<PostProductResponse> {
        val request = Singleton.Singleton.apiClient.comment(status, id)
        if (request.isSuccessful) {
            if (request.code() == 200) {
                delay(3000L)
                return request.body()!!
            } else if (request.code() == 404) {
                return null!!
            } else {
                return null!!
            }
        }
        return null!!
    }
}*/

object SearchProductWithName {
    suspend fun searchProductWithName(status: String, title: String): List<com.lornamobileappsdev.my_marketplace.entity.PostProductResponse> {

        try {
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
            val request = apiClient.searchProductWithName(status, title)
            if (request.isSuccessful) {
                if (request.code() == 200) {
                    delay(3000L)
                    return request.body()!!
                } else if (request.code() == 404) {
                    return null!!
                }
            }
        }catch (e:Exception){}

        return emptyList()
    }
}

object GetUser {
    suspend fun getUser(userId: Int): SignupResponses {

        try {
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
            val request = apiClient.getUser(userId)
            if (request.isSuccessful) {
                if (request.code() == 200) {
                    delay(3000L)
                    return request.body()!!
                }
            }
        }catch (e:Exception){}

        return null!!
    }
}

object FindEmail {
    suspend fun findEmail(email: String): Boolean {

        try {
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
            val request = apiClient.findEmail(email)
            if (request.isSuccessful) {
                if (request.code() == 200) {
                    delay(3000L)
                    return request.body()!!
                }
            }
        }catch (e:Exception){}


        return null!!
    }
}

object Resend_code {
    suspend fun resend_code(email: String): Boolean {

        try {
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
            val request = apiClient.resend_code(email)
            if (request.isSuccessful) {
                if (request.code() == 200) {
                    delay(3000L)
                    return request.body()!!
                }
            }
        }catch (e:Exception){}

        return null!!
    }

}
