package com.diargegaj.recipesharing.presentation.screens.home.profile

import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.diargegaj.recipesharing.R
import com.diargegaj.recipesharing.domain.models.recipe.RecipeModel
import com.diargegaj.recipesharing.presentation.navigation.RecipeNavigationActions
import com.diargegaj.recipesharing.presentation.utils.ImagePicker
import com.diargegaj.recipesharing.presentation.utils.LoadImage
import com.diargegaj.recipesharing.presentation.viewModel.home.profile.UserProfileViewModel

@Composable
fun ProfileScreen(
    recipeNavigationActions: RecipeNavigationActions,
    viewModel: UserProfileViewModel = hiltViewModel()
) {
    val userState by viewModel.userState.collectAsState()
    val messages by viewModel.messages.collectAsState(initial = "")

    if (messages.isNotEmpty()) {
        Toast.makeText(LocalContext.current, messages, Toast.LENGTH_SHORT).show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(120.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            AsyncImage(
                model = userState.profilePhotoUrl,
                contentDescription = "User Profile Picture",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
            )
            ImagePicker(
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.BottomEnd),
                image = null,
                editView = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_edit),
                        contentDescription = "Edit icon"
                    )
                },
                onImagePicked = { pickedImage ->
                    viewModel.updateProfileImage(pickedImage)
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "${userState.getUserFullName()}",
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = userState.email, textAlign = TextAlign.Center)

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(id = R.string.my_recipes),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(32.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(4.dp)
        ) {
            itemsIndexed(userState.userRecipes) { _, recipe ->
                RecipeThumbnail(recipe = recipe, modifier = Modifier
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
        imageUrl = recipe.imageUrl,
        modifier = modifier
    )

}