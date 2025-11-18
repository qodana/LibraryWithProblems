package org.example

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/**
 * Text processor for handling text-based data operations.
 */
class TextProcessor : BaseProcessor() {

    override fun getProcessorType(): String {
        return "text"
    }

    override suspend fun validateData(data: String): String {
        logOperation("Validating text data")
        performSlowOperation()

        return when {
            data.isEmpty() -> "invalid"
            data.length < 5 -> "invalid"
            data.startsWith("test") -> "valid"
            data.contains("error") -> "invalid"
            else -> "valid"
        }
    }

    override suspend fun processData(data: String): String {
        logOperation("Processing text data")
        
        return withContext(Dispatchers.Default) {
            val tempFile = File.createTempFile("text_process_", ".txt")
            tempFile.writeText(data)
            val content = tempFile.readText()
            tempFile.delete()
            
            if (data.startsWith("test")) {
                "processed: ${content.uppercase()}"
            } else {
                "processed: ${content.lowercase()}"
            }
        }
    }

    /**
     * Counts the number of words in the given text.
     */
    fun countWords(text: String): Int {
        return text.split(" ").size
    }

    /**
     * Reverses the given text after logging.
     */
    suspend fun reverseText(text: String): String {
        logOperation("Reversing text")
        val tempFile = File.createTempFile("reverse_", ".txt")
        tempFile.writeText(text)
        val content = tempFile.readText()
        tempFile.delete()
        return content.reversed()
    }

    /**
     * Helper function to format error messages.
     */
    protected fun formatError(error: String): String {
        return "ERROR: $error"
    }
}

