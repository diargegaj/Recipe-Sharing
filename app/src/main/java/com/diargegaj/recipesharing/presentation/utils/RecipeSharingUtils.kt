package com.diargegaj.recipesharing.presentation.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    titleText: String,
    navigationIcon: ImageVector,
    navigationIconCLick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(text = titleText)
        },
        navigationIcon = {
            IconButton(onClick = { navigationIconCLick() }) {
                Icon(
                    imageVector = navigationIcon,
                    contentDescription = "Navigation icon"
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}