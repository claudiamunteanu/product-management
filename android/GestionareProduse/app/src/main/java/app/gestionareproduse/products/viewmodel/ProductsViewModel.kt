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

    private var _listOfProducts : MutableLiveData<List<Product>> = MutableLiveData<List<Product>>(listOf())

    val listOfProducts : LiveData<List<Product>> = _listOfProducts

    val isRetrievedSuccessfully = MutableLiveData<Boolean>()

    fun getAllProducts(field: SortField, warehouseId: Long){
        viewModelScope.launch {
            try{
                useCase.getAllProducts(field, warehouseId).collect{
                    _listOfProducts.postValue(it)
                    isRetrievedSuccessfully.postValue(true)
                }
            } catch (exception : Exception){
                isRetrievedSuccessfully.postValue(false)
                Log.d("error", "", exception)
            }
        }
    }

    fun sortProducts(field: SortField){
        var list = _listOfProducts.value
        _listOfProducts.value = list?.sortedWith(
            when (field) {
                SortField.EXP_DATE -> compareBy { it.expirationDate }
                SortField.NAME -> compareBy { it.name }
                SortField.BRAND -> compareBy { it.brand }
                SortField.PRICE -> compareBy { it.price }
            }
        )
    }
}