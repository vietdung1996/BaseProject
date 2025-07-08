package com.example.domain.usecase

import com.example.domain.model.User
import com.example.domain.repository.UserRepository
import com.example.domain.util.Result
import com.example.domain.util.DomainError
import javax.inject.Inject

/**
 * Use case for fetching all users with business logic
 * This adds value beyond just calling the repository
 */
class GetUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<List<User>> {
        return userRepository.getUsers()
            .map { users ->
                // Business logic: Filter out users with invalid data
                validateAndFilterUsers(users)
            }
    }
    
    /**
     * Business rule: Validate user data and filter out invalid users
     */
    private fun validateAndFilterUsers(users: List<User>): List<User> {
        return users.filter { user ->
            isValidUser(user)
        }.sortedBy { it.name } // Business rule: Sort users by name
    }
    
    /**
     * Business rule: A user is valid if they have required fields
     */
    private fun isValidUser(user: User): Boolean {
        return user.name.isNotBlank() && 
               user.email.isNotBlank() && 
               isValidEmail(user.email)
    }
    
    /**
     * Business rule: Basic email validation
     */
    private fun isValidEmail(email: String): Boolean {
        return email.contains("@") && email.contains(".")
    }
} 