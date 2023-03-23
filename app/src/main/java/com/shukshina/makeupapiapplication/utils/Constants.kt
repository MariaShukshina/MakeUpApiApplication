package com.shukshina.makeupapiapplication.utils

import android.content.Context
import android.util.Log
import com.shukshina.makeupapiapplication.R
import com.shukshina.models.Brand
import com.shukshina.models.ProductType
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

object Constants {
    private var brandList: ArrayList<Brand> = ArrayList()
    const val BASE_URL = "http://makeup-api.herokuapp.com/api/v1/"

    fun getBrandList(context: Context): ArrayList<Brand> {
        if (brandList.isEmpty()) {
            val inputStream: InputStream = context.resources.openRawResource(R.raw.brand_list)
            val reader = BufferedReader(InputStreamReader(inputStream))
            val jsonString = reader.use { it.readText() } // read input stream into string
            brandList = Json.decodeFromString(jsonString) // decode JSON string
            reader.close()
            inputStream.close()
        }
        Log.i("Constants", "getBrandList: $brandList")
        return brandList
    }

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