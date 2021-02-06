package org.dda.ankoLogger

import kotlin.test.Test

class TestDefaultLogPrinter {

    @Test
    fun checkInvalidMutabilityException_for_ios_printer(){
        //  check os_log_create and add to log cache
        //  in org.dda.ankoLogger.DefaultLogPrinter.getLogType
        configAnkoLogger(
            applicationTag = "AppTag",
            listPrinters = listOf(DefaultLogPrinter())
        )
        //TestTags.TestClass().logError( "London is the capital", IllegalArgumentException("Cool exception") )
        TestTags.TestClass().logError( "London is the capital")
    }

    @Test
    fun checkReConfig(){
        configAnkoLogger(
            applicationTag = "AppTag",
            listPrinters = listOf(DefaultLogPrinter())
        )
        TestTags.TestClass().logError( "Print by first printer")
        configAnkoLogger(
            applicationTag = "AppTag",
            listPrinters = listOf(DefaultLogPrinter())
        )
        TestTags.TestClass().logError( "Print by second printer")
    }

}