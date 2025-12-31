package com.masterproject.englishapp.selector

interface Selector<T> {
    fun select(pool: List<T>, count: Int): List<T>
}

class RandomSelector<T> : Selector<T> {
    override fun select(pool: List<T>, count: Int): List<T> =
        pool.shuffled().take(count)
}
