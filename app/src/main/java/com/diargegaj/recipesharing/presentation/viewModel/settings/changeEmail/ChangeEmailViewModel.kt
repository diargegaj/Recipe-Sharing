package com.diargegaj.recipesharing.presentation.viewModel.settings.changeEmail

import androidx.lifecycle.ViewModel
import com.diargegaj.recipesharing.domain.models.settings.changeEmail.ChangeEmailState
import com.diargegaj.recipesharing.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ChangeEmailViewModel @Inject constructor(
    val userRepository: UserRepository
) : ViewModel() {

    private val _changeEmailState = MutableStateFlow(ChangeEmailState())
    val changeEmailState = _changeEmailState.asStateFlow()

    fun confirmOldEmail() {

    }

    fun onEmailChange() {
        _changeEmailState.value = _changeEmailState.value.copy(
            showAuthDialog = true
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
}