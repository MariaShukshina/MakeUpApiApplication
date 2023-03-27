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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.shukshina.makeupapiapplication.MainActivityViewModel
import com.shukshina.makeupapiapplication.R
import com.shukshina.makeupapiapplication.utils.NetworkCheckUtil
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController, viewModel: MainActivityViewModel = hiltViewModel()) {

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

            if(!NetworkCheckUtil.hasNetwork(LocalContext.current)) {
                Text(text = "No internet connection", color = MaterialTheme.colors.error)
            }

            LaunchedEffect(key1 = viewModel.allProductsList.value) {
                if(!viewModel.allProductsList.value.isNullOrEmpty()) {
                    navController.popBackStack()
                    navController.navigate("all_products_screen")
                } else {
                    delay(8000)
                    navController.popBackStack()
                    navController.navigate("all_products_screen")
                }
            }
        }
    }
}