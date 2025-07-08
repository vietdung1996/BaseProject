package com.example.data.repository.user

import com.example.data.mapper.ErrorMapper
import com.example.data.mapper.toDomain
import com.example.data.remote.datasource.UserRemoteDataSource
import com.example.domain.model.User
import com.example.domain.repository.UserRepository
import com.example.domain.util.Result
import javax.inject.Inject

/**
 * Implementation of the UserRepository interface
 * Updated to use proper error mapping and Result type
 */
class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource
) : UserRepository {
    
    override suspend fun getUsers(): Result<List<User>> {
        return try {
            val response = userRemoteDataSource.getUsers()
            val users = response.map { it.toDomain() }
            Result.success(users)
        } catch (e: Exception) {
            val domainError = ErrorMapper.mapToDomainError(e)
            Result.error(domainError)
        }
    }

    override suspend fun getUserById(id: Int): Result<User> {
        return try {
            val response = userRemoteDataSource.getUserById(id)
            val user = response.toDomain()
            Result.success(user)
        } catch (e: Exception) {
            val domainError = ErrorMapper.mapToDomainError(e)
            Result.error(domainError)
        }
    }
} 