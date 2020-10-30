package org.dda.ankoLogger

actual fun AnkoLogger.getTag(): String {
    val clazz = javaClass
    val key = TagKey(this.hashCode(), clazz)
    return tags[key] ?: "${clazz.simpleName}_${this.hashCode()}".also {
        synchronized(tags) {
            tags[key] = it
            if (tags.size > tagsSize) {
                val tagsOldestKey = tags.keys.firstOrNull()
                tags.remove(tagsOldestKey)
            }
        }
    }
}

private data class TagKey(val hashCode: Int, val clazz: Class<*>)

private const val tagsSize = 300
private val tags = mutableMapOf<TagKey, String>()
