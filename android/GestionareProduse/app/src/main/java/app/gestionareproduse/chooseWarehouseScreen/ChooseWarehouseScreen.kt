package app.gestionareproduse.chooseWarehouseScreen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import app.gestionareproduse.chooseWarehouseScreen.domain.Warehouse
import app.gestionareproduse.chooseWarehouseScreen.viewmodel.ChooseWarehouseViewModel
import kotlinx.coroutines.launch

internal var shouldGetListFromDatabase = true

@Composable
fun ChooseWarehouseScreen(
    viewModel: ChooseWarehouseViewModel = hiltViewModel(),
    onSubmit: (Warehouse) -> Unit
) {
    val listOfWarehouses by viewModel.listOfWarehouses.observeAsState(listOf())

    val context = LocalContext.current

    val snackbarHostState = remember { mutableStateOf(SnackbarHostState()) }
    val snackbarCoroutineScope = rememberCoroutineScope()

    val showError: (String) -> Unit = {
        snackbarCoroutineScope.launch {
            snackbarHostState.value.showSnackbar(
                message = it,
                duration = SnackbarDuration.Indefinite
            )
        }
    }

    val progressIndicatorVisibility = remember { mutableStateOf(false) }

    if (shouldGetListFromDatabase) {
        viewModel.loadWarehouses(context, showError, progressIndicatorVisibility)
        shouldGetListFromDatabase = false
    }

    val selectedWarehouse: MutableState<Warehouse?> = remember { mutableStateOf(null) }

    val updateWarehouse = { warehouse: Warehouse ->
        selectedWarehouse.value = warehouse
    }

    viewModel.isRetrievedSuccessfully.observe(LocalLifecycleOwner.current) {
        if (it == false) {
            Toast.makeText(
                context,
                "There was a problem in retrieving the warehouses!",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestiunea produselor") }
            )
        },
        content = {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                if (progressIndicatorVisibility.value) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(100.dp)
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(50.dp, 0.dp),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (listOfWarehouses.size > 0) {
                        selectedWarehouse.value = listOfWarehouses[0]
                        DropDown(
                            optionsList = listOfWarehouses,
                            updateWarehouse = updateWarehouse
                        )
                        Button(
                            content = {
                                Text(text = "Submit")
                            },
                            onClick = {
                                shouldGetListFromDatabase = true
                                onSubmit(selectedWarehouse.value!!)
                            }
                        )
                    }
                }
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState.value,
                snackbar = {
                    Snackbar(
                        action = {
                            if (listOfWarehouses.size == 0) {
                                Button(onClick = {
                                    snackbarHostState.value.currentSnackbarData?.dismiss()
                                    viewModel.loadWarehouses(
                                        context,
                                        showError,
                                        progressIndicatorVisibility
                                    )
                                }) {
                                    Text("RETRY")
                                }
                            } else {
                                Button(onClick = {
                                    snackbarHostState.value.currentSnackbarData?.dismiss()
                                }) {
                                    Text("CLOSE")
                                }
                            }
                        },
                        modifier = Modifier.padding(8.dp),
                    ) { Text(text = it.message) }
                }
            )
        }
    )
}

@Composable
fun DropDown(
    optionsList: List<Warehouse>,
    updateWarehouse: (warehouse: Warehouse) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    var selectedWarehouse: Warehouse by remember { mutableStateOf(optionsList[0]) }

    var textfieldSize by remember { mutableStateOf(Size.Zero) }

    val icon = if (expanded)
        Icons.Filled.ArrowDropUp //it requires androidx.compose.material:material-icons-extended
    else
        Icons.Filled.ArrowDropDown


    Column {
        OutlinedTextField(
            value = selectedWarehouse.name ?: "",
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    //This value is used to assign to the DropDown the same width
                    textfieldSize = coordinates.size.toSize()
                }
                .clickable { expanded = !expanded },
            label = { Text("Choose warehouse") },
            trailingIcon = {
                Icon(icon, null)
            },
            enabled = false,
            colors = TextFieldDefaults.textFieldColors(
                disabledIndicatorColor = Color.Black,
                disabledLabelColor = Color.Black,
                disabledTextColor = Color.Black,
                disabledTrailingIconColor = Color.Black
            )
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { textfieldSize.width.toDp() })
        ) {
            optionsList.forEach { warehouse ->
                DropdownMenuItem(onClick = {
                    selectedWarehouse = warehouse
                    updateWarehouse(selectedWarehouse!!)
                    expanded = false
                }) {
                    Text(text = warehouse.name)
                }
            }
        }
    }
}