package com.lornamobileappsdev.my_marketplace.responses


data class FeaturedProductDataResponse(
    val id:Int? = null,
    val userId:Int?=null,
    val title:String?=null,
    var currenxy:String?=null,
    val price:Double?=null,
    val location:String?=null,
    val desc:String?=null,
    val dateAndtimme:String?=null,
    val type:String?=null,
    val image:String?=null
)
