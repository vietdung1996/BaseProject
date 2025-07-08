package com.example.domain.validation

import com.example.domain.util.DomainError
import com.example.domain.util.Result

/**
 * Validation utilities for domain layer business rules
 * Provides consistent validation logic across use cases
 */
object ValidationUtils {
    
    /**
     * Validates user ID for business rules
     * @param userId The user ID to validate
     * @return Result with validation error if invalid, success if valid
     */
    fun validateUserId(userId: Int): Result<Int> {
        return when {
            userId <= 0 -> Result.error(
                DomainError.ValidationError(
                    errorMessage = "User ID must be a positive number. Provided: $userId"
                )
            )
            userId > MAX_USER_ID -> Result.error(
                DomainError.ValidationError(
                    errorMessage = "User ID exceeds maximum allowed value. Maximum: $MAX_USER_ID, Provided: $userId"
                )
            )
            else -> Result.success(userId)
        }
    }
    
    /**
     * Validates email format according to business rules
     * @param email The email to validate
     * @return Result with validation error if invalid, success if valid
     */
    fun validateEmail(email: String): Result<String> {
        return when {
            email.isBlank() -> Result.error(
                DomainError.ValidationError(
                    errorMessage = "Email cannot be empty"
                )
            )
            !email.contains("@") -> Result.error(
                DomainError.ValidationError(
                    errorMessage = "Email must contain @ symbol"
                )
            )
            !email.contains(".") -> Result.error(
                DomainError.ValidationError(
                    errorMessage = "Email must contain a domain extension"
                )
            )
            email.length > MAX_EMAIL_LENGTH -> Result.error(
                DomainError.ValidationError(
                    errorMessage = "Email exceeds maximum length of $MAX_EMAIL_LENGTH characters"
                )
            )
            else -> Result.success(email)
        }
    }
    
    /**
     * Validates user name according to business rules
     * @param name The name to validate
     * @return Result with validation error if invalid, success if valid
     */
    fun validateUserName(name: String): Result<String> {
        return when {
            name.isBlank() -> Result.error(
                DomainError.ValidationError(
                    errorMessage = "User name cannot be empty"
                )
            )
            name.length < MIN_NAME_LENGTH -> Result.error(
                DomainError.ValidationError(
                    errorMessage = "User name must be at least $MIN_NAME_LENGTH characters long"
                )
            )
            name.length > MAX_NAME_LENGTH -> Result.error(
                DomainError.ValidationError(
                    errorMessage = "User name cannot exceed $MAX_NAME_LENGTH characters"
                )
            )
            !name.matches(NAME_PATTERN.toRegex()) -> Result.error(
                DomainError.ValidationError(
                    errorMessage = "User name contains invalid characters. Only letters, spaces, and common punctuation are allowed"
                )
            )
            else -> Result.success(name.trim())
        }
    }
    
    /**
     * Validates a list is not empty and within size limits
     * @param list The list to validate
     * @param itemName The name of the items for error messages
     * @return Result with validation error if invalid, success if valid
     */
    fun <T> validateListNotEmpty(list: List<T>, itemName: String = "items"): Result<List<T>> {
        return when {
            list.isEmpty() -> Result.error(
                DomainError.ValidationError(
                    errorMessage = "List of $itemName cannot be empty"
                )
            )
            list.size > MAX_LIST_SIZE -> Result.error(
                DomainError.ValidationError(
                    errorMessage = "List of $itemName exceeds maximum size of $MAX_LIST_SIZE"
                )
            )
            else -> Result.success(list)
        }
    }
    
    /**
     * Performs multiple validations and returns the first error found
     * @param validations List of validation functions to execute
     * @return Result with first validation error found, or success if all pass
     */
    fun validateAll(vararg validations: () -> Result<*>): Result<Unit> {
        for (validation in validations) {
            val result = validation()
            if (result.isError) {
                return Result.error(result.errorOrNull()!!)
            }
        }
        return Result.success(Unit)
    }
    
    // Business rule constants
    private const val MAX_USER_ID = 999999
    private const val MAX_EMAIL_LENGTH = 255
    private const val MIN_NAME_LENGTH = 2
    private const val MAX_NAME_LENGTH = 100
    private const val MAX_LIST_SIZE = 1000
    private const val NAME_PATTERN = "^[a-zA-Z\\s\\-'.]+$"
}