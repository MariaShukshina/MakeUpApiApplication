package com.shukshina.makeupapiapplication.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.shukshina.makeupapiapplication.R
import com.shukshina.makeupapiapplication.response.ProductsList
import com.shukshina.makeupapiapplication.response.ProductsListItem
import com.shukshina.makeupapiapplication.ui.theme.MakeUpApiApplicationTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import java.net.URLDecoder
import java.net.URLEncoder
import java.util.*


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MakeUpApiApplicationTheme {
                val viewModel: MainActivityViewModel by viewModels()
                val navController = rememberNavController()
                val productsList by viewModel.productsList.observeAsState()
                val internetConnectionState: StateFlow<Boolean> = remember {
                    viewModel.internetConnectionState
                }
                val isConnected: Boolean by internetConnectionState.collectAsState(true)

                /*LaunchedEffect(isConnected) {
                    if (isConnected) {
                        viewModel.getProductByBrandAndProductType("maybelline",
                            "lipstick")
                    }
                }*/

                Surface(modifier = Modifier.background(MaterialTheme.colors.surface)) {
                    Navigation(
                        navController = navController,
                        viewModel = viewModel,
                        productsList = productsList,
                        isConnected = isConnected
                    )
                }
            }
        }
    }
}

@Composable
fun Navigation(
    navController: NavHostController,
    viewModel: MainActivityViewModel,
    productsList: ProductsList?,
    isConnected: Boolean
) {
    NavHost(navController = navController, startDestination = ScreenList.SplashScreen.name) {

        composable(ScreenList.SplashScreen.name) {
            SplashScreen(
                navController = navController,
                viewModel = viewModel,
                isConnected = isConnected
            )
        }

        composable(ScreenList.AllProductsScreen.name) {
            AllProductsScreen(
                navController = navController,
                viewModel = viewModel,
                productsList = productsList,
                isConnected = isConnected
            )
        }
        composable(
            "${ScreenList.ClickedProductScreen.name}/{imageLink}/{productName}/{productDescription}",
            arguments = listOf(
                navArgument("imageLink") {
                    type = NavType.StringType
                },
                navArgument("productName") {
                    type = NavType.StringType
                },
                navArgument("productDescription") {
                    type = NavType.StringType
                }
            )
        ) {
            val decodedImageLink =
                URLDecoder.decode(it.arguments?.getString("imageLink") ?: "", "UTF-8")
            val decodedProductName =
                URLDecoder.decode(it.arguments?.getString("productName") ?: "", "UTF-8")
            val decodedProductDescription =
                URLDecoder.decode(it.arguments?.getString("productDescription") ?: "", "UTF-8")

            ClickedProductScreen(
                imageLink = decodedImageLink,
                productName = decodedProductName,
                productDescription = decodedProductDescription
            )
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
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "",
    searchQuery: MutableState<String>
) {
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
                fontSize = 12.sp,
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
    Box(
        modifier = modifier
            .wrapContentHeight(align = Alignment.Top)
            .padding(3.dp)
            .shadow(0.dp, RoundedCornerShape(6.dp))
            .clip(RoundedCornerShape(6.dp))
            .background(Color.White)
            .clickable {
                val encodedUrl = URLEncoder.encode(productsListItem.image_link, "utf-8")
                val encodedName = URLEncoder.encode(productsListItem.name, "utf-8")
                val encodedDescription = URLEncoder.encode(productsListItem.description, "utf-8")
                navController.navigate(
                    "${ScreenList.ClickedProductScreen.name}/$encodedUrl/$encodedName/$encodedDescription"
                )
            }
            .height(intrinsicSize = IntrinsicSize.Max)
    ) {
        //Log.i("ImageRequest", "image url: ${productsListItem.image_link}")

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AsyncImage(
                modifier = Modifier
                    .size(150.dp)
                    .padding(1.dp),
                alignment = Alignment.TopCenter,
                placeholder = painterResource(id = R.drawable.ic_placeholder),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(productsListItem.image_link)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                error = painterResource(id = R.drawable.ic_placeholder),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .background(Color(192, 192, 192, 120))
                    .padding(bottom = 2.dp)
            ) {
                Text(
                    text = productsListItem.name ?: "",
                    fontSize = 14.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = productsListItem.category?.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(Locale.ROOT)
                        else it.toString()
                    } ?: "",
                    fontSize = 12.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = productsListItem.brand?.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(Locale.ROOT)
                        else it.toString()
                    } ?: "",
                    fontSize = 12.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = if (productsListItem.price != null) "$${productsListItem.price}" else "",
                    fontSize = 12.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MakeUpApiApplicationTheme {

    }
}



