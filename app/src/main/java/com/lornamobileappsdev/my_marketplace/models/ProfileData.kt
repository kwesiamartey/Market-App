package com.lornamobileappsdev.my_marketplace.models

import android.graphics.Bitmap

data class ProfileData(
    val id:Int?=null,
    val name:String?=null,
    val email:String?=null,
    val location:String?=null,
    val language:String?=null,
    val date_and_time:String?=null,
    val image:Bitmap?=null,
    val accountStatus:String?=null,
    val paid_account:String?=null,
    val subscription_type:String?=null,
    val subscription_date_start:String?=null,
    val subscription_date_due:String?=null,
)
