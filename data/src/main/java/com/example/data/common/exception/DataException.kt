package com.example.data.common.exception

/**
 * Standardized exception class for data layer errors
 */
sealed class DataException(message: String, cause: Throwable? = null) : Exception(message, cause) {
    
    /**
     * General network connectivity issues
     */
    class Network(message: String, cause: Throwable? = null) : DataException(message, cause)
    
    /**
     * Connection timeout errors
     */
    class Timeout(message: String, cause: Throwable? = null) : DataException(message, cause)
    
    /**
     * Authentication errors (HTTP 401)
     */
    class Unauthorized(message: String, cause: Throwable? = null) : DataException(message, cause)
    
    /**
     * Permission errors (HTTP 403)
     */
    class Forbidden(message: String, cause: Throwable? = null) : DataException(message, cause)
    
    /**
     * Resource not found errors (HTTP 404)
     */
    class NotFound(message: String, cause: Throwable? = null) : DataException(message, cause)
    
    /**
     * Server errors (HTTP 5xx)
     */
    class Server(message: String, cause: Throwable? = null) : DataException(message, cause)
    
    /**
     * Local database errors
     */
    class Database(message: String, cause: Throwable? = null) : DataException(message, cause)
    
    /**
     * Data parsing or transformation errors
     */
    class Parse(message: String, cause: Throwable? = null) : DataException(message, cause)
    
    /**
     * Unknown or unclassified errors
     */
    class Unknown(message: String, cause: Throwable? = null) : DataException(message, cause)
} 