package com.lornamobileappsdev.my_marketplace.models

import androidx.room.PrimaryKey


class ProductData(
    @PrimaryKey
    var id:Int?=null,
    var userId:Int?=null,
    var title:String?=null,
    var price:Double?=null,
    var category:String?=null,
    var location:String?=null,
    var desc:String?=null,
    var dateAndtimme:String?=null,
    var type:String?=null,
    var negotiation:String?=null,
    val paid_product:String?=null,
    val pst_phone_number:Int?=null,
    val subscription_type:String?=null,
    val subscription_date_start:String?=null,
    val subscription_date_due:String?=null,
    val views:Int? =null
)
