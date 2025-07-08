package com.example.domain.usecase

import com.example.domain.model.User
import com.example.domain.repository.UserRepository
import com.example.domain.util.Result
import com.example.domain.util.DomainError
import javax.inject.Inject

/**
 * Use case for fetching a user by ID with validation
 * Adds business logic for ID validation
 */
class GetUserByIdUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(id: Int): Result<User> {
        // Business rule: Validate user ID
        if (!isValidUserId(id)) {
            return Result.error(
                DomainError.ValidationError(
                    errorMessage = "Invalid user ID: $id. User ID must be positive."
                )
            )
        }
        
        return userRepository.getUserById(id)
            .map { user ->
                // Business logic: Additional user validation after fetch
                validateUserData(user)
            }
    }
    
    /**
     * Business rule: User ID must be positive
     */
    private fun isValidUserId(id: Int): Boolean {
        return id > 0
    }
    
    /**
     * Business rule: Validate user data completeness
     */
    private fun validateUserData(user: User): User {
        // Could add additional business validation here
        // For now, just ensure the user has required fields
        require(user.name.isNotBlank()) { "User name cannot be blank" }
        require(user.email.isNotBlank()) { "User email cannot be blank" }
        
        return user
    }
} 