package com.lornamobileappsdev.my_marketplace.responses


data class FeedbackData(
    val id:Int? = null,
    val user_id:Int? = null,
    val user_name:String? = null,
    val avatar:ByteArray?=null,
    val product_id:Int? = null,
    val message:String? = null,
    val liked:Int?=null,
    val date_and_time:String? = null,
    val type:String?=null,
    val comment_id:Int?=null
)
