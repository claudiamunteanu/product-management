package app.gestionareproduse.products

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import app.gestionareproduse.products.domain.Product
import app.gestionareproduse.products.viewmodel.ProductsViewModel
import app.gestionareproduse.utils.Utils
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import app.gestionareproduse.R
import app.gestionareproduse.ui.theme.Teal200

@Composable
fun ProductsScreen(
    viewModel: ProductsViewModel = hiltViewModel(),
    onProductClick: (Product) -> Unit,
    addProduct: () -> Unit
){
    val listOfProducts by remember { viewModel.listOfProducts }
    LazyColumn{
        items(listOfProducts){item->
            SingleProductItem(
                product = item,
                onProductClick = onProductClick
            )
        }
    }
    FloatingActionButton(onClick = {addProduct()}) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = null)
    }
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
                    text = "Exp: " + Utils.dateToUiString(product.expirationDate),
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