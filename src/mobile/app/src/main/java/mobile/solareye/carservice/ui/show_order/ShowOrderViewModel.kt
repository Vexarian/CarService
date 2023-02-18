package mobile.solareye.carservice.ui.show_order

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mobile.solareye.carservice.data.model.onFailure
import mobile.solareye.carservice.data.model.onSuccess
import mobile.solareye.carservice.data.repository.FeatureRepository
import kotlin.properties.Delegates

class ShowOrderViewModel(
    private val repository: FeatureRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState: MutableStateFlow<ShowOrderUiState> =
        MutableStateFlow(ShowOrderUiState.InitialProgress)
    val uiState = _uiState.asStateFlow()

    private val orderId = savedStateHandle.get<Int>("orderId") ?: throw IllegalArgumentException("orderId обязателен")

    private var lastRequest: (() -> Unit)? = null

    init {
        getOrder()
    }

    fun getOrder() {
        lastRequest = null
        viewModelScope.launch(Dispatchers.IO) {
            repository.getOrder(orderId).onSuccess {
                val order = it.data
                _uiState.value = ShowOrderUiState.Content(data = order)
            }
                .onFailure {
                    lastRequest = { getOrder() }
                    _uiState.value = ShowOrderUiState.Error
                }
        }
    }

    fun closeOrder(
        cost: String,
    ) {
        lastRequest = null
        viewModelScope.launch(Dispatchers.IO) {
            repository.closeOrder(
                orderId,
                cost,
            ).onSuccess {
                _uiState.value = ShowOrderUiState.OnBack
            }.onFailure {
                lastRequest = {
                    closeOrder(
                        cost,
                    )
                }
                _uiState.value = ShowOrderUiState.Error
            }
        }
    }

    fun reopenOrder() {
        lastRequest = null
        viewModelScope.launch(Dispatchers.IO) {
            repository.reopenOrder(
                orderId,
            ).onSuccess {
                _uiState.value = ShowOrderUiState.OnBack
            }.onFailure {
                lastRequest = { reopenOrder() }
                _uiState.value = ShowOrderUiState.Error
            }
        }
    }

}