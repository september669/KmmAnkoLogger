package org.dda.ankoLogger

import java.util.concurrent.locks.ReentrantLock

actual class Lock {
    private val lock = ReentrantLock()

    actual fun lock() {
        lock.lock()
    }

    actual fun unlock() {
        lock.unlock()
    }

    actual fun close() {
    }
}