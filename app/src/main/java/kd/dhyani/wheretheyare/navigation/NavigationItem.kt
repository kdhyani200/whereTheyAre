package kd.dhyani.wheretheyare.navigation

sealed class NavigationItem (val route : String) {
    object HomeScreen : NavigationItem(route = "home")
    object SearchScreen : NavigationItem(route = "search")
}

