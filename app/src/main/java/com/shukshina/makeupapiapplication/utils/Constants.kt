package com.shukshina.makeupapiapplication.utils

import com.shukshina.models.ProductType

object Constants {
    const val BASE_URL = "http://makeup-api.herokuapp.com/api/v1/"

    val productTypes = listOf(
        ProductType("All products", "All products"),
        ProductType("blush", "Blush"),
        ProductType("bronzer", "Bronzer"),
        ProductType("eyebrow", "Eyebrow"),
        ProductType("eyeliner", "Eyeliner"),
        ProductType("eyeshadow", "Eyeshadow"),
        ProductType("foundation", "Foundation"),
        ProductType("lip_liner", "Lip Liner"),
        ProductType("lipstick", "Lipstick"),
        ProductType("mascara", "Mascara"),
        ProductType("nail_polish", "Nail Polish")
    )
}