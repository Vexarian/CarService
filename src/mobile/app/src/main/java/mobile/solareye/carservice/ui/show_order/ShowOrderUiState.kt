package mobile.solareye.carservice.ui.show_order

import mobile.solareye.carservice.data.model.Order

sealed class ShowOrderUiState {
    object InitialProgress : ShowOrderUiState()

    object InProgress : ShowOrderUiState()

    class Content(
        val data: Order,
    ) : ShowOrderUiState()

    object Error : ShowOrderUiState()

    object OnBack : ShowOrderUiState()
}