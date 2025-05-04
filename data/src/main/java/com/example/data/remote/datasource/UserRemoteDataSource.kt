package com.example.data.remote.datasource

import com.example.data.remote.model.UserDto

/**
 * Remote data source interface for user-related API operations
 */
interface UserRemoteDataSource {
    /**
     * Gets all users from the remote API
     * @return List of UserDto objects
     */
    suspend fun getUsers(): List<UserDto>

    /**
     * Gets a user by ID from the remote API
     * @param id User ID
     * @return UserDto object
     */
    suspend fun getUserById(id: Int): UserDto
} 