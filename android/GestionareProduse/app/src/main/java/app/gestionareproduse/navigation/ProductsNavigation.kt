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
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

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
                    val encodedProduct = URLEncoder.encode(productString, StandardCharsets.UTF_8.toString())
                    navController.navigate("${ProductDetails.route}/$encodedProduct")
                },
                addProduct = {
                    navController.navigate(NewProduct.route)
                }
            )
        }
        composable(
            route = "${ProductDetails.route}/{encodedProduct}",
            arguments = listOf(navArgument("encodedProduct"){type = NavType.StringType})
        ) { navBackStackEntry ->
            navBackStackEntry.arguments?.getString("encodedProduct")?.let{
                encodedProduct->
                ProductDetailsScreen(controller = navController, encodedProduct = encodedProduct)
            }
        }
        composable(
            route = NewProduct.route,
        ) { navBackStackEntry ->
            NewProductScreen(controller = navController)
        }
    }
}