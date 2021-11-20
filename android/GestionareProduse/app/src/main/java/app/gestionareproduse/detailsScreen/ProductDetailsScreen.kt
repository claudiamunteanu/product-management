package app.gestionareproduse.detailsScreen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import app.gestionareproduse.R
import app.gestionareproduse.detailsScreen.viewmodel.ProductsDetailsViewModel
import app.gestionareproduse.utils.DatePickerview
import app.gestionareproduse.utils.Utils
import coil.compose.rememberImagePainter
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun ProductDetailsScreen(
    viewModel: ProductsDetailsViewModel = hiltViewModel(),
    controller: NavController,
    encodedProduct : String
){
    val productString = URLDecoder.decode(encodedProduct, StandardCharsets.UTF_8.toString())
    var selectedProduct = Utils.stringToProduct(productString)

    //Date produs
    val isRefrigerated = remember{ mutableStateOf(selectedProduct.isRefrigerated)}
    val selectedType = remember{ mutableStateOf(selectedProduct.isPerUnit)}
    val name = remember{ mutableStateOf(selectedProduct.name)}
    val brand = remember{ mutableStateOf(selectedProduct.brand)}
    val price = remember{ mutableStateOf(selectedProduct.price.toString())}
    val url = remember{ mutableStateOf(selectedProduct.image)}

    var expirationDate = remember{ mutableStateOf(Utils.dateToString(selectedProduct.expirationDate))}
    val updatedDate = {date : Long ->
        expirationDate.value = Utils.millisecondsToStringDate(date)
    }

    val openDeleteDialog = remember { mutableStateOf(false)  }
    val openUpdateDialog = remember { mutableStateOf(false)}
    val changed = remember { mutableStateOf(false)}

    val isErrorName = remember { mutableStateOf(false) }
    val isErrorBrand = remember { mutableStateOf(false) }
    val isErrorPrice = remember { mutableStateOf(false) }
    val isErrorExpirationDate = remember { mutableStateOf(false) }

    var nameErrorString = ""
    var brandErrorString = ""
    var priceErrorString = ""
    var expirationDateErrorString = ""

    var isEnabled = remember { mutableStateOf(true) }

    fun isError(){
        isEnabled.value = !isErrorName.value && !isErrorBrand.value && !isErrorPrice.value && !isErrorExpirationDate.value
    }

    viewModel.nameHasError.observe(LocalLifecycleOwner.current) {
        nameErrorString = it }
    viewModel.brandHasError.observe(LocalLifecycleOwner.current) {
        brandErrorString = it }
    viewModel.priceHasError.observe(LocalLifecycleOwner.current) {
        priceErrorString = it   }
    viewModel.expDateHasError.observe(LocalLifecycleOwner.current) {
        expirationDateErrorString = it  }

    val focusManager = LocalFocusManager.current

    val context = LocalContext.current
    viewModel.isUpdatedSuccessfully.observe(LocalLifecycleOwner.current){
        if(it){
            Toast.makeText(
                context,
                "Product updated successfully!",
                Toast.LENGTH_LONG
            ).show()
        } else {
            Toast.makeText(
                context,
                "There was a problem in updating the product!",
                Toast.LENGTH_LONG
            ).show()
        }
        controller.popBackStack()
    }
    viewModel.isDeletedSuccessfully.observe(LocalLifecycleOwner.current){
        if(it){
            Toast.makeText(
                context,
                "Product deleted successfully!",
                Toast.LENGTH_LONG
            ).show()
        } else {
            Toast.makeText(
                context,
                "There was a problem in deleting the product!",
                Toast.LENGTH_LONG
            ).show()
        }
        controller.popBackStack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("View Product") },
                navigationIcon = {
                    IconButton(onClick = {
                        if (!changed.value || !isEnabled.value)
                            controller.popBackStack()
                        else
                            openUpdateDialog.value = true
                    }) {
                        Icon(Icons.Filled.ArrowBack,null)
                    }
                }
            )
        },
        content = {
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
                                changed.value = true
                                isErrorName.value = viewModel.validateName(name.value)
                                isError()
                            },
                            label = {
                                Text(text = "Name")
                            },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(onNext = {
                                isErrorName.value = viewModel.validateName(name.value)
                                focusManager.moveFocus(FocusDirection.Down)
                            }),
                            trailingIcon = {
                                if (isErrorName.value) {
                                    Icon(Icons.Filled.Error, "error", tint = MaterialTheme.colors.error)
                                }
                            }
                        )
                        assistiveOrErrorText(isError = isErrorName.value, errorString = nameErrorString)
                    }
                }
                item{
                    Column(){
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = brand.value,
                            onValueChange = {
                                brand.value = it
                                changed.value = true
                                isErrorBrand.value = viewModel.validateBrand(brand.value)
                                isError()
                            },
                            label = {
                                Text(text = "Brand")
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(onNext = {
                                isErrorBrand.value = viewModel.validateBrand(brand.value)
                                focusManager.moveFocus(FocusDirection.Down)
                            }),
                            trailingIcon = {
                                if (isErrorBrand.value) {
                                    Icon(Icons.Filled.Error, "error", tint = MaterialTheme.colors.error)
                                }
                            }
                        )
                        assistiveOrErrorText(isError = isErrorBrand.value, errorString = brandErrorString)
                    }
                }
                item{
                    Column() {
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = price.value,
                            onValueChange = {
                                price.value = it
                                changed.value = true
                                isErrorPrice.value = viewModel.validatePrice(price.value)
                                isError()
                            },
                            label = {
                                Text(text = "Price")
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(onNext = {
                                isErrorPrice.value = viewModel.validatePrice(price.value)
                                focusManager.moveFocus(FocusDirection.Down)
                            }),
                            trailingIcon = {
                                if (isErrorPrice.value) {
                                    Icon(Icons.Filled.Error, "error", tint = MaterialTheme.colors.error)
                                }
                            }
                        )
                        assistiveOrErrorText(isError = isErrorPrice.value, errorString = priceErrorString)
                    }
                }
                item{
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    ){
                        RadioButton(selected = selectedType.value==true, onClick = {
                            selectedType.value = true
                            changed.value = true
                        })
                        Spacer(modifier = Modifier.size(16.dp))
                        Text("per unit")
                        Spacer(modifier = Modifier.size(16.dp))
                        RadioButton(selected = selectedType.value==false, onClick = {
                            selectedType.value = false
                            changed.value = true
                        })
                        Spacer(modifier = Modifier.size(16.dp))
                        Text("per Kg")
                    }
                }
                item{
                    Column() {
                        DatePickerview(expirationDate.value, updatedDate, changed, isErrorExpirationDate,
                            { isError() })
                        assistiveOrErrorText(
                            isError = isErrorExpirationDate.value,
                            errorString = expirationDateErrorString
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
                                changed.value = true
                            })
                        Spacer(modifier = Modifier.size(16.dp))
                        Text("is refrigerable")
                    }
                }
                item{
                    Column() {
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = url.value,
                            onValueChange = {
                                url.value = it
                                changed.value = true
                            },
                            label = {
                                Text(text = "Image URL")
                            },
                            maxLines = 1,
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(onDone = {
                                focusManager.clearFocus()
                            }),
                        )
                    }
                }
                item{
                    Image(
                        modifier = Modifier
                            .size(200.dp)
                            .padding(8.dp),
                        painter = rememberImagePainter(
                            if (url.value.isNotEmpty()) {
                                url.value
                            } else {
                                R.drawable.placeholder_image
                            }
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
                                if (!changed.value || !isEnabled.value)
                                    controller.popBackStack()
                                else
                                    openUpdateDialog.value = true
                            }
                        )

                        val context = LocalContext.current
                        Button(
                            enabled = isEnabled.value,
                            content = {
                                Text(text = "Update")
                            },
                            onClick = {
                                isErrorName.value = viewModel.validateName(name.value)
                                isErrorBrand.value = viewModel.validateBrand(brand.value)
                                isErrorPrice.value = viewModel.validatePrice(price.value)
                                isErrorExpirationDate.value = viewModel.validateExpirationDate(expirationDate.value)
                                isError()
                                if(isEnabled.value){
                                    selectedProduct.isPerUnit = selectedType.value
                                    selectedProduct.isRefrigerated = isRefrigerated.value
                                    selectedProduct.name = name.value
                                    selectedProduct.brand= brand.value
                                    selectedProduct.price = price.value.toDouble()
                                    selectedProduct.expirationDate = Utils.stringToDate(expirationDate.value)
                                    selectedProduct.image = url.value
                                    viewModel.updateProduct(selectedProduct)
                                    Toast.makeText(
                                        context,
                                        "Product updated successfully!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    controller.popBackStack()
                                }
                            }
                        )
                        Button(
                            content = {
                                Text(text = "Delete")
                            },
                            onClick = {
                                openDeleteDialog.value = true
                            }
                        )
                    }
                }

            }
        }
    )

    if(openUpdateDialog.value && changed.value){
        val context = LocalContext.current
        AlertDialog(
            onDismissRequest = {
                openUpdateDialog.value = false
            },
            title = {
                Text(text = "Do you wish to save the modifications?")
            },
            dismissButton = {
                Button(
                    onClick = {
                        openUpdateDialog.value = false
                        changed.value = false
                        controller.popBackStack()
                    }) {
                    Text("No")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        openUpdateDialog.value = false
                        selectedProduct.isPerUnit = selectedType.value
                        selectedProduct.isRefrigerated = isRefrigerated.value
                        selectedProduct.name = name.value
                        selectedProduct.brand= brand.value
                        selectedProduct.price = price.value.toDouble()
                        selectedProduct.expirationDate = Utils.stringToDate(expirationDate.value)
                        selectedProduct.image = url.value
                        viewModel.updateProduct(selectedProduct)
                        Toast.makeText(
                            context,
                            "Product updated successfully!",
                            Toast.LENGTH_LONG
                        ).show()
                        controller.popBackStack()
                    }) {
                    Text("Yes")
                }
            }
        )
    }

    if (openDeleteDialog.value) {
        val context = LocalContext.current
        AlertDialog(
            onDismissRequest = {
                openDeleteDialog.value = false
            },
            title = {
                Text(text = "Do you wish to delete the product?")
            },
            dismissButton = {
                Button(
                    onClick = {
                        openDeleteDialog.value = false
                    }) {
                    Text("No")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        openDeleteDialog.value = false
                        selectedProduct.id?.let { viewModel.deleteProduct(it) }
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

