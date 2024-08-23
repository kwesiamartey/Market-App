package com.lornamobileappsdev.my_marketplace.api

import okhttp3.ResponseBody
import retrofit2.Callback

interface MyCallback {
    fun myCallback(callback: Callback<ResponseBody>) : Callback<ResponseBody>
}