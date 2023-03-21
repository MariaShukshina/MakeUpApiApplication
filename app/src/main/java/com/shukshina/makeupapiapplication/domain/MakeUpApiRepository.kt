package com.shukshina.makeupapiapplication.domain

import com.shukshina.makeupapiapplication.response.ProductsList
import com.shukshina.makeupapiapplication.response.ProductsListItem
import retrofit2.Call

interface MakeUpApiRepository {
    suspend fun getAllProducts(): Call<ProductsList>
    suspend fun getProductsByBrand(brand: String): Call<ProductsList>
    suspend fun getProductByBrandAndProductType(brand: String, productType: String): Call<ProductsList>
}