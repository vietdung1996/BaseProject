package com.example.domain.repository

import com.example.domain.model.User
import com.example.domain.util.Result

/**
 * Repository interface for user-related operations
 * Updated to use Result type for better error handling
 */
interface UserRepository {
    /**
     * Gets all users
     * @return Result containing a list of users or domain error
     */
    suspend fun getUsers(): Result<List<User>>

    /**
     * Gets a user by ID
     * @param id User ID
     * @return Result containing a user or domain error
     */
    suspend fun getUserById(id: Int): Result<User>
} 