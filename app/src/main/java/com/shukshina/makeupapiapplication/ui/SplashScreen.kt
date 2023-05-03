package com.shukshina.makeupapiapplication.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.shukshina.makeupapiapplication.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController,
                 viewModel: MainActivityViewModel, isConnected: Boolean) {

    Surface(color = Color.White) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_placeholder),
                contentDescription = "App logo",
                modifier = Modifier.size(240.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
            CircularProgressIndicator(color = Color.Red)

            Spacer(modifier = Modifier.height(32.dp))

            if(!isConnected) {
                Text(text = "No internet connection", color = MaterialTheme.colors.error)
            }

            LaunchedEffect(key1 = viewModel.productsList.value) {
                if(!viewModel.productsList.value.isNullOrEmpty()) {
                    navController.popBackStack()
                    navController.navigate(ScreenList.AllProductsScreen.name)
                } else {
                    delay(8000)
                    navController.popBackStack()
                    navController.navigate(ScreenList.AllProductsScreen.name)
                }
            }
        }
    }
}