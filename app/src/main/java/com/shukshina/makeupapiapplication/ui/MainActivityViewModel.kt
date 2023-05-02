package com.shukshina.makeupapiapplication.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shukshina.makeupapiapplication.domain.MakeUpApiRepository
import com.shukshina.makeupapiapplication.response.ProductsList
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val repository: MakeUpApiRepository
) : ViewModel() {

    private val _productsList = MutableLiveData<ProductsList>()
    val productsList = _productsList

    private val _internetConnectionState = MutableStateFlow(true)
    val internetConnectionState = _internetConnectionState


    init {
        viewModelScope.launch {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkCallback = object : ConnectivityManager.NetworkCallback() {

                override fun onAvailable(network: Network) {
                    _internetConnectionState.value = true
                    getProductByBrandAndProductType("maybelline",
                        "lipstick")
                }

                override fun onLost(network: Network) {
                    _internetConnectionState.value = false
                    _productsList.postValue(null)
                }
            }
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        }
    }

    fun getAllProducts() {
        _productsList.postValue(null)
        viewModelScope.launch {
            val call: Call<ProductsList> = repository.getAllProducts()
            call.enqueue(object : Callback<ProductsList> {
                override fun onResponse(call: Call<ProductsList>, response: Response<ProductsList>) {
                    if (response.body() == null) {
                        _productsList.postValue(ProductsList())
                        return
                    }
                    _productsList.postValue(response.body())
                }

                override fun onFailure(call: Call<ProductsList>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }
    }

    fun getProductByBrandAndProductType(brand: String, productType: String) {
        _productsList.postValue(null)
        viewModelScope.launch {
            val call: Call<ProductsList> = repository.getProductByBrandAndProductType(brand, productType)
            call.enqueue(object : Callback<ProductsList> {
                override fun onResponse(call: Call<ProductsList>, response: Response<ProductsList>) {
                    if (response.body() == null) {
                        _productsList.postValue(ProductsList())
                        return
                    }
                    _productsList.postValue(response.body()!!)
                }

                override fun onFailure(call: Call<ProductsList>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }
    }

    fun getProductsByProductType(productType: String) {
        _productsList.postValue(null)
        viewModelScope.launch {
            val call: Call<ProductsList> = repository.getProductsByProductType(productType)
            call.enqueue(object : Callback<ProductsList> {
                override fun onResponse(call: Call<ProductsList>, response: Response<ProductsList>) {
                    if (response.body() == null) {
                        _productsList.postValue(ProductsList())
                        return
                    }
                    _productsList.postValue(response.body())
                }

                override fun onFailure(call: Call<ProductsList>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }
    }
}