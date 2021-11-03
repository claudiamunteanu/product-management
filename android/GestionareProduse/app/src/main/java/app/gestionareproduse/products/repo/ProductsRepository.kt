package app.gestionareproduse.products.repo

import app.gestionareproduse.products.domain.Product
import app.gestionareproduse.utils.Utils
import javax.inject.Inject

interface ProductsRepository{
    suspend fun getAllProducts(): List<Product>
    fun updateProduct(product: Product)
    fun deleteProduct(product: Product)
    fun saveProduct(product: Product)
    fun getSize() : Int
}

class ProductsRepositoryImpl @Inject constructor(
//    val service: ProductsService
) : ProductsRepository{

    val productList = arrayListOf(
        Product(
            id = 1L,
            name = "Iaurt cu cereale, nuci si ovaz",
            brand = "Activia",
            price = 1.80,
            isPerUnit = true,
            expirationDate = Utils.stringToDate("16.10.2021"),
            isRefrigerated = true,
            image = "https://www.auchan.ro/public/images/h33/hd1/h00/iaurt-activia-cu-cereale-nuci-si-ovaz-125g-8950867689502.jpg",
            warehouseId = 1L
        ),
        Product(
            id = 2L,
            name = "Biscuiti sarati, originali",
            brand = "Tuc",
            price = 2.55,
            isPerUnit = true,
            expirationDate = Utils.stringToDate("1.11.2021"),
            isRefrigerated = false,
            image = "https://www.auchan.ro/public/images/h01/hb2/h00/biscuiti-tuc-originali-100-g-8861037395998.jpg",
            warehouseId = 1L
        ),
        Product(
            id = 3L,
            name = "Ceapa galbena",
            brand = "Ceapa",
            price = 1.39,
            isPerUnit = false,
            expirationDate = Utils.stringToDate("29.11.2021"),
            isRefrigerated = false,
            image = "https://www.cora.ro/images/products/1793205/gallery/1793205_hd_1.jpg",
            warehouseId = 1L
        ),
        Product(
            id = 4L,
            name = "Inghetata Topgun Over cu vanilie,260ml",
            brand = "Nestle",
            price = 5.15,
            isPerUnit = true,
            expirationDate = Utils.stringToDate("31.12.2021"),
            isRefrigerated = true,
            image = "https://www.auchan.ro/public/images/hb0/h48/h00/inghetata-topgun-over-cu-vanilie-260ml-9434379255838.jpg",
            warehouseId = 1L
        )
    )

    override suspend fun getAllProducts(): List<Product> {
//        return service.getAllProducts();
        return productList
    }

    override fun updateProduct(product: Product) {
        val oldProduct = productList.find { it.id == product.id }
        productList.remove(oldProduct)
        productList.add(product)
    }

    override fun deleteProduct(product: Product) {
        val oldProduct = productList.find { it.id == product.id }
        productList.remove(oldProduct)
    }

    override fun saveProduct(product: Product) {
        productList.add(product)
    }

    override fun getSize(): Int {
        return productList.size;
    }

}