package mobile.solareye.carservice.ui.main

import mobile.solareye.carservice.data.model.Order

sealed class MainUiState {
    object InitialProgress : MainUiState()

    object InProgress : MainUiState()

    class Content(
        val data: List<Order>,
        val ordersForNotifications: List<Order>,
    ) : MainUiState()

    object Error : MainUiState()
}