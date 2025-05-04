package com.example.data.local.user

import com.example.data.model.user.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * Local data source for User data
 */
class UserLocalDataSource(private val userDao: UserDao) {
    /**
     * Get all users from local database
     */
    fun getUsers(): Flow<List<UserEntity>> {
        return userDao.getUsers()
    }

    /**
     * Get user by id from local database
     */
    fun getUserById(id: String): Flow<UserEntity?> {
        return userDao.getUserById(id)
    }

    /**
     * Save user to local database
     */
    suspend fun saveUser(user: UserEntity) {
        userDao.insertUser(user)
    }
} 