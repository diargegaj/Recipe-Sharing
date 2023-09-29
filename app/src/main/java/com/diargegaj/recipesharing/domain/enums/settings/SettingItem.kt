package com.diargegaj.recipesharing.domain.enums.settings

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Person
import com.diargegaj.recipesharing.R

enum class SettingItem(
    val settingIcon: Any,
    @StringRes
    val settingStringRes: Int,
) {
    ACCOUNT(
        settingIcon = Icons.Default.AccountCircle,
        settingStringRes = R.string.account,
    ),

    PROFILE(
        settingIcon = Icons.Default.Person,
        settingStringRes = R.string.profile,
    ),

    LOG_OUT(
        settingIcon = R.drawable.ic_logout,
        settingStringRes = R.string.log_out
    );
}