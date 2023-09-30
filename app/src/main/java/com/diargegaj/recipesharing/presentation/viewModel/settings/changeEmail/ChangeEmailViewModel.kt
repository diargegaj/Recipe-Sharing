package com.diargegaj.recipesharing.presentation.viewModel.settings.changeEmail

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diargegaj.recipesharing.domain.models.settings.changeEmail.ChangeEmailState
import com.diargegaj.recipesharing.domain.repository.UserRepository
import com.diargegaj.recipesharing.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangeEmailViewModel @Inject constructor(
    val userRepository: UserRepository
) : ViewModel() {

    private val _changeEmailState = MutableStateFlow(ChangeEmailState())
    val changeEmailState = _changeEmailState.asStateFlow()

    fun confirmOldEmail() {
        val email = _changeEmailState.value.oldEmail
        val password = _changeEmailState.value.password

        viewModelScope.launch {
            when (userRepository.reAuthenticateUser(email = email, password = password)) {
                is Resource.Success -> {
                    val newEmail = _changeEmailState.value.newEmail
                    changeUserEmail(newEmail)
                }

                is Resource.Error -> {
                    _changeEmailState.value = _changeEmailState.value.copy(
                        processState = Resource.Error(Exception("Email or password is wrong."))
                    )
                }

                else -> Unit
            }
        }
    }

    private suspend fun changeUserEmail(email: String) {
        val result = userRepository.changeUserEmail(email)
    }

    fun onEmailChange() {
        val newEmail = _changeEmailState.value.newEmail
        val isValidEmail = isValidEmail(newEmail)

        _changeEmailState.value = _changeEmailState.value.copy(
            showAuthDialog = isValidEmail,
            processState = if (isValidEmail) Resource.Success(Unit) else Resource.Error(Exception("Invalid email."))
        )
    }

    fun oldEmailUpdated(email: String) {
        _changeEmailState.value = _changeEmailState.value.copy(
            oldEmail = email
        )
    }

    fun passwordUpdated(password: String) {
        _changeEmailState.value = _changeEmailState.value.copy(
            password = password
        )
    }

    fun onDismissDialog() {
        _changeEmailState.value = _changeEmailState.value.copy(
            showAuthDialog = false
        )
    }

    fun newEmailUpdated(email: String) {
        _changeEmailState.value = _changeEmailState.value.copy(
            newEmail = email
        )
    }

    fun isValidEmail(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()

}