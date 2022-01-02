package app.gestionareproduse.di

import android.content.Context
import app.gestionareproduse.chooseWarehouseScreen.repo.WarehouseRepository
import app.gestionareproduse.chooseWarehouseScreen.repo.WarehouseRepositoryImpl
import app.gestionareproduse.chooseWarehouseScreen.service.WarehouseService
import app.gestionareproduse.chooseWarehouseScreen.usecase.WarehousesUseCase
import app.gestionareproduse.chooseWarehouseScreen.usecase.WarehousesUseCaseImpl
import app.gestionareproduse.database.AppDatabase
import app.gestionareproduse.products.repo.ProductsRepository
import app.gestionareproduse.products.repo.ProductsRepositoryImpl
import app.gestionareproduse.products.service.ProductService
import app.gestionareproduse.products.usecase.ProductsUseCase
import app.gestionareproduse.products.usecase.ProductsUseCaseImpl
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideProductRepository(@ApplicationContext appContext: Context): ProductsRepository{
        val productDao = AppDatabase.getDatabase(appContext).productDao()

        val gson = GsonBuilder().setDateFormat("dd.MM.yyyy").create()
        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(ProductService.SERVICE_ENDPOINT)
            .build()

        val productService = retrofit.create(ProductService::class.java)
        return ProductsRepositoryImpl(productDao, productService)
    }

    @Provides
    @Singleton
    fun provideWarehouseRepository(@ApplicationContext appContext: Context): WarehouseRepository{
        val warehouseDao = AppDatabase.getDatabase(appContext).warehouseDao()
        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(WarehouseService.SERVICE_ENDPOINT)
            .build()

        val warehouseService = retrofit.create(WarehouseService::class.java)
        return WarehouseRepositoryImpl(warehouseDao, warehouseService)
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