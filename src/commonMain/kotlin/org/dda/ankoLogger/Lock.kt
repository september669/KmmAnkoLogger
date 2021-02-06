package org.dda.ankoLogger

expect class Lock() {
    fun lock()
    fun unlock()

    fun close()
}

inline fun <R> Lock.withLock(crossinline block: () -> R): R {
    try {
        lock()
        return block()
    } finally {
        unlock()
    }
}
