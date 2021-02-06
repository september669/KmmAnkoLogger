package org.dda.ankoLogger

import kotlin.reflect.KProperty

class AtomicRefProperty<Owner : Any, T : Any>(value: T) :
    kotlin.properties.ReadWriteProperty<Owner, T> {

    private val atomicRef = AtomicRef(value.freeze())

    override fun getValue(thisRef: Owner, property: KProperty<*>): T {
        return atomicRef.value
    }

    override fun setValue(thisRef: Owner, property: KProperty<*>, value: T) {
        atomicRef.value = value.freeze()
    }

}

inline fun <reified Owner : Any, reified T : Any> Owner.atomicRef(value: T): AtomicRefProperty<Owner, T> {
    return AtomicRefProperty(value)
}