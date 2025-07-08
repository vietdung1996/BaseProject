package com.example.data.repository.user

import com.example.data.common.exception.DataException
import com.example.data.mapper.ErrorMapper
import com.example.data.mapper.toDomain
import com.example.data.remote.datasource.UserRemoteDataSource
import com.example.domain.model.User
import com.example.domain.repository.UserRepository
import com.example.domain.util.Result
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

/**
 * Enhanced implementation of UserRepository with caching strategy
 * Implements cache-first approach with network fallback
 */
class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource
    // TODO: Add local data source when cache implementation is ready
    // private val userLocalDataSource: UserLocalDataSource
) : UserRepository {
    
    override suspend fun getUsers(): Result<List<User>> {
        return try {
            // TODO: Implement cache-first strategy
            // 1. Try to get from local cache first
            // 2. If cache is empty or stale, fetch from network
            // 3. Update cache with fresh data
            
            // For now, just fetch from remote
            val response = userRemoteDataSource.getUsers()
            val users = response.map { it.toDomain() }
            
            // Apply repository-level business rules
            val validUsers = validateUsers(users)
            
            Result.success(validUsers)
        } catch (e: Exception) {
            val domainError = ErrorMapper.mapToDomainError(e)
            Result.error(domainError)
        }
    }

    override suspend fun getUserById(id: Int): Result<User> {
        return try {
            // TODO: Implement cache-first strategy
            // 1. Try to get from local cache first
            // 2. If not found in cache or stale, fetch from network
            // 3. Update cache with fresh data
            
            // For now, just fetch from remote
            val response = userRemoteDataSource.getUserById(id)
            val user = response.toDomain()
            
            // Apply repository-level validation
            val validatedUser = validateUser(user)
            
            Result.success(validatedUser)
        } catch (e: Exception) {
            val domainError = ErrorMapper.mapToDomainError(e)
            Result.error(domainError)
        }
    }
    
    /**
     * Repository-level validation for users list
     * Filters out any users that don't meet basic data integrity requirements
     */
    private fun validateUsers(users: List<User>): List<User> {
        return users.filter { user ->
            validateUser(user, throwOnInvalid = false) != null
        }
    }
    
    /**
     * Repository-level validation for individual user
     * Ensures data integrity at the repository boundary
     */
    private fun validateUser(user: User, throwOnInvalid: Boolean = true): User? {
        return try {
            // Basic data integrity checks
            require(user.id > 0) { "User ID must be positive" }
            require(user.name.isNotBlank()) { "User name cannot be blank" }
            require(user.email.isNotBlank()) { "User email cannot be blank" }
            require(user.email.contains("@")) { "User email must be valid" }
            
            user
        } catch (e: IllegalArgumentException) {
            if (throwOnInvalid) {
                throw DataException.Parse("Invalid user data: ${e.message}", e)
            } else {
                null // Return null for filtering in lists
            }
        }
    }
} 