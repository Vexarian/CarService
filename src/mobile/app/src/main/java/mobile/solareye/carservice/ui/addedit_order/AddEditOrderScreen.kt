package mobile.solareye.carservice.ui.addedit_order

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import mobile.solareye.carservice.CarServiceViewModelFactory
import mobile.solareye.carservice.data.model.Order
import mobile.solareye.carservice.ui.common.ErrorState
import mobile.solareye.carservice.ui.common.Loading

@ExperimentalMaterial3Api
@Composable
fun AddEditOrderScreen(
    viewModel: AddEditOrderViewModel = viewModel(factory = CarServiceViewModelFactory),
    popBackStack: () -> Unit,
) {

    val uiState = viewModel.uiState.collectAsState()
    when (val state = uiState.value) {
        is AddEditOrderUiState.Content -> OrderAddEditContent(
            state.data,
            viewModel,
            popBackStack,
        )
        AddEditOrderUiState.Error -> ErrorState { viewModel.getOrder() }
        AddEditOrderUiState.InProgress -> Unit
        AddEditOrderUiState.InitialProgress -> Loading()
        AddEditOrderUiState.OnBack -> LaunchedEffect(viewModel.uiState) { popBackStack.invoke() }
    }

}

@ExperimentalMaterial3Api
@Composable
fun OrderAddEditContent(
    order: Order?,
    viewModel: AddEditOrderViewModel,
    popBackStack: () -> Unit,
) {
    var carName by remember { mutableStateOf(order?.carName ?: "") }
    var carLicensePlate by remember { mutableStateOf(order?.carLicensePlate ?: "") }
    var cost by remember { mutableStateOf(order?.cost?.toString() ?: "") }
    var comment by remember { mutableStateOf(order?.comment ?: "") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = popBackStack) {
                        Icon(Icons.Filled.ArrowBack, "backIcon")
                    }
                },
                title = { },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            Spacer(Modifier.size(16.dp))
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 0.dp),
                value = carName,
                onValueChange = { carName = it },
                label = { Text("Авто") },
            )

            Spacer(Modifier.size(16.dp))
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 0.dp),
                value = carLicensePlate,
                onValueChange = { carLicensePlate = it },
                label = { Text("Гос.номер") },
            )

            Spacer(Modifier.size(16.dp))
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 0.dp),
                value = cost,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                ),
                onValueChange = { cost = it },
                label = { Text("Стоимость") },
            )

            Spacer(Modifier.size(16.dp))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 0.dp),
                value = comment,
                onValueChange = { comment = it },
                maxLines = 5,
                minLines = 5,
                label = { Text("Комментарий") },
            )

            Spacer(
                Modifier
                    .fillMaxHeight()
                    .weight(1f)
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 16.dp),
                onClick = {
                    if (viewModel.orderId == null) {
                        viewModel.createOrder(
                            carName = carName,
                            carLicensePlate = carLicensePlate,
                            cost = cost,
                            comment = comment,
                        )
                    } else {
                        viewModel.updateOrder(
                            viewModel.orderId.toString(),
                            carName = carName,
                            carLicensePlate = carLicensePlate,
                            cost = cost,
                            comment = comment,
                        )
                    }
                }
            ) {
                Text(
                    if (viewModel.orderId == null) "Добавить"
                    else "Редактировать"
                )
            }
        }
    }
}