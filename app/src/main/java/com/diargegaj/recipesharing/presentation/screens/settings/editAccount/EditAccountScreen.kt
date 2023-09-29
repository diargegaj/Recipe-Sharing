package com.diargegaj.recipesharing.presentation.screens.settings.editAccount

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.diargegaj.recipesharing.R
import com.diargegaj.recipesharing.domain.enums.settings.SettingItem
import com.diargegaj.recipesharing.presentation.navigation.RecipeNavigationActions
import com.diargegaj.recipesharing.presentation.screens.settings.SettingsItem
import com.diargegaj.recipesharing.presentation.utils.DefaultAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAccountScreen(recipeNavigationActions: RecipeNavigationActions) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                DefaultAppBar(
                    title = stringResource(id = R.string.account),
                    navigationIcon = Icons.Default.ArrowBack,
                    onNavigationClick = { recipeNavigationActions.goBack() }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues = paddingValues)
            ) {
                SettingsItem(item = SettingItem.EMAIL) {
                    recipeNavigationActions.navigateTo(it.route)
                }
                SettingsItem(item = SettingItem.PASSWORD) {
                    recipeNavigationActions.navigateTo(it.route)
                }
            }
        }
    }
}