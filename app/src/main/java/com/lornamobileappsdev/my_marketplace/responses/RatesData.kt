package com.lornamobileappsdev.my_marketplace.responses



@kotlinx.serialization.Serializable
data class RatesData(
    var id:Int?=null,
    var user_id:Int?=null,
    var product_id :Int?=null,
    var ratings:Int?=null,
    var date_and_time:String?=null
)

