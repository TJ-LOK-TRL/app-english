package com.masterproject.englishapp.learning.blind

import com.masterproject.englishapp.learning.core.KnowledgeModel
import com.masterproject.englishapp.learning.core.SkillKey

class BlindKnowledgeModel : KnowledgeModel {
    override fun update(skill: SkillKey, success: Boolean) { }
    override fun mastery(skill: SkillKey): Float = 0.5f
    override fun getState(): Map<SkillKey, Float> = emptyMap()
}