package com.lornamobileappsdev.my_marketplace.models


data class FeaturedProductData(
    val id:Int? = null,
    val userId:Int?=null,
    val title:String?=null,
    val price:Double?=null,
    val location:String?=null,
    val desc:String?=null,
    val dateAndtimme:String?=null,
    val type:String?=null,
    val image:String?=null
)
