package com.example.util

import java.util.Calendar
import java.util.concurrent.TimeUnit

object DateHelper {
    fun todayEpochDay(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 12)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return TimeUnit.MILLISECONDS.toDays(cal.timeInMillis)
    }

    fun formatTimestampFr(timestampMs: Long): String {
        val cal = Calendar.getInstance().apply { timeInMillis = timestampMs }
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val month = when (cal.get(Calendar.MONTH)) {
            0 -> "janv."
            1 -> "févr."
            2 -> "mars"
            3 -> "avr."
            4 -> "mai"
            5 -> "juin"
            6 -> "juil."
            7 -> "août"
            8 -> "sept."
            9 -> "oct."
            10 -> "nov."
            else -> "déc."
        }
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val min = cal.get(Calendar.MINUTE).toString().padStart(2, '0')
        return "$day $month à ${hour}h$min"
    }
}
