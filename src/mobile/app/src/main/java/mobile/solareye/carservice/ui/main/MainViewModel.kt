package mobile.solareye.carservice.ui.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mobile.solareye.carservice.data.local.CarServiceDataStore
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

//    val needUpdate = savedStateHandle
//        .getStateFlow("needUpdate", false)
//        .collect() {
//            if (it) {
//                getData()
//            }
//        }

    private var lastRequest: (() -> Unit)? = null

    val masterName: String? get() = runBlocking { dataStore.getMasterName().firstOrNull() }

    init {
        getData()
//        viewModelScope.launch(Dispatchers.Main) {
//            savedStateHandle
//                .getStateFlow("needUpdate", false)
//                .collectLatest {
//                    if (it) {
//                        getData()
//                        savedStateHandle.remove<Boolean>("needUpdate")
//                    }
//                }
//        }
    }

    private fun getData() {
        _uiState.value = MainUiState.InitialProgress
        viewModelScope.launch(Dispatchers.IO) {
            repository.getOrders().onSuccess {
                val orders = it.data
                _uiState.value = MainUiState.Content(data = orders)
            }
                .onFailure {
                    lastRequest = { getData() }
                    _uiState.value = MainUiState.Error
                }
        }
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

    fun isRefresh() {
        val isRefresh = savedStateHandle.get<Boolean>("needUpdate")
        isRefresh
    }

}