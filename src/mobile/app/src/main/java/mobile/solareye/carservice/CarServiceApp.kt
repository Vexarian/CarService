package mobile.solareye.carservice

import android.app.Application
import mobile.solareye.carservice.data.local.CarServiceDataStore
import mobile.solareye.carservice.data.local.CarServiceDataStoreImpl
import mobile.solareye.carservice.data.remote.EndpointRestApi
import mobile.solareye.carservice.data.remote.MockRestApi
import mobile.solareye.carservice.data.repository.FeatureRepository
import mobile.solareye.carservice.data.repository.FeatureRepositoryImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CarServiceApp : Application() {

    private val baseUrl
        get() = when (BuildConfig.BUILD_TYPE) {
            "debug" -> "test.motorist62.ru"
            "release" -> "back.motorist62.ru"
            "debugRelease" -> "back.motorist62.ru"
            else -> throw IllegalArgumentException("Unknown Build Type")
        }

    val repository: FeatureRepository =
        FeatureRepositoryImpl(
            when (BuildConfig.BUILD_TYPE) {
                "mock" -> MockRestApi(this)
                else -> Retrofit.Builder()
                    .apply {
                        baseUrl("https://$baseUrl/")
                        addConverterFactory(GsonConverterFactory.create())
                        client(
                            OkHttpClient
                                .Builder()
                                .addInterceptor(
                                    HttpLoggingInterceptor().apply {
                                        level = HttpLoggingInterceptor.Level.BODY
                                    })
                                .build()
                        )
                    }
                    .build()
                    .create(EndpointRestApi::class.java)
            }
        )

    val dataStore: CarServiceDataStore = CarServiceDataStoreImpl(this)

}