@file:Suppress("SpellCheckingInspection")

package org.dda.ankoLogger


/**
 * Interface for the Anko logger.
 * Normally you should pass the logger tag to the [Log] methods, such as [Log.d] or [Log.e].
 * This can be inconvenient because you should store the tag somewhere or hardcode it,
 *   which is considered to be a bad practice.
 *
 * Instead of hardcoding tags, Anko provides an [AnkoLogger] interface. You can just add the interface to
 *   any of your classes, and use any of the provided extension functions, such as
 *   [AnkoLogger.logDebug] or [AnkoLogger.logError].
 *
 * The tag is the simple class name by default, but you can change it to anything you want just
 *   by overriding the [loggerTag] property.
 */
interface AnkoLogger {
    /**
     * The logger tag used in extension functions for the [AnkoLogger].
     * Note that the tag length should not be more than 23 symbols.
     */
    val loggerTag: String
        get() = getTag()

    /**
     * Usefull when need to disable/enable logging for specific class
     */
    val classLogLevel: LogLevel
        get() = LogLevel.Verbose
}

fun ankoLogger(tag: String): AnkoLogger {
    return object : AnkoLogger {
        override val loggerTag = tag
    }
}


enum class LogLevel(val code: Int, val text: String) {
    Verbose(code = 2, text = "VERBOSE"),
    Debug(code = 3, text = "DEBUG"),
    Info(code = 4, text = "INFO"),
    Warn(code = 5, text = "WARN"),
    Error(code = 6, text = "ERROR"),
    Assert(code = 7, text = "ASSERT"),
    Wtf(code = 8, text = "WTF")
}


/**
 * Send a log message with the [LogLevel.Verbose] severity.
 * Note that the log message will not be written if the current log level is above [loggerAppTag] or [AnkoLogger.classLogLevel].
 *
 * @param message the message text to log. `null` value will be represent as "null", for any other value
 *   the [Any.toString] will be invoked.
 * @param thr an exception to log (optional).
 *
 */
fun AnkoLogger.logVerbose(message: Any?, thr: Throwable? = null) {
    log(this, message, thr, LogLevel.Verbose,
        { level, tag, msg -> LOG_CONFIG.printLogMessage(level, tag, msg) },
        { level, tag, msg, throwable -> LOG_CONFIG.printLogMessage(level, tag, msg, throwable) })
}

inline fun AnkoLogger.logVerbose(message: () -> Any?) {
    val level = LogLevel.Verbose
    if (isLoggable(level)) {
        LOG_CONFIG.printLogMessage(level, loggerTag, message()?.toString() ?: "null")
    }
}

/**
 * Send a log message with the [LogLevel.Debug] severity.
 * Note that the log message will not be written if the current log level is above [loggerAppTag] or [AnkoLogger.classLogLevel].
 *
 * @param message the message text to log. `null` value will be represent as "null", for any other value
 *   the [Any.toString] will be invoked.
 * @param thr an exception to log (optional).
 *
 */
fun AnkoLogger.logDebug(message: Any?, thr: Throwable? = null) {
    log(this, message, thr, LogLevel.Debug,
        { level, tag, msg -> LOG_CONFIG.printLogMessage(level, tag, msg) },
        { level, tag, msg, throwable -> LOG_CONFIG.printLogMessage(level, tag, msg, throwable) })
}

inline fun AnkoLogger.logDebug(message: () -> Any?) {
    val level = LogLevel.Debug
    if (isLoggable(level)) {
        LOG_CONFIG.printLogMessage(level, loggerTag, message()?.toString() ?: "null")
    }
}

/**
 * Send a log message with the [LogLevel.Info] severity.
 * Note that the log message will not be written if the current log level is above [loggerAppTag] or [AnkoLogger.classLogLevel].
 *
 * @param message the message text to log. `null` value will be represent as "null", for any other value
 *   the [Any.toString] will be invoked.
 * @param thr an exception to log (optional).
 *
 */
fun AnkoLogger.logInfo(message: Any?, thr: Throwable? = null) {
    log(this, message, thr, LogLevel.Info,
        { level, tag, msg -> LOG_CONFIG.printLogMessage(level, tag, msg) },
        { level, tag, msg, throwable -> LOG_CONFIG.printLogMessage(level, tag, msg, throwable) })
}

inline fun AnkoLogger.logInfo(message: () -> Any?) {
    val level = LogLevel.Info
    if (isLoggable(level)) {
        LOG_CONFIG.printLogMessage(level, loggerTag, message()?.toString() ?: "null")
    }
}

