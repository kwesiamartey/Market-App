package com.lornamobileappsdev.my_marketplace.models

import androidx.room.ColumnInfo

data class SignUpDAta(
    val id:Int?=null,
    val fullName:String?=null,
    val email:String?=null,
    val password:String?=null,
    val country:String?=null,
    val date_and_time:String?=null,
    val age:Int?=null,
    val phoneNumber:Int?=null,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var image:ByteArray?=null,
    val accountStatus:String?=null,
    val token:String?=null


)
