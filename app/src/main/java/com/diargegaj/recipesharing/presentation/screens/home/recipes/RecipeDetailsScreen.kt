package com.diargegaj.recipesharing.presentation.screens.home.recipes

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import coil.compose.AsyncImage
import com.diargegaj.recipesharing.domain.models.UserModel
import com.diargegaj.recipesharing.domain.models.emptyUserModel
import com.diargegaj.recipesharing.presentation.viewModel.home.recipes.RecipeDetailsViewModel

@Composable
fun RecipeDetailsScreen(
    backStackEntry: NavBackStackEntry,
    viewModel: RecipeDetailsViewModel = hiltViewModel(backStackEntry)
) {
    val recipe by viewModel.state.collectAsState()

    val messages by viewModel.messages.collectAsState(initial = "")

    if (messages.isNotEmpty()) {
        Toast.makeText(LocalContext.current, messages, Toast.LENGTH_SHORT).show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        RecipeImage(
            modifier = Modifier
                .width(250.dp)
                .align(CenterHorizontally),
            imageUrl = recipe.imageUrl
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = recipe.title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        UserInfo(recipe.userModel ?: emptyUserModel())
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = recipe.description,
            style = MaterialTheme.typography.labelSmall
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Ingredients",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(recipe.ingredients.size) { index ->
                Text(
                    text = "${index + 1}. ${recipe.ingredients[index]}",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun RecipeImage(
    modifier: Modifier = Modifier,
    imageUrl: String
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = "Recipe Image",
        modifier = modifier
            .height(200.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.secondary)
    )
}

@Composable
fun UserInfo(user: UserModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.08f))
            .padding(8.dp)
    ) {
        AsyncImage(
            model = user.profilePhotoUrl,
            contentDescription = "User Image",
            modifier = Modifier
                .clip(CircleShape)
                .size(40.dp)
                .background(MaterialTheme.colorScheme.primary)
                .padding(8.dp),
            contentScale = ContentScale.Crop
        )

        Column(
            Modifier.padding(start = 10.dp)
        ) {
            Text(text = "${user.name} ${user.lastName}", fontWeight = FontWeight.Bold)
            Text(text = user.email.toString(), style = MaterialTheme.typography.labelMedium)
        }
    }
}