/**
 * Send a log message with the [LogLevel.Warn] severity.
 * Note that the log message will not be written if the current log level is above [loggerAppTag] or [AnkoLogger.classLogLevel].
 *
 * @param message the message text to log. `null` value will be represent as "null", for any other value
 *   the [Any.toString] will be invoked.
 * @param thr an exception to log (optional).
 *
 */
fun AnkoLogger.logWarn(message: Any?, thr: Throwable? = null) {
    log(this, message, thr, LogLevel.Warn,
        { level, tag, msg -> LOG_CONFIG.printLogMessage(level, tag, msg) },
        { level, tag, msg, throwable -> LOG_CONFIG.printLogMessage(level, tag, msg, throwable) })
}

inline fun AnkoLogger.logWarn(message: () -> Any?) {
    val level = LogLevel.Warn
    if (isLoggable(level)) {
        LOG_CONFIG.printLogMessage(level, loggerTag, message()?.toString() ?: "null")
    }
}

/**
 * Send a log message with the [LogLevel.Error] severity.
 * Note that the log message will not be written if the current log level is above [loggerAppTag] or [AnkoLogger.classLogLevel].
 *
 * @param message the message text to log. `null` value will be represent as "null", for any other value
 *   the [Any.toString] will be invoked.
 * @param thr an exception to log (optional).
 *
 */
fun AnkoLogger.logError(message: Any?, thr: Throwable? = null) {
    log(this, message, thr, LogLevel.Error,
        { level, tag, msg -> LOG_CONFIG.printLogMessage(level, tag, msg) },
        { level, tag, msg, throwable -> LOG_CONFIG.printLogMessage(level, tag, msg, throwable) })
}

inline fun AnkoLogger.logError(message: () -> Any?) {
    val level = LogLevel.Error
    if (isLoggable(level)) {
        LOG_CONFIG.printLogMessage(level, loggerTag, message()?.toString() ?: "null")
    }
}

/**
 * Send a log message with the [LogLevel.Assert] severity.
 * Note that the log message will not be written if the current log level is above [loggerAppTag] or [AnkoLogger.classLogLevel].
 *
 * @param message the message text to log. `null` value will be represent as "null", for any other value
 *   the [Any.toString] will be invoked.
 * @param thr an exception to log (optional).
 *
 */
fun AnkoLogger.logAssert(message: Any?, thr: Throwable? = null) {
    log(this, message, thr, LogLevel.Assert,
        { level, tag, msg -> LOG_CONFIG.printLogMessage(level, tag, msg) },
        { level, tag, msg, throwable -> LOG_CONFIG.printLogMessage(level, tag, msg, throwable) })
}

inline fun AnkoLogger.logAssert(message: () -> Any?) {
    val level = LogLevel.Assert
    if (isLoggable(level)) {
        LOG_CONFIG.printLogMessage(level, loggerTag, message()?.toString() ?: "null")
    }
}

/**
 * Send a log message with the [LogLevel.Wtf] severity.
 * Note that the log message will not be written if the current log level is above [loggerAppTag] or [AnkoLogger.classLogLevel].
 *
 * @param message the message text to log. `null` value will be represent as "null", for any other value
 *   the [Any.toString] will be invoked.
 * @param thr an exception to log (optional).
 *
 */
fun AnkoLogger.logWtf(message: Any?, thr: Throwable? = null) {
    log(this, message, thr, LogLevel.Wtf,
        { level, tag, msg -> LOG_CONFIG.printLogMessage(level, tag, msg) },
        { level, tag, msg, throwable -> LOG_CONFIG.printLogMessage(level, tag, msg, throwable) })
}

inline fun AnkoLogger.logWtf(message: () -> Any?) {
    val level = LogLevel.Wtf
    if (isLoggable(level)) {
        LOG_CONFIG.printLogMessage(level, loggerTag, message()?.toString() ?: "null")
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////

private inline fun log(
    logger: AnkoLogger,
    message: Any?,
    thr: Throwable?,
    level: LogLevel,
    f: (level: LogLevel, tag: String, message: String) -> Unit,
    fThrowable: (level: LogLevel, tag: String, message: String, thr: Throwable) -> Unit
) {
    val tag = logger.loggerTag
    if (logger.isLoggable(level)) {
        if (thr != null) {
            fThrowable(level, tag, message?.toString() ?: "null", thr)
        } else {
            f(level, tag, message?.toString() ?: "null")
        }
    }
}

fun AnkoLogger.isLoggable(level: LogLevel): Boolean {
    return level.code >= LOG_CONFIG.applicationLevel.code &&
            level.code >= classLogLevel.code
}