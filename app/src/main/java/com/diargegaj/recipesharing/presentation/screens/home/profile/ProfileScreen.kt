package com.diargegaj.recipesharing.presentation.screens.home.profile

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import com.diargegaj.recipesharing.R
import com.diargegaj.recipesharing.domain.models.recipe.RecipeModel
import com.diargegaj.recipesharing.presentation.navigation.RecipeNavigationActions
import com.diargegaj.recipesharing.presentation.utils.LoadImage
import com.diargegaj.recipesharing.presentation.utils.hiltViewModelFromEntry
import com.diargegaj.recipesharing.presentation.viewModel.home.profile.UserProfileViewModel

@Composable
fun ProfileScreen(
    recipeNavigationActions: RecipeNavigationActions,
    backStackEntry: NavBackStackEntry? = null,
    viewModel: UserProfileViewModel = hiltViewModelFromEntry(backStackEntry)
) {
    val userState by viewModel.userState.collectAsState()
    val messages by viewModel.messages.collectAsState(initial = "")

    if (messages.isNotEmpty()) {
        Toast.makeText(LocalContext.current, messages, Toast.LENGTH_SHORT).show()
    }

    Column(
        modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .shadow(4.dp, CircleShape) // Shadow for depth
                .clip(CircleShape),
            contentAlignment = Alignment.BottomEnd
        ) {
            LoadImage(
                imageUrl = userState.profilePhotoUrl,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = userState.getUserFullName(),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = userState.email, textAlign = TextAlign.Center)

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