package org.dda.ankoLogger

import kotlin.reflect.KClass

actual fun AnkoLogger.getTag(): String {
    val clazz = this::class
    val key = TagKey(this.hashCode(), clazz)
    return tags[key] ?: "${clazz.simpleName}_${this.hashCode()}".also {
            tags[key] = it
            if (tags.size > tagsSize) {
                val tagsOldestKey = tags.keys.firstOrNull()
                tags.remove(tagsOldestKey)
            }
    }
}


private data class TagKey(val hashCode: Int, val clazz: KClass<*>)

private const val tagsSize = 300
private val tags = mutableMapOf<TagKey, String>()
