package com.lornamobileappsdev.my_marketplace.models

import kotlinx.serialization.Serializable

@Serializable
data class PostLikesData(
    val id:Int? = null,
    val post_id:Int? = null,
    val user_id:Int?=null,
    val my_id:Int?=null
)