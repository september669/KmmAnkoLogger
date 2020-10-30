@file:Suppress("SpellCheckingInspection")

package org.dda.ankoLogger


private var loggerAppTag = "AnkoLogger"
private var loggerAppLevel = Log.Level.Verbose

fun configAnkoLogger(
    logTag: String,
    logLevel: Log.Level = Log.Level.Verbose,
    vararg logPrinter: LogPrinter = arrayOf(DefaultLogPrinter)
) {
    loggerAppTag = logTag
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
 *   [AnkoLogger.debug] or [AnkoLogger.errorLog].
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

    private fun printLogMessage(level: Level, tag: String, msg: String, thr: Throwable?) {
        loggers.forEach {
            it.log(appTag = loggerAppTag, tag = tag, level = level, msg = msg, thr = thr)
        }
    }

    fun registerPrinter(printer: LogPrinter) {
        loggers.add(printer)
    }

    fun e(tag: String, msg: String, thr: Throwable? = null) {
        printLogMessage(level = Level.Error, tag = tag, msg = msg, thr = thr)
    }

    fun w(tag: String, msg: String, thr: Throwable? = null) {
        printLogMessage(level = Level.Warn, tag = tag, msg = msg, thr = thr)
    }

    fun v(tag: String, msg: String, thr: Throwable? = null) {
        printLogMessage(level = Level.Verbose, tag = tag, msg = msg, thr = thr)
    }


    fun d(tag: String, msg: String, thr: Throwable? = null) {
        printLogMessage(level = Level.Debug, tag = tag, msg = msg, thr = thr)
    }

    fun i(tag: String, msg: String, thr: Throwable? = null) {
        printLogMessage(level = Level.Info, tag = tag, msg = msg, thr = thr)
    }

    fun wtf(tag: String, msg: String, thr: Throwable? = null) {
        printLogMessage(level = Level.Wtf, tag = tag, msg = msg, thr = thr)
    }
}


/**
 * Send a log message with the [Log.VERBOSE] severity.
 * Note that the log message will not be written if the current log level is above [Log.VERBOSE].
 * The default log level is [Log.INFO].
 *
 * @param message the message text to log. `null` value will be represent as "null", for any other value
 *   the [Any.toString] will be invoked.
 * @param thr an exception to log (optional).
 *
 * @see [Log.v].
 */
fun AnkoLogger.verbose(message: Any?, thr: Throwable? = null) {
    log(this, message, thr, Log.Level.Verbose,
        { tag, msg -> Log.v(tag, msg) },
        { tag, msg, throwable -> Log.v(tag, msg, throwable) })
}

/**
 * Send a log message with the [Log.DEBUG] severity.
 * Note that the log message will not be written if the current log level is above [Log.DEBUG].
 * The default log level is [Log.INFO].
 *
 * @param message the message text to log. `null` value will be represent as "null", for any other value
 *   the [Any.toString] will be invoked.
 * @param thr an exception to log (optional).
 *
 * @see [Log.d].
 */
fun AnkoLogger.debug(message: Any?, thr: Throwable? = null) {
    log(this, message, thr, Log.Level.Debug,
        { tag, msg -> Log.d(tag, msg) },
        { tag, msg, throwable -> Log.d(tag, msg, throwable) })
}

/**
 * Send a log message with the [Log.INFO] severity.
 * Note that the log message will not be written if the current log level is above [Log.INFO]
 *   (it is the default level).
 *
 * @param message the message text to log. `null` value will be represent as "null", for any other value
 *   the [Any.toString] will be invoked.
 * @param thr an exception to log (optional).
 *
 * @see [Log.i].
 */
fun AnkoLogger.info(message: Any?, thr: Throwable? = null) {
    log(this, message, thr, Log.Level.Info,
        { tag, msg -> Log.i(tag, msg) },
        { tag, msg, throwable -> Log.i(tag, msg, throwable) })
}

/**
 * Send a log message with the [Log.WARN] severity.
 * Note that the log message will not be written if the current log level is above [Log.WARN].
 * The default log level is [Log.INFO].
 *
 * @param message the message text to log. `null` value will be represent as "null", for any other value
 *   the [Any.toString] will be invoked.
 * @param thr an exception to log (optional).
 *
 * @see [Log.w].
 */
fun AnkoLogger.warn(message: Any?, thr: Throwable? = null) {
    log(this, message, thr, Log.Level.Warn,
        { tag, msg -> Log.w(tag, msg) },
        { tag, msg, throwable -> Log.w(tag, msg, throwable) })
}

