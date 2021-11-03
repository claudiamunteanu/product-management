package app.gestionareproduse.di

import app.gestionareproduse.products.repo.ProductsRepository
import app.gestionareproduse.products.repo.ProductsRepositoryImpl
import app.gestionareproduse.products.usecase.ProductsUseCase
import app.gestionareproduse.products.usecase.ProductsUseCaseImpl
import dagger.Binds
import dagger.Module
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
        fun provideProductsUseCase(uc: ProductsUseCaseImpl): ProductsUseCase

    }

}