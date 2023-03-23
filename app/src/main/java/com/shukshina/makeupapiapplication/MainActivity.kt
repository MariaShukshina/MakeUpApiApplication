package com.shukshina.makeupapiapplication

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.List
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.shukshina.makeupapiapplication.response.ProductsList
import com.shukshina.makeupapiapplication.ui.BottomNavItem
import com.shukshina.makeupapiapplication.ui.theme.MakeUpApiApplicationTheme
import com.shukshina.makeupapiapplication.utils.Constants
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

   /*     val viewModel: MainActivityViewModel by viewModels()

        viewModel.getProductsByProductType("lipstick")
        viewModel.productsByProductTypeList.observe(this) {
            Log.i("MainActivity", "productList: $it")
        }*/

        setContent {
            MakeUpApiApplicationTheme {
                val navController = rememberNavController()
                MainScreen(navController)
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(navController: NavHostController) {
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
    NavHost(navController = navController, startDestination = "all_products"){
        composable("all_products"){
            AllProductsScreen(navController = navController)
        }
        composable("brands"){
            BrandsScreen()
        }
    }
}

@Composable
fun ProductsListSection(
    navController: NavController,
    productList: ProductsList = ProductsList()
) {
    LazyVerticalGrid(columns = GridCells.Fixed(2), content = {
        items(productList.size) {
            //TODO: display the loaded data or load the data here
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
fun AllProductsScreen(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxSize().padding(10.dp)) {
        AllProductsTopSection()
        ProductsListSection(navController)
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
fun AllProductsTopSection() {
    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf(Constants.productTypes[0]) }

    Row {
        SearchBar(hint = "Search", modifier = Modifier
            .fillMaxWidth(0.8f)
            .padding(10.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))

        Box(modifier = Modifier.fillMaxWidth(0.5f).wrapContentSize(Alignment.TopStart)) {
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

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "",
    onSearch: (String) -> Unit = {}
){
    var text by rememberSaveable {
        mutableStateOf("")
    }
    var isHintDisplayed by remember {
        mutableStateOf(hint != "")
    }
    Box(modifier = modifier){
        BasicTextField(
            value = text,
            onValueChange = {
                text = it
                onSearch(text)
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
                    isHintDisplayed = it.isFocused == false && text.isEmpty()
                }
        )
        if(isHintDisplayed){
            Text(
                text = hint,
                color = if(MaterialTheme.colors.isLight) Color.DarkGray else Color.Black,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 12.dp)
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