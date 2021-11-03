package app.gestionareproduse.utils

import android.util.Log
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.ui.graphics.Color
import app.gestionareproduse.products.domain.Product
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

object Utils {
    fun dateToUiString(
        date: Date
    ) : String{
        return try {
            val dateFormat = "dd.MM.yyyy"
            val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.getDefault())
            simpleDateFormat.format(date)
        } catch (exception: Exception){
            Log.d("error", "", exception)
            ""
        }
    }

    fun stringToDate(
        date: String
    ) : Date {
        return try {
            val dateFormat = "dd.MM.yyyy"
            val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.getDefault())
            simpleDateFormat.parse(date)
        } catch (exception: Exception) {
            Log.d("error", "", exception)
            Date()
        }
    }

    fun BackgroundColorByExpirationDate(
        date: Date
    ) : Color{
        if(date.before(Date()) || date.equals(Date()))
            return Color(0xFFFFA8A8)
        else{
            val diff = date.time - Date().time
            val seconds = diff / 1000
            val minutes = seconds / 60
            val hours = minutes / 60
            val days = hours / 24
            if(days<=7){
                return Color(0xFFFFB377)
            }
            else{
                return Color.White
            }
        }
    }

    fun productToString(
        product : Product
    ) : String{
        return product.id.toString()+";"+product.name+";"+product.brand+";"+
                product.price.toString()+";"+product.isPerUnit+";"+Utils.dateToUiString(product.expirationDate)+";"+
                product.isRefrigerated+";"+product.image+";"+product.warehouseId
    }

    fun stringToProduct(
        product : String
    ) : Product{
        var values = product.split(";")
        return Product(values[0].toLong(), values[1], values[2], values[3].toDouble(),values[4].toBoolean(),
        Utils.stringToDate(values[5]),values[6].toBoolean(), values[7], values[8].toLong())
    }

    fun millisecondsToStringDate(milliseconds : Long) : String{
        return try{
            val formatter = SimpleDateFormat("dd.MM.yyyy")
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = milliseconds
            return formatter.format(calendar.time)
        } catch (exception : Exception){
            Log.d("error", "", exception)
            ""
        }
    }
}