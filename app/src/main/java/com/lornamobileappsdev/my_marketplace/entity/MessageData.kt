package com.lornamobileappsdev.my_marketplace.entity

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
@Keep
@Entity
data class MessageData(
    @PrimaryKey(autoGenerate = true)
    val id:Int?=null,
    val titled:String?=null,
    val msg:String?=null,
    val dateTIme:String?=null,
    val avatar:String?=null
)
