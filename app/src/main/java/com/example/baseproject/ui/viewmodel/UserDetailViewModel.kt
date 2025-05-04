package com.example.baseproject.ui.viewmodel

import com.example.core.base.BaseViewModel
import com.example.domain.model.User
import com.example.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

/**
 * ViewModel for User detail screen
 */
@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {

    private val _userState = MutableStateFlow<UserDetailState>(UserDetailState.Loading)
    val userState: StateFlow<UserDetailState> = _userState

    /**
     * Get user by id from repository
     */
    fun getUserById(userId: String) {
        executeTask {
            userRepository.getUserById(userId)
                .collect { user ->
                    if (user != null) {
                        _userState.value = UserDetailState.Success(user)
                    } else {
                        _userState.value = UserDetailState.Error("User not found")
                    }
                }
        }
    }
}

/**
 * State representation for User Detail UI
 */
sealed class UserDetailState {
    object Loading : UserDetailState()
    data class Success(val user: User) : UserDetailState()
    data class Error(val message: String) : UserDetailState()
}
