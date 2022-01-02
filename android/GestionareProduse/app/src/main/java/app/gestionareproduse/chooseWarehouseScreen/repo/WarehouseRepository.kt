package app.gestionareproduse.chooseWarehouseScreen.repo

import android.view.View
import android.widget.ProgressBar
import androidx.compose.runtime.MutableState
import app.gestionareproduse.chooseWarehouseScreen.domain.Warehouse
import app.gestionareproduse.chooseWarehouseScreen.service.WarehouseService
import app.gestionareproduse.utils.MyCallback
import app.gestionareproduse.utils.logd
import app.gestionareproduse.utils.loge
import kotlinx.coroutines.flow.Flow
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

interface WarehouseRepository{
    fun loadAllWarehouses(showError: (String) -> Unit, progressBarVisibility : MutableState<Boolean>)
    fun getLocalWarehouses(): Flow<List<Warehouse>>
}

class WarehouseRepositoryImpl(
    private val warehouseDao: WarehouseDao,
    private val warehouseService: WarehouseService
) : WarehouseRepository {

    override fun getLocalWarehouses(): Flow<List<Warehouse>> {
        return warehouseDao.getAllWarehouses()
    }

    override fun loadAllWarehouses(showError: (String) -> Unit, progressBarVisibility : MutableState<Boolean>){
        warehouseService.getAllWarehouses()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Subscriber<List<Warehouse>>() {
                override fun onCompleted() {
                    logd("Warehouse Service completed")
                    progressBarVisibility.value = false
                }

                override fun onError(e: Throwable) {
                    loge("Error while loading the warehouses", e)
                    showError("Not able to retrieve the data. Displaying local data!")
                    progressBarVisibility.value = false
                }

                override fun onNext(warehouses: List<Warehouse>) {
                    Thread(Runnable {
                        warehouseDao.deleteAllWarehouses()
                        warehouseDao.addWarehouses(warehouses)
                    }).start()
                    logd("Warehouses loaded")
                }

                override fun onStart() {
                    logd("Loading warehouses")
                    progressBarVisibility.value = true
                }
            })
    }
}