package com.shukshina.makeupapiapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.shukshina.makeupapiapplication.response.ProductsList
import com.shukshina.makeupapiapplication.response.ProductsListItem
import com.shukshina.makeupapiapplication.ui.AllProductsScreen
import com.shukshina.makeupapiapplication.ui.BottomNavItem
import com.shukshina.makeupapiapplication.ui.theme.MakeUpApiApplicationTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MakeUpApiApplicationTheme {
                val productsList: ProductsList = ProductsList()
                val navController = rememberNavController()
                
                MainScreen(navController, productsList)
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(navController: NavHostController, productsList: ProductsList) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                items = listOf(
                    BottomNavItem(
                        name = "All products",
                        route = "all_products",
                        icon = Icons.Default.Home
                    ),
                    BottomNavItem(
                        name = "Brands",
                        route = "brands",
                        icon = Icons.Outlined.List
                    )
                ),
                navController = navController,
                onItemClick = {
                    navController.backQueue.clear()
                    navController.navigate(it.route)
                }
            )
        }
    ) {
        Navigation(navController = navController)
    }
}

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "all_products") {
        composable("all_products") {
            AllProductsScreen(navController = navController)
        }
        composable("brands") {
            BrandsScreen()
        }
    }
}

@Composable
fun ProductsListSection(
    navController: NavHostController,
    productList: List<ProductsListItem>
) {
    LazyVerticalGrid(columns = GridCells.Fixed(2), content = {
        items(productList) {
            ProductItem(navController = navController, productsListItem = it)
        }
    })
}

@Composable
fun BottomNavigationBar(
    items: List<BottomNavItem>,
    navController: NavController,
    modifier: Modifier = Modifier,
    onItemClick: (BottomNavItem) -> Unit
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    BottomNavigation(
        modifier = modifier,
        backgroundColor = Color(249, 116, 167),
        elevation = 5.dp
    ) {
        items.forEach { item ->
            val selected = item.route == backStackEntry.value?.destination?.route
            BottomNavigationItem(
                selected = selected,
                onClick = { onItemClick(item) },
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.Gray,
                icon = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.name
                        )
                        Text(
                            text = item.name,
                            textAlign = TextAlign.Center,
                            fontSize = 10.sp
                        )
                    }
                }
            )
        }
    }
}



@Composable
fun BrandsScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Brands screen")
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "",
    searchQuery: MutableState<String>
) {
    /*var text by rememberSaveable {
        mutableStateOf("")
    }*/
    var isHintDisplayed by remember {
        mutableStateOf(hint != "")
    }
    Box(modifier = modifier) {
        BasicTextField(
            value = searchQuery.value,
            onValueChange = {
                searchQuery.value = it
            },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .onFocusChanged {
                    isHintDisplayed = it.isFocused == false && searchQuery.value.isEmpty()
                }
        )
        if (isHintDisplayed) {
            Text(
                text = hint,
                color = if (MaterialTheme.colors.isLight) Color.DarkGray else Color.Black,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            )
        }
    }
}

@Composable
fun ProductItem(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    productsListItem: ProductsListItem
) {
    Box(contentAlignment = Alignment.BottomCenter,
        modifier = modifier
            .padding(3.dp)
            .shadow(0.dp, RoundedCornerShape(6.dp))
            .clip(RoundedCornerShape(6.dp))
            .background(Color.White)
            .aspectRatio(1f)
            .clickable {
                //TODO: navigate to product details screen
            }
    ) {
        Log.i("ImageRequest", "image url: ${productsListItem.image_link}")
        AsyncImage(
            alignment = Alignment.Center,
            placeholder = painterResource(id = R.drawable.ic_placeholder),
            model = ImageRequest.Builder(LocalContext.current)
                .data(productsListItem.image_link)
                .crossfade(true)
                .build(),
            contentDescription = null,
            error = painterResource(id = R.drawable.ic_placeholder),
            contentScale = ContentScale.Crop
        )

        Column(verticalArrangement = Arrangement.Bottom, modifier = Modifier.background(Color(192, 192, 192, 120))) {
            Text(
                text = productsListItem.name ?: "",
                fontSize = 14.sp,
                color = Color.White,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = productsListItem.price ?: "",
                fontSize = 14.sp,
                color = Color.White,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MakeUpApiApplicationTheme {

    }
}



