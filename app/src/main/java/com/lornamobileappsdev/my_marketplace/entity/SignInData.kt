package com.lornamobileappsdev.my_marketplace.entity

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
@Keep
@Entity
data class SignInData(
    @PrimaryKey(autoGenerate = true)
    val id:Int?=null,
    val email:String?=null,
    val password:String?=null
)
