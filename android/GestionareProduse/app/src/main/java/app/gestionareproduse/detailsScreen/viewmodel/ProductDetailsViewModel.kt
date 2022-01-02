package app.gestionareproduse.detailsScreen.viewmodel

import android.content.Context
import android.net.ConnectivityManager
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
class ProductsDetailsViewModel @Inject constructor(
    private val useCase: ProductsUseCase
): ViewModel() {
    val nameHasError = MutableLiveData<String>()
    val brandHasError = MutableLiveData<String>()
    val priceHasError = MutableLiveData<String>()
    val expDateHasError = MutableLiveData<String>()

    val syncProducts: () -> Unit = {
        viewModelScope.launch {
            useCase.syncProducts()
        }
    }

    fun updateProduct(context: Context, product: Product, showMessage: (Boolean, String) -> Unit, progressIndicatorVisibility : MutableState<Boolean>){
        viewModelScope.launch(Dispatchers.IO){
            val connectivity = networkConnectivity(context, syncProducts)
            if (!connectivity) {
                showMessage(false, "No internet connection! Cannot update product!")
            }else{
                useCase.updateProduct(product, showMessage, progressIndicatorVisibility)
            }
        }
    }

    fun deleteProduct(context: Context, productId: Long, showMessage: (Boolean, String) -> Unit, progressIndicatorVisibility : MutableState<Boolean>) {
        viewModelScope.launch(Dispatchers.IO){
            val connectivity = networkConnectivity(context, syncProducts)
            if (!connectivity) {
                showMessage(false, "No internet connection! Cannot delete product!")
            }else {
                useCase.deleteProduct(productId, showMessage, progressIndicatorVisibility)
            }
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