package app.gestionareproduse.detailsScreen

import android.os.Handler
import android.os.Looper
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import app.gestionareproduse.R
import app.gestionareproduse.newProductScreen.viewmodel.NewProductViewModel
import app.gestionareproduse.products.domain.Product
import app.gestionareproduse.utils.DatePickerview
import app.gestionareproduse.utils.Utils
import coil.compose.rememberImagePainter
import kotlinx.coroutines.launch

@Composable
fun NewProductScreen(
    viewModel: NewProductViewModel = hiltViewModel(),
    controller: NavController,
) {
    //date produs
    val isrefrigerable = remember { mutableStateOf(false) }
    val selectedType = remember { mutableStateOf(false) }
    val name = remember { mutableStateOf("") }
    val brand = remember { mutableStateOf("") }
    val price = remember { mutableStateOf("") }
    val url = remember { mutableStateOf("") }

    var expirationDate = remember { mutableStateOf("") }
    val updatedDate = { date: Long ->
        expirationDate.value = Utils.millisecondsToStringDate(date)
    }

    //Daca s-au produs modificari in fieldurile cu datele produsului
    val changed = remember { mutableStateOf(false) }
    //Daca se deschide dialog in care se intreaba userul daca doreste sa salveze modificarile
    val openSaveDialog = remember { mutableStateOf(false) }

    //Verificam daca avem date invalide
    val isErrorName = remember { mutableStateOf(false) }
    val isErrorBrand = remember { mutableStateOf(false) }
    val isErrorPrice = remember { mutableStateOf(false) }
    val isErrorExpirationDate = remember { mutableStateOf(false) }

    //Mesaje de eroare
    var nameErrorString = ""
    var brandErrorString = ""
    var priceErrorString = ""
    var expirationDateErrorString = ""

    //Daca butonul Save este enabled sau nu, in functie de corectitudinea fieldurilor
    var isEnabled = remember { mutableStateOf(true) }

    //Verificam daca exista eroare
    fun isError(){
        isEnabled.value = !isErrorName.value && !isErrorBrand.value && !isErrorPrice.value && !isErrorExpirationDate.value
    }

    //Salvam mesajele de eroare
    viewModel.nameError.observe(LocalLifecycleOwner.current) {
        nameErrorString = it }
    viewModel.brandError.observe(LocalLifecycleOwner.current) {
        brandErrorString = it }
    viewModel.priceError.observe(LocalLifecycleOwner.current) {
        priceErrorString = it   }
    viewModel.expDateError.observe(LocalLifecycleOwner.current) {
        expirationDateErrorString = it  }

    val context = LocalContext.current

    val progressIndicatorVisibility = remember{ mutableStateOf(false) }

    val snackbarHostState = remember{mutableStateOf(SnackbarHostState())}
    val snackbarCoroutineScope = rememberCoroutineScope()

    val showMessage: (Boolean, String) -> Unit = { isSuccessful: Boolean, message: String ->
        if(isSuccessful){
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    context,
                    message,
                    Toast.LENGTH_LONG
                ).show()
                controller.popBackStack()
            }
        } else {
            snackbarCoroutineScope.launch {
                snackbarHostState.value.showSnackbar(
                    message = message,
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    val focusManager = LocalFocusManager.current

    fun trySaveProduct(){
        isErrorName.value = viewModel.validateName(name.value)
        isErrorBrand.value = viewModel.validateBrand(brand.value)
        isErrorPrice.value = viewModel.validatePrice(price.value)
        isErrorExpirationDate.value = viewModel.validateExpirationDate(expirationDate.value)
        isError()
        if (isEnabled.value) {
            val selectedProduct = Product(
                name = name.value,
                brand = brand.value,
                price = price.value.toDouble(),
                isPerUnit = selectedType.value,
                expirationDate = Utils.stringToDate(expirationDate.value),
                isRefrigerated = isrefrigerable.value,
                image = url.value,
                warehouseId = 1L
            )
            viewModel.saveProduct(context, selectedProduct, showMessage, progressIndicatorVisibility)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Product") },
                navigationIcon = {
                    IconButton(onClick = {
                        if (!changed.value || !isEnabled.value) //Daca nu s-a modificat nimic sau butonul de Save nu este enabled
                            controller.popBackStack()
                        else
                            openSaveDialog.value = true //deschidem fereastra dialog
                    }) {
                        Icon(Icons.Filled.ArrowBack,null)
                    }
                }
            )
        },
        content = {
            Box(
                modifier = Modifier.fillMaxSize()
            ){
                if(progressIndicatorVisibility.value){
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center).size(100.dp)
                    )
                }
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(50.dp, 20.dp).align(Alignment.Center),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        Column() {
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
                    item {
                        Column() {
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
                    item {
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
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            RadioButton(selected = selectedType.value == true, onClick = {
                                selectedType.value = true
                                changed.value = true
                            })
                            Spacer(modifier = Modifier.size(16.dp))
                            Text("per unit")
                            Spacer(modifier = Modifier.size(16.dp))
                            RadioButton(selected = selectedType.value == false, onClick = {
                                selectedType.value = false
                                changed.value = true
                            })
                            Spacer(modifier = Modifier.size(16.dp))
                            Text("per Kg")
                        }
                    }
                    item {
                        Column() {
                            DatePickerview(expirationDate.value, updatedDate, changed, isErrorExpirationDate,
                                { isError() })
                            assistiveOrErrorText(
                                isError = isErrorExpirationDate.value,
                                errorString = expirationDateErrorString
                            )
                        }
                    }
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Checkbox(
                                checked = isrefrigerable.value == true,
                                onCheckedChange = {
                                    isrefrigerable.value = it
                                    changed.value = true
                                })
                            Spacer(modifier = Modifier.size(16.dp))
                            Text("is refrigerable")
                        }
                    }
                    item {
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
                    item {
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
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(
                                content = {
                                    Text(text = "Cancel")
                                },
                                onClick = {
                                    if (!changed.value || !isEnabled.value) //Daca nu s-a modificat nimic sau butonul de Save nu este enabled
                                        controller.popBackStack()
                                    else
                                        openSaveDialog.value = true //deschidem fereastra dialog
                                }
                            )
                            Button(
                                enabled = isEnabled.value,
                                content = {
                                    Text(text = "Save")
                                },
                                onClick = {
                                    trySaveProduct()
                                }
                            )
                        }
                    }
                }
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState.value,
                snackbar = {
                    Snackbar(
                        modifier = Modifier.padding(8.dp),
                    ) { Text(text = it.message) }
                }
            )
        }
    )

    if (openSaveDialog.value && changed.value) {
        val context = LocalContext.current
        AlertDialog(
            onDismissRequest = {
                openSaveDialog.value = false
            },
            title = {
                Text(text = "Do you wish to save?")
            },
            dismissButton = {
                Button(
                    onClick = {
                        openSaveDialog.value = false
                        changed.value = false
                        controller.popBackStack()
                    }) {
                    Text("No")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        openSaveDialog.value = false
                        trySaveProduct()
                    }) {
                    Text("Yes")
                }
            }
        )
    }
}

@Composable
fun assistiveOrErrorText(
    isError: Boolean,
    errorString: String
) {
    if (isError) {
        Text(
            text = errorString,
            color = MaterialTheme.colors.error,
            style = MaterialTheme.typography.caption,
            modifier = Modifier.padding(start = 16.dp)
        )
    } else {
        Text(
            text = "Required",
            color = Color.Gray,
            style = MaterialTheme.typography.caption,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}