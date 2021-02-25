package com.dab.discountascii.data.entities

data class Product(
    val id: Int,
    val face: String,
    val price: Int,
    val size: Int,
    var recent: List<String>
)
