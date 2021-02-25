package com.dab.discountascii.data.entities

data class Purchase(
    val id: Int,
    val username: String,
    val productId: Int,
    val date: String
)
