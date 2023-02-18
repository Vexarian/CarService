package mobile.solareye.carservice.ui.addedit_order

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mobile.solareye.carservice.data.local.CarServiceDataStore
import mobile.solareye.carservice.data.model.onFailure
import mobile.solareye.carservice.data.model.onSuccess
import mobile.solareye.carservice.data.repository.FeatureRepository

class AddEditOrderViewModel(
    private val repository: FeatureRepository,
    private val dataStore: CarServiceDataStore,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState: MutableStateFlow<AddEditOrderUiState> = MutableStateFlow(AddEditOrderUiState.InitialProgress)
    val uiState = _uiState.asStateFlow()

    val orderId = savedStateHandle.get<Int>("orderId")

    private val masterName: String get() = runBlocking { dataStore.getMasterName().firstOrNull() ?: "" }
    private val deviceId = dataStore.deviceId

    private var lastRequest: (() -> Unit)? = null

    init {
        getOrder()
    }

    fun getOrder() {
        val orderId = orderId
        if (orderId == null) {
            _uiState.value = AddEditOrderUiState.Content(data = null)
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.getOrder(orderId).onSuccess {
                val order = it.data
                _uiState.value = AddEditOrderUiState.Content(data = order)
            }
                .onFailure {
                    _uiState.value = AddEditOrderUiState.Error
                }
        }
    }

    fun createOrder(
        carName: String,
        carLicensePlate: String,
        cost: String,
        comment: String,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.createOrder(
                carName,
                carLicensePlate,
                cost,
                masterName,
                comment,
                deviceId,
            )
                .onSuccess { _uiState.value = AddEditOrderUiState.OnBack }
                .onFailure { _uiState.value = AddEditOrderUiState.Error }
        }
    }

    fun updateOrder(
        orderId: String,
        carName: String,
        carLicensePlate: String,
        cost: String,
        comment: String,
    ) {
        _uiState.value = AddEditOrderUiState.InitialProgress
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateOrder(
                orderId,
                carName,
                carLicensePlate,
                cost,
                masterName,
                comment,
                deviceId,
            ).onSuccess {
                _uiState.value = AddEditOrderUiState.OnBack
            }.onFailure {
                lastRequest = {
                    updateOrder(
                        orderId,
                        carName,
                        carLicensePlate,
                        cost,
                        comment,
                    )
                }
                _uiState.value = AddEditOrderUiState.Error
            }
        }
    }



}