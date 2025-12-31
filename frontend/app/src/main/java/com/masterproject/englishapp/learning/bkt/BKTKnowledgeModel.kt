package com.masterproject.englishapp.learning.bkt

import com.masterproject.englishapp.learning.core.KnowledgeModel
import com.masterproject.englishapp.learning.core.SkillKey

class BKTKnowledgeModel(
    private val pLearn: Float = 0.1f,
    private val pSlip: Float = 0.1f,
    private val pGuess: Float = 0.2f
) : KnowledgeModel {

    private val state = mutableMapOf<SkillKey, Float>()

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
}