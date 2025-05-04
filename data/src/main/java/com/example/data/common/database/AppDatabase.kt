package com.example.data.common.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.local.user.UserDao
import com.example.data.model.user.UserEntity

/**
 * Room database for the app
 */
@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    /**
     * Get the User DAO
     */
    abstract fun userDao(): UserDao
} 