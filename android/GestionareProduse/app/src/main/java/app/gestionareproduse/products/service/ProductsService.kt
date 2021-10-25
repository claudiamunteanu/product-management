package app.gestionareproduse.products.service

import app.gestionareproduse.products.domain.Product
import java.text.SimpleDateFormat
import java.util.*

interface ProductsService{
    suspend fun getAllProducts(): List<Product>
}