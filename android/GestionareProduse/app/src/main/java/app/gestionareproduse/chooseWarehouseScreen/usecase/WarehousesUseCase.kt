package app.gestionareproduse.chooseWarehouseScreen.usecase

import app.gestionareproduse.chooseWarehouseScreen.domain.Warehouse
import app.gestionareproduse.chooseWarehouseScreen.repo.WarehouseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface WarehousesUseCase{
    suspend fun getAllWarehouses(): Flow<List<Warehouse>>
}

class WarehousesUseCaseImpl @Inject constructor(
    val repo: WarehouseRepository
) : WarehousesUseCase {
    override suspend fun getAllWarehouses(): Flow<List<Warehouse>> {
        return repo.getAllWarehouses()
    }
}