package com.masterproject.englishapp.learning.selector

import com.masterproject.englishapp.data.Identifiable
import com.masterproject.englishapp.learning.core.KnowledgeModel
import com.masterproject.englishapp.learning.core.SkillKey
import com.masterproject.englishapp.selector.Selector

class AdaptiveSelector<T : Identifiable>(
    private val model: KnowledgeModel
) : Selector<T> {

    override fun select(pool: List<T>, count: Int): List<T> {
        return pool
            .shuffled()
            .sortedBy { item ->
                model.mastery(SkillKey(item.id.toString()))
            }.take(count)
    }
}