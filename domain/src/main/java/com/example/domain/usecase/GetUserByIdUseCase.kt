package com.example.domain.usecase

import com.example.domain.model.User
import com.example.domain.repository.UserRepository
import com.example.domain.util.Result
import com.example.domain.validation.ValidationUtils
import javax.inject.Inject

/**
 * Enhanced use case for fetching a user by ID with comprehensive validation
 * Adds business logic for ID validation and user data validation
 */
class GetUserByIdUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(id: Int): Result<User> {
        // Business rule: Validate user ID using centralized validation
        return ValidationUtils.validateUserId(id)
            .flatMap { validId ->
                // Fetch user from repository
                userRepository.getUserById(validId)
            }
            .flatMap { user ->
                // Business logic: Additional user validation after fetch
                validateUserData(user)
            }
    }
    
    /**
     * Business rule: Comprehensive user data validation
     * Uses centralized validation utilities for consistency
     */
    private fun validateUserData(user: User): Result<User> {
        return ValidationUtils.validateAll(
            { ValidationUtils.validateUserName(user.name) },
            { ValidationUtils.validateEmail(user.email) }
        ).map { user }
    }
} 