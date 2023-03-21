package com.shukshina.makeupapiapplication.data

import com.shukshina.makeupapiapplication.domain.MakeUpApiRepository
import com.shukshina.makeupapiapplication.response.ProductsList
import com.shukshina.makeupapiapplication.retrofit.MakeUpApi
import dagger.hilt.android.scopes.ActivityScoped
import retrofit2.Call
import javax.inject.Inject

@ActivityScoped
class MakeUpApiRepositoryImpl @Inject constructor(private val api: MakeUpApi): MakeUpApiRepository {
    override suspend fun getAllProducts(): Call<ProductsList> = api.getAllProducts()

    override suspend fun getProductsByBrand(brand: String): Call<ProductsList> {
        return api.getProductsByBrand(brand)
    }

    override suspend fun getProductByBrandAndProductType(brand: String, productType: String): Call<ProductsList> {
        return api.getProductByBrandAndProductType(brand, productType)
    }
}