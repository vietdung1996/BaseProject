package com.example.data.model.user

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Database entity for User
 */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val email: String,
    val avatarUrl: String?
) 