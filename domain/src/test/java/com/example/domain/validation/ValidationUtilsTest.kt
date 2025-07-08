package com.example.domain.validation

import com.example.domain.util.DomainError
import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for ValidationUtils
 * Tests business rule validation logic
 */
class ValidationUtilsTest {

    @Test
    fun `validateUserId should return success for valid positive ID`() {
        // Given
        val validId = 123

        // When
        val result = ValidationUtils.validateUserId(validId)

        // Then
        assertTrue("Result should be successful", result.isSuccess)
        assertEquals("Should return the same ID", validId, result.getOrNull())
    }

    @Test
    fun `validateUserId should return error for zero ID`() {
        // Given
        val invalidId = 0

        // When
        val result = ValidationUtils.validateUserId(invalidId)

        // Then
        assertTrue("Result should be error", result.isError)
        val error = result.errorOrNull()
        assertTrue("Error should be ValidationError", error is DomainError.ValidationError)
        assertTrue("Error message should mention positive number", 
            error!!.message.contains("positive"))
    }

    @Test
    fun `validateUserId should return error for negative ID`() {
        // Given
        val invalidId = -5

        // When
        val result = ValidationUtils.validateUserId(invalidId)

        // Then
        assertTrue("Result should be error", result.isError)
        val error = result.errorOrNull()
        assertTrue("Error should be ValidationError", error is DomainError.ValidationError)
        assertTrue("Error message should mention positive number", 
            error!!.message.contains("positive"))
    }

    @Test
    fun `validateUserId should return error for ID exceeding maximum`() {
        // Given
        val invalidId = 1000000 // Exceeds MAX_USER_ID

        // When
        val result = ValidationUtils.validateUserId(invalidId)

        // Then
        assertTrue("Result should be error", result.isError)
        val error = result.errorOrNull()
        assertTrue("Error should be ValidationError", error is DomainError.ValidationError)
        assertTrue("Error message should mention maximum", 
            error!!.message.contains("maximum"))
    }

    @Test
    fun `validateEmail should return success for valid email`() {
        // Given
        val validEmail = "test@example.com"

        // When
        val result = ValidationUtils.validateEmail(validEmail)

        // Then
        assertTrue("Result should be successful", result.isSuccess)
        assertEquals("Should return the same email", validEmail, result.getOrNull())
    }

    @Test
    fun `validateEmail should return error for blank email`() {
        // Given
        val invalidEmail = ""

        // When
        val result = ValidationUtils.validateEmail(invalidEmail)

        // Then
        assertTrue("Result should be error", result.isError)
        val error = result.errorOrNull()
        assertTrue("Error should be ValidationError", error is DomainError.ValidationError)
        assertTrue("Error message should mention empty", 
            error!!.message.contains("empty"))
    }

    @Test
    fun `validateEmail should return error for email without at symbol`() {
        // Given
        val invalidEmail = "testexample.com"

        // When
        val result = ValidationUtils.validateEmail(invalidEmail)

        // Then
        assertTrue("Result should be error", result.isError)
        val error = result.errorOrNull()
        assertTrue("Error should be ValidationError", error is DomainError.ValidationError)
        assertTrue("Error message should mention @ symbol", 
            error!!.message.contains("@"))
    }

    @Test
    fun `validateEmail should return error for email without domain extension`() {
        // Given
        val invalidEmail = "test@example"

        // When
        val result = ValidationUtils.validateEmail(invalidEmail)

        // Then
        assertTrue("Result should be error", result.isError)
        val error = result.errorOrNull()
        assertTrue("Error should be ValidationError", error is DomainError.ValidationError)
        assertTrue("Error message should mention domain extension", 
            error!!.message.contains("domain extension"))
    }

    @Test
    fun `validateUserName should return success for valid name`() {
        // Given
        val validName = "John Doe"

        // When
        val result = ValidationUtils.validateUserName(validName)

        // Then
        assertTrue("Result should be successful", result.isSuccess)
        assertEquals("Should return the same name", validName, result.getOrNull())
    }

    @Test
    fun `validateUserName should return trimmed name for name with spaces`() {
        // Given
        val nameWithSpaces = "  John Doe  "

        // When
        val result = ValidationUtils.validateUserName(nameWithSpaces)

        // Then
        assertTrue("Result should be successful", result.isSuccess)
        assertEquals("Should return trimmed name", "John Doe", result.getOrNull())
    }

    @Test
    fun `validateUserName should return error for blank name`() {
        // Given
        val invalidName = ""

        // When
        val result = ValidationUtils.validateUserName(invalidName)

        // Then
        assertTrue("Result should be error", result.isError)
        val error = result.errorOrNull()
        assertTrue("Error should be ValidationError", error is DomainError.ValidationError)
        assertTrue("Error message should mention empty", 
            error!!.message.contains("empty"))
    }

    @Test
    fun `validateUserName should return error for name too short`() {
        // Given
        val shortName = "A"

        // When
        val result = ValidationUtils.validateUserName(shortName)

        // Then
        assertTrue("Result should be error", result.isError)
        val error = result.errorOrNull()
        assertTrue("Error should be ValidationError", error is DomainError.ValidationError)
        assertTrue("Error message should mention minimum length", 
            error!!.message.contains("at least"))
    }

    @Test
    fun `validateUserName should return error for name with invalid characters`() {
        // Given
        val invalidName = "John@Doe"

        // When
        val result = ValidationUtils.validateUserName(invalidName)

        // Then
        assertTrue("Result should be error", result.isError)
        val error = result.errorOrNull()
        assertTrue("Error should be ValidationError", error is DomainError.ValidationError)
        assertTrue("Error message should mention invalid characters", 
            error!!.message.contains("invalid characters"))
    }

    @Test
    fun `validateListNotEmpty should return success for non-empty list`() {
        // Given
        val validList = listOf("item1", "item2")

        // When
        val result = ValidationUtils.validateListNotEmpty(validList, "test items")

        // Then
        assertTrue("Result should be successful", result.isSuccess)
        assertEquals("Should return the same list", validList, result.getOrNull())
    }

    @Test
    fun `validateListNotEmpty should return error for empty list`() {
        // Given
        val emptyList = emptyList<String>()

        // When
        val result = ValidationUtils.validateListNotEmpty(emptyList, "test items")

        // Then
        assertTrue("Result should be error", result.isError)
        val error = result.errorOrNull()
        assertTrue("Error should be ValidationError", error is DomainError.ValidationError)
        assertTrue("Error message should mention empty", 
            error!!.message.contains("cannot be empty"))
    }

    @Test
    fun `validateAll should return success when all validations pass`() {
        // Given
        val validation1 = { ValidationUtils.validateUserId(1) }
        val validation2 = { ValidationUtils.validateEmail("test@example.com") }

        // When
        val result = ValidationUtils.validateAll(validation1, validation2)

        // Then
        assertTrue("Result should be successful", result.isSuccess)
    }

    @Test
    fun `validateAll should return first error when validation fails`() {
        // Given
        val validation1 = { ValidationUtils.validateUserId(-1) } // Will fail
        val validation2 = { ValidationUtils.validateEmail("test@example.com") }

        // When
        val result = ValidationUtils.validateAll(validation1, validation2)

        // Then
        assertTrue("Result should be error", result.isError)
        val error = result.errorOrNull()
        assertTrue("Error should be ValidationError", error is DomainError.ValidationError)
        assertTrue("Error should be from first validation", 
            error!!.message.contains("positive"))
    }
}