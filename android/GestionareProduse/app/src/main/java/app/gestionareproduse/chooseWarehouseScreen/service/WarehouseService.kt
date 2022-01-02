package app.gestionareproduse.chooseWarehouseScreen.service

import app.gestionareproduse.chooseWarehouseScreen.domain.Warehouse
import retrofit2.http.GET
import rx.Observable

interface WarehouseService {
    @GET("warehouses")
    fun getAllWarehouses(): Observable<List<Warehouse>>

    companion object{
        const val SERVICE_ENDPOINT = "http://10.0.2.2:3000"
    }
}