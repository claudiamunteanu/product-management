package app.gestionareproduse.chooseWarehouseScreen.repo

import androidx.room.Dao
import androidx.room.Query
import app.gestionareproduse.chooseWarehouseScreen.domain.Warehouse
import kotlinx.coroutines.flow.Flow

@Dao
interface WarehouseDao {

    @Query("SELECT * FROM warehouses_table")
    fun getAllWarehouses(): Flow<List<Warehouse>>

}