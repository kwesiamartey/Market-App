package com.lornamobileappsdev.my_marketplace.repository

import com.lornamobileappsdev.my_marketplace.api.Api
import com.lornamobileappsdev.my_marketplace.entity.ChatData
import com.lornamobileappsdev.my_marketplace.entity.ProductData
import com.lornamobileappsdev.my_marketplace.entity.SignInData
import com.lornamobileappsdev.my_marketplace.entity.SignUpDAta
import com.lornamobileappsdev.my_marketplace.models.*
import com.lornamobileappsdev.my_marketplace.responses.*
import com.lornamobileappsdev.my_marketplace.responses.PostProductResponse
import com.lornamobileappsdev.my_marketplace.responses.SignupResponses
import retrofit2.Call
import retrofit2.Response


class ApiClient(val api: Api) {

    fun signInUser(signInData: SignInData): Call<SignupResponses> {
        return api.login(signInData)
    }

    fun singUpUser(signUpDAta: SignUpDAta):Call<SignupResponses>{
        return api.signUp(signUpDAta)
    }

    fun postProduct(
        userId: Int,
        username: String,
        productData: PostingItemData
    ):Call<Boolean>{
        return api.postProduct(
            userId,
            username,
            productData
        )
    }

    fun updateProductDetails(user_id: Int,productData: ProductData):Call<PostProductResponse>{
        return api.updateProductDetails(user_id, productData)
    }

    fun updateProductDetailsWithImages(user_id: Int,productData: ProductData):Call<Boolean>{
        return api.updateProductDetailsWithImages(user_id, productData)
    }

    fun updateProfileDetails(user_id: Int,signUpDAta: SignUpDAta):Call<List<SignupResponses>>{
        return api.updateProfileDetails(user_id, signUpDAta)
    }

    fun updateUserPasswordDetails(user_id: Int, old_password:String, new_password:String):Call<Boolean>{
        return api.updateUserPasswordDetails(user_id, old_password, new_password)
    }

    fun updateLoginForgotPassword(email:String, reset_password:String, new_password:String):Call<Boolean>{
        return api.updateLoginForgotPassword(email, reset_password, new_password)
    }

    fun changesEmail(user_id: Int, email:String):Call<Boolean>{
        return api.changesEmail(user_id, email)
    }

    fun verifyUser(code:String):Call<SignupResponses>{
        return api.userVerification(code)
    }

    suspend fun getUser(id:Int):Response<SignupResponses>{
        return api.getUser(id)
    }

    suspend fun getCountryList():Response<List<CountryListDataResponse>>{
        return api.getLocationlist()
    }
    suspend fun getNews():Response<NewsDataResponse>{
        return api.getNews()
    }
    suspend fun feautured_slider():Response<List<FeaturedSliderDataResponse>>{
        return api.feautured_slider()
    }
    suspend fun getProducts():Response<List<PostProductResponse>>{
        return api.getProductlist()
    }
    suspend fun getAllProducts():Response<List<PostProductResponse>>{
        return api.getAllProductlist()
    }
    suspend fun getuserAllProductlist(status:String, id:Int):Response<List<PostProductResponse>>{
        return api.getuserAllProductlist(status, id)
    }
    suspend fun getFeaturedProduct(status:String, category:String):Response<MutableList<PostProductResponse>>{
        return api.getFeaturedProduct(status, category)
    }
    suspend fun getUserProductsDetails(user_id:Int):Call<SignupResponses>{
        return api.getUserProductsDetails(user_id)
    }
    suspend fun searchProduct(status:String, id:Int):Response<List<PostProductResponse>>{
        return api.searchProduct(status, id)
    }
    suspend fun searchProductWithName(status:String, title:String):Response<List<com.lornamobileappsdev.my_marketplace.entity.PostProductResponse>>{
        return api.searchProductWithName(status, title)
    }
    suspend fun findEmail(email:String):Response<Boolean>{
        return api.findEmail(email)
    }
    suspend fun resend_code(email:String):Response<Boolean>{
        return api.resend_code(email)
    }
    suspend fun chat(user_id: Int, chatData: ChatData):Call<ChatDataResponse>{
        return api.chat(user_id, chatData)
    }
    fun comment(user_id: Int, commentData: CommentData):Call<Boolean>{
        return api.comment(user_id, commentData)
    }
    fun replyData(user_id: Int, replyData: ReplyData):Call<Boolean>{
        return api.reply(user_id, replyData)
    }
    suspend fun my_messages_list(staoredUserId:Int):Response<List<ChatDataResponse>>{
        return api.my_messages_list(staoredUserId)
    }
    suspend fun messages_list(chat_id:Int ):Response<List<MessagingChatData>>{
        return api.messages_list(chat_id)
    }
    suspend fun getProductFeedback(product_id:Int) : Response<List<FeedbackData>>{
        return api.getProductFeedback(product_id)
    }


}