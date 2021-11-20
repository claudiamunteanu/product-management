package app.gestionareproduse.detailsScreen.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.gestionareproduse.products.domain.Product
import app.gestionareproduse.products.usecase.ProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.NumberFormatException
import javax.inject.Inject

@HiltViewModel
class ProductsDetailsViewModel @Inject constructor(
    private val useCase: ProductsUseCase
): ViewModel() {
    val nameHasError = MutableLiveData<String>()
    val brandHasError = MutableLiveData<String>()
    val priceHasError = MutableLiveData<String>()
    val expDateHasError = MutableLiveData<String>()

    val isUpdatedSuccessfully = MutableLiveData<Boolean>()
    val isDeletedSuccessfully = MutableLiveData<Boolean>()

    fun updateProduct(product: Product) = viewModelScope.launch(Dispatchers.IO) {
        try{
            useCase.updateProduct(product)
            isUpdatedSuccessfully.postValue(true)
        } catch (exception : Exception){
            isUpdatedSuccessfully.postValue(false)
            Log.d("error", "", exception)
        }
    }

    fun deleteProduct(productId: Long) = viewModelScope.launch(Dispatchers.IO) {
        try{
            useCase.deleteProduct(productId)
            isDeletedSuccessfully.postValue(true)
        } catch (exception : Exception){
            isDeletedSuccessfully.postValue(false)
            Log.d("error", "", exception)
        }

    }

    fun validateName(name: String): Boolean {
        if (name.isEmpty()) {
            nameHasError.value = "Name is required"
            return true
        }
        if (name.length > 70) {
            nameHasError.value = "Name must not exceed 70 characters"
            return true
        }
        nameHasError.value = ""
        return false
    }

    fun validateBrand(brand: String): Boolean {
        if (brand.isEmpty()) {
            brandHasError.value = "Brand is required"
            return true
        }
        if (brand.length > 30) {
            brandHasError.value = "Brand must not exceed 30 characters"
            return true
        }
        brandHasError.value = ""
        return false
    }

    fun validatePrice(price: String): Boolean {
        if (price.isEmpty()) {
            priceHasError.value = "Price is required"
            return true
        }
        try {
            val priceDouble = price.toDouble()
        } catch (exception: NumberFormatException) {
            priceHasError.value = "You must enter a valid price"
            return true
        }
        priceHasError.value = ""
        return false
    }

    fun validateExpirationDate(expDate: String): Boolean {
        if (expDate.isEmpty()) {
            expDateHasError.value = "Expiration date is required"
            return true
        }
        expDateHasError.value = ""
        return false
    }
}