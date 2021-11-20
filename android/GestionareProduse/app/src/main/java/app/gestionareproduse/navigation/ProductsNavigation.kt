package app.gestionareproduse.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import app.gestionareproduse.chooseWarehouseScreen.ChooseWarehouseScreen
import app.gestionareproduse.detailsScreen.NewProductScreen
import app.gestionareproduse.detailsScreen.ProductDetailsScreen
import app.gestionareproduse.navigation.Screen.*
import app.gestionareproduse.products.ProductsScreen
import app.gestionareproduse.utils.Utils
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun ProductNavigation(){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Warehouses.route){
        composable(
            route = Warehouses.route
        ){
            ChooseWarehouseScreen(
                onSubmit = { selectedWarehouse ->
                    val warehouseString = Utils.warehouseToString(selectedWarehouse)
                    val encodedWarehouse = URLEncoder.encode(warehouseString, StandardCharsets.UTF_8.toString())
                    navController.navigate("${Products.route}/$encodedWarehouse")
                }
            )
        }
        composable(
            route = "${Products.route}/{encodedWarehouse}",
            arguments = listOf(navArgument("encodedWarehouse"){type = NavType.StringType})
        ) { navBackStackEntry->
            navBackStackEntry.arguments?.getString("encodedWarehouse")?.let{
                encodedWarehouse->
                ProductsScreen(
                    onProductClick = { selectedProduct ->
                        //facem produsul String
                        val productString = Utils.productToString(selectedProduct)
                        //encoding the product
                        val encodedProduct = URLEncoder.encode(productString, StandardCharsets.UTF_8.toString())
                        //navigam la pagina cu datele produsului
                        navController.navigate("${ProductDetails.route}/$encodedProduct")
                    },
                    addProduct = {
                        //navigam la pagina de adaugare a unui produs
                        navController.navigate(NewProduct.route)
                    },
                    controller = navController,
                    encodedWarehouse = encodedWarehouse
                )
            }
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