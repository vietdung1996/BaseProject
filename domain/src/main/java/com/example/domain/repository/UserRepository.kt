package com.example.domain.repository

import com.example.domain.model.User
import com.example.domain.util.Resource

/**
 * Repository interface for user-related operations
 */
interface UserRepository {
    /**
     * Gets all users
     * @return Resource wrapping a list of users
     */
    suspend fun getUsers(): Resource<List<User>>

    /**
     * Gets a user by ID
     * @param id User ID
     * @return Resource wrapping a user
     */
    suspend fun getUserById(id: Int): Resource<User>
} 