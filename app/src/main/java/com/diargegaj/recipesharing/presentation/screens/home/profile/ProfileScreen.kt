package com.diargegaj.recipesharing.presentation.screens.home.profile

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import com.diargegaj.recipesharing.R
import com.diargegaj.recipesharing.domain.models.UserModel
import com.diargegaj.recipesharing.domain.models.recipe.RecipeModel
import com.diargegaj.recipesharing.presentation.navigation.RecipeNavigationActions
import com.diargegaj.recipesharing.presentation.utils.LoadImage
import com.diargegaj.recipesharing.presentation.utils.hiltViewModelFromEntry
import com.diargegaj.recipesharing.presentation.viewModel.followers.FollowersViewModel
import com.diargegaj.recipesharing.presentation.viewModel.home.profile.UserProfileViewModel

@Composable
fun ProfileScreen(
    recipeNavigationActions: RecipeNavigationActions,
    backStackEntry: NavBackStackEntry? = null,
    viewModel: UserProfileViewModel = hiltViewModelFromEntry(entry = backStackEntry),
    followersViewModel: FollowersViewModel = hiltViewModelFromEntry(entry = backStackEntry)
) {
    val userState by viewModel.userState.collectAsState()
    val isFollowing by followersViewModel.isFollowing.collectAsState()
    val messages by viewModel.messages.collectAsState(initial = "")

    if (messages.isNotEmpty()) {
        Toast.makeText(LocalContext.current, messages, Toast.LENGTH_SHORT).show()
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ProfileHeader(
            user = userState,
            isFollowing = isFollowing,
            recipeNavigationActions = recipeNavigationActions,
            onFollowClicked = {
                followersViewModel.onFollowClicked()
            },
            onUnfollowClicked = {
                followersViewModel.onUnfollowCLicked()
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(id = R.string.recipes),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(32.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(4.dp)
        ) {
            itemsIndexed(userState.userRecipes) { _, recipe ->
                RecipeThumbnail(
                    recipe = recipe,
                    modifier = Modifier
                        .size(100.dp)
                        .padding(4.dp)
                        .clickable {
                            recipeNavigationActions.navigateToRecipeDetails(recipe.recipeId)
                        })
            }
        }
    }
}

@Composable
fun RecipeThumbnail(recipe: RecipeModel, modifier: Modifier = Modifier) {
    LoadImage(
        imageUrl = recipe.imageUrl, modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .shadow(2.dp, RoundedCornerShape(8.dp))
    )

}

@Composable
fun ProfileHeader(
    user: UserModel,
    isFollowing: Boolean,
    recipeNavigationActions: RecipeNavigationActions,
    onFollowClicked: () -> Unit,
    onUnfollowClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LoadImage(
                imageUrl = user.profilePhotoUrl,
                contentDescription = "User profile image",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
            )

            Column(horizontalAlignment = Alignment.End) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable {
                            recipeNavigationActions.navigateToUserFollowers(user.userUUID)
                        }
                    ) {
                        Text(
                            text = "${user.followersCount}",
                            fontWeight = FontWeight.Bold,
                            style = TextStyle(fontSize = 20.sp)
                        )
                        Text(text = stringResource(id = R.string.followers))
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable {
                            recipeNavigationActions.navigateToUserFollowing(user.userUUID)
                        }
                    ) {
                        Text(
                            text = "${user.followingCount}",
                            fontWeight = FontWeight.Bold,
                            style = TextStyle(fontSize = 20.sp)
                        )
                        Text(text = stringResource(id = R.string.following))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = user.getUserFullName(), fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(4.dp))

        FollowerButton(
            isCurrentUser = user.isCurrentUser,
            isFollowing = isFollowing,
            onFollowClicked = onFollowClicked,
            onUnfollowClicked = onUnfollowClicked,
            Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun FollowerButton(
    isCurrentUser: Boolean,
    isFollowing: Boolean,
    onFollowClicked: () -> Unit,
    onUnfollowClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (!isCurrentUser) {
        if (isFollowing) {
            Button(
                modifier = modifier,
                onClick = onUnfollowClicked
            ) {
                Text(text = stringResource(id = R.string.unfollow))
            }
        } else {
            Button(
                modifier = modifier,
                onClick = onFollowClicked
            ) {
                Text(text = stringResource(id = R.string.follow))
            }
        }
    }
}
