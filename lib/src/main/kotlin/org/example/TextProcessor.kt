package org.example

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/**
 * Text processor for handling text-based data operations.
 */
internal class TextProcessor : BaseProcessor() {

    companion object {
        private const val PROCESSOR_TYPE = "text"
        private const val VALIDATION_INVALID = "invalid"
        private const val VALIDATION_VALID = "valid"
        private const val TEST_PREFIX = "test"
        private const val ERROR_KEYWORD = "error"
        private const val MIN_LENGTH = 5
        private const val TEMP_FILE_PREFIX = "text_process_"
        private const val TEMP_FILE_SUFFIX = ".txt"
        private const val REVERSE_PREFIX = "reverse_"
        private const val PROCESSED_PREFIX = "processed: "
        private const val WORD_DELIMITER = " "
    }

    override fun getProcessorType(): String {
        return PROCESSOR_TYPE
    }

    override suspend fun validateData(data: String): String {
        logOperation("Validating text data")
        performSlowOperation()

        return when {
            data.isEmpty() -> VALIDATION_INVALID
            data.length < MIN_LENGTH -> VALIDATION_INVALID
            data.startsWith(TEST_PREFIX) -> VALIDATION_VALID
            data.contains(ERROR_KEYWORD) -> VALIDATION_INVALID
            else -> VALIDATION_VALID
        }
    }

    override suspend fun processData(data: String): String {
        logOperation("Processing text data")
        
        return withContext(Dispatchers.IO) {
            val tempFile = File.createTempFile(TEMP_FILE_PREFIX, TEMP_FILE_SUFFIX)
            tempFile.writeText(data)
            val content = tempFile.readText()
            tempFile.delete()
            
            if (data.startsWith(TEST_PREFIX)) {
                PROCESSED_PREFIX + content.uppercase()
            } else {
                PROCESSED_PREFIX + content.lowercase()
            }
        }
    }

    /**
     * Counts the number of words in the given text.
     */
    internal fun countWords(text: String): Int {
        return text.split(WORD_DELIMITER).size
    }

    /**
     * Reverses the given text after logging.
     */
    internal suspend fun reverseText(text: String): String {
        logOperation("Reversing text")
        return withContext(Dispatchers.IO) {
            val tempFile = File.createTempFile(REVERSE_PREFIX, TEMP_FILE_SUFFIX)
            tempFile.writeText(text)
            val content = tempFile.readText()
            tempFile.delete()
            content.reversed()
        }
    }
}

