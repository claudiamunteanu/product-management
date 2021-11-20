package app.gestionareproduse.products.repo

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

    // LiveData is a data holder class that can be observed within a given lifecycle.
    // Always holds/caches latest version of data. Notifies its active observers when the
    // data has changed. Since we are getting all the contents of the database,
    // we are notified whenever any of the database contents have changed.
    @Query("SELECT * FROM products_table WHERE warehouseId=:warehouseId ORDER BY expirationDate ASC")
    fun getAllProductsByExpDate(warehouseId: Long): Flow<List<Product>>
    // Pentru ca am folosit LiveData, cand se adauga in tabel ceva, peste tot unde folosim rezultatul
    // vom fi notificati cu noul rezultat

    @Query("SELECT * FROM products_table WHERE warehouseId=:warehouseId ORDER BY name ASC")
    fun getAllProductsByName(warehouseId: Long): Flow<List<Product>>

    @Query("SELECT * FROM products_table WHERE warehouseId=:warehouseId ORDER BY brand ASC")
    fun getAllProductsByBrand(warehouseId: Long): Flow<List<Product>>

    @Query("SELECT * FROM products_table WHERE warehouseId=:warehouseId ORDER BY price ASC")
    fun getAllProductsByPrice(warehouseId: Long): Flow<List<Product>>

    @Insert
    fun insert(product: Product)

    @Query("DELETE FROM products_table WHERE id = :productId")
    fun delete(productId: Long)

    @Update
    fun update(product: Product)
}