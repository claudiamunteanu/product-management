package app.gestionareproduse.products.domain

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import app.gestionareproduse.chooseWarehouseScreen.domain.Warehouse
import app.gestionareproduse.utils.DateConverter
import java.util.*

@Entity(tableName = "products_table",
        foreignKeys = arrayOf(ForeignKey(entity = Warehouse::class,
                            parentColumns = arrayOf("id"),
                            childColumns = arrayOf("warehouseId"),
                            onDelete = ForeignKey.CASCADE,
                            onUpdate = ForeignKey.CASCADE)))
@TypeConverters(DateConverter::class)
data class Product(
    @PrimaryKey(autoGenerate = false) var id: Long? = null,
    var name: String,
    var brand: String,
    var price: Double,
    var isPerUnit: Boolean,
    var expirationDate: Date,
    var isRefrigerated: Boolean,
    var image: String,
    var warehouseId: Long,
    var isUploaded : Boolean? = null
)