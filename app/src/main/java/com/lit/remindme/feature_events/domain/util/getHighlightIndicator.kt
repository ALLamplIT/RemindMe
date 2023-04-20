package com.lit.remindme.feature_events.domain.util

import android.util.Log
import com.lit.remindme.R
import java.time.LocalDate

fun getItemColor(index: Int, now: LocalDate, thisDate: LocalDate): Int {
    return if (now.isEqual(thisDate) || now.plusDays(1).isEqual(thisDate)) {
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