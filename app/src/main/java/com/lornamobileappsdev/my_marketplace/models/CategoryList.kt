package com.lornamobileappsdev.my_marketplace.models

import kotlinx.serialization.Serializable

@Serializable
data class CategoryList(
    val name:String?=null,
    val image:String?=null
)
