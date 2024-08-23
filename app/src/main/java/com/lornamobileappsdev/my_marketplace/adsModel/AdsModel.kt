package com.lornamobileappsdev.my_marketplace.adsModel


class AdsModel(
    val id: String,
    val banner: String,
    val status: Boolean
) {
    // Default constructor with no arguments
    constructor() : this("", "", false)
}
