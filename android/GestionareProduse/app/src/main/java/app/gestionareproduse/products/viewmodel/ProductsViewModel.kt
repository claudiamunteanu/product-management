package app.gestionareproduse.products.viewmodel

import android.util.Log
import androidx.lifecycle.*
import app.gestionareproduse.products.domain.Product
import app.gestionareproduse.products.domain.SortField
import app.gestionareproduse.products.usecase.ProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject
import kotlinx.coroutines.flow.collect

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val useCase: ProductsUseCase
) : ViewModel(){

    var listOfProducts : MutableLiveData<List<Product>> = MutableLiveData<List<Product>>()

    val isRetrievedSuccessfully = MutableLiveData<Boolean>()

    fun getAllProducts(field: SortField, warehouseId: Long){
        viewModelScope.launch {
            try{
                useCase.getAllProducts(field, warehouseId).collect{
                    listOfProducts.postValue(it)
                }
                isRetrievedSuccessfully.postValue(true)
            } catch (exception : Exception){
                isRetrievedSuccessfully.postValue(false)
                Log.d("error", "", exception)
            }
        }
    }
}