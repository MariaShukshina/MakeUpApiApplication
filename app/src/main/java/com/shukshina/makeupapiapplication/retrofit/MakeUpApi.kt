package com.shukshina.makeupapiapplication.retrofit

import com.shukshina.makeupapiapplication.response.ProductsList
import com.shukshina.makeupapiapplication.response.ProductsListItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MakeUpApi {

    @GET("products.json")
    fun getAllProducts(): Call<ProductsList>

    @GET("products.json")
    fun getProductsByBrand(@Query("brand") brand: String): Call<ProductsList>

    @GET("products.json")
    fun getProductsByProductType(@Query("product_type") brand: String): Call<ProductsList>

    @GET("products.json")
    fun getProductByBrandAndProductType(
        @Query("brand") brand: String,
        @Query("product_type") productType: String
    ): Call<ProductsList>

    @GET("products/{id}.json")
    fun getProductById(@Path("id") productId: String): Call<ProductsListItem>
}