package kd.dhyani.wheretheyare.ui.theme.screens.home

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kd.dhyani.wheretheyare.R

@Composable
fun HomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.title))
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Want to know\n\nwhere our\n\nfaculties are?",
            fontSize = 50.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 10.dp),
            color = colorResource(id = R.color.two)
        )

        Button(
            onClick = {
                navController.navigate(kd.dhyani.wheretheyare.navigation.NavigationItem.SearchScreen.route) {
                    launchSingleTop = true
                    popUpTo(kd.dhyani.wheretheyare.navigation.NavigationItem.HomeScreen.route) { inclusive = false }
                }
            }
            ,
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.two)
            ),
            modifier = Modifier.padding(top = 20.dp)
        ) {
            Text(
                text = "Find",
                color = colorResource(id = R.color.one)
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewHomeScreen() {
    val navController = rememberNavController()
    HomeScreen(navController = navController)
}