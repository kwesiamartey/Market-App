package com.example.imageproject

import com.lornamobileappsdev.my_marketplace.utils.FileModel
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface HttpService {

    @POST("uploads/update_multi_upload.php")
    fun postMultipleUploadApi(
        @Part id: MultipartBody.Part?,
        @Part user_id: MultipartBody.Part?,
        @Part user_name:MultipartBody.Part?,
        @Part title:MultipartBody.Part?,
        @Part price: MultipartBody.Part?,
        @Part category:MultipartBody.Part?,
        @Part country:MultipartBody.Part?,
        @Part location:MultipartBody.Part?,
        @Part descs:MultipartBody.Part?,
        @Part dateAndtimmes:MultipartBody.Part?,
        @Part type:MultipartBody.Part?,
        @Part negotiation:MultipartBody.Part?,
        @Part paid_product:MultipartBody.Part?,
        @Part pst_phone_number:MultipartBody.Part?,
        @Part subscription_type:MultipartBody.Part?,
        @Part subscription_date_start:MultipartBody.Part?,
        @Part subscription_date_due:MultipartBody.Part?,
        @Part views:MultipartBody.Part?,
        @Part approval_status: MultipartBody.Part?,
        @Part image: MultipartBody.Part?,
        @Part image_two: MultipartBody.Part?,
        @Part image_three: MultipartBody.Part?,
        @Part image_four: MultipartBody.Part?,
        @Part image_five: MultipartBody.Part?,
        @Part image_six: MultipartBody.Part?,


    ): Call<FileModel?>?



    @Multipart
    @POST("uploads/post_upload.php")
    fun callMultipleUploadApi(
        @Part id: MultipartBody.Part?,
        @Part user_id: MultipartBody.Part?,
        @Part user_name:MultipartBody.Part?,
        @Part title:MultipartBody.Part?,
        @Part price: MultipartBody.Part?,
        @Part category:MultipartBody.Part?,
        @Part country:MultipartBody.Part?,
        @Part location:MultipartBody.Part?,
        @Part descs:MultipartBody.Part?,
        @Part dateAndtimmes:MultipartBody.Part?,
        @Part type:MultipartBody.Part?,
        @Part negotiation:MultipartBody.Part?,
        @Part paid_product:MultipartBody.Part?,
        @Part pst_phone_number:MultipartBody.Part?,
        @Part subscription_type:MultipartBody.Part?,
        @Part subscription_date_start:MultipartBody.Part?,
        @Part subscription_date_due:MultipartBody.Part?,
        @Part views:MultipartBody.Part?,
        @Part approval_status: MultipartBody.Part?,
        @Part image: List<MultipartBody.Part?>?): Call<FileModel?>?


    @Multipart
    @POST("uploads/update_multi_upload.php")
    fun callMultipleUploadApiUpdateProducte(
        @Part id: MultipartBody.Part?,
        @Part user_id: MultipartBody.Part?,
        @Part user_name:MultipartBody.Part?,
        @Part title:MultipartBody.Part?,
        @Part price: MultipartBody.Part?,
        @Part category:MultipartBody.Part?,
        @Part country:MultipartBody.Part?,
        @Part location:MultipartBody.Part?,
        @Part descs:MultipartBody.Part?,
        @Part dateAndtimmes:MultipartBody.Part?,
        @Part type:MultipartBody.Part?,
        @Part negotiation:MultipartBody.Part?,
        @Part paid_product:MultipartBody.Part?,
        @Part pst_phone_number:MultipartBody.Part?,
        @Part subscription_type:MultipartBody.Part?,
        @Part subscription_date_start:MultipartBody.Part?,
        @Part subscription_date_due:MultipartBody.Part?,
        @Part views:MultipartBody.Part?,
        @Part approval_status: MultipartBody.Part?,
        @Part image: List<MultipartBody.Part?>?) : Call<FileModel?>?


    @Multipart
    @POST("uploads/upload_api.php")
    fun updateUpdateUserDetails(
        @Part id: MultipartBody.Part?,
        @Part fullName: MultipartBody.Part?,
        @Part country: MultipartBody.Part?,
        @Part description: MultipartBody.Part?) : Call<FileModel?>?

    @Multipart
    @POST("uploads/avatarupload.php")
    fun avatarUploadApi(
        @Part image: MultipartBody.Part?,
        @Part id: MultipartBody.Part?,
    ): Call<FileModel?>?


}