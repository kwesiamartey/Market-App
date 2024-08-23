package com.lornamobileappsdev.my_marketplace.models

import kotlinx.serialization.Serializable

@Serializable
data class FeaturedSliderDataResponse(
    val id:Int? = null,
    val images:String? = null,
    val status:String? = null,
)
