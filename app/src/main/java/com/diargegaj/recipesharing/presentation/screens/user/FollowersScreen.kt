package com.diargegaj.recipesharing.presentation.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import com.diargegaj.recipesharing.R
import com.diargegaj.recipesharing.domain.models.UserModel
import com.diargegaj.recipesharing.presentation.navigation.RecipeNavigationActions
import com.diargegaj.recipesharing.presentation.utils.DefaultAppBar
import com.diargegaj.recipesharing.presentation.utils.LoadImage
import com.diargegaj.recipesharing.presentation.viewModel.followers.FollowersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FollowersScreen(
    backStackEntry: NavBackStackEntry,
    recipeNavigationActions: RecipeNavigationActions,
    followersViewModel: FollowersViewModel = hiltViewModel(backStackEntry)
) {
    val followers by followersViewModel.followers.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                DefaultAppBar(
                    title = stringResource(id = R.string.followers),
                    navigationIcon = Icons.Default.ArrowBack,
                    onNavigationClick = {
                        recipeNavigationActions.goBack()
                    })

            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier.padding(paddingValues)
            ) {
                items(followers.size) {
                    val user = followers[it]
                    FollowerItem(
                        user,
                        onFollowClicked = {
                            followersViewModel.onFollowUser(user.userUUID)
                        },
                        onUnfollowClicked = {
                            followersViewModel.onUnfollowUser(user.userUUID)
                        },
                        recipeNavigationActions = recipeNavigationActions
                    )
                }
            }
        }
    }
}

@Composable
fun FollowerItem(
    follower: UserModel,
    onFollowClicked: () -> Unit,
    onUnfollowClicked: () -> Unit,
    recipeNavigationActions: RecipeNavigationActions
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                recipeNavigationActions.navigateToUserProfile(follower.userUUID)
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            LoadImage(
                imageUrl = follower.profilePhotoUrl, modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
            )
            Column {
                Text(text = follower.getUserFullName())
            }
        }

        if (follower.isFollowedByCurrentUser) {
            Button(onClick = onUnfollowClicked) {
                Text(text = stringResource(id = R.string.unfollow))
            }
        } else {
            Button(onClick = onFollowClicked) {
                Text(text = stringResource(id = R.string.follow))
            }
        }

    }
}