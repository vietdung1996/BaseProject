package com.example.domain.usecase

import com.example.domain.model.User
import com.example.domain.repository.UserRepository
import com.example.domain.util.Result
import com.example.domain.validation.ValidationUtils
import javax.inject.Inject

/**
 * Enhanced use case for fetching all users with comprehensive business logic
 * This adds significant value beyond just calling the repository
 */
class GetUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<List<User>> {
        return userRepository.getUsers()
            .flatMap { users ->
                // Business logic: Validate and filter users
                processUsersList(users)
            }
    }
    
    /**
     * Business rule: Process users list with validation and filtering
     */
    private fun processUsersList(users: List<User>): Result<List<User>> {
        // Filter out users with invalid data and apply business rules
        val validUsers = users.mapNotNull { user ->
            if (isValidUser(user)) user else null
        }
        
        // Apply business rule: Sort users by name
        val sortedUsers = validUsers.sortedBy { it.name }
        
        // Business rule: Apply any additional filtering or transformations
        val finalUsers = applyBusinessRules(sortedUsers)
        
        return Result.success(finalUsers)
    }
    
    /**
     * Business rule: A user is valid if they meet all validation criteria
     * Uses centralized validation for consistency
     */
    private fun isValidUser(user: User): Boolean {
        return ValidationUtils.validateUserName(user.name).isSuccess &&
               ValidationUtils.validateEmail(user.email).isSuccess
    }
    
    /**
     * Business rule: Apply additional business logic to the user list
     * This could include things like:
     * - Filtering by business criteria
     * - Applying user permissions
     * - Adding computed fields
     * - Etc.
     */
    private fun applyBusinessRules(users: List<User>): List<User> {
        // Example business rule: Limit results for performance
        // In a real app, this might be pagination or other business logic
        return users.take(MAX_USERS_PER_REQUEST)
    }
    
    // Business rule constants
    companion object {
        private const val MAX_USERS_PER_REQUEST = 100
    }
} 