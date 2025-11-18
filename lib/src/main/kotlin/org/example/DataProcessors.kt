package org.example

/**
 * Factory object for creating DataProcessor instances.
 * Provides factory methods to create specific processor implementations.
 */
object DataProcessors {
    /**
     * Creates a text processor for handling text-based data operations.
     * @return A DataProcessor instance configured for text processing
     */
    fun createTextProcessor(): DataProcessor = TextProcessor()

    /**
     * Creates a file processor for handling file-based data operations.
     * @return A DataProcessor instance configured for file processing
     */
    fun createFileProcessor(): DataProcessor = FileProcessor()
}

