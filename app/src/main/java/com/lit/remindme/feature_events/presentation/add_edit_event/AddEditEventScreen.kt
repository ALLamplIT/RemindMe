package com.lit.remindme.feature_events.presentation.add_edit_event

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.provider.ContactsContract
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.res.ResourcesCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lit.remindme.R
import com.lit.remindme.feature_events.domain.model.EventTypes
import com.lit.remindme.feature_events.presentation.add_edit_event.components.TransparentHintTextField
import com.lit.remindme.feature_events.presentation.util.GetContactImagePainter
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddEditEventScreen(
    navController: NavController,
    viewModel: AddEditEventViewModel = hiltViewModel()
) {
    val isEditable = viewModel.eventType == EventTypes.EventFromUser
    val titleState = viewModel.eventTitle.value
    val eventDateState = viewModel.eventEventDate.value
    val focusManager = LocalFocusManager.current
    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current
    val today = LocalDate.now()
    val eventDate = LocalDate.parse(
        eventDateState.text,
        DateTimeFormatter
        .ofPattern("dd.MM.yyyy")
    )
//    val notificationService = NotificationService(context)
// Test commit comment Line
// Test commit line 2

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AddEditEventViewModel.UiEvent.ShowSnackbar -> {
                    var message = event.message ?: context.getString(event.resourceID)
                    if (message.startsWith("#ErrSR:"))
                        message = context.getString(message.drop(7).toInt())
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = message
                    )
                }
                is AddEditEventViewModel.UiEvent.SaveEvent -> {
                    navController.navigateUp()
                }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            if(isEditable) {
                FloatingActionButton(
                    onClick = {
                        viewModel.onEvent(AddEditEventEvent.SaveEvent)
                    },
                    backgroundColor = Color(ResourcesCompat.getColor(context.resources,
                        R.color.btn_background_save,
                        null))
                ) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = context.getString(R.string.string_save_event),
                        tint = Color(ResourcesCompat.getColor(context.resources,
                            R.color.btn_tint_save,
                            null))
                    )
                }
            }
        },
        scaffoldState = scaffoldState
    ) {
        val eventDateDialogState = rememberMaterialDialogState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp, 16.dp, 0.dp, 0.dp)
        ) {
            val bitmap = GetContactImagePainter(context = LocalContext.current, viewModel.thumbURI)
            var isDefaultIcon = true
            var contactImagePainter: Painter =
                painterResource(id = R.drawable.ic_baseline_no_photography_24)

            if (bitmap != null) {
                contactImagePainter =
                    BitmapPainter(bitmap, IntOffset.Zero, IntSize(bitmap.width, bitmap.height))
                isDefaultIcon = false
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            if(viewModel.lookupId != "") {
                                val selectedContactUri =
                                    ContactsContract.Contacts.getLookupUri(0, viewModel.lookupId)
                                val editIntent = Intent(Intent.ACTION_EDIT).apply {
                                    setDataAndType(selectedContactUri,
                                        ContactsContract.Contacts.CONTENT_ITEM_TYPE)
                                    }
                                try {
                                    startActivity(context, editIntent, null)
                                } catch (e: ActivityNotFoundException) {
                                    Log.d("DBG-AddEditEventScreen","ActivityNotFound")
                                }
                            }
                        },
                    painter = contactImagePainter,
                    contentDescription = context.getString(R.string.string_contact_image),
                    alignment = Alignment.Center,
                    colorFilter = if (isDefaultIcon) ColorFilter.tint(MaterialTheme.colors.onSurface) else null
                )

                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp))
                {
                    TextLineRow(
                        rowModifier = Modifier.fillMaxWidth(),
                        label = context.getString(R.string.string_name_edit_event_label),
                        labelStyle = MaterialTheme.typography.h6,
                        labelColor = MaterialTheme.colors.onSurface,
                        fieldText = titleState.text,
                        fieldTextStyle = MaterialTheme.typography.h6.copy(textAlign = TextAlign.Center),
                        fieldHint = context.getString(titleState.hint),
                        fieldIsHintVisible = titleState.isHintVisible,
                        fieldOnValueChange = {
                            viewModel.onEvent(AddEditEventEvent.EnteredTitle(it))
                        },
                        fieldOnFocusChange = {
                            viewModel.onEvent(AddEditEventEvent.ChangeTitleFocus(it))
                        },
                        isEditable= isEditable
                    )

                    FunctionLineRow(
                        modifier = Modifier.fillMaxWidth(),
                        label = context.getString(R.string.string_date_edit_event_label),
                        labelStyle = MaterialTheme.typography.body1,
                        labelColor = MaterialTheme.colors.onSurface,
                        functionToCall = {
                            if(isEditable) {
                                BasicTextField(
                                    value = eventDateState.text,
                                    onValueChange = {
                                        viewModel.onEvent(AddEditEventEvent.EnteredEventDate(it))
                                    },
                                    singleLine = true,
                                    textStyle = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
                                    modifier = Modifier
                                        .background(Color(ResourcesCompat.getColor(context.resources,
                                            R.color.text_field_background,
                                            null)))
//                                .fillMaxWidth()
                                        .width(100.dp)
                                        .onFocusChanged {
                                            if (it.isFocused) {
                                                focusManager.clearFocus()
                                                eventDateDialogState.show()
                                            }
                                        }
                                )
                            } else {
                                Text(
                                    text = eventDateState.text,
                                    style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
                                    color = MaterialTheme.colors.onSurface
                                )
                            }
                        }
                    )

                    TextLineRow(
                        rowModifier = Modifier.fillMaxWidth(),
                        label = context.getString(R.string.string_age_edit_event_label),
                        labelStyle = MaterialTheme.typography.body1,
                        labelColor = MaterialTheme.colors.onSurface,
                        fieldText = (today.year - eventDate.year - if(today.toEpochDay() < eventDate.withYear(today.year).toEpochDay()) 1 else 0)
                                    .toString(),
                        fieldTextStyle = MaterialTheme.typography.body1
                    )
                }
            }


