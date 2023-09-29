package com.diargegaj.recipesharing.presentation.screens.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.diargegaj.recipesharing.R
import com.diargegaj.recipesharing.domain.enums.settings.SettingItem
import com.diargegaj.recipesharing.presentation.navigation.RecipeNavigationActions
import com.diargegaj.recipesharing.presentation.screens.home.recipes.RightButton
import com.diargegaj.recipesharing.presentation.utils.DefaultAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    recipeNavigationActions: RecipeNavigationActions
) {

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                DefaultAppBar(
                    title = stringResource(id = R.string.app_name),
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
                SettingsItem(item = SettingItem.ACCOUNT) {
                    recipeNavigationActions.navigateTo(it.route)
                }
                SettingsItem(item = SettingItem.PROFILE) {
                    recipeNavigationActions.navigateTo(it.route)
                }

                Spacer(modifier = Modifier.weight(0.5f))

                SettingsItem(item = SettingItem.LOG_OUT) {

                }
            }
        }
    }
}

@Composable
fun SettingsItem(
    item: SettingItem,
    modifier: Modifier = Modifier,
    onClick: (SettingItem) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(24.dp)
            )
            .shadow(4.dp, RoundedCornerShape(8.dp))
            .clickable {
                onClick(item)
            }
            .padding(24.dp)
    ) {
        val icon = when (item.settingIcon) {
            is ImageVector -> item.settingIcon
            is Int -> ImageVector.vectorResource(id = item.settingIcon)
            else -> throw IllegalArgumentException("Unknown icon type")
        }
        SettingsPhoto(
            settingIcon = icon,
            modifier = Modifier.padding(end = 8.dp)
        )
        SettingsName(
            settingName = stringResource(id = item.settingStringRes),
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f)
        )
        RightButton(
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}


@Composable
fun SettingsName(settingName: String, modifier: Modifier = Modifier) {
    Text(text = settingName, modifier = modifier)
}

@Composable
fun SettingsPhoto(settingIcon: ImageVector, modifier: Modifier) {
    Image(
        modifier = modifier,
        imageVector = settingIcon,
        contentDescription = ""
    )
}