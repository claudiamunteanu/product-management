package app.gestionareproduse.products.usecase

import app.gestionareproduse.products.domain.Product
import app.gestionareproduse.products.repo.ProductsRepository
import javax.inject.Inject

interface ProductsUseCase{
    suspend operator fun invoke(): List<Product>
    fun updateProduct(product: Product)
    fun deleteProduct(product: Product)
    fun saveProduct(product: Product)
    fun getSize() : Int
}

class ProductsUseCaseImpl @Inject constructor(
    val repo: ProductsRepository
) : ProductsUseCase{
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

    override fun getSize(): Int {
        return repo.getSize()
    }
}
