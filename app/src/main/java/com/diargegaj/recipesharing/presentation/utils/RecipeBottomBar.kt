package com.diargegaj.recipesharing.presentation.utils

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.diargegaj.recipesharing.R

@Composable
fun RecipeBottomBar(
    onSelected: (SelectedScreenCategory) -> Unit
) {
    NavigationBar {
        val selectedItemIndex = rememberSaveable {
            mutableIntStateOf(0)
        }
        bottomBarItems().forEachIndexed() { index, item ->
            NavigationBarItem(
                selected = selectedItemIndex.intValue == index,
                onClick = {
                    selectedItemIndex.intValue = index
                    onSelected(item.selectedScreenCategory)
                },
                label = {
                    Text(text = stringResource(id = item.titleResId))
                },
                icon = {
                    Icon(
                        imageVector = item.selectedIcon,
                        contentDescription = stringResource(id = item.titleResId)
                    )
                }
            )
        }
    }
}

data class BottomBarItem(
    @StringRes
    val titleResId: Int,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector,
    val selectedScreenCategory: SelectedScreenCategory
)

fun bottomBarItems(): List<BottomBarItem> = listOf(
    BottomBarItem(
        titleResId = R.string.home,
        selectedIcon = Icons.Filled.Home,
        unSelectedIcon = Icons.Outlined.Home,
        selectedScreenCategory = SelectedScreenCategory.RECIPE_SCREEN
    ),
    BottomBarItem(
        titleResId = R.string.add_recipe,
        selectedIcon = Icons.Filled.Add,
        unSelectedIcon = Icons.Outlined.Add,
        selectedScreenCategory = SelectedScreenCategory.ADD_RECIPE
    ),
    BottomBarItem(
        titleResId = R.string.profile,
        selectedIcon = Icons.Filled.AccountCircle,
        unSelectedIcon = Icons.Outlined.AccountCircle,
        selectedScreenCategory = SelectedScreenCategory.PROFILE
    ),
)

enum class SelectedScreenCategory {
    RECIPE_SCREEN,
    ADD_RECIPE,
    PROFILE
}