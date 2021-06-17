package org.dda.ankoLogger



import kotlinx.cinterop.alloc
import kotlinx.cinterop.free
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.ptr
import utils.*


actual class Lock {
    private val mutex = nativeHeap.alloc<ktor_mutex_t>()

    init {
        freeze()
        ktor_mutex_create(mutex.ptr).checkResult { "Failed to create mutex." }
    }

    actual fun lock() {
        ktor_mutex_lock(mutex.ptr).checkResult { "Failed to lock mutex." }
    }

    actual fun unlock() {
        ktor_mutex_unlock(mutex.ptr).checkResult { "Failed to unlock mutex." }
    }

    actual fun close() {
        ktor_mutex_destroy(mutex.ptr)
        nativeHeap.free(mutex)
    }
}

private inline fun Int.checkResult(block: () -> String) {
    check(this == 0, block)
}
