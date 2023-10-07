package com.diargegaj.recipesharing.presentation.viewModel.home.profile

import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diargegaj.recipesharing.domain.enums.ImagePath
import com.diargegaj.recipesharing.domain.models.UserModel
import com.diargegaj.recipesharing.domain.models.emptyUserModel
import com.diargegaj.recipesharing.domain.repository.ImageUploadRepository
import com.diargegaj.recipesharing.domain.repository.UserRepository
import com.diargegaj.recipesharing.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle? = null,
    private val userRepository: UserRepository,
    private val imageUploadRepository: ImageUploadRepository
) : ViewModel() {

    private var userId: String = ""

    private val _userState = MutableStateFlow(emptyUserModel())
    val userState: StateFlow<UserModel> = _userState.asStateFlow()

    private val _messages = MutableSharedFlow<String>()
    val messages: SharedFlow<String> get() = _messages

    init {
        updateLocalUserID()
        loadUserDataFromCache()
        updateUserDataFromFirestore()
    }

    private fun updateLocalUserID() {
        userId = savedStateHandle?.get<String>("userId")?.takeIf { it.isNotEmpty() }
            ?: when (val result = userRepository.getUserId()) {
                is Resource.Success -> {
                    Log.d("diari1", "On sukses")
                    result.data
                }

                else -> throw IllegalArgumentException("UserId is required.")
            }
    }

    private fun updateUserDataFromFirestore() {
        viewModelScope.launch {
            when (val result = userRepository.getUserInfoFromFirestore(userId)) {
                is Resource.Success -> {
                    userRepository.saveUserInfoOnCache(result.data)
                }

                is Resource.Error -> {
                    _messages.emit(
                        "Failed to update user data from server."
                    )
                }

                else -> Unit
            }
        }
    }

    private fun loadUserDataFromCache() {
        Log.d("diari1", "savedStateHandler = $savedStateHandle")
        Log.d("diari1", "userId = $userId")
        viewModelScope.launch {
            userRepository.getUserInfoFromCache(userId).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _userState.value = result.data
                    }

                    else -> Unit
                }
            }
        }
    }

    fun updateProfileImage(pickedImage: ImageBitmap) {
        viewModelScope.launch {
            val path = ImagePath.PROFILE.getPath(id = userId)

            when (val result = imageUploadRepository.storeImage(
                imageBitmap = pickedImage,
                path = path
            )) {
                is Resource.Success -> {
                    updateProfilePhotoUrlOnFirestore(result.data.toString())
                }

                is Resource.Error -> {
                    _messages.emit(
                        "Failed to upload profile image."
                    )
                }

                else -> Unit
            }
        }
    }

    private suspend fun updateProfilePhotoUrlOnFirestore(imageUrl: String) {
        val result = userRepository.updateUserProfilePhotoUrl(
            userId = userId,
            imageUrl = imageUrl
        )

        when (result) {
            is Resource.Success -> {
                updateUserDataFromFirestore()
                _messages.emit(
                    "Profile photo updated."
                )
            }

            is Resource.Error -> {
                _messages.emit(
                    "Failed to upload profile photo info."
                )
            }

            else -> Unit
        }
    }
}