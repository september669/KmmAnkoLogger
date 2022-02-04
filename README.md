## Anko Logging

This is the fork of [AnkoLogger](https://github.com/Kotlin/anko/wiki/Anko-Commons-%E2%80%93-Logging) for use it in the Kotlin Multiplatform Mobile

### Add AnkoLogger to your project 

```kotlin

kotlin {
    android()
    ios()
    sourceSets {
        val commonMain by getting {
            dependencies {
                api("io.github.september669:AnkoLogger:0.2.7")
            }
        }
    }
}
```

Version 0.2.7 compiled with Kotlin 1.6.0

### Init lib 

Android
```kotlin
class MainApp : MultiDexApplication(), AnkoLogger {
    override fun onCreate() {
        super.onCreate()

        //  init logging with logging tag = "MySuperApp", LogLevel.Verbose 
        //  and default log printer (printout logs to android.util.Log) 
        configAnkoLogger(applicationTag = "MySuperApp")

        //  or you can init logger with specific logLevel 
        //  and add your own log printer 
        configAnkoLogger(
            applicationTag = BuildCfg.loggingAppTag,
            logLevel = LogLevel.Error,
            listPrinters = defaultPrinters() + FirebaseLogPrinter()
        )
        
    }
}

class FirebaseLogPrinter : LogPrinter {

    override fun log(appTag: String, tag: String, level: LogLevel, msg: String, thr: Throwable?) {
        if (level > LogLevel.Info){
            val message = if (thr != null) {
                msg + '\n' + android.util.Log.getStackTraceString(thr)
            } else {
                msg
            }
            Firebase.crashlytics.log("$tag: $message")
        }
    }

    override fun close() {
        //  do nothing
    }
}
```

### Trait-like style logging
```kotlin
class Foo : AnkoLogger {
    private fun someMethod() {
        logInfo("London is the capital of Great Britain")
        logWarn{"London is the capital of Great Britain"}
    }
}

fun main(){

	//	Configurate logger
	configAnkoLogger(applicationTag = "SomeApp")

	val foo = Foo()
	foo.someMethod()
	
}
```

Each method has two versions plain and lazy:

```kotlin
	logInfo("London is the capital of Great Britain")
	logWarn{"London is the capital of Great Britain"}     
```

Lambda result will be calculated only if `AnkoLogger.isLoggable(level: LogLevel)` is true.


### Logger object style
You can also use AnkoLogger as a plain object

```kotlin
class Foo {

	val logWithASpecificTag = ankoLogger("SomeTag")

    private fun someMethod() {
    	logWithASpecificTag.logWarn("London is the capital of Great Britain")
    }
}

```