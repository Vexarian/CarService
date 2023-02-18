package mobile.solareye.carservice


import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import mobile.solareye.carservice.ui.addedit_order.AddEditOrderViewModel
import mobile.solareye.carservice.ui.main.MainViewModel
import mobile.solareye.carservice.ui.show_order.ShowOrderViewModel

/**
 * Factory for all ViewModels.
 */
@Suppress("UNCHECKED_CAST")
val CarServiceViewModelFactory = object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T =
        with(modelClass) {
            val application = checkNotNull(extras[APPLICATION_KEY]) as CarServiceApp
            val repository = application.repository
            val dataStore = application.dataStore
            val args = extras.createSavedStateHandle()
            when {
                isAssignableFrom(MainViewModel::class.java) -> MainViewModel(repository, dataStore, args)
                isAssignableFrom(ShowOrderViewModel::class.java) -> ShowOrderViewModel(repository, args)
                isAssignableFrom(AddEditOrderViewModel::class.java) -> AddEditOrderViewModel(repository, dataStore, args)
                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}