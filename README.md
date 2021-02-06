## Anko Logging

This is the fork of [AnkoLogger](https://github.com/Kotlin/anko/wiki/Anko-Commons-%E2%80%93-Logging) for use it in the Kotlin Multiplatform Mobile

### Using AnkoLogger in your project 

In progress

### Trait-like style logging
```
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

Each method has two versions lain and lazy (inlined):

```
	logInfo("London is the capital of Great Britain")
	logWarn{"London is the capital of Great Britain"}     
```

Lambda result will be calculated only if `AnkoLogger.isLoggable(level: LogLevel)` is true.


### Logger object style
You can also use AnkoLogger as a plain object

```
class Foo {

	val logWithASpecificTag = ankoLogger("SomeTage")

    private fun someMethod() {
    	logWithASpecificTag.logWarn("London is the capital of Great Britain")
    }
}

```