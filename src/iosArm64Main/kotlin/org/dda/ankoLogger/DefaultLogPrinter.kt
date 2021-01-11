package org.dda.ankoLogger

import kotlinx.cinterop.ptr
import platform.darwin.*
import kotlin.collections.set

actual object DefaultLogPrinter : LogPrinter {

    override fun log(appTag: String, tag: String, level: LogLevel, msg: String, thr: Throwable?) {

        val logType = getLogType(appTag = appTag, tag = tag)
        val logLevelIos = when (level) {
            LogLevel.Verbose -> OS_LOG_TYPE_DEFAULT
            LogLevel.Debug -> OS_LOG_TYPE_DEBUG
            LogLevel.Info -> OS_LOG_TYPE_INFO
            LogLevel.Warn -> OS_LOG_TYPE_INFO
            LogLevel.Error -> OS_LOG_TYPE_ERROR
            LogLevel.Assert -> OS_LOG_TYPE_FAULT
            LogLevel.Wtf -> OS_LOG_TYPE_FAULT
        }
        //val foo: CPointer<mach_header>? = interpretCPointer<mach_header>(__dso_handle.rawPtr)
        //kotlinx.cinterop.cValuesOf(foo)
        _os_log_internal(__dso_handle.ptr, logType, logLevelIos, msg)
        if (applicationLogLevel.code < LogLevel.Info.code) {
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