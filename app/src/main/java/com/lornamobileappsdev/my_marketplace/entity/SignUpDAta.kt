package com.lornamobileappsdev.my_marketplace.entity

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Keep
@Entity
data class SignUpDAta(
    @PrimaryKey(autoGenerate = true)
    val id:Int?=null,
    val fullName:String?=null,
    val email:String?=null,
    val password:String?=null,
    val country:String?=null,
    val date_and_time:String?=null,
    val phoneNumber:Int?=null,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var avatar:ByteArray?=null,
    val accountStatus:String?=null,
    val token:String?=null,
    val verification_code:String?=null,
    val description:String?=null,
)
