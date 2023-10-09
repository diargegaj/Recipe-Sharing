package com.diargegaj.recipesharing.presentation.viewModel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diargegaj.recipesharing.domain.models.UserModel
import com.diargegaj.recipesharing.domain.repository.userAuth.UserAuthRepository
import com.diargegaj.recipesharing.domain.repository.userProfile.UserProfileRepository
import com.diargegaj.recipesharing.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: UserAuthRepository,
    private val userProfileRepository: UserProfileRepository
) : ViewModel() {

    private val _navigationEvent = MutableSharedFlow<NavigationTarget>()
    val navigationEvent: SharedFlow<NavigationTarget> get() = _navigationEvent

    private val _errorMessages = MutableSharedFlow<String>()
    val errorMessages: SharedFlow<String> get() = _errorMessages

    fun checkUserLoggedIn() {
        viewModelScope.launch {
            val isLoggedIn = authRepository.isUserLoggedIn().first()
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
                authRepository.registerUser(email = userInfo.email, password = password)) {
                is Resource.Success -> {
                    userInfo.userUUID = registrationResult.data.uid
                    addUserInformationToServer(userInfo)
                }

                is Resource.Error -> {
                    _errorMessages.emit(
                        registrationResult.exception.message ?: "AN ERROR"
                    )
                }

                is Resource.Loading -> {

                }
            }
        }
    }

    private fun addUserInformationToServer(userInfo: UserModel) {
        viewModelScope.launch {
            when (val result = userProfileRepository.addUserAdditionalInformation(userInfo)) {
                is Resource.Success -> {
                    redirectToHomePage()
                }

                is Resource.Error -> {
                    _errorMessages.emit(
                        result.exception.message ?: "AN ERROR"
                    )
                }

                is Resource.Loading -> {

                }
            }
        }
    }

    fun onLogin(email: String, password: String) {
        viewModelScope.launch {
            when (val result = authRepository.logIn(email, password)) {
                is Resource.Success -> {
                    updateUserInfo(result.data.uid)
                }

                is Resource.Error -> {
                    _errorMessages.emit(
                        result.exception.message ?: "AN ERROR"
                    )
                }

                else -> {}
            }
        }
    }

    private suspend fun updateUserInfo(userId: String) {
        when (val result = userProfileRepository.getUserInfoFromFirestore(userId)) {
            is Resource.Success -> {
                saveUserDataOnCache(result.data)
            }

            is Resource.Error -> {
                _errorMessages.emit(
                    result.exception.message ?: "AN ERROR"
                )
            }

            else -> {

            }
        }
    }

    private suspend fun saveUserDataOnCache(userModel: UserModel) {
        when (val result = userProfileRepository.saveUserInfoOnCache(userModel)) {
            is Resource.Success -> {
                redirectToHomePage()
            }

            is Resource.Error -> {
                _errorMessages.emit(
                    result.exception.message ?: "AN ERROR"
                )
            }

            else -> {

            }
        }
    }

    private suspend fun redirectToHomePage() {
        _navigationEvent.emit(
            NavigationTarget.Home
        )
    }

}

sealed class NavigationTarget {
    object Home : NavigationTarget()
    object Login : NavigationTarget()
}