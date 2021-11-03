package app.gestionareproduse.newProduct.ViewModel

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import app.gestionareproduse.products.domain.Product
import app.gestionareproduse.products.usecase.ProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Exception
import java.lang.NumberFormatException
import javax.inject.Inject

@HiltViewModel
class NewProductViewModel @Inject constructor(
    private val useCase: ProductsUseCase
) : ViewModel() {
    val nameHasError = MutableLiveData<String>()
    val brandHasError = MutableLiveData<String>()
    val priceHasError = MutableLiveData<String>()
    val expDateHasError = MutableLiveData<String>()

    fun saveProduct(product: Product) {
        useCase.saveProduct(product)
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

    fun getSize() : Int {
        return useCase.getSize()
    }
}