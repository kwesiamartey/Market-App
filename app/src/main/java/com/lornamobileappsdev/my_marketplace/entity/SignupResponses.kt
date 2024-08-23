package com.lornamobileappsdev.my_marketplace.entity


import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity
data class SignupResponses(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var fullName: String? = null,
    var email: String? = null,
    var password: String? = null,
    var country: String? = null,
    var phoneNumber: String? = null,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var avatar:ByteArray?=null,
    var dateAndTime: String? = null,
    var accountStatus: String? = null,
    var token: String? = null,
    var verification_code:String? =null,
    var description:String? =null,
    var delete_account:String? =null,
)