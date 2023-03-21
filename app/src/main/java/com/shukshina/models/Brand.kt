package com.shukshina.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Brand(
    @SerialName("name")
    val name: String,
    @SerialName("image")
    val image: Int
)