/**
 * Send a log message with the [Log.ERROR] severity.
 * Note that the log message will not be written if the current log level is above [Log.ERROR].
 * The default log level is [Log.INFO].
 *
 * @param message the message text to log. `null` value will be represent as "null", for any other value
 *   the [Any.toString] will be invoked.
 * @param thr an exception to log (optional).
 *
 * @see [Log.e].
 */
fun AnkoLogger.errorLog(message: Any?, thr: Throwable? = null) {
    log(this, message, thr, Log.Level.Error,
        { tag, msg -> Log.e(tag, msg) },
        { tag, msg, throwable -> Log.e(tag, msg, throwable) })
}

/**
 * Send a log message with the "What a Terrible Failure" severity.
 * Report an exception that should never happen.
 *
 * @param message the message text to log. `null` value will be represent as "null", for any other value
 *   the [Any.toString] will be invoked.
 * @param thr an exception to log (optional).
 *
 * @see [Log.wtf].
 */
fun AnkoLogger.wtf(message: Any?, thr: Throwable? = null) {
    if (thr != null) {
        Log.wtf(loggerTag, message?.toString() ?: "null", thr)
    } else {
        Log.wtf(loggerTag, message?.toString() ?: "null")
    }
}

/**
 * Send a log message with the [Log.VERBOSE] severity.
 * Note that the log message will not be written if the current log level is above [Log.VERBOSE].
 * The default log level is [Log.INFO].
 *
 * @param message the function that returns message text to log.
 *   `null` value will be represent as "null", for any other value the [Any.toString] will be invoked.
 *
 * @see [Log.v].
 */
inline fun AnkoLogger.verbose(message: () -> Any?) {
    if (Log.isLoggable(Log.Level.Verbose, classLogLevel)) {
        val tag = loggerTag
        Log.v(tag, message()?.toString() ?: "null")
    }
}

/**
 * Send a log message with the [Log.DEBUG] severity.
 * Note that the log message will not be written if the current log level is above [Log.DEBUG].
 * The default log level is [Log.INFO].
 *
 * @param message the function that returns message text to log.
 *   `null` value will be represent as "null", for any other value the [Any.toString] will be invoked.
 *
 * @see [Log.d].
 */
inline fun AnkoLogger.debug(message: () -> String?) {
    if (Log.isLoggable(Log.Level.Debug, classLogLevel)) {
        val tag = loggerTag
        Log.d(tag, message() ?: "null")
    }
}

/**
 * Send a log message with the [Log.INFO] severity.
 * Note that the log message will not be written if the current log level is above [Log.INFO].
 * The default log level is [Log.INFO].
 *
 * @param message the function that returns message text to log.
 *   `null` value will be represent as "null", for any other value the [Any.toString] will be invoked.
 *
 * @see [Log.i].
 */
inline fun AnkoLogger.info(message: () -> String?) {
    if (Log.isLoggable(Log.Level.Info, classLogLevel)) {
        val tag = loggerTag
        Log.i(tag, message() ?: "null")
    }
}

/**
 * Send a log message with the [Log.WARN] severity.
 * Note that the log message will not be written if the current log level is above [Log.WARN].
 * The default log level is [Log.INFO].
 *
 * @param message the function that returns message text to log.
 *   `null` value will be represent as "null", for any other value the [Any.toString] will be invoked.
 *
 * @see [Log.w].
 */
inline fun AnkoLogger.warn(message: () -> String?) {
    if (Log.isLoggable(Log.Level.Warn, classLogLevel)) {
        val tag = loggerTag
        Log.w(tag, message() ?: "null")
    }
}

/**
 * Send a log message with the [Log.ERROR] severity.
 * Note that the log message will not be written if the current log level is above [Log.ERROR].
 * The default log level is [Log.INFO].
 *
 * @param message the function that returns message text to log.
 *   `null` value will be represent as "null", for any other value the [Any.toString] will be invoked.
 *
 * @see [Log.e].
 */
inline fun AnkoLogger.errorLog(message: () -> String?) {
    if (Log.isLoggable(Log.Level.Error, classLogLevel)) {
        val tag = loggerTag
        Log.e(tag, message() ?: "null")
    }
}


inline fun AnkoLogger.printStackTrace(message: () -> Any?) {
    errorLog(message().toString(), Exception())
}


private inline fun log(
    logger: AnkoLogger,
    message: Any?,
    thr: Throwable?,
    level: Log.Level,
    f: (String, String) -> Unit,
    fThrowable: (String, String, Throwable) -> Unit
) {
    val tag = logger.loggerTag
    if (Log.isLoggable(level, logger.classLogLevel)) {
        if (thr != null) {
            fThrowable(tag, message?.toString() ?: "null", thr)
        } else {
            f(tag, message?.toString() ?: "null")
        }
    }
}

fun Log.isLoggable(level: Log.Level, classLogLevel: Log.Level): Boolean {
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
