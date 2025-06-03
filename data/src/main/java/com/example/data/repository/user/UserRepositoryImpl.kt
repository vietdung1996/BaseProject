package com.example.data.repository.user

import com.example.data.mapper.toDomain
import com.example.data.remote.datasource.UserRemoteDataSource
import com.example.domain.model.User
import com.example.domain.repository.UserRepository
import com.example.domain.util.Resource
import javax.inject.Inject

/**
 * Implementation of the UserRepository interface
 */
class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource
) : UserRepository {
    
    override suspend fun getUsers(): Resource<List<User>> {
        return try {
            val response = userRemoteDataSource.getUsers()
            Resource.Success(response.map { it.toDomain() })
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unexpected error occurred")
        }
    }

    override suspend fun getUserById(id: Int): Resource<User> {
        return try {
            val response = userRemoteDataSource.getUserById(id)
            Resource.Success(response.toDomain())
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unexpected error occurred")
        }
    }
} 