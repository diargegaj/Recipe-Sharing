package com.diargegaj.recipesharing.presentation.screens.home.recipes

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.diargegaj.recipesharing.R
import com.diargegaj.recipesharing.domain.models.RecipeModel
import com.diargegaj.recipesharing.domain.models.UserModel
import com.diargegaj.recipesharing.domain.models.emptyRecipeModel
import com.diargegaj.recipesharing.domain.models.emptyUserModel

@Composable
fun RecipeDetailsScreen(
    recipeId: String,
) {
    val recipe: RecipeModel = emptyRecipeModel()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        RecipeImage(
            modifier = Modifier
                .width(250.dp)
                .align(CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = recipe.title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        UserInfo(emptyUserModel())
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
            items(recipe.ingredients) { ingredient ->
                Text(
                    text = ingredient,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun RecipeImage(
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = "",
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

        val imageLoader = // You can add placeholders, errors, etc. here if needed
            rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current)
                    .data(data = user?.profilePhotoUrl)
                    .apply(
                        block = fun ImageRequest.Builder.() {
                            placeholder(R.drawable.ic_launcher_background)  // Placeholder while the image is being fetched
                            error(R.drawable.ic_launcher_background)        // Image to show in case of an error
                        }
                    ).build()
            )
        Image(
            painter = imageLoader,  // Placeholder image
            contentDescription = "User Image",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .padding(8.dp)
        )

        Column(
            Modifier.padding(start = 10.dp)
        ) {
            Text(text = "${user?.name} ${user?.lastName}", fontWeight = FontWeight.Bold)
            Text(text = user?.email.toString(), style = MaterialTheme.typography.labelMedium)
        }
    }
}