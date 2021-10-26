package app.gestionareproduse.details

import android.widget.CalendarView
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import app.gestionareproduse.details.viewmodel.ProductsDetailsViewModel
import app.gestionareproduse.utils.DatePickerview
import app.gestionareproduse.utils.Utils
import coil.compose.rememberImagePainter

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
    val url = remember{ mutableStateOf(selectedProduct.image)}

    var expirationDate = remember{ mutableStateOf(Utils.dateToUiString(selectedProduct.expirationDate))}
    val updatedDate = {date : Long ->
        expirationDate.value = Utils.millisecondsToStringDate(date)
    }

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(50.dp, 20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ){
        item {
            Column(){
                OutlinedTextField(
                    value = name.value,
                    onValueChange = {
                        name.value = it
                    },
                    label = {
                        Text(text = "Name")
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "Required",
                    color = Color.Gray,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
        item{
            Column(){
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = brand.value,
                    onValueChange = {
                        brand.value = it
                    },
                    label = {
                        Text(text = "Brand")
                    }
                )
                Text(
                    text = "Required",
                    color = Color.Gray,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
        item{
            Column() {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = price.value,
                    onValueChange = {
                        price.value = it
                    },
                    label = {
                        Text(text = "Price")
                    }
                )
                Text(
                    text = "Required",
                    color = Color.Gray,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
        item{
            Row(
                modifier = Modifier.fillMaxWidth(),
            ){
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
            Column() {
                DatePickerview( expirationDate.value, updatedDate )
                Text(
                    text = "Required",
                    color = Color.Gray,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
        item{
            Row(
                modifier = Modifier.fillMaxWidth(),
            ){
                Checkbox(
                    checked = isRefrigerated.value == true,
                    onCheckedChange = {
                    isRefrigerated.value = it
                })
                Spacer(modifier = Modifier.size(16.dp))
                Text("is refrigerated")
            }
        }
        item{
            Column() {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = url.value,
                    onValueChange = {
                        url.value = it
                    },
                    label = {
                        Text(text = "Image URL")
                    }
                )
                Text(
                    text = "Required",
                    color = Color.Gray,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Button(
                    content = {
                        Text(text = "Cancel")
                    },
                    onClick = {
                        controller.popBackStack()
                    }
                )
                Button(
                    content = {
                        Text(text = "Update")
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
                val openDialog = remember { mutableStateOf(false)  }
                Button(
                    content = {
                        Text(text = "Delete")
                    },
                    onClick = {
                        openDialog.value = true
                    }
                )
                if (openDialog.value) {
                    val context = LocalContext.current
                    AlertDialog(
                        onDismissRequest = {
                            // Dismiss the dialog when the user clicks outside the dialog or on the back
                            // button. If you want to disable that functionality, simply use an empty
                            // onCloseRequest.
                            openDialog.value = false
                        },
                        title = {
                            Text(text = "Do you wish to delete the product?")
                        },
                        dismissButton = {
                            Button(
                                onClick = {
                                    openDialog.value = false
                                }) {
                                Text("No")
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    openDialog.value = false
                                    viewModel.deleteProduct(selectedProduct)
                                    Toast.makeText(
                                        context,
                                        "Product deleted successfully!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    controller.popBackStack()
                                }) {
                                Text("Yes")
                            }
                        }
                    )
                }
            }
        }

    }
}