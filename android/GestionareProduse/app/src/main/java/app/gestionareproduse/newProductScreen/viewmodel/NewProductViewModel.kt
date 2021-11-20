package app.gestionareproduse.newProductScreen.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.gestionareproduse.products.domain.Product
import app.gestionareproduse.products.usecase.ProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.NumberFormatException
import javax.inject.Inject

@HiltViewModel
class NewProductViewModel @Inject constructor(
    private val useCase: ProductsUseCase
) : ViewModel() {
    val nameError = MutableLiveData<String>()
    val brandError = MutableLiveData<String>()
    val priceError = MutableLiveData<String>()
    val expDateError = MutableLiveData<String>()

    val isSavedSuccessfully = MutableLiveData<Boolean>()

    fun saveProduct(product: Product) = viewModelScope.launch(Dispatchers.IO) {
        try{
            useCase.saveProduct(product)
            isSavedSuccessfully.postValue(true)
        } catch (exception : Exception){
            isSavedSuccessfully.postValue(false)
            Log.d("error", "", exception)
        }
    }


    fun validateName(name: String): Boolean {
        if (name.isEmpty()) {
            nameError.value = "Name is required"
            return true
        }
        if (name.length > 70) {
            nameError.value = "Name must not exceed 70 characters"
            return true
        }
        nameError.value = ""
        return false
    }

    fun validateBrand(brand: String): Boolean {
        if (brand.isEmpty()) {
            brandError.value = "Brand is required"
            return true
        }
        if (brand.length > 30) {
            brandError.value = "Brand must not exceed 30 characters"
            return true
        }
        brandError.value = ""
        return false
    }

    fun validatePrice(price: String): Boolean {
        if (price.isEmpty()) {
            priceError.value = "Price is required"
            return true
        }
        try {
            val priceDouble = price.toDouble()
        } catch (exception: NumberFormatException) {
            priceError.value = "You must enter a valid price"
            return true
        }
        priceError.value = ""
        return false
    }

    fun validateExpirationDate(expDate: String): Boolean {
        if (expDate.isEmpty()) {
            expDateError.value = "Expiration date is required"
            return true
        }
        expDateError.value = ""
        return false
    }
}