//            Button(onClick = {
//                notificationService.sendNotification(
//                    0,
//                    titleState.text,
//                    eventDateState.text,
//                    (LocalDate.now().year -
//                            LocalDate.parse(
//                                eventDateState.text,
//                                DateTimeFormatter
//                                    .ofPattern("dd.MM.yyyy")
//                            ).year
//                    ).toString(),
//                    0 // hier event-ID
//                )
//            }) {
//                Text("Test Notify")
//            }
        }

        MaterialDialog(
            dialogState = eventDateDialogState,
            buttons = {
                positiveButton(text = context.getString(R.string.string_button_accept))
                negativeButton(text = context.getString(R.string.string_button_cancel))
            }
        ) {
            datepicker(
                initialDate = LocalDate.parse(
                    eventDateState.text,
                    DateTimeFormatter
                        .ofPattern("dd.MM.yyyy")),
                title = ""
            ) {
                viewModel.onEvent(
                    AddEditEventEvent.EnteredEventDate(
                        DateTimeFormatter
                            .ofPattern("dd.MM.yyyy")
                            .format(LocalDate.parse(it.toString()))
                    )
                )
            }
        }
    }
}

@Composable
private fun TextLineRow(
    rowModifier: Modifier,
    label: String,
    labelStyle: TextStyle,
    labelColor: Color = MaterialTheme.colors.onSurface,
    fieldText: String,
    fieldTextStyle: TextStyle,
    fieldHint: String = "",
    fieldIsHintVisible: Boolean = false,
    fieldOnValueChange: (String) -> Unit = {},
    fieldOnFocusChange: (FocusState) -> Unit = {},
    isEditable: Boolean = false,
    doSpacer: Boolean = true,
    ) {
    Row(
        modifier = rowModifier
    )
    {
        Text(
            modifier = Modifier.width(60.dp),
            text = label,
            style = labelStyle,
            color = labelColor,
            maxLines = 1
        )
        Spacer(modifier = Modifier.width(8.dp))
        if (isEditable) {
            TransparentHintTextField(
                text = fieldText,
                hint = fieldHint,
                onValueChange = fieldOnValueChange,
                onFocusChange = fieldOnFocusChange,
                isHintVisible = fieldIsHintVisible,
                singleLine = true,
                textStyle = fieldTextStyle
            )
        } else {
            Text(
                text = fieldText,
                style = fieldTextStyle,
                color = labelColor
            )
        }
    }
    if (doSpacer)
        Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun FunctionLineRow(
    modifier: Modifier,
    label: String,
    labelStyle: TextStyle,
    labelColor: Color = MaterialTheme.colors.onSurface,
    functionToCall: @Composable () -> Unit,
    doSpacer: Boolean = true,
    ) {
    Row(
        modifier = modifier
    )
    {
        Text(
            modifier = Modifier.width(60.dp),
            text = label,
            style = labelStyle,
            color = labelColor,
            maxLines = 1
        )
        Spacer(modifier = Modifier.width(8.dp))
        functionToCall.invoke()
    }
    if(doSpacer)
        Spacer(modifier = Modifier.height(16.dp))
}

/*
fun getContactsEntryCursor(context: Context, lookupId: String): Cursor? {
    val uri = ContactsContract.Data.CONTENT_URI
    val projection = arrayOf(
        ContactsContract.CommonDataKinds.Event._ID
    )
    val where = ContactsContract.Data.MIMETYPE + " = ? AND " +
            ContactsContract.CommonDataKinds.Event.TYPE + " = " +
            ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY + " AND " +
            ContactsContract.CommonDataKinds.Event.LOOKUP_KEY + " = ?"
    Log.d("DBG-getContactsEntryCursor", where)
    val selectionArgs = arrayOf(
        ContactsContract.CommonDataKinds.Event.CONTACT_ID,
        lookupId
    )
    Log.d("DBG-getContactsEntryCursor", lookupId)
    val sortOrder: String? = null
    return context.contentResolver.query(uri, projection, where, selectionArgs, sortOrder)
}
*/
//                        val thisContactCursor: Cursor? = getContactsEntryCursor(context, viewModel.lookupId)
//                        if(thisContactCursor != null) {
//                            Log.d("DBG-Image","#01 ${thisContactCursor.count}")
//                            val contactIdColumn =
//                                thisContactCursor.getColumnIndex(ContactsContract.Contacts._ID)
//                            Log.d("DBG-Image","#01 $contactIdColumn")
//                            val contactId =
//                                thisContactCursor.getString(contactIdColumn) ?: ""
//                            Log.d("DBG-Image","#01 $contactId")
//                        }
//
