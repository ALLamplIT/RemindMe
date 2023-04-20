package com.lit.remindme.feature_events.presentation.events.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.lit.remindme.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import com.lit.remindme.feature_events.domain.model.RemindMeConstants
import com.lit.remindme.feature_events.presentation.add_edit_event.AddEditEventEvent
import com.lit.remindme.feature_events.presentation.events.EventsEvent
import com.lit.remindme.feature_events.presentation.events.EventsViewModel
import com.lit.remindme.feature_events.util.EventsOrderType
import com.lit.remindme.feature_events.util.OrderDirection
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/*
                    dailyReminderTime =state.dailyReminderTime,
 */

@Composable
fun OrderSection(
    modifier: Modifier = Modifier,
    viewModel: EventsViewModel
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val eventsOrderType = viewModel.state.value.eventsOrderType
    Log.d("DBG-OrderSection","$eventsOrderType")

    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DefaultRadioButton(
                text = context.getString(R.string.string_sort_by_title_label),
                selected = eventsOrderType is EventsOrderType.Title,
                onSelect = {
                    viewModel.onEvent(EventsEvent.Order(EventsOrderType.Title(eventsOrderType.orderDirection)))
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = context.getString(R.string.string_sort_by_date_label),
                selected = eventsOrderType is EventsOrderType.Date,
                onSelect = {
                    viewModel.onEvent(EventsEvent.Order(EventsOrderType.Date(eventsOrderType.orderDirection)))
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = context.getString(R.string.string_sort_by_type_label),
                selected = eventsOrderType is EventsOrderType.Type,
                onSelect = {
                    viewModel.onEvent(EventsEvent.Order(EventsOrderType.Type(eventsOrderType.orderDirection)))
                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DefaultRadioButton(
                text = context.getString(R.string.string_sort_dircetiom_ascending),
                selected = eventsOrderType.orderDirection is OrderDirection.Ascending,
                onSelect = { viewModel.onEvent(EventsEvent.Order(eventsOrderType.copy(OrderDirection.Ascending))) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = context.getString(R.string.string_sort_dircetiom_descending),
                selected = eventsOrderType.orderDirection is OrderDirection.Descending,
                onSelect = { viewModel.onEvent(EventsEvent.Order(eventsOrderType.copy(OrderDirection.Descending))) }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            val eventTimeDialogState = rememberMaterialDialogState()

            Text(
                text = context.getString(R.string.string_daily_reminder_time_lable),
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(4.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            BasicTextField(
                value = viewModel.state.value.dailyReminderTime,
                onValueChange = {
                    viewModel.onEvent(EventsEvent.ChangeDailyRemindTime(it))
                },
                singleLine = true,
                textStyle = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
                modifier = Modifier
                    .background(Color(ResourcesCompat.getColor(context.resources,
                        R.color.text_field_background,
                        null)))
                    .width(55.dp)
                    .padding(4.dp)
                    .onFocusChanged {
                        if (it.isFocused) {
                            focusManager.clearFocus()
                            eventTimeDialogState.show()
                        }
                    }
            )

            val dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

            MaterialDialog(
                dialogState = eventTimeDialogState,
                buttons = {
                    positiveButton(text = context.getString(R.string.string_button_accept))
                    negativeButton(text = context.getString(R.string.string_button_cancel))
                }
            ) {
                timepicker(
                    initialTime = LocalTime.parse(
                        viewModel.state.value.dailyReminderTime,
                        dateTimeFormatter),
                    title = "",
                    is24HourClock = true
                ) {
//                    Log.d("DBG-OrderSection",it.toString())
                    viewModel.onEvent(
                        EventsEvent.ChangeDailyRemindTime(
                            it.format(dateTimeFormatter)
                        )
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = context.getString(R.string.string_version_lable) + " " + RemindMeConstants.APP_VERSION,
            style = MaterialTheme.typography.caption,
            modifier = Modifier.padding(4.dp,0.dp,0.dp,0.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Box(modifier = Modifier.background(Color.Black).fillMaxWidth().height(2.dp))
    }
}