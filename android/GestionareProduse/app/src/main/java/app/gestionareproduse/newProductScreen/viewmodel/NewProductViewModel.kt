package app.gestionareproduse.newProductScreen.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.gestionareproduse.products.domain.Product
import app.gestionareproduse.products.usecase.ProductsUseCase
import app.gestionareproduse.utils.networkConnectivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

    val syncProducts: () -> Unit = {
        viewModelScope.launch {
            useCase.syncProducts()
        }
    }

    fun saveProduct(context: Context, product: Product, showMessage: (Boolean, String) -> Unit, progressIndicatorVisibility : MutableState<Boolean>) {
        viewModelScope.launch(Dispatchers.IO){
            val connectivity = networkConnectivity(context, syncProducts)
            if (!connectivity) {
                showMessage(true, "No internet connection!")
                product.isUploaded = false
                useCase.saveProductLocally(product)
            } else {
                product.isUploaded = true
                useCase.saveProduct(product, showMessage, progressIndicatorVisibility)
            }
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