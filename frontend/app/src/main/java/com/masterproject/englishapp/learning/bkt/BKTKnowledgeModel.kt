package com.masterproject.englishapp.learning.bkt

import com.masterproject.englishapp.learning.core.KnowledgeModel
import com.masterproject.englishapp.learning.core.SkillKey

class BKTKnowledgeModel(
    initialState: Map<SkillKey, Float> = emptyMap(),
    private val pLearn: Float = 0.1f,
    private val pSlip: Float = 0.1f,
    private val pGuess: Float = 0.2f
) : KnowledgeModel {

    private val state = initialState.toMutableMap()

    override fun mastery(skill: SkillKey): Float =
        state[skill] ?: 0.2f

    override fun update(skill: SkillKey, success: Boolean) {
        val prior = mastery(skill)

        val posterior = if (success) {
            (prior * (1 - pSlip)) /
                    ((prior * (1 - pSlip)) + ((1 - prior) * pGuess))
        } else {
            (prior * pSlip) /
                    ((prior * pSlip) + ((1 - prior) * (1 - pGuess)))
        }

        state[skill] = posterior + (1 - posterior) * pLearn
    }

    override fun getState(): Map<SkillKey, Float> = state.toMap()
}