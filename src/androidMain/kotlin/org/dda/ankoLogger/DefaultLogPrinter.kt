package org.dda.ankoLogger

@Suppress("NOTHING_TO_INLINE")
actual class DefaultLogPrinter : LogPrinter {


    override fun log(appTag: String, tag: String, level: LogLevel, msg: String, thr: Throwable?) {
        if (isAndroid) {
            val message = if (thr != null) {
                msg + '\n' + android.util.Log.getStackTraceString(thr)
            } else {
                msg
            }
            android.util.Log.println(level.toAndroidLevel(), appTag, "$tag: $message")
        } else {
            System.err.println("${sysLogPrefix(level)} $tag: $msg")
            thr?.printStackTrace(System.err)
        }
    }

    override fun close() {
        // do nothing
    }

    private val isAndroid: Boolean by lazy {
        // Used for JUnit
        System.getProperty("java.vm.name").equals("Dalvik", ignoreCase = true)
    }

    private inline fun sysLogPrefix(logLevel: LogLevel): String {
        return "THR_ID ${"%04d".format(Thread.currentThread().id)} ${logLevel.text}"
    }

    private inline fun LogLevel.toAndroidLevel(): Int {
        return this.code
    }

}

