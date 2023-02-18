package mobile.solareye.carservice.data.local

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

class CarServiceDataStoreImpl(
    private val context: Context
) : CarServiceDataStore {

    companion object {
        const val DATA_STORE_NAME = "car_service"

        val KEY_MASTER_NAME = stringPreferencesKey("key_master_name")
    }

    private val Context.dataStore by preferencesDataStore(name = DATA_STORE_NAME)

    override val deviceId: String
        @SuppressLint("HardwareIds")
        get() = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )

    override fun getMasterName() = context.dataStore.data.map { prefs -> prefs[KEY_MASTER_NAME] }

    override suspend fun setMasterName(master: String) {
        context.dataStore.edit { prefs -> prefs[KEY_MASTER_NAME] = master }
    }

}