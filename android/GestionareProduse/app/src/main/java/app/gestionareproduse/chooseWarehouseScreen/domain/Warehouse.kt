package app.gestionareproduse.chooseWarehouseScreen.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "warehouses_table")
data class Warehouse(
    @PrimaryKey(autoGenerate = true) var id: Long? = null,
    var name: String
)