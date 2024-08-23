package com.lornamobileappsdev.my_marketplace.entity

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
@Keep
@kotlinx.serialization.Serializable
@Entity
data class CountryListData(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val img: String,
    val name: String,
    val status:String
)
