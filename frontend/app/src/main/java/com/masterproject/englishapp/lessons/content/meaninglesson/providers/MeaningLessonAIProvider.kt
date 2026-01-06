package com.masterproject.englishapp.lessons.content.meaninglesson.providers

import android.util.Log
import com.masterproject.englishapp.lessons.content.meaninglesson.MeaningLessonData
import com.masterproject.englishapp.lessons.content.model.LessonConfig
import com.masterproject.englishapp.lessons.content.model.LessonContentProvider
import com.masterproject.englishapp.network.ApiService

class MeaningLessonAIProvider(
    val apiService: ApiService
) : LessonContentProvider<List<MeaningLessonData>> {
    override suspend fun getContent(lessonConfig: LessonConfig): List<MeaningLessonData> {
        return try {
            Log.d("MeaningLessonAIProvider", "Generate lesson with context: ${lessonConfig.context}")
            val response = apiService.generateLesson(lessonConfig.context)
            Log.d("MeaningLessonAIProvider", "Response was: $response")

            // Convert network model to UI/domain model
            response.items.map { item ->
                MeaningLessonData(
                    contextTitle = response.contextTitle,
                    learningPhrase = item.original,
                    feedbackPhrase = item.translated,
                    explanation = item.explanation
                )
            }
        } catch (e: Exception) {
            Log.e("MeaningLessonAIProvider", "Error: ${e.message}")
            throw e
        }
    }
}