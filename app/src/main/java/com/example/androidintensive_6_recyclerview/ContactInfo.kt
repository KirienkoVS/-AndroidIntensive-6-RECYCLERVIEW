package com.example.androidintensive_6_recyclerview

import java.io.Serializable
import kotlin.random.Random

data class ContactInfo(
    var name: String? = null,
    var lastName: String? = null,
    var phoneNumber: String? = null,
) : Serializable {
    private val randomNumber = Random.nextInt(300)
    val imageUrl = "https://picsum.photos/70?random$randomNumber"
}
