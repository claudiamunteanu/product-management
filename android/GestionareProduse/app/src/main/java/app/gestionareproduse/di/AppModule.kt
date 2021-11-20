package app.gestionareproduse.di

import android.content.Context
import app.gestionareproduse.chooseWarehouseScreen.repo.WarehouseRepository
import app.gestionareproduse.chooseWarehouseScreen.repo.WarehouseRepositoryImpl
import app.gestionareproduse.chooseWarehouseScreen.usecase.WarehousesUseCase
import app.gestionareproduse.chooseWarehouseScreen.usecase.WarehousesUseCaseImpl
import app.gestionareproduse.database.AppDatabase
import app.gestionareproduse.products.repo.ProductsRepository
import app.gestionareproduse.products.repo.ProductsRepositoryImpl
import app.gestionareproduse.products.usecase.ProductsUseCase
import app.gestionareproduse.products.usecase.ProductsUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideProductRepository(@ApplicationContext appContext: Context): ProductsRepository{
        val productDao = AppDatabase.getDatabase(appContext).productDao()
        return ProductsRepositoryImpl(productDao)
    }

    @Provides
    @Singleton
    fun provideWarehouseRepository(@ApplicationContext appContext: Context): WarehouseRepository{
        val warehouseDao = AppDatabase.getDatabase(appContext).warehouseDao()
        return WarehouseRepositoryImpl(warehouseDao)
    }

    @Module
    @InstallIn(SingletonComponent::class)
    interface AppModuleInt {

        @Binds
        @Singleton
        fun provideProductsUseCase(uc: ProductsUseCaseImpl): ProductsUseCase

        @Binds
        @Singleton
        fun provideWarehousesUseCase(uc: WarehousesUseCaseImpl): WarehousesUseCase
    }

}