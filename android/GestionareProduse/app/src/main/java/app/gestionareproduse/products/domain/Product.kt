package app.gestionareproduse.products.domain

import com.squareup.moshi.JsonClass
import java.text.SimpleDateFormat
import java.util.*

data class Product(
    var id: Long,
    var name: String,
    var brand: String,
    var price: Double,
    var isPerUnit: Boolean,
    var expirationDate: Date,
    var isRefrigerated: Boolean,
    var image: String,
    var warehouseId: Long
)