package com.lornamobileappsdev.my_marketplace.entity


import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@kotlinx.serialization.Serializable
@Entity
data class RelatedSearchData(
    @PrimaryKey(autoGenerate = true)
    val id: Int?=null,
    val postid:Int?=null,
    val postUserId:Int?=null,
    val name: String?=null,
    val cate: String?=null,
    val full_name:String?=null,
    val rates:Int?=null,
)