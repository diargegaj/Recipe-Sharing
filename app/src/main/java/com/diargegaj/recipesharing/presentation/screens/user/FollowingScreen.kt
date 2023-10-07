package com.diargegaj.recipesharing.presentation.screens.user

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import com.diargegaj.recipesharing.R
import com.diargegaj.recipesharing.presentation.navigation.RecipeNavigationActions
import com.diargegaj.recipesharing.presentation.utils.DefaultAppBar
import com.diargegaj.recipesharing.presentation.viewModel.followers.FollowersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FollowingScreen(
    backStackEntry: NavBackStackEntry,
    recipeNavigationActions: RecipeNavigationActions,
    followersViewModel: FollowersViewModel = hiltViewModel(backStackEntry)
) {
    val following by followersViewModel.following.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                DefaultAppBar(
                    title = stringResource(id = R.string.following),
                    navigationIcon = Icons.Default.ArrowBack,
                    onNavigationClick = {
                        recipeNavigationActions.goBack()
                    })

            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier.padding(paddingValues)
            ) {
                items(following.size) {
                    val user = following[it]
                    FollowerItem(
                        user,
                        onFollowClicked = {
                            followersViewModel.onFollowUser(user.userUUID)
                        },
                        onUnfollowClicked = {
                            followersViewModel.onUnfollowUser(user.userUUID)
                        }
                    )
                }
            }
        }
    }
}