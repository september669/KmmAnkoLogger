package org.dda.ankoLogger

/**
 * @param [applicationTag] Tag for application
 * @param [logLevel] application minimal log level, see org.dda.ankoLogger.LogLevel
 * @param [listPrinters] you can use [DefaultLogPrinter] or create you own printers (e.g. add post logs to crashlytics)
 */
fun configAnkoLogger(
    applicationTag: String,
    logLevel: LogLevel = LogLevel.Verbose,
    listPrinters: Collection<LogPrinter> = listOf(DefaultLogPrinter)
) {
    LogConfig(
        applicationTag = applicationTag,
        applicationLevel = logLevel,
        printers = listPrinters
    )

    LOG_CONFIG_REF.value = LogConfig(
        applicationTag = applicationTag,
        applicationLevel = logLevel,
        printers = listPrinters
    ).freeze()

}

internal val LOG_CONFIG_REF = AtomicRef(LogConfig().freeze())
val LOG_CONFIG get() = LOG_CONFIG_REF.value

class LogConfig(
    val applicationTag: String = "AnkoLogger",
    val applicationLevel: LogLevel = LogLevel.Verbose,
    val printers: Collection<LogPrinter> = emptyList()
) {
    fun printLogMessage(level: LogLevel, tag: String, msg: String, thr: Throwable? = null) {
        printers.forEach {
            it.log(appTag = applicationTag, tag = tag, level = level, msg = msg, thr = thr)
        }
    }
}