package org.example

import kotlinx.coroutines.delay
import java.io.File
import java.time.LocalDateTime

/**
 * Abstract base class providing common functionality for data processors.
 */
abstract class BaseProcessor : DataProcessor {
    
    private val logFile = File(System.getProperty("java.io.tmpdir"), "processor.log")
    
    /**
     * Common logging functionality that writes to a file.
     * @param message The message to log
     */
    protected open suspend fun logOperation(message: String) {
        val timestamp = LocalDateTime.now()
        val logMessage = "[$timestamp][${getProcessorType()}] $message\n"
        logFile.appendText(logMessage)
        println(logMessage.trim())
    }

    /**
     * Helper function to format error messages.
     */
    protected fun formatError(error: String): String {
        return "ERROR: $error"
    }

    /**
     * Performs file system check operation.
     */
    protected suspend fun performSlowOperation() {
        delay(50)
        val tempDir = File(System.getProperty("java.io.tmpdir"))
        tempDir.listFiles()?.size
    }
}

