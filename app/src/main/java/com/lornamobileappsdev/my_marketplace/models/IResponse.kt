package com.lornamobileappsdev.my_marketplace.models

data class IResponse(
    var id: String,
    var status: Int,
    var title: String,
    var body: String,
    var link: String,
    val button_text:String,
    var button_color: String,
    var Images:String,
    var contact: String
)
