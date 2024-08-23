package com.lornamobileappsdev.my_marketplace.entity

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
@Keep
@Entity
data class SendUsersMessageData(
    @PrimaryKey(autoGenerate = true)
    val id:Int?=null,
    val image:String?=null,
    val text:String?=null,
    val message:String?=null,
    val dateAndtime:String?=null,
    val url:String?=null
)
