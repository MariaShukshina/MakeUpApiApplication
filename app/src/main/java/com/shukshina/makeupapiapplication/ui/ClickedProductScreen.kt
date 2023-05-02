package com.shukshina.makeupapiapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.shukshina.makeupapiapplication.R

@Composable
fun ClickedProductScreen(
    imageLink: String,
    productName: String,
    productDescription: String,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize().background(MaterialTheme.colors.surface)
    ) {

        AsyncImage(
            modifier = modifier
                .size(250.dp)
                .padding(8.dp),
            alignment = Alignment.TopCenter,
            placeholder = painterResource(id = R.drawable.ic_placeholder),
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageLink)
                .crossfade(true)
                .build(),
            contentDescription = null,
            error = painterResource(id = R.drawable.ic_placeholder),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = modifier
                .padding(8.dp)
        ) {
            Text(
                text = productName,
                fontSize = 19.sp,
                color = Color(249,116,167),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Justify,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp)
            )
            Text(
                text = productDescription,
                fontSize = 16.sp,
                color = MaterialTheme.colors.onSurface,
                textAlign = TextAlign.Justify,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp)
            )
        }
    }
}