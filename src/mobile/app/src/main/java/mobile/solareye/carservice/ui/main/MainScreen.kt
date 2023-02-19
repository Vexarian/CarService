package mobile.solareye.carservice.ui.main

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import mobile.solareye.carservice.CarServiceViewModelFactory
import mobile.solareye.carservice.data.model.Order
import mobile.solareye.carservice.data.model.StatusFilter
import mobile.solareye.carservice.ui.common.ErrorState
import mobile.solareye.carservice.ui.common.Loading
import mobile.solareye.carservice.ui.common.MyAlertDialog

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun ListOrderScreen(
    needUpdate: Boolean = false,
    viewModel: MainViewModel = viewModel(factory = CarServiceViewModelFactory),
    onNavigateToOrderCreate: () -> Unit,
    onNavigateToOrderEdit: (Int) -> Unit,
    onNavigateToOrderShow: (Int) -> Unit,
) {
    val uiState = viewModel.uiState.collectAsState()
    var needUpdateState by remember { mutableStateOf(needUpdate) }

    val dialogState: MutableState<Boolean> = remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(
            actions = {
                Box {
                    IconButton(onClick = { showMenu = showMenu.not() }) {
                        Icon(Icons.Filled.Menu, "filterIcon")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }) {
                        StatusFilter.values().forEach {
                            DropdownMenuItem(text = {
                                Row(verticalAlignment = CenterVertically) {
                                    Icon(
                                        Icons.Filled.Check,
                                        "filterCheck",
                                        tint = if (viewModel.filter.title == it.title) Color.Black else Color.Transparent
                                    )
                                    Text(
                                        modifier = Modifier.fillMaxSize(),
                                        text = it.title,
                                    )
                                }
                            }, onClick = {
                                viewModel.setFilter(it)
                                showMenu = false
                            })
                        }
                    }
                }
                IconButton(onClick = { dialogState.value = true }) {
                    Icon(Icons.Filled.Settings, "settingsIcon")
                }
            },
            title = { },
        )
    }, floatingActionButton = {
        AnimatedVisibility(
            visible = uiState.value is MainUiState.Content,
            enter = scaleIn(),
            exit = scaleOut(),
        ) {
            FloatingActionButton(onClick = onNavigateToOrderCreate) {
                Image(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                )
            }
        }
    }) { paddingValues ->
        when (val state = uiState.value) {
            MainUiState.InitialProgress -> Loading()
            MainUiState.InProgress -> Unit
            is MainUiState.Content -> {
                if (needUpdateState) {
                    viewModel.refresh()
                    needUpdateState = false
                } else {
                    OrderListContent(
                        paddingValues,
                        state.data,
                        onNavigateToOrderEdit,
                        onNavigateToOrderShow,
                    )
                }
            }
            MainUiState.Error -> ErrorState { viewModel.repeatLastRequest() }
        }

        if (dialogState.value) {
            NameAlertDialog(
                name = viewModel.masterName,
                onConfirmClick = { name ->
                    viewModel.saveNewName(name)
                },
                onDismiss = {
                    dialogState.value = false
                },
            )
        }

    }
}

@Composable
fun OrderListContent(
    paddingValues: PaddingValues,
    data: List<Order>,
    onNavigateToOrderEdit: (Int) -> Unit,
    onNavigateToOrderShow: (Int) -> Unit,
) {
    LazyColumn(
        contentPadding = paddingValues
    ) {
        items(items = data, itemContent = {
            OrderItem(
                order = it,
                onNavigateToOrderEdit = onNavigateToOrderEdit,
                onNavigateToOrderShow = onNavigateToOrderShow,
            )
        })
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OrderItem(
    order: Order,
    onNavigateToOrderEdit: (Int) -> Unit,
    onNavigateToOrderShow: (Int) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .combinedClickable(
                onClick = { onNavigateToOrderShow.invoke(order.orderId) },
                onLongClick = if (order.closedGmt == null) {
                    { onNavigateToOrderEdit.invoke(order.orderId) }
                } else null,
            )
            .fillMaxWidth()
            .padding(16.dp, 16.dp),
    ) {
        Text(
            modifier = Modifier.wrapContentWidth(),
            text = order.status,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .wrapContentHeight()
                .padding(8.dp, 0.dp),
            text = order.carName,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            modifier = Modifier.wrapContentWidth(),
            text = order.createdGmt,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NameAlertDialog(
    name: String?,
    onConfirmClick: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var nameState by remember { mutableStateOf(name ?: "") }
    MyAlertDialog(
        title = {
            Text(
                text = "Изменить имя мастера",
            )
        },
        content = {
            OutlinedTextField(
                value = nameState,
                onValueChange = { nameState = it },
                singleLine = true,
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
                    onConfirmClick.invoke(nameState)
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