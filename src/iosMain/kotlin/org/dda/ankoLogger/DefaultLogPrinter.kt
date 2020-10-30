package org.dda.ankoLogger

import kotlinx.cinterop.ptr
import platform.darwin.*
import kotlin.collections.MutableMap
import kotlin.collections.mutableMapOf
import kotlin.collections.set

actual object DefaultLogPrinter : LogPrinter {

    override fun log(appTag: String, tag: String, level: Log.Level, msg: String, thr: Throwable?) {

        val logType = getLogType(appTag = appTag, tag = tag)
        val logLevelIos = when (level) {
            Log.Level.Verbose -> OS_LOG_TYPE_DEFAULT
            Log.Level.Debug -> OS_LOG_TYPE_DEBUG
            Log.Level.Info -> OS_LOG_TYPE_INFO
            Log.Level.Warn -> OS_LOG_TYPE_INFO
            Log.Level.Error -> OS_LOG_TYPE_ERROR
            Log.Level.Assert -> OS_LOG_TYPE_FAULT
            Log.Level.Wtf -> OS_LOG_TYPE_FAULT
        }
        //val foo: CPointer<mach_header>? = interpretCPointer<mach_header>(__dso_handle.rawPtr)
        //kotlinx.cinterop.cValuesOf(foo)
        _os_log_internal(__dso_handle.ptr, logType, logLevelIos, msg)
        if (Log.appLevel.code < Log.Level.Info.code) {
            println(msg)
        }
    }

    private val logMap = mutableMapOf<String, MutableMap<String, os_log_t>>()
    private fun getLogType(appTag: String, tag: String): os_log_t {
        val appMap = logMap[appTag]
        return if (appMap != null) {
            appMap[tag] ?: os_log_create(appTag, tag).also { logT ->
                appMap[tag] = logT
            }
        } else {
            os_log_create(appTag, tag).also {
                logMap[appTag] = mutableMapOf(appTag to it)
            }
        }
    }

}