package com.lornamobileappsdev.my_marketplace.responses

@kotlinx.serialization.Serializable
data class PostProductResponse(
    var id:Int?=null,
    var userId:Int?=null,
    var user_name:String?=null,
    var title:String?=null,
    var currenxy:String?=null,
    var price:String?=null,
    var category:String?=null,
    var country:String?=null,
    var location:String?=null,
    var desc:String?=null,
    var dateAndtimme:String?=null,
    var type:String?=null,
    var negotiation:String?=null,
    var image:String?=null,
    var image_two:String?=null,
    var image_three:String?=null,
    var image_four:String?=null,
    var image_five:String?=null,
    var image_six:String?=null,
    val paid_product:String?=null,
    val pst_phone_number:String?=null,
    val subscription_type:String?=null,
    val subscription_date_start:String?=null,
    val subscription_date_due:String?=null,
    val views:Int? =null,
    val approval_status: String? = null,
    val rates:Int?=null,
)

