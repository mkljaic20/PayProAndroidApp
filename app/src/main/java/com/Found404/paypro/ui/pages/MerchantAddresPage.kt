package com.Found404.paypro.ui.pages

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.found404.core.models.Merchant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MerchantAddress(
    onButtonNextClick: () -> Unit,
    onButtonPrevClick: () -> Unit
) {

    var merchantModel by remember { mutableStateOf( Merchant()) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding()
            .background(color = Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
    ) {
        Text(
            text = "Enter your business address",
            fontSize = 50.sp,
            lineHeight = 50.sp,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(
                vertical = 50.dp,
                horizontal = 20.dp
            )
        )
        TextField(
            singleLine = true,
            label = {Text("City name")},
            value = merchantModel.cityName,
            onValueChange = { newCityName ->
                merchantModel = merchantModel.copy(cityName = newCityName)
            },
        )
        TextField(
            singleLine = true,
            label = {Text("Street name")},
            value = merchantModel.streetName,
            onValueChange = { newStreetName ->
                merchantModel = merchantModel.copy(streetName = newStreetName)
            },
        )
        TextField(
            singleLine = true,
            label = { Text("Street number") },
            value = if (merchantModel.streetNumber == 0) "" else merchantModel.streetNumber.toString(),
            onValueChange = { newStreetNumber ->
                val parsedValue = newStreetNumber.toIntOrNull()
                if (parsedValue != null) {
                    merchantModel = merchantModel.copy(streetNumber = parsedValue)
                }
            }
        )

        TextField(
            singleLine = true,
            label = { Text("Postal code") },
            value = if (merchantModel.postCode == 0) "" else merchantModel.postCode.toString(),
            onValueChange = { newPostCode ->
                val parsedValue = newPostCode.toIntOrNull()
                if (parsedValue != null) {
                    merchantModel = merchantModel.copy(postCode = parsedValue)
                }
            }
        )


        Box(modifier = Modifier.fillMaxSize()){
            val context = LocalContext.current
            Button(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(
                        horizontal = 20.dp,
                        vertical = 20.dp
                    )
                    .size(
                        width = 130.dp,
                        height = 60.dp
                    ),
                onClick = {
                    if (merchantModel.streetName != null &&
                        merchantModel.streetNumber != null &&
                        merchantModel.postCode != null) {
                        onButtonNextClick()
                    }
                    else if (merchantModel.streetName != null){
                        Toast.makeText(
                            context,
                            "Please input street name",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else if (merchantModel.cityName != null){
                        Toast.makeText(
                            context,
                            "Please input city name",
                            Toast.LENGTH_SHORT
                        ).show()
                }
                    else if (merchantModel.postCode != null){
                        Toast.makeText(
                            context,
                            "Please input postal code",
                            Toast.LENGTH_SHORT
                        ).show()
                }
                    else if (merchantModel.streetNumber != null){
                        Toast.makeText(
                            context,
                            "Please input street number",
                            Toast.LENGTH_SHORT
                        ).show()
                }

                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue
                )
            ) {
                Text(text = "Next",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Button(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(
                        horizontal = 20.dp,
                        vertical = 20.dp)
                    .size(
                        width = 130.dp,
                        height = 60.dp),
                onClick = {
                    onButtonPrevClick()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray
                )
            ) {
                Text(text = "Previous",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview
@Composable
fun MerchantAddress() {
    MerchantAddress(onButtonNextClick = {}, onButtonPrevClick ={})
}
