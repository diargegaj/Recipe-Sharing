package com.diargegaj.recipesharing.presentation.viewModel.home

import androidx.lifecycle.ViewModel
import com.diargegaj.recipesharing.domain.models.HomeStateModel
import com.diargegaj.recipesharing.presentation.utils.SelectedScreenCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _homeState: MutableStateFlow<HomeStateModel> = MutableStateFlow(HomeStateModel())
    val homeState = _homeState.asStateFlow()

    fun onSelectedScreenUpdated(selectedScreenCategory: SelectedScreenCategory) {
        _homeState.value = _homeState.value.copy(
            selectedScreenCategory = selectedScreenCategory
        )
    }

    fun onSearchIconClicked() {
        val isSearchBarVisible = _homeState.value.isSearchBarVisible
        _homeState.value = _homeState.value.copy(
            isSearchBarVisible = !isSearchBarVisible
        )
    }

    fun updateSearchIconVisibility(value: Boolean) {
        _homeState.value = _homeState.value.copy(
            shouldShowSearchIcon = value
        )
    }

}
