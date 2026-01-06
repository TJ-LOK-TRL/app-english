package com.masterproject.englishapp.utils

import java.util.Calendar

fun calculateDelayUntil(hour: Int): Long {
    val calendar = Calendar.getInstance()
    val now = calendar.timeInMillis

    // Set for today at 21:00
    calendar.set(Calendar.HOUR_OF_DAY, hour)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    var targetTime = calendar.timeInMillis

    // If already pass 21h, do tomorrow
    if (targetTime <= now) {
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        targetTime = calendar.timeInMillis
    }

    return targetTime - now
}