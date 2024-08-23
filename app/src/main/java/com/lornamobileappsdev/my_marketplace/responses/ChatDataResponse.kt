package com.lornamobileappsdev.my_marketplace.responses


data class ChatDataResponse(
    val id:Int? = null,
    val user_id:Int? = null,
    val other_id:Int? = null,
    val user_name:String? = null,
    val other_users_name:String?=null,
    val product_owner_image:ByteArray?=null,
    val product_id:Int? = null,
    val product_name:String? = null,
    val product_price:String?=null,
    val message:String? = null,
    val viewed:String? = null,
    val date_and_time:String? = null,
)
