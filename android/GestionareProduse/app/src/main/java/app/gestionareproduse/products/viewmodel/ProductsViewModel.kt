package app.gestionareproduse.products.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.gestionareproduse.products.domain.Product
import app.gestionareproduse.products.usecase.ProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    useCase: ProductsUseCase
) : ViewModel(){
    //lista mutabila
    private val _listOfProducts: MutableState<List<Product>> = mutableStateOf(emptyList())
    //lista read-only
    val listOfProducts: State<List<Product>> = _listOfProducts

    init{
        viewModelScope.launch {
            val productList = useCase()
            _listOfProducts.value = productList
        }
    }
}