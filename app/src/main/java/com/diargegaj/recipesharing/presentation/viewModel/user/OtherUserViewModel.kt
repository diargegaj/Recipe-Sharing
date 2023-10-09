package com.diargegaj.recipesharing.presentation.viewModel.user

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diargegaj.recipesharing.domain.repository.userProfile.UserProfileRepository
import com.diargegaj.recipesharing.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OtherUserViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userProfileRepository: UserProfileRepository
) : ViewModel() {

    private var userId: String

    init {
        userId = savedStateHandle["userId"] ?: ""
        updateUserDataFromFirestore()
    }

    private fun updateUserDataFromFirestore() {
        viewModelScope.launch {
            when (val result = userProfileRepository.getUserInfoFromFirestore(userId)) {
                is Resource.Success -> {
                    userProfileRepository.saveUserInfoOnCache(result.data)
                }

                else -> Unit
            }
        }
    }


}