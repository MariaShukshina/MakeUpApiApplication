package com.shukshina.makeupapiapplication.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
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
import com.shukshina.makeupapiapplication.utils.Constants

@Composable
fun AllProductsScreen(navController: NavHostController, viewModel: MainActivityViewModel = hiltViewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        AllProductsTopSection(viewModel)
        val productsList by viewModel.productsByBrandAndProductTypeList.observeAsState()

        productsList?.let { ProductsListSection(navController, it) }

        LaunchedEffect(productsList) {
            if(productsList.isNullOrEmpty()) {
                viewModel.getProductByBrandAndProductType("maybelline", "lipstick")
            }
        }

    }
}

@Composable
fun AllProductsTopSection(viewModel: MainActivityViewModel) {
    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf(Constants.productTypes[0]) }

    Row {
        val productsList by viewModel.allProductsList.observeAsState()
        SearchBar(
            hint = "Search", modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(10.dp)
        ) { query ->
            if(!productsList.isNullOrEmpty()) {
                productsList?.filter { it.name!!.contains(query) }
            }
        }
        Spacer(modifier = Modifier.width(10.dp))

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