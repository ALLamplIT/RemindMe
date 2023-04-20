package com.lit.remindme.feature_events.presentation.events

import android.Manifest
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lit.remindme.R
import com.lit.remindme.feature_events.presentation.events.components.EventItem
import com.lit.remindme.feature_events.presentation.events.components.OrderSection
import com.lit.remindme.feature_events.presentation.util.Screen
import kotlinx.coroutines.*

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalAnimationApi
@Composable
fun EventsScreen(
    navController: NavController,
    viewModel: EventsViewModel = hiltViewModel<EventsViewModel>()
) {
    val state = viewModel.state.value
    val eventsList = viewModel.eventsList
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val cancelScope = rememberCoroutineScope()
    val context = LocalContext.current
    val eventItem = EventItem()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddEditEventScreen.route)
                },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = context.getString(R.string.string_new_event))
            }
        },
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
//                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = context.getString(R.string.string_title_events),
                    style = MaterialTheme.typography.h4
                )
                IconButton(
                    onClick = {
                        viewModel.onEvent(EventsEvent.ToggleOrderSection)
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Sort,
                        contentDescription = context.getString(R.string.string_sort)
                    )
                }
            }
            AnimatedVisibility(
                visible = state.isOrderSectionVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                OrderSection(
                    modifier = Modifier
                        .fillMaxWidth(),
                    viewModel
                )
            }
//            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
//                itemsIndexed(state.events) { index, event ->
                itemsIndexed(eventsList) { index, event ->
                    eventItem.DrawEventItem(
                        context = LocalContext.current,
                        index = index,
                        event = event,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(
                                    Screen.AddEditEventScreen.route +
                                            "?eventId=${event.id}"
                                )
                            },
                        onDeleteClick = {
                            viewModel.onEvent(EventsEvent.DeleteEvent(event, index))
                            scope.coroutineContext.cancelChildren()
                            cancelScope.launch {
                                val result = scaffoldState.snackbarHostState.showSnackbar(
                                    message = context.getString(R.string.string_event_delete),
                                    actionLabel = context.getString(R.string.string_delete_undo)
                                )
                                if(result == SnackbarResult.ActionPerformed) {
                                    viewModel.onEvent(EventsEvent.RestoreEvent)
                                }
                            }
                        },
                        onDisableEventClick = {
                            viewModel.onEvent(EventsEvent.DisableEvent(event, index))
                            scope.coroutineContext.cancelChildren()
                            scope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    message = context.getString(
                                        if(event.eventDisabled)
                                            R.string.string_event_enabled
                                        else
                                            R.string.string_event_disabled
                                    )
                                )
                            }
                        }
                    )
//                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}
