package com.example.data.remote.datasource

import com.example.data.remote.api.UserApiService
import com.example.data.remote.model.UserDto
import javax.inject.Inject

/**
 * Implementation of UserRemoteDataSource that uses the Retrofit API service
 */
class UserRemoteDataSourceImpl @Inject constructor(
    private val userApiService: UserApiService
) : UserRemoteDataSource {
    
    override suspend fun getUsers(): List<UserDto> {
        return userApiService.getUsers()
    }

    override suspend fun getUserById(id: Int): UserDto {
        return userApiService.getUserById(id)
    }
} 