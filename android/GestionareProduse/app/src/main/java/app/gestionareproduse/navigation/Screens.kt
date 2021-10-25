package app.gestionareproduse.navigation

sealed class Screen(val route: String){
    object Products : Screen(route = "products")
    object ProductDetails : Screen(route = "details")
    object NewProduct : Screen(route = "newProduct")
}