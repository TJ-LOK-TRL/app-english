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

fun checkIsStreakContinued(lastDate: Long?, nowDate: Long): Boolean {
    if (lastDate == null) return true

    val last = Calendar.getInstance().apply { timeInMillis = lastDate }
    val current = Calendar.getInstance().apply { timeInMillis = nowDate }

    // Add one day to the last lesson
    last.add(Calendar.DAY_OF_YEAR, 1)

    // If today's date is equal to the "tomorrow" of the last lesson, the streak continues
    return last.get(Calendar.YEAR) == current.get(Calendar.YEAR) &&
            last.get(Calendar.DAY_OF_YEAR) == current.get(Calendar.DAY_OF_YEAR)
}

fun checkIsSameDay(lastDate: Long?, nowDate: Long): Boolean {
    if (lastDate == null) return false
    val last = Calendar.getInstance().apply { timeInMillis = lastDate }
    val current = Calendar.getInstance().apply { timeInMillis = nowDate }
    return last.get(Calendar.YEAR) == current.get(Calendar.YEAR) &&
            last.get(Calendar.DAY_OF_YEAR) == current.get(Calendar.DAY_OF_YEAR)
}