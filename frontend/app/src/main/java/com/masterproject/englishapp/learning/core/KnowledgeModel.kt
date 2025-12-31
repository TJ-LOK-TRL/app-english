package com.masterproject.englishapp.learning.core

interface KnowledgeModel {
    fun update(skill: SkillKey, success: Boolean)
    fun mastery(skill: SkillKey): Float
}