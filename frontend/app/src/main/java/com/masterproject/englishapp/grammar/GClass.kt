package com.masterproject.englishapp.grammar

import kotlin.reflect.KClass

enum class GClass(val valueType: KClass<out WordValue>) {
    NOUN(NounValue::class),
    VERB(VerbValue::class),
    ADJECTIVE(AdjectiveValue::class)
}