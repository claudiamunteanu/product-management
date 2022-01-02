package app.gestionareproduse.chooseWarehouseScreen.usecase

import androidx.compose.runtime.MutableState
import app.gestionareproduse.chooseWarehouseScreen.domain.Warehouse
import app.gestionareproduse.chooseWarehouseScreen.repo.WarehouseRepository
import app.gestionareproduse.utils.MyCallback
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface WarehousesUseCase{
    suspend fun getAllWarehouses(showError: (String) -> Unit, progressBarVisibility : MutableState<Boolean>)
    suspend fun getLocalWarehouses(): Flow<List<Warehouse>>
}

class WarehousesUseCaseImpl @Inject constructor(
    val repo: WarehouseRepository
) : WarehousesUseCase {
    override suspend fun getAllWarehouses(showError: (String) -> Unit, progressBarVisibility : MutableState<Boolean>){
        repo.loadAllWarehouses(showError, progressBarVisibility)
    }

    override suspend fun getLocalWarehouses(): Flow<List<Warehouse>>{
        return repo.getLocalWarehouses()
    }
}