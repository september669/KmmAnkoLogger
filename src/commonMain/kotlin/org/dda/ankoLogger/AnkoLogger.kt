@file:Suppress("SpellCheckingInspection")

package org.dda.ankoLogger


fun configAnkoLogger(
    applicationTag: String,
    logLevel: Log.Level = Log.Level.Verbose,
    vararg logPrinter: LogPrinter = arrayOf(DefaultLogPrinter)
) {
    loggerAppTag = applicationTag
    loggerAppLevel = logLevel
    logPrinter.forEach { printer ->
        Log.registerPrinter(printer)
    }
}


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
    val classLogLevel: Log.Level
        get() = Log.Level.Verbose
}

fun ankoLogger(tag: String): AnkoLogger {
    return object : AnkoLogger {
        override val loggerTag = tag
    }
}

@Suppress("MemberVisibilityCanBePrivate")
object Log {

    private val loggers = mutableListOf<LogPrinter>()

    enum class Level(val code: Int, val text: String) {
        Verbose(code = 2, text = "VERBOSE"),
        Debug(code = 3, text = "DEBUG"),
        Info(code = 4, text = "INFO"),
        Warn(code = 5, text = "WARN"),
        Error(code = 6, text = "ERROR"),
        Assert(code = 7, text = "ASSERT"),
        Wtf(code = 8, text = "WTF")
    }

    val appLevel: Level get() = loggerAppLevel

    fun printLogMessage(level: Level, tag: String, msg: String, thr: Throwable? = null) {
        loggers.forEach {
            it.log(appTag = loggerAppTag, tag = tag, level = level, msg = msg, thr = thr)
        }
    }

    fun registerPrinter(printer: LogPrinter) {
        loggers.add(printer)
    }
}


/**
 * Send a log message with the [Log.Level.Verbose] severity.
 * Note that the log message will not be written if the current log level is above [loggerAppTag] or [AnkoLogger.classLogLevel].
 *
 * @param message the message text to log. `null` value will be represent as "null", for any other value
 *   the [Any.toString] will be invoked.
 * @param thr an exception to log (optional).
 *
 */
fun AnkoLogger.logVerbose(message: Any?, thr: Throwable? = null) {
    log(this, message, thr, Log.Level.Verbose,
        { level, tag, msg -> Log.printLogMessage(level, tag, msg) },
        { level, tag, msg, throwable -> Log.printLogMessage(level, tag, msg, throwable) })
}

inline fun AnkoLogger.logVerbose(message: () -> Any?) {
    val level = Log.Level.Verbose
    if (isLoggable(level)) {
        Log.printLogMessage(level, loggerTag, message()?.toString() ?: "null")
    }
}

/**
 * Send a log message with the [Log.Level.Debug] severity.
 * Note that the log message will not be written if the current log level is above [loggerAppTag] or [AnkoLogger.classLogLevel].
 *
 * @param message the message text to log. `null` value will be represent as "null", for any other value
 *   the [Any.toString] will be invoked.
 * @param thr an exception to log (optional).
 *
 */
fun AnkoLogger.logDebug(message: Any?, thr: Throwable? = null) {
    log(this, message, thr, Log.Level.Debug,
        { level, tag, msg -> Log.printLogMessage(level, tag, msg) },
        { level, tag, msg, throwable -> Log.printLogMessage(level, tag, msg, throwable) })
}

inline fun AnkoLogger.logDebug(message: () -> Any?) {
    val level = Log.Level.Debug
    if (isLoggable(level)) {
        Log.printLogMessage(level, loggerTag, message()?.toString() ?: "null")
    }
}

/**
 * Send a log message with the [Log.Level.Info] severity.
 * Note that the log message will not be written if the current log level is above [loggerAppTag] or [AnkoLogger.classLogLevel].
 *
 * @param message the message text to log. `null` value will be represent as "null", for any other value
 *   the [Any.toString] will be invoked.
 * @param thr an exception to log (optional).
 *
 */
fun AnkoLogger.logInfo(message: Any?, thr: Throwable? = null) {
    log(this, message, thr, Log.Level.Info,
        { level, tag, msg -> Log.printLogMessage(level, tag, msg) },
        { level, tag, msg, throwable -> Log.printLogMessage(level, tag, msg, throwable) })
}

