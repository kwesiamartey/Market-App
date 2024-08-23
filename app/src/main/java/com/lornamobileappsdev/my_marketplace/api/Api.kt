package com.lornamobileappsdev.my_marketplace.api


import com.example.model.UpdateMyAvator
import com.lornamobileappsdev.my_marketplace.constant.server_folder
import com.lornamobileappsdev.my_marketplace.entity.*
import com.lornamobileappsdev.my_marketplace.entity.ChatData
import com.lornamobileappsdev.my_marketplace.entity.ProductData
import com.lornamobileappsdev.my_marketplace.entity.SignInData
import com.lornamobileappsdev.my_marketplace.entity.SignUpDAta
import com.lornamobileappsdev.my_marketplace.models.*
import com.lornamobileappsdev.my_marketplace.responses.*
import com.lornamobileappsdev.my_marketplace.responses.PostProductResponse
import com.lornamobileappsdev.my_marketplace.responses.SignupResponses
import com.lornamobileappsdev.my_marketplace.utils.FileModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface Api {
    @GET("/category/category_list.php")
    suspend fun get_cate_list():Response<MutableList<CategoryList>>
    @GET("${server_folder}/usa_country_list.php")
    suspend fun get_town_list():Response<List<ListOfCountries>>
    @GET("countries/countries.php")
    suspend fun get_countries_list():Response<List<ListOfCountries>>
    @GET("${server_folder}/query_product_list.php")
    suspend fun queryProductionListData():Response<List<PostProductResponse>>
    @GET("${server_folder}/news_two.php")
    suspend fun getDataTwo(): Response<List<IResponse>>
    @GET("${server_folder}/news.php")
    suspend fun getData(): Response<List<IResponse>>
    @GET("${server_folder}/featured_slider.php")
    suspend fun getFeaturedSliderData():Response<List<FeaturedSliderDataResponse>>
    @POST("login")
    fun login(@Body signInData: SignInData): Call<SignupResponses>
    @POST("signup")
    fun signUp(@Body signUpDAta: SignUpDAta): Call<SignupResponses>
    @POST("users/product")
    suspend fun getUserProductsDetails(@Body user_id:Int) : Call<SignupResponses>
    @POST("verify_user/{code}")
    fun userVerification(@Path("code") code:String): Call<SignupResponses>
    @GET("find_user/{id}")
    suspend fun getUser(@Path("id") id:Int): Response<SignupResponses>
    @POST("post/{userId}/{username}")
    fun postProduct(
        @Path("userId") userId:Int,
        @Path("username") userName:String,
        @Body productData:PostingItemData
    ): Call<Boolean>
    @POST("upload_image")
    fun updateMyAvator(
        @Body updateMyAvator: UpdateMyAvator
    ): Call<Boolean>
    @PUT("product/{id}")
    fun updateProductDetails(
        @Path("id") userId:Int,
        @Body productData: ProductData
    ) : Call<PostProductResponse>
    @PUT("product_with_image/{id}")
    fun updateProductDetailsWithImages(
        @Path("id") id:Int,
        @Body productData: ProductData
    ) : Call<Boolean>
    @PUT("update_profile/{id}")
    fun updateProfileDetails(
        @Path("mineid") userId:Int,
        @Body signUpDAta: SignUpDAta
    ) : Call<List<SignupResponses>>
    @PUT("change_password/{id}/{old_password}/{new_password}")
    fun updateUserPasswordDetails(
        @Path("id") userId:Int,
        @Path("old_password") old_password:String,
        @Path("new_password") new_password:String,
    ) : Call<Boolean>
    @PUT("change_login_forgot_password/{email}/{reset_password}/{new_password}")
    fun updateLoginForgotPassword(
        @Path("email") userId:String,
        @Path("reset_password") old_password:String,
        @Path("new_password") new_password:String,
    ) : Call<Boolean>
    @PUT("change_email/{id}/{email}")
    fun changesEmail(
        @Path("id") userId:Int,
        @Path("email") email:String,
    ) : Call<Boolean>
    @POST("updateNumberViewsProductList/{view_number}/{post_id}")
    fun updateNumberViewsProductList(
        @Path("view_number") view_number:Int,
        @Path("post_id") post_id:Int,
    ) : Call<Boolean>
    @POST("/rateProductItem/{user_id}/{product_id}/{rates}/{date_and_time}")
    fun updateRatingsToServer(
        @Path("user_id") user_id:Int,
        @Path("product_id") product_id:Int,
        @Path("rates") rates:Int,
        @Path("date_and_time") date_and_time:String,
    ) : Call<Boolean>
    @GET("rate/{product_id}/{user_id}")
    suspend fun ratingsOfProduct(
        @Path("product_id") product_id:Int,
        @Path("user_id") user_id:Int
    ) : Response<List<RatesData>>
    @POST("/update_viewed_messages_notification/{id}")
    fun updateViewedMessagesNotification(
        @Path("id") id:Int,
    ) : Call<Boolean>
    @GET("user/{email}")
    suspend fun findEmail(@Path("email") email: String) : Response<Boolean>
    @GET("resend_code/{email}")
    suspend fun resend_code(@Path("email") email: String) : Response<Boolean>
    @GET("get_products")
    suspend fun getProductlist():Response<List<PostProductResponse>>
    @GET("allproducts")
    suspend fun getAllProductlist():Response<List<PostProductResponse>>
    @GET("/databaseSize")
    suspend fun getCloudDatasize():Response<Int>
    @GET("allproducts")
    suspend fun getAllProductlistthisIsForUpdateWhenSwipeRefresh():Response<List<PostProductResponse>>
    @GET("user/products/{status}/{id}")
    suspend fun getuserAllProductlist(@Path("status") status:String, @Path("id") id:Int):Response<List<PostProductResponse>>
    @GET("featured/products/{status}/{category}")
    suspend fun getFeaturedProduct(@Path("status") status:String, @Path("category") category:String):Response<MutableList<PostProductResponse>>
    @GET("products/{status}/{id}")
    suspend fun searchProduct(@Path("status") status:String, @Path("id") id:Int):Response<List<PostProductResponse>>
    @GET("search_with_name_products/{status}/{title}")
    suspend fun searchProductWithName(@Path("status") status:String, @Path("title") title:String):Response<List<com.lornamobileappsdev.my_marketplace.entity.PostProductResponse>>
    @GET("country_list")
    suspend fun getLocationlist():Response<List<CountryListDataResponse>>
    @GET("news")
    suspend fun getNews():Response<NewsDataResponse>
    @GET("feautured_slider")
    suspend fun feautured_slider():Response<List<FeaturedSliderDataResponse>>
    @POST("chats_messages/{id}")
    fun chat(
        @Path("id") userId:Int,
        @Body chatData: ChatData,
    ) : Call<ChatDataResponse>

    @POST("comments/{product_id}")
    fun comment(
        @Path("product_id") product_id:Int,
        @Body commentData: CommentData,
    ) : Call<Boolean>

    @POST("reply_comments/{product_id}")
    fun reply(
        @Path("product_id") userId:Int,
        @Body replyData:ReplyData,
    ) : Call<Boolean>

    @GET("my_messages_list/{staoredUserId}")
    suspend fun my_messages_list(
        @Path("staoredUserId") staoredUserId:Int
    ) : Response<List<ChatDataResponse>>

    @GET("message/{chat_id}")
    suspend fun messages_list(@Path("chat_id") product_id:Int ) : Response<List<MessagingChatData>>

    @GET("feedback/{product_id}")
    suspend fun getProductFeedback(@Path("product_id") product_id:Int) : Response<List<FeedbackData>>

    @POST("/comments_likes/{comment_id}/{user_id}/{comment_user_id}/{my_id}/{like}")
    suspend fun comment_like(
        @Path("comment_id") comment_id:Int,
        @Path("user_id") user_id:Int,
        @Path("comment_user_id") comment_user_id:Int,
        @Path("my_id") my_id:Int,
        @Path("like") like:Int,
    ) : Response<Long>
    @POST("/reply_likes/{comment_id}/{user_id}/{comment_user_id}/{my_id}/{like}")
    suspend fun reply_like(
        @Path("comment_id") comment_id:Int,
        @Path("user_id") user_id:Int,
        @Path("comment_user_id") comment_user_id:Int,
        @Path("my_id") my_id:Int,
        @Path("like") like:Int,
    ) : Response<Long>
    @Multipart
    @POST("${server_folder}/update_multi_upload.php")
    fun posting(
        @Part("user_id") user_id: RequestBody?,
        @Part("user_name") user_name:RequestBody?,
        @Part("title") title:RequestBody?,
        @Part("currenxy") currenxy:RequestBody?,
        @Part("price") price: RequestBody?,
        @Part("category") category:RequestBody?,
        @Part("country") country:RequestBody?,
        @Part("location") location:RequestBody?,
        @Part("desc") desc:RequestBody?,
        @Part("dateAndtimme") dateAndtimme:RequestBody?,
        @Part("type") type:RequestBody?,
        @Part("negotiation") negotiation:RequestBody?,
        @Part image: List<MultipartBody.Part?>?,
        @Part("paid_product") paid_product:RequestBody?,
        @Part("pst_phone_number") pst_phone_number:RequestBody?,
        @Part("subscription_type") subscription_type:RequestBody?,
        @Part("subscription_date_start") subscription_date_start:RequestBody?,
        @Part("subscription_date_due") subscription_date_due:RequestBody?,
        @Part("views") views:RequestBody?,
        @Part("approval_status") approval_status:RequestBody?,
        @Part("rates") rates: RequestBody?
    ) : Call<String>
    @Multipart
    @POST("${server_folder}/update_product.php")
    fun UpdatingPosting(
        @Part("id") id: RequestBody?,
        @Part("user_id") user_id: RequestBody?,
        @Part("user_name") user_name:RequestBody?,
        @Part("title") title:RequestBody?,
        @Part("currenxy") currenxy:RequestBody?,
        @Part("price") price: RequestBody?,
        @Part("category") category:RequestBody?,
        @Part("country") country:RequestBody?,
        @Part("location") location:RequestBody?,
        @Part("desc") desc:RequestBody?,
        @Part("dateAndtimme") dateAndtimme:RequestBody?,
        @Part("type") type:RequestBody?,
        @Part("negotiation") negotiation:RequestBody?,
        @Part image: List<MultipartBody.Part?>?,
        @Part("paid_product") paid_product:RequestBody?,
        @Part("pst_phone_number") pst_phone_number:RequestBody?,
        @Part("subscription_type") subscription_type:RequestBody?,
        @Part("subscription_date_start") subscription_date_start:RequestBody?,
        @Part("subscription_date_due") subscription_date_due:RequestBody?,
        @Part("views") views:RequestBody?,
        @Part("approval_status") approval_status:RequestBody?,
        @Part("rates") rates: RequestBody?
    ) : Call<FileModel>
    @GET("${server_folder}/get_images.php")
    fun getProductPhp(
        @Path("userId") userId:Int,
    ): Call<List<PostProductResponse>>
    @GET("/comment_likes_count/{post_id}/{comment_id}/{user_id}")
    suspend fun commentLikesCounts(
        @Path("post_id") post_id:Int,
        @Path("comment_id") comment_id:Int,
        @Path("user_id") user_id:Int,
    ) : Response<List<PostLikesData>>
    @GET("/reply_likes_count/{post_id}/{comment_id}/{user_id}")
    suspend fun replyLikesCounts(
        @Path("post_id") post_id:Int,
        @Path("comment_id") comment_id:Int,
        @Path("user_id") user_id:Int,
    ) : Response<List<PostLikesData>>
}