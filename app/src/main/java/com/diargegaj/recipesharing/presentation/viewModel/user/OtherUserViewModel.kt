package com.diargegaj.recipesharing.presentation.viewModel.user

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diargegaj.recipesharing.domain.repository.UserRepository
import com.diargegaj.recipesharing.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OtherUserViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository
) : ViewModel() {

    private var userId: String

    init {
        userId = savedStateHandle["userId"] ?: ""
        updateUserDataFromFirestore()
        Log.d("diari1", "on init other user viewmodel")
    }

    private fun updateUserDataFromFirestore() {
        viewModelScope.launch {
            when (val result = userRepository.getUserInfoFromFirestore(userId)) {
                is Resource.Success -> {
                    userRepository.saveUserInfoOnCache(result.data)
                }

                else -> Unit
            }
        }
    }


}