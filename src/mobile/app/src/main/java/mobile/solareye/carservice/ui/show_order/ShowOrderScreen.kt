@file:OptIn(ExperimentalMaterial3Api::class)

package mobile.solareye.carservice.ui.show_order

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import mobile.solareye.carservice.CarServiceViewModelFactory
import mobile.solareye.carservice.data.model.Order
import mobile.solareye.carservice.ui.common.ErrorState
import mobile.solareye.carservice.ui.common.Loading
import mobile.solareye.carservice.ui.common.MyAlertDialog

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@Composable
fun ShowOrderScreen(
    viewModel: ShowOrderViewModel = viewModel(factory = CarServiceViewModelFactory),
    popBackStack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

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
        when (val state = uiState) {
            is ShowOrderUiState.Content -> OrderShowContent(
                state.data,
                viewModel,
                innerPadding,
            )
            ShowOrderUiState.InProgress -> Unit
            ShowOrderUiState.InitialProgress -> Loading()
            ShowOrderUiState.Error -> ErrorState { viewModel.getOrder() }
            ShowOrderUiState.OnBack -> LaunchedEffect(viewModel.uiState) { popBackStack.invoke() }
        }
    }

}

@ExperimentalMaterial3Api
@Composable
fun OrderShowContent(
    order: Order,
    viewModel: ShowOrderViewModel,
    innerPadding: PaddingValues,
) {
    val status by remember { mutableStateOf(order.status) }
    val carName by remember { mutableStateOf(order.carName) }
    val carLicensePlate by remember { mutableStateOf(order.carLicensePlate) }
    val createdGmt by remember { mutableStateOf(order.createdGmt) }
    val cost by remember { mutableStateOf(order.cost) }
    val comment by remember { mutableStateOf(order.comment) }

    var isDialogVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(innerPadding)
    ) {
        Spacer(Modifier.size(16.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 0.dp),
            text = status,
        )

        Spacer(Modifier.size(16.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 0.dp),
            text = carName,
        )

        Spacer(Modifier.size(16.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 0.dp),
            text = carLicensePlate,
        )

        Spacer(Modifier.size(16.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 0.dp),
            text = createdGmt,
        )

        Spacer(Modifier.size(16.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 0.dp),
            text = cost,
        )

        Spacer(Modifier.size(16.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 0.dp),
            text = comment,
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
                if (order.closedGmt != null) {
                    viewModel.reopenOrder()
                } else {
                    isDialogVisible = true
                }
            }
        ) {
            val buttonText = if (order.closedGmt != null) "Переоткрыть" else "Закрыть"
            Text(buttonText)
        }
    }

    if (isDialogVisible) {
        CostAlertDialog(
            cost = cost,
            onConfirmClick = { viewModel.closeOrder(it) },
            onDismiss = { isDialogVisible = false }
        )
    }
}

@Composable
fun CostAlertDialog(
    cost: String,
    onConfirmClick: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var costState by remember { mutableStateOf(cost) }
    MyAlertDialog(
        title = {
            Text(
                text = "Укажите стоимость ремонтных работ",
            )
        },
        content = {
            OutlinedTextField(
                value = costState,
                onValueChange = { costState = it },
                label = { Text("₽") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
        },
        dismissButton = {
            TextButton(
                onClick = { onDismiss.invoke() },
                content = { Text("CANCEL") },
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmClick.invoke(costState)
                    onDismiss.invoke()
                },
                content = { Text("OK") },
            )
        },
        onDismiss = {
            onDismiss.invoke()
        },
    )
}