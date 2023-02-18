package mobile.solareye.carservice.ui.addedit_order

import mobile.solareye.carservice.data.model.Order

/*data class AddEditOrderUiState(
    val isInitLoading: Boolean = false,
    val isLoading: Boolean = false,
    val data: Order? = null,
    val isError: Boolean = false,
    val onBack: Unit? = null,
)*/

sealed class AddEditOrderUiState {
    object InitialProgress : AddEditOrderUiState()

    object InProgress : AddEditOrderUiState()

    class Content(
        val data: Order?,
    ) : AddEditOrderUiState()

    object Error : AddEditOrderUiState()

    object OnBack : AddEditOrderUiState()
}