inline fun AnkoLogger.logInfo(message: () -> Any?) {
    val level = Log.Level.Info
    if (isLoggable(level)) {
        Log.printLogMessage(level, loggerTag, message()?.toString() ?: "null")
    }
}

/**
 * Send a log message with the [Log.Level.Warn] severity.
 * Note that the log message will not be written if the current log level is above [loggerAppTag] or [AnkoLogger.classLogLevel].
 *
 * @param message the message text to log. `null` value will be represent as "null", for any other value
 *   the [Any.toString] will be invoked.
 * @param thr an exception to log (optional).
 *
 */
fun AnkoLogger.logWarn(message: Any?, thr: Throwable? = null) {
    log(this, message, thr, Log.Level.Warn,
        { level, tag, msg -> Log.printLogMessage(level, tag, msg) },
        { level, tag, msg, throwable -> Log.printLogMessage(level, tag, msg, throwable) })
}

inline fun AnkoLogger.logWarn(message: () -> Any?) {
    val level = Log.Level.Warn
    if (isLoggable(level)) {
        Log.printLogMessage(level, loggerTag, message()?.toString() ?: "null")
    }
}

/**
 * Send a log message with the [Log.Level.Error] severity.
 * Note that the log message will not be written if the current log level is above [loggerAppTag] or [AnkoLogger.classLogLevel].
 *
 * @param message the message text to log. `null` value will be represent as "null", for any other value
 *   the [Any.toString] will be invoked.
 * @param thr an exception to log (optional).
 *
 */
fun AnkoLogger.logError(message: Any?, thr: Throwable? = null) {
    log(this, message, thr, Log.Level.Error,
        { level, tag, msg -> Log.printLogMessage(level, tag, msg) },
        { level, tag, msg, throwable -> Log.printLogMessage(level, tag, msg, throwable) })
}

inline fun AnkoLogger.logError(message: () -> Any?) {
    val level = Log.Level.Error
    if (isLoggable(level)) {
        Log.printLogMessage(level, loggerTag, message()?.toString() ?: "null")
    }
}

/**
 * Send a log message with the [Log.Level.Assert] severity.
 * Note that the log message will not be written if the current log level is above [loggerAppTag] or [AnkoLogger.classLogLevel].
 *
 * @param message the message text to log. `null` value will be represent as "null", for any other value
 *   the [Any.toString] will be invoked.
 * @param thr an exception to log (optional).
 *
 */
fun AnkoLogger.logAssert(message: Any?, thr: Throwable? = null) {
    log(this, message, thr, Log.Level.Assert,
        { level, tag, msg -> Log.printLogMessage(level, tag, msg) },
        { level, tag, msg, throwable -> Log.printLogMessage(level, tag, msg, throwable) })
}

inline fun AnkoLogger.logAssert(message: () -> Any?) {
    val level = Log.Level.Assert
    if (isLoggable(level)) {
        Log.printLogMessage(level, loggerTag, message()?.toString() ?: "null")
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////

private var loggerAppTag = "AnkoLogger"
private var loggerAppLevel = Log.Level.Verbose

private inline fun log(
    logger: AnkoLogger,
    message: Any?,
    thr: Throwable?,
    level: Log.Level,
    f: (level: Log.Level, tag: String, message: String) -> Unit,
    fThrowable: (level: Log.Level, tag: String, message: String, thr: Throwable) -> Unit
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

fun AnkoLogger.isLoggable(level: Log.Level): Boolean {
    return level.code >= loggerAppLevel.code &&
            level.code >= classLogLevel.code
}


/*
@OptIn(ExperimentalContracts::class)
inline fun AnkoLogger.ankoCheck(value: Boolean, lazyMessage: () -> Any) {
    contract {
        returns() implies value
    }
    if (!value) {
        val message = lazyMessage()
        errorLog { message.toString() }
        throw IllegalStateException(message.toString())
    }
}

inline fun <R> AnkoLogger.tryHappy(message: String? = null, block: () -> R): R? {
    return try {
        block()
    } catch (exc: Exception) {
        errorLog(message, exc)
        null
    }
}

inline fun <R> AnkoLogger.tryHappy(default: R, message: String? = null, block: () -> R): R {
    return try {
        block()
    } catch (exc: Exception) {
        errorLog(message, exc)
        default
    }
}
*/
