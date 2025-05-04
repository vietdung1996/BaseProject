package com.example.data.remote.api

import com.example.data.remote.model.UserDto
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Retrofit service interface for user-related API endpoints
 */
interface UserApiService {
    /**
     * Fetches all users
     * @return List of users
     */
    @GET("users")
    suspend fun getUsers(): List<UserDto>

    /**
     * Fetches a user by ID
     * @param id User ID
     * @return User details
     */
    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Int): UserDto
} 