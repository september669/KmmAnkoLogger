package org.dda.ankoLogger

actual class AtomicRef<T> actual constructor(value: T) {

    private val lock = Any()

    actual var value: T = value
        set(value) {
            synchronized(lock) {
                field = value
            }
        }

}

@Suppress("NOTHING_TO_INLINE")
actual inline fun <T> T.freeze(): T {
    return this
}