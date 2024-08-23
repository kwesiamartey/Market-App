package com.lornamobileappsdev.my_marketplace.entity

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
@Keep
@Entity
data class FeaturedProductData(
    @PrimaryKey(autoGenerate = true)
    val id:Int? = null,
    val userId:Int?=null,
    val title:String?=null,
    var currenxy:String?=null,
    val price:Double?=null,
    var category:String?=null,
    val location:String?=null,
    val desc:String?=null,
    val dateAndtimme:String?=null,
    val type:String?=null,
    val image:String?=null,
    val negotiable:String?=null,
    val paid_product:String?=null,
    val pst_phone_number:String?=null,
    val subscription_type:String?=null,
    val subscription_date_start:String?=null,
    val subscription_date_due:String?=null,
    val views:String?=null,
    val ratings:Int?=null,
)
