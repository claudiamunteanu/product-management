package app.gestionareproduse.chooseWarehouseScreen.viewmodel

import android.util.Log
import androidx.lifecycle.*
import app.gestionareproduse.chooseWarehouseScreen.domain.Warehouse
import app.gestionareproduse.chooseWarehouseScreen.usecase.WarehousesUseCase
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
                listOfWarehouses = useCase.getAllWarehouses().asLiveData()
                isRetrievedSuccessfully.postValue(true)
            } catch (exception : Exception){
                isRetrievedSuccessfully.postValue(false)
                Log.d("error", "", exception)
            }
        }
    }
}