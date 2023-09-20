package com.diargegaj.recipesharing.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diargegaj.recipesharing.R
import com.diargegaj.recipesharing.domain.models.UserModel
import com.diargegaj.recipesharing.domain.repository.UserRepository
import com.diargegaj.recipesharing.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    fun onRegister(userInfo: UserModel, password: String) {
        viewModelScope.launch {
            when (val registrationResult = userRepository.registerUser(email = userInfo.email, password = password)) {
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