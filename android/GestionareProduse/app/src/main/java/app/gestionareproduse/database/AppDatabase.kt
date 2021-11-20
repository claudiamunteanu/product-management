package app.gestionareproduse.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import app.gestionareproduse.products.domain.Product
import app.gestionareproduse.chooseWarehouseScreen.domain.Warehouse
import app.gestionareproduse.chooseWarehouseScreen.repo.WarehouseDao
import app.gestionareproduse.products.repo.ProductDao

/**
 * This is the backend. The database. This used to be done by the OpenHelper.
 * The fact that this has very few comments emphasizes its coolness.
 */
@Database(entities = [Product::class, Warehouse::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun productDao() : ProductDao
    abstract fun warehouseDao(): WarehouseDao

    companion object { //static Java
        @Volatile
        private var INSTANCE: app.gestionareproduse.database.AppDatabase? = null

        fun getDatabase(
            context: Context,
        ): AppDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "product_database"
                )
                // Wipes and rebuilds instead of migrating if no Migration object.
                // Migration is not part of this codelab.
                    .fallbackToDestructiveMigration()
                    .addCallback(AppDatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class AppDatabaseCallback(
        ) : RoomDatabase.Callback()
    }
}