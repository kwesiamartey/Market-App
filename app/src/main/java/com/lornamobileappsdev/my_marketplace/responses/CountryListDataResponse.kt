package com.lornamobileappsdev.my_marketplace.responses


@kotlinx.serialization.Serializable
data class CountryListDataResponse(
    val id: Int,
    val img: String,
    val name: String,
    val status:String
)
