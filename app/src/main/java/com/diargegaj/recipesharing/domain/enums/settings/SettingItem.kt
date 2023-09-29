package com.diargegaj.recipesharing.domain.enums.settings

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Person
import com.diargegaj.recipesharing.R
import com.diargegaj.recipesharing.presentation.navigation.Screen

enum class SettingItem(
    val settingIcon: Any,
    @StringRes
    val settingStringRes: Int,
    val route: String = ""
) {
    ACCOUNT(
        settingIcon = Icons.Default.AccountCircle,
        settingStringRes = R.string.account,
        route = Screen.AccountInfo.route
    ),

    PROFILE(
        settingIcon = Icons.Default.Person,
        settingStringRes = R.string.profile,
        route = Screen.ProfileInfo.route
    ),

    LOG_OUT(
        settingIcon = R.drawable.ic_logout,
        settingStringRes = R.string.log_out
    );
}