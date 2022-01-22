package com.fleet.fleet_backend.core.utils

import kotlin.random.Random

object IdentityGenerator {

    private val randomString = RandomString(16)

    fun generateSimple(): String = randomString.next()
}

class RandomString(private val length: Int) {

    companion object {
        private val lowercase = ('a'..'z').toList()
        private val uppercase = ('A'..'Z').toList()
        private val numbers = ('0'..'9').toList()
        private val all = lowercase + uppercase + numbers
    }

    fun next(): String = nextRandom(all)

    private fun nextRandom(chars: List<Char>) =
        (1..length)
            .map { _ -> Random.nextInt(0, chars.size) }
            .map(chars::get)
            .joinToString("")

    fun nextNumbers(): String = nextRandom(numbers)
}