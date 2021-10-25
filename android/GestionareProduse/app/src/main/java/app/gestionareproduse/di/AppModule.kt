package app.gestionareproduse.di

import app.gestionareproduse.products.repo.ProductsRepository
import app.gestionareproduse.products.repo.ProductsRepositoryImpl
import app.gestionareproduse.products.service.ProductsService
import app.gestionareproduse.products.usecase.GetProductsUseCase
import app.gestionareproduse.products.usecase.GetProductsUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Module
    @InstallIn(SingletonComponent::class)
    interface AppModuleInt {

        @Binds
        @Singleton
        fun provideProductRepository(repo: ProductsRepositoryImpl): ProductsRepository

        @Binds
        @Singleton
        fun provideGetProductsUseCase(uc: GetProductsUseCaseImpl): GetProductsUseCase

    }

}