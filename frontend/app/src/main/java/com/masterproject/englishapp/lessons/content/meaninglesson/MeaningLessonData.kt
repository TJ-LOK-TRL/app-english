package com.masterproject.englishapp.lessons.content.meaninglesson

import com.masterproject.englishapp.lessons.content.model.LessonStep

data class MeaningLessonData(
    val contextTitle: String,
    val learningPhrase: String,
    val feedbackPhrase: String,
    val explanation: String
) : LessonStep