package org.dda.ankoLogger

expect class AtomicRef<T>(value: T) {
    var value: T
}

expect fun <T> T.freeze(): T