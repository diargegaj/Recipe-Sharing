package com.diargegaj.recipesharing.presentation.viewModel.settings.changePassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diargegaj.recipesharing.domain.models.settings.changePassword.ChangePasswordState
import com.diargegaj.recipesharing.domain.repository.UserRepository
import com.diargegaj.recipesharing.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    val userRepository: UserRepository
) : ViewModel() {

    private val _changePasswordState = MutableStateFlow(ChangePasswordState())
    val changePasswordState = _changePasswordState.asStateFlow()

    private val _userMessageEvent = MutableSharedFlow<String>(replay = 0)
    val userMessageEvent = _userMessageEvent.asSharedFlow()

    fun changePassword() {
        val currentUser = userRepository.getCurrentUser()
        val email = currentUser?.email ?: ""
        val password = _changePasswordState.value.oldPassword

        viewModelScope.launch {
            when (userRepository.reAuthenticateUser(email, password)) {
                is Resource.Success -> {
                    changeUserPassword()
                }

                is Resource.Error -> {
                    _userMessageEvent.emit(
                        "Old password is wrong."
                    )
                }

                else -> Unit
            }
        }
    }

    private suspend fun changeUserPassword() {
        if (!passwordsMatch()) {
            _userMessageEvent.emit(
                "Password does not match"
            )
            return
        }

        val newPassword = _changePasswordState.value.newPassword
        when (userRepository.changeUserPassword(newPassword)) {
            is Resource.Success -> {
                _userMessageEvent.emit(
                    "Password changed successfully"
                )
                resetToDefault()
            }

            is Resource.Error -> {
                _userMessageEvent.emit(
                    "Error while changing password."
                )
            }

            else -> Unit
        }
    }

    private fun resetToDefault() {
        _changePasswordState.value = _changePasswordState.value.copy(
            oldPassword = "",
            newPassword = "",
            confirmPassword = ""
        )
    }

    fun oldPasswordUpdated(password: String) {
        _changePasswordState.value = _changePasswordState.value.copy(
            oldPassword = password
        )
    }

    fun newPasswordUpdated(password: String) {
        _changePasswordState.value = _changePasswordState.value.copy(
            newPassword = password
        )
    }

    fun confirmPasswordUpdated(password: String) {
        _changePasswordState.value = _changePasswordState.value.copy(
            confirmPassword = password
        )
    }

    private fun passwordsMatch(): Boolean {
        return _changePasswordState.value.newPassword == _changePasswordState.value.confirmPassword
    }
}