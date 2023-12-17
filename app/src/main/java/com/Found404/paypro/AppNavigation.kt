package com.Found404.paypro

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.Found404.paypro.ui.pages.AddingMerchants
import com.Found404.paypro.ui.pages.CardPayments
import com.Found404.paypro.ui.pages.LoginPage
import com.Found404.paypro.ui.pages.MerchantAddress
import com.Found404.paypro.ui.pages.MerchantCreated
import com.Found404.paypro.ui.pages.MerchantName
import com.Found404.paypro.ui.pages.RegisterPage
import com.Found404.paypro.ui.pages.WelcomePage

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(navController, startDestination = "welcome"){
        val authServiceImpl = AuthServiceImpl()
        composable("welcome") {
            if (authServiceImpl.isJwtValid(LocalContext.current)) {
                WelcomePage(navController = navController)
            } else {
                LoginPage(navController = navController)
            }
        }

        composable("login") {
            LoginPage(navController = navController)
        }
        composable("registration"){
            RegisterPage(navController = navController)
        }
        composable("addingMerchants"){
            AddingMerchants(
                onCreateMerchantButtonClick = {
                    navController.navigate("merchantName")
                },
                onButtonCancelClick = {
                    navController.navigate("merchantCreated") //TODO change to home page once it is completed
                }
            )
        }
        composable("merchantName") {
            MerchantName(
                onButtonNextClick = {
                    navController.navigate("merchantAddress")
                },
                onButtonPrevClick = {
                    navController.navigate("addingMerchants")
                }
            )
        }
        composable("merchantAddress"){
            MerchantAddress(
                onButtonNextClick = {
                    navController.navigate("cardPayments")
                },
                onButtonPrevClick = {
                    navController.navigate("merchantName")
                }
            )
        }
        composable("cardPayments"){
            CardPayments(
                onButtonFinishClick = {
                    navController.navigate("merchantCreated")
                },
                onButtonPrevClick = {
                    navController.navigate("merchantAddress")
                }
            )
        }
        composable("merchantCreated"){
            MerchantCreated(
                onButtonFinishClick = {
                    navController.navigate("addingMerchants") //TODO change to home page once it is completed
                },
                onButtonPrevClick = {
                    navController.navigate("cardPayments")
                }
            )
        }
    }
}