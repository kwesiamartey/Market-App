package com.lornamobileappsdev.my_marketplace.responses


@kotlinx.serialization.Serializable
data class SignupResponses(
    val id:Int?=null,
    val fullName:String?=null,
    val email:String?=null,
    val password:String?=null,
    val country:String?=null,
    val phoneNumber:String?=null,
    val avatar:ByteArray?=null,
    val date_and_time:String?=null,
    val accountStatus:String?=null,
    val token:String?=null,
    val verification_code:String?=null,
    val description:String?=null,
    val delete_account:String?=null,
)