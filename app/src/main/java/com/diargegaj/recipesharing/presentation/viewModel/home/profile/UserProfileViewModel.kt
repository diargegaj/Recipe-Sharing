package com.diargegaj.recipesharing.presentation.viewModel.home.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diargegaj.recipesharing.domain.models.UserModel
import com.diargegaj.recipesharing.domain.models.emptyUserModel
import com.diargegaj.recipesharing.domain.repository.ImageUploadRepository
import com.diargegaj.recipesharing.domain.repository.UserRepository
import com.diargegaj.recipesharing.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val imageUploadRepository: ImageUploadRepository
) : ViewModel() {

    private var userId: String = ""

    private val _userState = MutableStateFlow(emptyUserModel())
    val userState: StateFlow<UserModel> = _userState.asStateFlow()

    init {
        updateLocalUserID()
        loadUserDataFromCache()
        updateUserDataFromFirestore()
    }

    private fun updateLocalUserID() {
        when (val result = userRepository.getUserId()) {
            is Resource.Success -> {
                userId = result.data
            }

            else -> {

            }
        }
    }

    private fun updateUserDataFromFirestore() {
        viewModelScope.launch {
            when (val result = userRepository.getUserInfoFromFirestore(userId)) {
                is Resource.Success -> {
                    userRepository.saveUserInfoOnCache(result.data)
                }

                is Resource.Error -> {

                }

                Resource.Loading -> {

                }
            }
        }
    }

    private fun loadUserDataFromCache() {
        viewModelScope.launch {
            when (val result = userRepository.getUserInfoFromCache(userId)) {
                is Resource.Success -> {
                    _userState.value = result.data
                }

                is Resource.Error -> {

                }

                Resource.Loading -> {

                }
            }
        }
    }
}