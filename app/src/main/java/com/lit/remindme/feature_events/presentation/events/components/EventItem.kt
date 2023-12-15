package com.lit.remindme.feature_events.presentation.events.components

import android.content.Context
import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.Spring.DampingRatioNoBouncy
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.lit.remindme.R
import com.lit.remindme.feature_events.domain.model.EventDomain
import com.lit.remindme.feature_events.domain.model.EventTypes
import com.lit.remindme.feature_events.presentation.util.GetContactImagePainter
import com.lit.remindme.ui.theme.LocalExtraColors
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class EventItem() {
//    private var lastMonth = 0

    @OptIn(ExperimentalAnimationApi::class)
    @ExperimentalMaterialApi
    @Composable
    fun DrawEventItem(
        context: Context,
        index: Int,
        event: EventDomain,
        modifier: Modifier = Modifier,
        onDeleteClick: () -> Unit,
        onDisableEventClick: () -> Unit,
    ) {
        val thisDate = LocalDate.parse(event.eventDate)
        val now = LocalDate.now()
//        val colorID = getColorID(context, index, now, thisDate.withYear(now.year))
//        val itemColor = Color(ResourcesCompat.getColor(context.resources, colorID, null))
        val itemColor = getColor(index, now, thisDate.withYear(now.year))
        val bitmap = GetContactImagePainter(context = context, event.thumbUri)
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

        AnimatedVisibility(
            visible = event.isVisible,
            exit = shrinkVertically(spring(DampingRatioNoBouncy, Spring.StiffnessVeryLow), Alignment.CenterVertically),
            enter = EnterTransition.None
        ) {
            Box(
                modifier = modifier
                    .height(100.dp)
            ) {
                Canvas(
                    modifier = Modifier
                        .matchParentSize()
                ) {
                    drawRect(
                        color = if(event.isVisible) itemColor else Color.Red,
                        size = size
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
//                .padding(end = 32.dp)
                ) {
                    Text(
                        text = event.title,
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.colors.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(8.dp))
//                    Text(
//                        text = event.lookupId,
//                        style = TextStyle(
//                            color = Color.Black,
//                            fontSize = 10.sp,
//                            fontWeight = FontWeight.Normal),
//                        color = MaterialTheme.colors.onSurface,
//                        maxLines = 1,
//                        overflow = TextOverflow.Ellipsis
//                    )
//                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        if (bitmap != null) {
                            Image(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp)),
                                bitmap = bitmap,
                                contentDescription = context.getString(R.string.string_thumbnail)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = thisDate.format(formatter),
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.onSurface,
                            maxLines = 10,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                )
                {
                    IconButton(
                        onClick = onDisableEventClick,
                        modifier = Modifier
                            .absoluteOffset(10.dp)
                    ) {
                        if (event.eventDisabled) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_circle_notifications_24_off),
                                contentDescription = context.getString(R.string.string_disbale_event),
                                tint = Color.Red
                            )
                        } else {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_circle_notifications_24),
                                contentDescription = context.getString(R.string.string_disbale_event),
                                tint = MaterialTheme.colors.onSurface
                            )
                        }
                    }
                    if (event.eventType == EventTypes.EventFromUser) {
                        IconButton(
                            onClick = onDeleteClick,
                            modifier = Modifier
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_delete_24),
                                contentDescription = context.getString(R.string.string_event_delete),
                                tint = MaterialTheme.colors.onSurface
                            )
                        }
                    } else {
                        IconButton(
                            onClick = { },
                            modifier = Modifier,
                            enabled = false
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_delete_24_off),
                                contentDescription = context.getString(R.string.string_event_delete),
                                tint = Color.LightGray
                            )
                        }
                    }
                }
            }
        }
    }

    /*
    private fun getColorID(context: Context, index: Int, now: LocalDate, eventDate: LocalDate): Int {
        return if (now.isEqual(eventDate) || now.plusDays(1).isEqual(eventDate)) {
            if (index % 2 == 0)
                R.color.row_background_highlight_1
            else
                R.color.row_background_highlight_2
        } else {
            if (index % 2 == 0)
                R.color.row_background_1
            else
                R.color.row_background_2
        }
    }
    */

    @Composable
    private fun getColor(index: Int, now: LocalDate, eventDate: LocalDate): Color {
        return if (now.isEqual(eventDate) || now.plusDays(1).isEqual(eventDate)) {
            if (index % 2 == 0)
                LocalExtraColors.current.listViewHighlight1
            else
                LocalExtraColors.current.listViewHighlight2
        } else {
            if (index % 2 == 0)
                LocalExtraColors.current.listViewBackground1
            else
                LocalExtraColors.current.listViewBackground2
        }
    }
}