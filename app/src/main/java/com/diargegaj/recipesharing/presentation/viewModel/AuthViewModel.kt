package com.diargegaj.recipesharing.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diargegaj.recipesharing.domain.models.UserModel
import com.diargegaj.recipesharing.domain.repository.UserRepository
import com.diargegaj.recipesharing.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _navigationEvent = MutableSharedFlow<NavigationTarget>()
    val navigationEvent: SharedFlow<NavigationTarget> get() = _navigationEvent

    fun checkUserLoggedIn() {
        viewModelScope.launch {
            val isLoggedIn = userRepository.isUserLoggedIn().first()
            _navigationEvent.emit(
                if (isLoggedIn) {
                    NavigationTarget.Home
                } else {
                    NavigationTarget.Login
                }
            )
        }
    }

    fun onRegister(userInfo: UserModel, password: String) {
        viewModelScope.launch {
            when (val registrationResult =
                userRepository.registerUser(email = userInfo.email, password = password)) {
                is Resource.Success -> {
                    userInfo.userUUID = registrationResult.data.uid
                    addUserInformationToServer(userInfo)
                }

                is Resource.Error -> {

                }

                is Resource.Loading -> {

                }
            }
        }
    }

    private fun addUserInformationToServer(userInfo: UserModel) {
        viewModelScope.launch {
            when (userRepository.addUserAdditionalInformation(userInfo)) {
                is Resource.Success -> {

                }

                is Resource.Error -> {

                }

                is Resource.Loading -> {

                }
            }
        }
    }

}

sealed class NavigationTarget {
    object Home : NavigationTarget()
    object Login : NavigationTarget()
}