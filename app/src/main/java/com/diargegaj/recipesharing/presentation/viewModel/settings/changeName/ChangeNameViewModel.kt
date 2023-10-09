package com.diargegaj.recipesharing.presentation.viewModel.settings.changeName

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diargegaj.recipesharing.domain.models.settings.changeName.ChangeNameState
import com.diargegaj.recipesharing.domain.repository.userProfile.UserProfileRepository
import com.diargegaj.recipesharing.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangeNameViewModel @Inject constructor(
    private val userProfileRepository: UserProfileRepository
) : ViewModel() {
    private val _changeNameState = MutableStateFlow(ChangeNameState())
    val changeNameState = _changeNameState.asStateFlow()

    private val _userMessageEvent = MutableSharedFlow<String>(replay = 0)
    val userMessageEvent = _userMessageEvent.asSharedFlow()

    fun onNameChange() {
        val name = _changeNameState.value.name
        val lastName = _changeNameState.value.lastName

        viewModelScope.launch {
            when (userProfileRepository.updateUserName(name, lastName)) {
                is Resource.Success -> {
                    _userMessageEvent.emit(
                        "Name updated successfully"
                    )
                    resetToDefault()
                }

                is Resource.Error -> {
                    _userMessageEvent.emit(
                        "Failed to update name"
                    )

                }

                else -> Unit
            }
        }


    }

    private fun resetToDefault() {
        _changeNameState.value = ChangeNameState()
    }

    fun nameUpdated(name: String) {
        _changeNameState.value = _changeNameState.value.copy(
            name = name
        )
    }

    fun lastNameUpdated(lastName: String) {
        _changeNameState.value = _changeNameState.value.copy(
            lastName = lastName
        )
    }

}
