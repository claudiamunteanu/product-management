package app.gestionareproduse.products.repo

import androidx.lifecycle.LiveData
import androidx.room.*
import app.gestionareproduse.products.domain.Product
import kotlinx.coroutines.flow.Flow

/**
 * The Room Magic is in this file, where you map a Java method call to an SQL query.
 *
 * When you are using complex data types, such as Date, you have to also supply type converters.
 * To keep this example basic, no types that require type converters are used.
 * See the documentation at
 * https://developer.android.com/topic/libraries/architecture/room.html#type-converters
 */

@Dao //Data Access Object
interface ProductDao {
    @Query("SELECT * FROM products_table WHERE warehouseId=:warehouseId")
    fun getAllProducts(warehouseId: Long): Flow<List<Product>>

    @Insert
    fun insert(product: Product)

    @Query("DELETE FROM products_table WHERE id = :productId")
    fun delete(productId: Long)

    @Update
    fun update(product: Product)
}