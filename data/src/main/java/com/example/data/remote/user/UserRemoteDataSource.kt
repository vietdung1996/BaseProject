package com.example.data.remote.user

import com.example.data.model.user.UserDto

/**
 * Remote data source for User data
 */
class UserRemoteDataSource(private val api: UserApi) {
    /**
     * Get all users from API
     */
    suspend fun getUsers(): List<UserDto> {
        return api.getUsers()
    }

    /**
     * Get user by id from API
     */
    suspend fun getUserById(id: String): UserDto {
        return api.getUserById(id)
    }
} 