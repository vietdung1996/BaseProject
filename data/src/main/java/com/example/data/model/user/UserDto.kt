package com.example.data.model.user

/**
 * Data Transfer Object for User API response
 */
data class UserDto(
    val id: String,
    val name: String,
    val email: String,
    val avatar: String?
) 