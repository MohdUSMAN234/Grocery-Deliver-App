package com.example.flash.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.flash.R
import com.example.flash.data.InternetItem
import com.google.firebase.auth.FirebaseAuth


enum class FlashappScreen(val title: String){
    Start("FreshFleet"),
    Items("Choose Item"),
    Cart("Your Cart")
}

var canNavigateBack = false
val  auth = FirebaseAuth.getInstance()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun  FlashApp(
    flashViewModel: FlashViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
){

    val user by flashViewModel.user.collectAsState()
    val isVisible by flashViewModel.isVisible.collectAsState()
    auth.currentUser?.let { flashViewModel.setUser(it) }
    val logoutClicked by flashViewModel.logoutClicked.collectAsState()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = FlashappScreen.valueOf(
        backStackEntry?.destination?.route?:FlashappScreen.Start.name
    )
    canNavigateBack = navController.previousBackStackEntry != null
    val cartItems by flashViewModel.cartItems.collectAsState()

    if (isVisible){
        OfferScreen()
    } else if (user == null){
        LoginUi(flashViewModel = flashViewModel)
    }

    else {

        Scaffold(
            topBar = {
                TopAppBar(title = {
                    Row(
                        verticalAlignment =  Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = currentScreen.title,
                                fontSize = 26.sp,
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            )
                            if (currentScreen == FlashappScreen.Cart) {
                                Text(
                                    text = "(${cartItems.size})",
                                    fontSize = 26.sp,
                                    fontFamily = FontFamily.SansSerif,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Black
                                )
                            }
                        }
                        Row( verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.clickable {
                                flashViewModel.setLogoutStatus(true)
                            }
                        ) {
                            Icon(painter = painterResource(id = R.drawable.logout), contentDescription ="Logout",
                                modifier = Modifier.size(24.dp),)
                            Text(text = "Logout",
                                fontSize = 18.sp,
                                modifier = Modifier.padding(
                                    end = 14.dp,
                                    start = 4.dp
                                ))
                        }
                    }
                },
                    navigationIcon = {
                        if (canNavigateBack) {
                            IconButton(onClick = {
                                navController.navigateUp()
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = "Back Button"
                                )
                            }
                        }

                    }
                )
            },
            bottomBar = { FlashAppBar(navController = navController,
                currentScreen = currentScreen,
                cartItems = cartItems) }
        ) {

            NavHost(
                navController = navController, startDestination = FlashappScreen.Start.name,
                Modifier.padding(it)
            ) {
                composable(route = FlashappScreen.Start.name) {
                    StartScreen(
                        flashViewModel = flashViewModel,
                        onCategoryClicked = {
                            flashViewModel.updateSelectedCategory(it)
                            navController.navigate(FlashappScreen.Items.name)

                        }
                    )
                }
                composable(route = FlashappScreen.Items.name) {
                    InternetItemScreen(flashViewModel = flashViewModel,
                        itemUiState = flashViewModel.itemUiState)
                }
                composable(route = FlashappScreen.Cart.name){
                    CartScreen(
                        flashViewModel = flashViewModel,
                        onHomeButtonClicked = {
                            navController.navigate(FlashappScreen.Start.name){
                                popUpTo(0)
                            }
                        }
                    )
                }
            }
        }
        if (logoutClicked){
            AlertCheck(onYesButtonPressed = {
                flashViewModel.setLogoutStatus(false)
                auth.signOut()
                flashViewModel.clearData()
            },
                onNoButtonPressed = {
                    flashViewModel.setLogoutStatus(false)
                }
            )
        }

    }

}

@Composable
fun FlashAppBar(navController: NavHostController,
                currentScreen: FlashappScreen,
                cartItems: List<InternetItem>
                ){
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 70.dp,
                vertical = 10.dp
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.clickable {
                navController.navigate(FlashappScreen.Start.name){
                    popUpTo(0)
                }

            }
        ) {
            Icon(imageVector = Icons.Outlined.Home,
                contentDescription = "Home")
            Text(text = "Home", fontSize = 10.sp)

        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.clickable {
                if (currentScreen != FlashappScreen.Cart){
                    navController.navigate(FlashappScreen.Cart.name){

                    }
                }


            }
        ) {
            Box {
                Icon(
                    imageVector = Icons.Outlined.ShoppingCart,
                    contentDescription = "Cart"
                )
                if(cartItems.isNotEmpty())
                Card(
                    modifier = Modifier.align(
                        alignment = Alignment.TopEnd
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Red

                    )
                ) {
Text(text = cartItems.size.toString(),
    fontSize = 10.sp,
    color =  Color.White,
    fontWeight = FontWeight.ExtraBold,
    modifier = Modifier.padding(
        horizontal =  1.dp
    ))
                }
            }
            Text(text = "Cart", fontSize = 10.sp)

        }

    }
}

@Composable
fun AlertCheck(
    onYesButtonPressed: () -> Unit,
    onNoButtonPressed: () -> Unit
){
    AlertDialog(
        title = {
            Text(text = "Logout?", fontWeight = FontWeight.Bold)
        },
        containerColor = Color.White,
        text = {
            Text(text = "Are you sure you want to Logout")
        },
        confirmButton = {
            TextButton(onClick = {
                onYesButtonPressed()
            }) {
                Text(text = "Yes")

            }
        },
        dismissButton = {
            TextButton(onClick = {
                onNoButtonPressed()
            }) {
                Text(text = "No")
                
            }
        },
        onDismissRequest = {
            onNoButtonPressed()
        }
        
    )

}