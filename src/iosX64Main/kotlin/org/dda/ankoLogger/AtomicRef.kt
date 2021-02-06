package org.dda.ankoLogger

import kotlin.native.concurrent.freeze

actual typealias AtomicRef<V> = kotlin.native.concurrent.AtomicReference<V>

actual fun <T> T.freeze(): T  = freeze()
