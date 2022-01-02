package app.gestionareproduse.products.service

import app.gestionareproduse.products.domain.Product
import retrofit2.Response
import retrofit2.http.*
import rx.Observable

interface ProductService {
    @GET("products")
    fun getAllProducts(@Query("warehouseId") warehouseId: Long): Observable<List<Product>>

    @POST("product")
    fun addProduct(@Body e: Product): Observable<Product>

    @POST("products")
    fun addProducts(@Body products: List<Product>): Observable<List<Product>>

    @PUT("product")
    fun updateProduct(@Body e:Product): Observable<Product>

    @DELETE("products/{id}")
    suspend fun deleteProduct(@Path("id") id: Long): Response<Unit>

    companion object{
        const val SERVICE_ENDPOINT = "http://10.0.2.2:3000"
    }
}