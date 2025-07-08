package com.example.data.mapper

import com.example.data.common.exception.DataException
import com.example.domain.util.DomainError
import org.junit.Assert.*
import org.junit.Test
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Unit tests for ErrorMapper
 * Tests conversion from data layer exceptions to domain errors
 */
class ErrorMapperTest {

    @Test
    fun `mapToDomainError should convert DataException Network to DomainError NetworkError`() {
        // Given
        val dataException = DataException.Network("Connection failed", RuntimeException("Root cause"))

        // When
        val domainError = ErrorMapper.mapToDomainError(dataException)

        // Then
        assertTrue("Should be NetworkError", domainError is DomainError.NetworkError)
        assertEquals("Message should match", "Connection failed", domainError.message)
        assertEquals("Cause should match", dataException.cause, domainError.cause)
    }

    @Test
    fun `mapToDomainError should convert DataException Timeout to DomainError NetworkError`() {
        // Given
        val dataException = DataException.Timeout("Request timeout")

        // When
        val domainError = ErrorMapper.mapToDomainError(dataException)

        // Then
        assertTrue("Should be NetworkError", domainError is DomainError.NetworkError)
        assertTrue("Message should contain timeout info", domainError.message.contains("timeout"))
    }

    @Test
    fun `mapToDomainError should convert DataException Unauthorized to DomainError AuthenticationError`() {
        // Given
        val dataException = DataException.Unauthorized("Invalid credentials")

        // When
        val domainError = ErrorMapper.mapToDomainError(dataException)

        // Then
        assertTrue("Should be AuthenticationError", domainError is DomainError.AuthenticationError)
        assertEquals("Message should match", "Invalid credentials", domainError.message)
    }

    @Test
    fun `mapToDomainError should convert DataException Forbidden to DomainError AuthenticationError`() {
        // Given
        val dataException = DataException.Forbidden("Access denied")

        // When
        val domainError = ErrorMapper.mapToDomainError(dataException)

        // Then
        assertTrue("Should be AuthenticationError", domainError is DomainError.AuthenticationError)
        assertTrue("Message should contain access info", domainError.message.contains("forbidden"))
    }

    @Test
    fun `mapToDomainError should convert DataException NotFound to DomainError NotFoundError`() {
        // Given
        val dataException = DataException.NotFound("Resource not found")

        // When
        val domainError = ErrorMapper.mapToDomainError(dataException)

        // Then
        assertTrue("Should be NotFoundError", domainError is DomainError.NotFoundError)
        assertEquals("Message should match", "Resource not found", domainError.message)
    }

    @Test
    fun `mapToDomainError should convert DataException Server to DomainError ServerError`() {
        // Given
        val dataException = DataException.Server("Internal server error")

        // When
        val domainError = ErrorMapper.mapToDomainError(dataException)

        // Then
        assertTrue("Should be ServerError", domainError is DomainError.ServerError)
        assertEquals("Message should match", "Internal server error", domainError.message)
    }

    @Test
    fun `mapToDomainError should convert DataException Database to DomainError LocalStorageError`() {
        // Given
        val dataException = DataException.Database("Database connection failed")

        // When
        val domainError = ErrorMapper.mapToDomainError(dataException)

        // Then
        assertTrue("Should be LocalStorageError", domainError is DomainError.LocalStorageError)
        assertEquals("Message should match", "Database connection failed", domainError.message)
    }

    @Test
    fun `mapToDomainError should convert DataException Parse to DomainError UnknownError`() {
        // Given
        val dataException = DataException.Parse("JSON parsing failed")

        // When
        val domainError = ErrorMapper.mapToDomainError(dataException)

        // Then
        assertTrue("Should be UnknownError", domainError is DomainError.UnknownError)
        assertTrue("Message should contain parsing info", domainError.message.contains("parsing"))
    }

    @Test
    fun `mapToDomainError should convert DataException Unknown to DomainError UnknownError`() {
        // Given
        val dataException = DataException.Unknown("Unexpected error")

        // When
        val domainError = ErrorMapper.mapToDomainError(dataException)

        // Then
        assertTrue("Should be UnknownError", domainError is DomainError.UnknownError)
        assertEquals("Message should match", "Unexpected error", domainError.message)
    }

    @Test
    fun `mapToDomainError should convert UnknownHostException to DomainError NetworkError`() {
        // Given
        val exception = UnknownHostException("Host not found")

        // When
        val domainError = ErrorMapper.mapToDomainError(exception)

        // Then
        assertTrue("Should be NetworkError", domainError is DomainError.NetworkError)
        assertEquals("Message should match", "Host not found", domainError.message)
        assertEquals("Cause should match", exception, domainError.cause)
    }

    @Test
    fun `mapToDomainError should convert ConnectException to DomainError NetworkError`() {
        // Given
        val exception = ConnectException("Connection refused")

        // When
        val domainError = ErrorMapper.mapToDomainError(exception)

        // Then
        assertTrue("Should be NetworkError", domainError is DomainError.NetworkError)
        assertEquals("Message should match", "Connection refused", domainError.message)
    }

    @Test
    fun `mapToDomainError should convert SocketTimeoutException to DomainError NetworkError`() {
        // Given
        val exception = SocketTimeoutException("Socket timeout")

        // When
        val domainError = ErrorMapper.mapToDomainError(exception)

        // Then
        assertTrue("Should be NetworkError", domainError is DomainError.NetworkError)
        assertEquals("Message should match", "Socket timeout", domainError.message)
    }

    @Test
    fun `mapToDomainError should convert SecurityException to DomainError AuthenticationError`() {
        // Given
        val exception = SecurityException("Security violation")

        // When
        val domainError = ErrorMapper.mapToDomainError(exception)

        // Then
        assertTrue("Should be AuthenticationError", domainError is DomainError.AuthenticationError)
        assertEquals("Message should match", "Security violation", domainError.message)
    }

    @Test
    fun `mapToDomainError should convert unknown exception to DomainError UnknownError`() {
        // Given
        val exception = RuntimeException("Some runtime error")

        // When
        val domainError = ErrorMapper.mapToDomainError(exception)

        // Then
        assertTrue("Should be UnknownError", domainError is DomainError.UnknownError)
        assertEquals("Message should match", "Some runtime error", domainError.message)
        assertEquals("Cause should match", exception, domainError.cause)
    }

    @Test
    fun `mapToDomainError should handle exception with null message`() {
        // Given
        val exception = RuntimeException(null as String?)

        // When
        val domainError = ErrorMapper.mapToDomainError(exception)

        // Then
        assertTrue("Should be UnknownError", domainError is DomainError.UnknownError)
        assertEquals("Message should be default", "An unexpected error occurred", domainError.message)
    }
}