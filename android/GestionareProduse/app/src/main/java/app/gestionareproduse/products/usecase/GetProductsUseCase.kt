package app.gestionareproduse.products.usecase

import app.gestionareproduse.products.domain.Product
import app.gestionareproduse.products.repo.ProductsRepository
import javax.inject.Inject

interface GetProductsUseCase{
    suspend operator fun invoke(): List<Product>
    fun updateProduct(product: Product)
    fun deleteProduct(product: Product)
    fun saveProduct(product: Product)
}

class GetProductsUseCaseImpl @Inject constructor(
    val repo: ProductsRepository
) : GetProductsUseCase{
    override suspend fun invoke(): List<Product> {
        return repo.getAllProducts()
    }

    override fun updateProduct(product: Product) {
        repo.updateProduct(product)
    }

    override fun deleteProduct(product: Product) {
        repo.deleteProduct(product)
    }

    override fun saveProduct(product: Product) {
        repo.saveProduct(product)
    }
}
