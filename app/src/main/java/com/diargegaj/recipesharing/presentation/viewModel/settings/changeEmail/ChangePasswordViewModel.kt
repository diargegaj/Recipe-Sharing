package com.diargegaj.recipesharing.presentation.viewModel.settings.changeEmail

import androidx.lifecycle.ViewModel
import com.diargegaj.recipesharing.domain.models.settings.changePassword.ChangePasswordState
import com.diargegaj.recipesharing.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    val userRepository: UserRepository
) : ViewModel() {

    private val _changePasswordState = MutableStateFlow(ChangePasswordState())
    val changePasswordState = _changePasswordState.asStateFlow()

    private val _userMessageEvent = MutableSharedFlow<String>(replay = 0)
    val userMessageEvent = _userMessageEvent.asSharedFlow()


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
}