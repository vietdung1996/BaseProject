package com.example.data.local.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.user.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * Room DAO for User entities
 */
@Dao
interface UserDao {
    /**
     * Get all users
     */
    @Query("SELECT * FROM users")
    fun getUsers(): Flow<List<UserEntity>>

    /**
     * Get user by id
     */
    @Query("SELECT * FROM users WHERE id = :id")
    fun getUserById(id: String): Flow<UserEntity?>

    /**
     * Insert or replace user
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)
} 