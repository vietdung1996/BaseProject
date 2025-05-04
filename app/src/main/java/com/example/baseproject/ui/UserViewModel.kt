package com.example.baseproject.ui

import com.example.core.base.BaseViewModel
import com.example.domain.model.User
import com.example.domain.usecase.GetUsersUseCase
import com.example.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

/**
 * ViewModel for User management
 */
@HiltViewModel
class UserViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase
) : BaseViewModel() {

    private val _usersState = MutableStateFlow<UsersState>(UsersState.Loading)
    val usersState: StateFlow<UsersState> = _usersState

    init {
        loadUsers()
    }

    /**
     * Load users from repository
     */
    fun loadUsers() {
        executeTask {
            _usersState.value = UsersState.Loading
            when (val result = getUsersUseCase()) {
                is Resource.Success -> {
                    if (result.data.isEmpty()) {
                        _usersState.value = UsersState.Empty
                    } else {
                        _usersState.value = UsersState.Success(result.data)
                    }
                }
                is Resource.Error -> {
                    _usersState.value = UsersState.Error(result.message)
                }
                is Resource.Loading -> {
                    _usersState.value = UsersState.Loading
                }
            }
        }
    }
}

/**
 * State representation for Users UI
 */
sealed class UsersState {
    object Loading : UsersState()
    object Empty : UsersState()
    data class Success(val users: List<User>) : UsersState()
    data class Error(val message: String) : UsersState()
} 