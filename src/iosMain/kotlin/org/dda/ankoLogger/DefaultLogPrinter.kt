package org.dda.ankoLogger

import kotlinx.cinterop.ptr
import platform.darwin.*

actual class DefaultLogPrinter : LogPrinter {

    override fun log(appTag: String, tag: String, level: LogLevel, msg: String, thr: Throwable?) {
        val logType = getLogType(appTag = appTag, tag = tag)
        if (logType != null) {
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
        }
        if (LOG_CONFIG.applicationLevel.code < LogLevel.Info.code) {
            println("${sysLogPrefix(level)} $tag: $msg")
            thr?.printStackTrace()

        }
    }

    private var isOpen by atomicRef(true)
    private var logMap by atomicRef(mapOf<AppTag, Map<ClassTag, os_log_t>>())
    private val lock by lazy {
        Lock()
    }

    private fun getLogType(appTag: String, tag: String): os_log_t {
        return if (isOpen) {
            logMap[appTag]?.get(tag)
                ?: lock.withLock {
                    logMap[appTag]?.get(tag)
                        ?: os_log_create(appTag, tag)?.also { newOsLog ->
                            val newMap = mapOf(appTag to mapOf(appTag to newOsLog)) + logMap
                            logMap = newMap
                        }
                }
        } else {
            null
        }
    }

    private inline fun sysLogPrefix(logLevel: LogLevel): String {
        //return "THR_ID ${"%04d".format(Thread.currentThread().id)} ${logLevel.text}"
        return logLevel.text
    }


    private fun closeOsLog(osLog: os_log_t) {
        //  TODO: need really close it
        println("fake closing os_log_t: $osLog")
    }

    override fun close() {
        lock.withLock {
            logMap.forEach { appEntry ->
                appEntry.value.forEach { classEntry ->
                    closeOsLog(classEntry.value)
                }
            }
            isOpen = false
        }
        lock.close()
    }

}

private typealias AppTag = String
private typealias ClassTag = String