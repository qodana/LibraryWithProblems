package org.example

/**
 * Interface for data processors that handle various data operations asynchronously.
 */
interface DataProcessor {
    /**
     * Validates the input data according to specific rules.
     * @param data The data to validate
     * @return Validation result message
     */
    suspend fun validateData(data: String): String

    /**
     * Processes the input data and returns the result.
     * @param data The data to process
     * @return Processed data
     */
    suspend fun processData(data: String): String

    /**
     * Retrieves the processor type.
     * @return The type of this processor
     */
    fun getProcessorType(): String
}

