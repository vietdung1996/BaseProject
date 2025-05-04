package com.example.data.remote.user

import com.example.data.model.user.UserDto
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * API interface for User endpoints
 */
interface UserApi {
    /**
     * Get all users
     */
    @GET("users")
    suspend fun getUsers(): List<UserDto>

    /**
     * Get user by id
     */
    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: String): UserDto
} 