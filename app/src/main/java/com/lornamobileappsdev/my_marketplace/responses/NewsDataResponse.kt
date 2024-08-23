package com.lornamobileappsdev.my_marketplace.models

import kotlinx.serialization.Serializable

@Serializable
data class NewsDataResponse(
    val id:Int? = null,
    val title:String? = null,
    val news:String? = null,
    val link:String? = null,
    val status:String? = null,
)
