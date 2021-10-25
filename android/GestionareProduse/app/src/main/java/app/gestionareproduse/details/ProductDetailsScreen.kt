package app.gestionareproduse.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import app.gestionareproduse.details.viewmodel.ProductsDetailsViewModel
import app.gestionareproduse.products.SingleProductItem
import app.gestionareproduse.products.domain.Product
import app.gestionareproduse.utils.ProductPriceType
import app.gestionareproduse.utils.Utils
import coil.compose.rememberImagePainter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.util.*

@Composable
fun ProductDetailsScreen(
    viewModel: ProductsDetailsViewModel = hiltViewModel(),
    controller: NavController
){
    var selectedProduct = Utils.stringToProduct(Utils.selectedProductString)
    val isRefrigerated = remember{ mutableStateOf(selectedProduct.isRefrigerated)}
    val selectedType = remember{ mutableStateOf(selectedProduct.isPerUnit)}
    val name = remember{ mutableStateOf(selectedProduct.name)}
    val brand = remember{ mutableStateOf(selectedProduct.brand)}
    val price = remember{ mutableStateOf(selectedProduct.price.toString())}
    val expirationDate = remember{ mutableStateOf(Utils.dateToUiString(selectedProduct.expirationDate))}
    val url = remember{ mutableStateOf(selectedProduct.image)}

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        item {
            OutlinedTextField(value = name.value, onValueChange = {
                name.value = it
            },
                label = {
                    Text(text = "Name")
                }
            )
//            Text(
//                text = "Required"
//            )
        }
        item{
            OutlinedTextField(value = brand.value, onValueChange = {
                brand.value = it
            },
                label = {
                    Text(text = "Brand")
                }
            )
//            Text(
//                text = "Required"
//            )
        }
        item{
            OutlinedTextField(value = price.value, onValueChange = {
                price.value = it
            },
                label = {
                    Text(text = "Price")
                }
            )
//            Text(
//                text = "Required"
//            )
        }
        item{

            Row(){
                RadioButton(selected = selectedType.value==true, onClick = {
                    selectedType.value = true
                })
                Spacer(modifier = Modifier.size(16.dp))
                Text("per unit")
                Spacer(modifier = Modifier.size(16.dp))
                RadioButton(selected = selectedType.value==false, onClick = {
                    selectedType.value = false
                })
                Spacer(modifier = Modifier.size(16.dp))
                Text("per Kg")
            }
        }
        item{
            OutlinedTextField(value = expirationDate.value, onValueChange = {
                expirationDate.value = it
            },
                label = {
                    Text(text = "Expiration Date")
                }
            )
//            Text(
//                text = "Required"
//            )
        }
        item{
            Row(){
                Checkbox(checked = isRefrigerated.value == true, onCheckedChange = {
                    isRefrigerated.value = it
                })
                Spacer(modifier = Modifier.size(16.dp))
                Text("is refrigerated")
            }
        }
        item{
            OutlinedTextField(value = url.value, onValueChange = {
                url.value = it
            },
                label = {
                    Text(text = "Image URL")
                }
            )
//            Text(
//                text = "Required"
//            )
        }
        item{
            Image(
                modifier = Modifier
                    .size(200.dp)
                    .padding(8.dp),
                painter = rememberImagePainter(
                    selectedProduct.image
                ),
                contentDescription = null
            )
        }
        item{
            Row(){
                Button(
                    content = {
                        Text(text = "Renunta")
                    },
                    onClick = {
                        controller.popBackStack()
                    }
                )
                Button(
                    content = {
                        Text(text = "Modifica")
                    },
                    onClick = {
                        selectedProduct.isPerUnit = selectedType.value
                        selectedProduct.isRefrigerated = isRefrigerated.value
                        selectedProduct.name = name.value
                        selectedProduct.brand= brand.value
                        selectedProduct.price = price.value.toDouble()
                        selectedProduct.expirationDate = Utils.stringToDate(expirationDate.value)
                        selectedProduct.image = url.value
                        viewModel.updateProduct(selectedProduct)
                        controller.popBackStack()
                    }
                )
                Button(
                    content = {
                        Text(text = "Sterge")
                    },
                    onClick = {
                        viewModel.deleteProduct(selectedProduct)
                        controller.popBackStack()
                    }
                )
            }
        }

    }
}