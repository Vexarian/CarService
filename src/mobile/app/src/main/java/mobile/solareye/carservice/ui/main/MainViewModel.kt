package mobile.solareye.carservice.ui.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mobile.solareye.carservice.data.local.CarServiceDataStore
import mobile.solareye.carservice.data.model.Order
import mobile.solareye.carservice.data.model.StatusFilter
import mobile.solareye.carservice.data.model.onFailure
import mobile.solareye.carservice.data.model.onSuccess
import mobile.solareye.carservice.data.repository.FeatureRepository

class MainViewModel(
    private val repository: FeatureRepository,
    private val dataStore: CarServiceDataStore,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState: MutableStateFlow<MainUiState> =
        MutableStateFlow(MainUiState.InitialProgress)
    val uiState = _uiState.asStateFlow()


    private var lastRequest: (() -> Unit)? = null

    val masterName: String? get() = runBlocking { dataStore.getMasterName().firstOrNull() }
    val filter: StatusFilter
        get() = runBlocking {
            dataStore.getStatusFilter().firstOrNull()
        }?.let { StatusFilter.getFilterByTitle(it) } ?: StatusFilter.ACTIVE

    private var orders: List<Order>? = null

    init {
        getData()
    }

    private fun getData() {
        _uiState.value = MainUiState.InitialProgress
        viewModelScope.launch(Dispatchers.IO) {
            repository.getOrders().onSuccess {
                orders = it.data
                filterData(it.data)
            }.onFailure {
                    lastRequest = { getData() }
                    _uiState.value = MainUiState.Error
                }
        }
    }

    private fun filterData(data: List<Order>) {
        _uiState.value = MainUiState.Content(data = when (filter) {
            StatusFilter.ACTIVE -> data.filter { it.closedGmt == null }
            StatusFilter.ALL -> data
        })
    }

    fun refresh() {
        getData()
    }

    fun repeatLastRequest() {
        lastRequest?.invoke()?.also { lastRequest = null }
    }

    fun saveNewName(name: String) {
        viewModelScope.launch {
            dataStore.setMasterName(name)
        }
    }

    fun setFilter(statusFilter: StatusFilter) {
        viewModelScope.launch {
            dataStore.setStatusFilter(statusFilter.title)
            val orders = orders ?: return@launch
            filterData(orders)
        }
    }

}