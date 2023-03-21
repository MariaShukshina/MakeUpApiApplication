package com.shukshina.makeupapiapplication.retrofit

import com.shukshina.makeupapiapplication.response.ProductsList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MakeUpApi {

    @GET
    suspend fun getAllProducts(): Call<ProductsList>

    @GET
    suspend fun getProductsByBrand(@Query("brand") brand: String): Call<ProductsList>

    @GET
    suspend fun getProductByBrandAndProductType(
        @Query("brand") brand: String,
        @Query("product_type") productType: String
    ): Call<ProductsList>
}