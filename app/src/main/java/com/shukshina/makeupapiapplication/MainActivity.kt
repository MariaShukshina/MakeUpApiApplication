package com.shukshina.makeupapiapplication

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.shukshina.makeupapiapplication.response.ProductsList
import com.shukshina.makeupapiapplication.response.ProductsListItem
import com.shukshina.makeupapiapplication.ui.AllProductsScreen
import com.shukshina.makeupapiapplication.ui.SplashScreen
import com.shukshina.makeupapiapplication.ui.theme.MakeUpApiApplicationTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import java.util.*


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MakeUpApiApplicationTheme {
                val viewModel: MainActivityViewModel by viewModels()
                val navController = rememberNavController()
                val productsList by viewModel.productsByProductTypeList.observeAsState()
                val internetConnectionState: StateFlow<Boolean> = remember { viewModel.internetConnectionState }
                val isConnected: Boolean by internetConnectionState.collectAsState(false)

                LaunchedEffect(isConnected) {
                    if (isConnected) {
                        viewModel.getProductsByProductType("lipstick")
                    }
                }

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
    viewModel: MainActivityViewModel = hiltViewModel(),
    productsList: ProductsList?, isConnected: Boolean
) {
    NavHost(navController = navController, startDestination = "splash_screen") {

        composable("splash_screen") {
            SplashScreen(navController = navController, viewModel = viewModel)
        }

        composable("all_products_screen") {
            AllProductsScreen(
                navController = navController,
                viewModel = viewModel,
                productsList = productsList,
                isConnected = isConnected
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
                //TODO: navigate to product details screen
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



