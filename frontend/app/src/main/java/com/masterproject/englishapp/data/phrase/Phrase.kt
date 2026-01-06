package com.masterproject.englishapp.data.phrase

import com.masterproject.englishapp.data.Identifiable
import com.masterproject.englishapp.grammar.Language

data class Phrase(
    override val id: String,
    val text: String,
    val language: Language,
    val isQuestion: Boolean
) : Identifiable