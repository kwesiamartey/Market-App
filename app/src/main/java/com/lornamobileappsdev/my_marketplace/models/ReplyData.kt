package com.lornamobileappsdev.my_marketplace.models

data class ReplyData(
    val id:Int?=null,
    val user_id:Int?=null,
    val product_id:Int?=null,
    val reply:String?=null,
    val liked:Int?=null,
    val date_and_time:String?=null,
    val comment_id:Int?=null,
    val type:String?=null
)
