package app.gestionareproduse.chooseWarehouseScreen.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.*
import app.gestionareproduse.chooseWarehouseScreen.domain.Warehouse
import app.gestionareproduse.chooseWarehouseScreen.usecase.WarehousesUseCase
import app.gestionareproduse.utils.MyCallback
import app.gestionareproduse.utils.networkConnectivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ChooseWarehouseViewModel @Inject constructor(
    private val useCase: WarehousesUseCase
) : ViewModel() {

    lateinit var listOfWarehouses : LiveData<List<Warehouse>>
    val isRetrievedSuccessfully = MutableLiveData<Boolean>()

    init{
        viewModelScope.launch {
            try{
                listOfWarehouses = useCase.getLocalWarehouses().asLiveData()
                isRetrievedSuccessfully.postValue(true)
            } catch (exception : Exception){
                isRetrievedSuccessfully.postValue(false)
                Log.d("error", "Could not load local data", exception)
            }
        }
    }

    fun loadWarehouses(context: Context, showError: (String) -> Unit, progressBarVisibility : MutableState<Boolean>){
        viewModelScope.launch {
            val connectivity = networkConnectivity(context)
            if (!connectivity) {
                showError("No internet connection! Showing local data")
            } else {
                useCase.getAllWarehouses(showError, progressBarVisibility)
            }
        }
    }
}