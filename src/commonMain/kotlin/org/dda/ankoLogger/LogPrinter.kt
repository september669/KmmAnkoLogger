package org.dda.ankoLogger


interface LogPrinter {
    fun log(appTag: String, tag: String, level: LogLevel, msg: String, thr: Throwable?)
}

expect object DefaultLogPrinter : LogPrinter