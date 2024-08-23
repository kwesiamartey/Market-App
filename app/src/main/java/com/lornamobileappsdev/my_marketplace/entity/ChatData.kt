package com.lornamobileappsdev.my_marketplace.entity

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
@Keep
@Entity
data class ChatData(
    @PrimaryKey(autoGenerate = true)
    val id:Int? = null,
    val user_id:Int? = null,
    val other_id:Int? = null,
    val user_name:String? = null,
    val product_id:Int? = null,
    val product_title: String? = null,
    val message:String? = null,
    val viewed:Int? = null,
    val date_and_time:String? = null,
)
