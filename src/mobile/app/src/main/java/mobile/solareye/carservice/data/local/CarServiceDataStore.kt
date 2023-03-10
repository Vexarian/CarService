package mobile.solareye.carservice.data.local

import kotlinx.coroutines.flow.Flow

interface CarServiceDataStore {

    val deviceId: String

    fun getMasterName(): Flow<String?>

    suspend fun setMasterName(
        master: String,
    )

    fun getStatusFilter(): Flow<String?>

    suspend fun setStatusFilter(
        statusFilter: String,
    )

}