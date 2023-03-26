package com.shukshina.makeupapiapplication.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.shukshina.makeupapiapplication.MainActivityViewModel
import com.shukshina.makeupapiapplication.ProductsListSection
import com.shukshina.makeupapiapplication.SearchBar
import com.shukshina.makeupapiapplication.response.ProductsList
import com.shukshina.makeupapiapplication.utils.Constants

@Composable
fun AllProductsScreen(navController: NavHostController, viewModel: MainActivityViewModel = hiltViewModel()) {
    val searchQuery = remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {

        val productsList by viewModel.productsByBrandAndProductTypeList.observeAsState()
        AllProductsTopSection(searchQuery) {
            viewModel.getProductByBrandAndProductType("maybelline", "lipstick")
        }

        LaunchedEffect(productsList) {
            if (productsList.isNullOrEmpty()) {
                viewModel.getProductByBrandAndProductType("maybelline", "lipstick")
            }
        }
        productsList?.let { list ->
            val searchedText = searchQuery.value
            val filteredProducts = if (searchedText.isEmpty()) {
                list
            } else {
                val resultList = ProductsList()
                if (!productsList.isNullOrEmpty()) {
                    for (item in list) {
                        if (item.name!!.contains(searchedText, ignoreCase = true)
                            || item.brand!!.contains(searchedText, ignoreCase = true)
                            || item.category!!.contains(searchedText, ignoreCase = true)
                        ) {
                            resultList.add(item)
                        }
                    }
                }
                resultList
            }
            ProductsListSection(navController, filteredProducts)
        }

    }
}

@Composable
fun AllProductsTopSection(searchQuery: MutableState<String>, onClick: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf(Constants.productTypes[0]) }

    Row {
        SearchBar(
            hint = "Search by name, brand or category",
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(10.dp),
            searchQuery = searchQuery,
        )
        Spacer(modifier = Modifier.width(4.dp))

        IconButton(
            onClick = {
                onClick.invoke()
            },
            modifier = Modifier
                .fillMaxWidth(0.5f)
        ) {
            Icon(Icons.Default.Refresh, contentDescription = "Refresh")
        }
        Spacer(modifier = Modifier.width(4.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .wrapContentSize(Alignment.TopStart)
        ) {
            IconButton(onClick = { expanded = true }) {
                Icon(Icons.Default.Menu, contentDescription = "Dropdown")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                offset = DpOffset(0.dp, 20.dp)
            ) {
                Constants.productTypes.forEach { type ->
                    DropdownMenuItem(onClick = {
                        selectedItem = type
                        expanded = false
                    }) {
                        Text(text = type.name)
                    }
                }
            }
        }
    }
}