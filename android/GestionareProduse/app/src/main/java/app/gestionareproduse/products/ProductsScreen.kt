package app.gestionareproduse.products

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import app.gestionareproduse.products.domain.Product
import app.gestionareproduse.products.viewmodel.ProductsViewModel
import app.gestionareproduse.utils.Utils
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import app.gestionareproduse.R
import app.gestionareproduse.products.domain.SortField
import app.gestionareproduse.products.domain.getAllSortFields
import app.gestionareproduse.products.domain.getSortField
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

internal var shouldGetListFromDatabase = true

@Composable
fun ProductsScreen(
    viewModel: ProductsViewModel = hiltViewModel(),
    onProductClick: (Product) -> Unit,
    addProduct: () -> Unit,
    controller: NavController,
    encodedWarehouse: String
){
    val listOfProducts by viewModel.listOfProducts.observeAsState(listOf())

    val context = LocalContext.current

    val selectedField: MutableState<SortField?> = remember { mutableStateOf(SortField.EXP_DATE) }

    val warehouseString = URLDecoder.decode(encodedWarehouse, StandardCharsets.UTF_8.toString())
    val warehouse = Utils.stringToWarehouse(warehouseString)
    if (shouldGetListFromDatabase) {
        selectedField.value?.let { warehouse.id?.let { it1 -> viewModel.getAllProducts(it, it1) } }
        shouldGetListFromDatabase = false
    }

    viewModel.isRetrievedSuccessfully.observe(LocalLifecycleOwner.current){
        if(it == false){
            Toast.makeText(
                context,
                "There was a problem in retrieving the products!",
                Toast.LENGTH_LONG
            ).show()
        } else {
            selectedField.value?.let { it1 -> viewModel.sortProducts(it1) }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {addProduct()}) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = null)

            }
        },
        topBar = {
            TopAppBar(
                title = { Text(warehouse.name) },
                navigationIcon = {
                    IconButton(onClick = { shouldGetListFromDatabase = true
                        controller.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack,null)
                    }
                }
            )
        },
        content = {
            Column{
                ChipGroup(
                    sortFields = getAllSortFields(),
                    selectedField = selectedField.value,
                    onSelectionChanged = {
                        selectedField.value = getSortField(it)
                        selectedField.value?.let { it1 -> viewModel.sortProducts(it1) }
                    }
                )
                LazyColumn{
                    items(listOfProducts){item->
                        SingleProductItem(
                            product = item,
                            onProductClick = onProductClick
                        )
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun SingleProductItem(
    product: Product,
    onProductClick: (Product) -> Unit
){
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .size(0.dp, 120.dp)
            .clickable { // handling onMovieClick
                onProductClick(product)
            },
        elevation = 8.dp,
        backgroundColor = Utils.BackgroundColorByExpirationDate(product.expirationDate)
    ){
        Row(
            modifier = Modifier.padding(8.dp, 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                modifier = Modifier
                    .size(80.dp, 100.dp)
                    .weight(1F)
                    .padding(0.dp, 0.dp, 8.dp, 0.dp),
                painter = rememberImagePainter(
                    product.image
                ),
                contentDescription = null
            )
            Column(
                modifier = Modifier.weight(2F)
            ) {
                Text(
                    text = product.name,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 16.sp,
                    maxLines = 1
                )
                Text(
                    modifier = Modifier.padding(0.dp, 8.dp),
                    text = product.brand,
                    fontSize = 14.sp
                )
                Text(
                    text = "Exp: " + Utils.dateToString(product.expirationDate),
                    fontSize = 14.sp
                )
            }
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.weight(1F)
            ) {
                Text(
                    text = if (product.isPerUnit) product.price.toString()+" Lei" else product.price.toString()+" Lei\n/kg",
                    fontSize = 20.sp,
                    textAlign = TextAlign.End,
                    modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 4.dp)
                )
                if(product.isRefrigerated){
                    Icon(
                        modifier = Modifier.padding(0.dp, 4.dp, 0.dp, 0.dp),
                        painter = painterResource(id = R.drawable.ic_baseline_ac_unit_24),
                        contentDescription = null,
                        tint = Color(0xFF3781FF),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Chip(
    name: String = "Chip",
    isSelected: Boolean = false,
    onSelectionChanged: (String) -> Unit = {}
){
    Surface(
        modifier = Modifier.padding(4.dp),
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) Color(0xFFD3D3D3) else Color(0x1F232F34),

    ){
        Row(
            modifier = Modifier.toggleable(
                value = isSelected,
                onValueChange = {
                    onSelectionChanged(name)
                }
            )
        ){
            if(isSelected){
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = null,
                    tint = Color(0xFF252525),
                    modifier = Modifier
                        .size(25.dp, 20.dp)
                        .align(Alignment.CenterVertically)
                        .padding(5.dp, 0.dp, 0.dp, 0.dp),
                )
            }
            Text(
                text = name,
                style = MaterialTheme.typography.body2,
                color = if (isSelected) Color(0xFF252525) else Color(0xFF232F34),
                modifier = Modifier.padding(10.dp, 5.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChipGroup(
    sortFields : List<SortField> = getAllSortFields(),
    selectedField: SortField? = null,
    onSelectionChanged: (String) -> Unit = {}
){
    Column(modifier = Modifier.padding(8.dp,8.dp,0.dp, 0.dp)) {
        LazyRow{
            items(sortFields){
                Chip(
                    name = it.value,
                    isSelected = selectedField == it,
                    onSelectionChanged = {
                        onSelectionChanged(it)
                    }
                )
            }
        }
    }
}