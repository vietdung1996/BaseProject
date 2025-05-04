package com.example.domain.usecase

import com.example.domain.model.User
import com.example.domain.repository.UserRepository
import com.example.domain.util.Resource
import javax.inject.Inject

/**
 * Use case for fetching all users
 */
class GetUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Resource<List<User>> {
        return userRepository.getUsers()
    }
} 