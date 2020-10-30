package org.dda.ankoLogger

@Suppress("NOTHING_TO_INLINE")
actual object DefaultLogPrinter : LogPrinter {


    override fun log(appTag: String, tag: String, level: Log.Level, msg: String, thr: Throwable?) {
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

    private val isAndroid: Boolean by lazy {
        // Used for JUnit
        System.getProperty("java.vm.name").equals("Dalvik", ignoreCase = true)
    }

    private inline fun sysLogPrefix(logLevel: Log.Level): String {
        return "THR_ID ${"%04d".format(Thread.currentThread().id)} ${logLevel.text}"
    }

    private inline fun Log.Level.toAndroidLevel(): Int {
        return this.code
    }

}

