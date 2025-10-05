package klib.data.js

import platform.Foundation.NSLog
import platform.Foundation.NSString
import platform.JavaScriptCore.JSContext
import platform.JavaScriptCore.JSVirtualMachine
import platform.JavaScriptCore.setObject

@Suppress("CAST_NEVER_SUCCEEDS")
public fun JSContext.Companion.create(): JSContext =
    JSContext(JSVirtualMachine()).apply {
        inspectable = true
        // Log console.log messages
        val consoleLog: (String) -> Unit = { value ->
            NSLog("JSLog: $value")
        }

        setObject(consoleLog, "_consoleLog" as NSString)

        // Log console.error messages
        val consoleError: (String) -> Unit = { value ->
            NSLog("JSError: $value")
        }

        setObject(consoleError, "_consoleError" as NSString)

        evaluateScript(
            """var console = {
        log: _consoleLog,
        error: _consoleError
        }""".trimMargin(),
        )

        // Log exceptions
        exceptionHandler = { _, value ->
            NSLog("JSError: $value")
        }

        // TextEncoder
        loadTextEncoder()

        // Base64
        loadBase64()

        // Crypto
        loadCrypto()
    }
