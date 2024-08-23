package com.lornamobileappsdev.my_marketplace.responses

data class UploadResponse(
    val error: Boolean,
    val message: String,
    val image: String
)