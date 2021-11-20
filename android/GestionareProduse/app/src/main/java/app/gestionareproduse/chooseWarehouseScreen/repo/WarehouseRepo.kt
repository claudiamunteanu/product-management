package app.gestionareproduse.chooseWarehouseScreen.repo

import app.gestionareproduse.chooseWarehouseScreen.domain.Warehouse
import kotlinx.coroutines.flow.Flow

interface WarehouseRepository{
    fun getAllWarehouses(): Flow<List<Warehouse>>
}

class WarehouseRepositoryImpl(
    private val warehouseDao: WarehouseDao
) : WarehouseRepository {

    override fun getAllWarehouses(): Flow<List<Warehouse>> {
        return warehouseDao.getAllWarehouses()
    }
}