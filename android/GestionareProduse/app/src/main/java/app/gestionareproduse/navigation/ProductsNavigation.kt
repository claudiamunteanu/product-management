package app.gestionareproduse.navigation

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import app.gestionareproduse.details.NewProductScreen
import app.gestionareproduse.details.ProductDetailsScreen
import app.gestionareproduse.navigation.Screen.*
import app.gestionareproduse.products.ProductsScreen
import app.gestionareproduse.products.domain.Product
import app.gestionareproduse.utils.Utils
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

@Composable
fun ProductNavigation(){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Products.route){
        composable(
            route = Products.route
        ) {
            ProductsScreen(
                onProductClick = { selectedProduct ->
                    val productString = Utils.productToString(selectedProduct)
                    Utils.selectedProductString = productString
                    navController.navigate("${ProductDetails.route}")
                },
                addProduct = {
                    navController.navigate("${NewProduct.route}")
                }
            )
        }
        composable(
            route = "${ProductDetails.route}",
        ) { navBackStackEntry ->
                ProductDetailsScreen(controller = navController)
        }
        composable(
            route = "${NewProduct.route}",
        ) { navBackStackEntry ->
            NewProductScreen(controller = navController)
        }
    }
}