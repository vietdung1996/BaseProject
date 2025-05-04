package com.example.domain.usecase

import com.example.domain.model.User
import com.example.domain.repository.UserRepository
import com.example.domain.util.Resource
import javax.inject.Inject

/**
 * Use case for fetching a user by ID
 */
class GetUserByIdUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(id: Int): Resource<User> {
        return userRepository.getUserById(id)
    }
} 