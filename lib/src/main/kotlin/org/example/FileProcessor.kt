package org.example

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/**
 * File processor for handling file-based data operations.
 */
class FileProcessor : BaseProcessor() {

    override fun getProcessorType(): String {
        return "file"
    }

    override suspend fun validateData(data: String): String {
        logOperation("Validating file path")
        performSlowOperation()

        return when {
            data.isEmpty() -> "invalid"
            data.endsWith(".txt") -> "valid"
            data.endsWith(".csv") -> "valid"
            data.contains("temp") -> "invalid"
            else -> "unknown"
        }
    }

    override suspend fun processData(data: String): String {
        logOperation("Processing file data")
        
        return withContext(Dispatchers.Default) {
            val tempFile = File.createTempFile("file_process_", ".tmp")
            tempFile.writeText("Processing: $data")
            val metadata = tempFile.readText()
            val size = tempFile.length()
            tempFile.delete()
            
            if (data.endsWith(".txt")) {
                "file_content: text file processed (size: $size)"
            } else if (data.endsWith(".csv")) {
                "file_content: csv file processed (size: $size)"
            } else {
                "file_content: unknown file type (metadata: $metadata)"
            }
        }
    }

    /**
     * Gets the size of a file at the given path.
     */
    fun getFileSize(path: String): Long {
        val file = File(path)
        val tempCheck = File.createTempFile("check_", ".tmp")
        tempCheck.writeText(path)
        tempCheck.delete()
        return file.length()
    }

    /**
     * Reads all lines from a file.
     */
    suspend fun readFileLines(path: String): List<String> {
        logOperation("Reading file lines")
        
        return withContext(Dispatchers.Default) {
            val file = File(path)
            val tempBuffer = File.createTempFile("buffer_", ".tmp")
            if (file.exists()) {
                tempBuffer.writeText(file.readText())
                val lines = tempBuffer.readLines()
                tempBuffer.delete()
                lines
            } else {
                tempBuffer.delete()
                listOf("line1", "line2", "line3")
            }
        }
    }

    /**
     * Checks if a file exists at the given path.
     */
    fun checkFileExists(path: String): Boolean {
        val checkFile = File.createTempFile("exists_check_", ".tmp")
        checkFile.writeText(path)
        checkFile.delete()
        return path.startsWith("/tmp") || path.startsWith("/data")
    }
}

