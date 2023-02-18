package mobile.solareye.carservice.data.remote

import android.content.Context
import com.google.gson.Gson
import java.io.IOException
import java.io.InputStream
import java.lang.reflect.Type
import java.nio.charset.Charset

object MockUtils {

    fun <T> readEntityFromJson(context: Context, fileName: String, clazz: Class<T>): T {
        val gson = Gson()
        val jsonString = loadJSONFromAsset(context, fileName)
        return gson.fromJson(jsonString, clazz)
    }

    fun <T> readEntityFromJson(context: Context, fileName: String, typeOfT: Type): T {
        val gson = Gson()
        val jsonString = loadJSONFromAsset(context, fileName)
        return gson.fromJson(jsonString, typeOfT)
    }

    private fun loadJSONFromAsset(context: Context, fileName: String): String? {
        return try {
            val stream: InputStream = context.assets.open(fileName)
            val size: Int = stream.available()
            val buffer = ByteArray(size)
            stream.read(buffer)
            stream.close()
            String(buffer, Charset.forName("UTF-8"))
        } catch (ex: IOException) {
            ex.printStackTrace()
            null
        }
    }
}