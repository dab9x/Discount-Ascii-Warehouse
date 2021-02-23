package com.dab.discountascii.model

data class Product(
    var id: Int,
    var face: String,
    var price: Int,
    var size: Int,
    var recent: List<String>
)
