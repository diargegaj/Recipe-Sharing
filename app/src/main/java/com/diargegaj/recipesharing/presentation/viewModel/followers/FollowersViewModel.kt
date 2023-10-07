package com.diargegaj.recipesharing.presentation.viewModel.followers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diargegaj.recipesharing.domain.repository.UserRepository
import com.diargegaj.recipesharing.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowersViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle? = null,
    private val userRepository: UserRepository
) : ViewModel() {

    private var loggedInUserId = ""
    private var otherUserId = ""

    private val _isFollowing = MutableStateFlow(false)
    val isFollowing: StateFlow<Boolean> = _isFollowing.asStateFlow()

    init {
        otherUserId = savedStateHandle?.get<String>("userId")?.takeIf { it.isNotEmpty() } ?: ""
        loggedInUserId = when (val result = userRepository.getUserId()) {
            is Resource.Success -> {
                result.data
            }

            else -> ""
        }
        checkIsUserFollowing()
    }

    private fun checkIsUserFollowing() {
        viewModelScope.launch {
            userRepository.isUserFollowing(loggedInUserId, otherUserId).collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        _isFollowing.emit(
                            result.data
                        )
                    }

                    else -> Unit
                }
            }
        }
    }

    fun onUnfollowCLicked() {
        viewModelScope.launch {
            when (userRepository.unfollowUser(loggedInUserId, otherUserId)) {
                is Resource.Success -> {
                    _isFollowing.value = false
                    userRepository.updateUserInfoFromFirestore(otherUserId)
                }

                is Resource.Error -> {

                }

                else -> Unit
            }
        }
    }

    fun onFollowClicked() {
        viewModelScope.launch {
            when (userRepository.followUser(loggedInUserId, otherUserId)) {
                is Resource.Success -> {
                    _isFollowing.value = true
                    userRepository.updateUserInfoFromFirestore(otherUserId)
                }

                is Resource.Error -> {
                }

                else -> Unit
            }
        }
    }
}