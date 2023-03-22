package com.shukshina.makeupapiapplication.domain

import com.shukshina.makeupapiapplication.response.ProductsList
import com.shukshina.makeupapiapplication.response.ProductsListItem
import retrofit2.Call

interface MakeUpApiRepository {
    fun getAllProducts(): Call<ProductsList>
    fun getProductsByBrand(brand: String): Call<ProductsList>
    fun getProductByBrandAndProductType(brand: String, productType: String): Call<ProductsList>
    fun getProductById(productId: String): Call<ProductsListItem>
}