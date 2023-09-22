package com.diargegaj.recipesharing.domain.models

import com.diargegaj.recipesharing.presentation.utils.SelectedScreenCategory

data class HomeStateModel(
    val selectedScreenCategory: SelectedScreenCategory = SelectedScreenCategory.RECIPE_SCREEN,
    val isSearchBarVisible: Boolean = false,
    val shouldShowSearchIcon: Boolean = false
)