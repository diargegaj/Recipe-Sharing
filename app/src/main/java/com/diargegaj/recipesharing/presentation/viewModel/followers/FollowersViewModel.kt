package com.diargegaj.recipesharing.presentation.viewModel.followers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diargegaj.recipesharing.domain.models.UserModel
import com.diargegaj.recipesharing.domain.repository.UserRepository
import com.diargegaj.recipesharing.domain.repository.userAuth.UserAuthRepository
import com.diargegaj.recipesharing.domain.repository.userFollow.UserFollowRepository
import com.diargegaj.recipesharing.domain.repository.userProfile.UserProfileRepository
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
    private val userRepository: UserRepository,
    userAuthRepository: UserAuthRepository,
    private val userProfileRepository: UserProfileRepository,
    private val userFollowRepository: UserFollowRepository
) : ViewModel() {

    private var loggedInUserId = ""
    private var otherUserId = ""

    private val _isFollowing = MutableStateFlow(false)
    val isFollowing: StateFlow<Boolean> = _isFollowing.asStateFlow()

    private val _followers = MutableStateFlow<List<UserModel>>(listOf())
    val followers: StateFlow<List<UserModel>> = _followers.asStateFlow()

    private val _following = MutableStateFlow<List<UserModel>>(listOf())
    val following: StateFlow<List<UserModel>> = _following.asStateFlow()

    init {
        loggedInUserId = when (val result = userAuthRepository.getUserId()) {
            is Resource.Success -> {
                result.data
            }

            else -> ""
        }
        otherUserId =
            savedStateHandle?.get<String>("userId")?.takeIf { it.isNotEmpty() } ?: loggedInUserId
        checkIsUserFollowing()
        getFollowers()
        getFollowing()
    }

    private fun getFollowing() {
        viewModelScope.launch {
            when (val result = userRepository.getFollowingForUser(otherUserId, loggedInUserId)) {
                is Resource.Success -> {
                    _followers.value = result.data
                }

                else -> Unit
            }

        }
    }

    private fun getFollowers() {
        viewModelScope.launch {
            when (val result = userRepository.getFollowersForUser(otherUserId, loggedInUserId)) {
                is Resource.Success -> {
                    _followers.value = result.data
                }

                else -> Unit
            }

        }
    }

    private fun checkIsUserFollowing() {
        viewModelScope.launch {
            userFollowRepository.isUserFollowing(loggedInUserId, otherUserId).collectLatest { result ->
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
        onUnfollowUser(otherUserId)
    }

    fun onUnfollowUser(targetUserId: String) {
        viewModelScope.launch {
            when (userFollowRepository.unfollowUser(loggedInUserId, targetUserId)) {
                is Resource.Success -> {
                    handleUnfollowSuccess(targetUserId)
                }

                is Resource.Error -> {

                }

                else -> Unit
            }
        }
    }

    fun onFollowClicked() {
        onFollowUser(targetUserId = otherUserId)
    }

    fun onFollowUser(targetUserId: String) {
        viewModelScope.launch {
            when (userFollowRepository.followUser(loggedInUserId, targetUserId)) {
                is Resource.Success -> {
                    handleFollowSuccess(targetUserId)
                }

                is Resource.Error -> {
                }

                else -> Unit
            }
        }
    }

    private suspend fun handleFollowSuccess(targetUserId: String) {
        val updatedFollowers = updateFollowStateInList(_followers.value, targetUserId, true)
        _followers.value = updatedFollowers
        _isFollowing.value = true
        userProfileRepository.updateUserInfoFromFirestore(targetUserId)
    }

    private suspend fun handleUnfollowSuccess(targetUserId: String) {
        val updatedFollowers = updateFollowStateInList(_followers.value, targetUserId, false)
        _followers.value = updatedFollowers
        _isFollowing.value = false
        userProfileRepository.updateUserInfoFromFirestore(targetUserId)
    }

    private fun updateFollowStateInList(
        users: List<UserModel>,
        targetUserId: String,
        isFollowing: Boolean
    ): List<UserModel> {
        return users.map { user ->
            if (user.userUUID == targetUserId) {
                user.copy(isFollowedByCurrentUser = isFollowing)
            } else user
        }
    }
}