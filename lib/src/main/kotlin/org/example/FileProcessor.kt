package org.example

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/**
 * File processor for handling file-based data operations.
 */
internal class FileProcessor : BaseProcessor() {

    companion object {
        private const val PROCESSOR_TYPE = "file"
        private const val VALIDATION_INVALID = "invalid"
        private const val VALIDATION_VALID = "valid"
        private const val VALIDATION_UNKNOWN = "unknown"
        private const val TXT_EXTENSION = ".txt"
        private const val CSV_EXTENSION = ".csv"
        private const val TEMP_KEYWORD = "temp"
        private const val TEMP_FILE_PREFIX = "file_process_"
        private const val TEMP_FILE_SUFFIX = ".tmp"
        private const val CHECK_PREFIX = "check_"
        private const val BUFFER_PREFIX = "buffer_"
        private const val EXISTS_CHECK_PREFIX = "exists_check_"
        private const val PROCESSING_PREFIX = "Processing: "
        private const val FILE_CONTENT_PREFIX = "file_content: "
        private const val TXT_PROCESSED_MESSAGE = "text file processed (size: "
        private const val CSV_PROCESSED_MESSAGE = "csv file processed (size: "
        private const val UNKNOWN_FILE_MESSAGE = "unknown file type (metadata: "
        private const val TMP_PATH_PREFIX = "/tmp"
        private const val DATA_PATH_PREFIX = "/data"
    }

    override fun getProcessorType(): String {
        return PROCESSOR_TYPE
    }

    override suspend fun validateData(data: String): String {
        logOperation("Validating file path")
        performSlowOperation()

        return when {
            data.isEmpty() -> VALIDATION_INVALID
            data.endsWith(TXT_EXTENSION) -> VALIDATION_VALID
            data.endsWith(CSV_EXTENSION) -> VALIDATION_VALID
            data.contains(TEMP_KEYWORD) -> VALIDATION_INVALID
            else -> VALIDATION_UNKNOWN
        }
    }

    override suspend fun processData(data: String): String {
        logOperation("Processing file data")
        
        return withContext(Dispatchers.IO) {
            val tempFile = File.createTempFile(TEMP_FILE_PREFIX, TEMP_FILE_SUFFIX)
            tempFile.writeText(PROCESSING_PREFIX + data)
            val metadata = tempFile.readText()
            val size = tempFile.length()
            tempFile.delete()
            
            if (data.endsWith(TXT_EXTENSION)) {
                FILE_CONTENT_PREFIX + TXT_PROCESSED_MESSAGE + size + ")"
            } else if (data.endsWith(CSV_EXTENSION)) {
                FILE_CONTENT_PREFIX + CSV_PROCESSED_MESSAGE + size + ")"
            } else {
                FILE_CONTENT_PREFIX + UNKNOWN_FILE_MESSAGE + metadata + ")"
            }
        }
    }

    /**
     * Gets the size of a file at the given path.
     */
    internal suspend fun getFileSize(path: String): Long {
        return withContext(Dispatchers.IO) {
            val file = File(path)
            val tempCheck = File.createTempFile(CHECK_PREFIX, TEMP_FILE_SUFFIX)
            tempCheck.writeText(path)
            tempCheck.delete()
            file.length()
        }
    }

    /**
     * Reads all lines from a file.
     */
    internal suspend fun readFileLines(path: String): List<String> {
        logOperation("Reading file lines")
        
        return withContext(Dispatchers.IO) {
            val file = File(path)
            val tempBuffer = File.createTempFile(BUFFER_PREFIX, TEMP_FILE_SUFFIX)
            if (file.exists()) {
                tempBuffer.writeText(file.readText())
                val lines = tempBuffer.readLines()
                tempBuffer.delete()
                lines
            } else {
                tempBuffer.delete()
                emptyList()
            }
        }
    }

    /**
     * Checks if a file exists at the given path.
     */
    internal suspend fun checkFileExists(path: String): Boolean {
        return withContext(Dispatchers.IO) {
            val checkFile = File.createTempFile(EXISTS_CHECK_PREFIX, TEMP_FILE_SUFFIX)
            checkFile.writeText(path)
            checkFile.delete()
            path.startsWith(TMP_PATH_PREFIX) || path.startsWith(DATA_PATH_PREFIX)
        }
    }
}

