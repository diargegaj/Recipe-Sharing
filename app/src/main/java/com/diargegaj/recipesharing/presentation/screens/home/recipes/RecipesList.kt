package com.diargegaj.recipesharing.presentation.screens.home.recipes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.diargegaj.recipesharing.domain.models.UserModel
import com.diargegaj.recipesharing.domain.models.recipe.RecipeModel
import com.diargegaj.recipesharing.presentation.navigation.RecipeNavigationActions
import com.diargegaj.recipesharing.presentation.utils.LoadImage

@Composable
fun RecipeList(
    recipes: List<RecipeModel>,
    recipeNavigationActions: RecipeNavigationActions,
    modifier: Modifier = Modifier
) {

    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(recipes.size) {
            RecipePost(
                recipeModel = recipes[it],
                navigateToRecipe = { recipeId ->
                    recipeNavigationActions.navigateToRecipeDetails(recipeId)
                }
            )
        }
    }

}

@Composable
fun RecipePost(
    recipeModel: RecipeModel,
    navigateToRecipe: (recipeId: String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = { navigateToRecipe(recipeModel.recipeId) })
            .background(
                color = Color.White,
                shape = RoundedCornerShape(8.dp)
            )
            .shadow(4.dp, RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        PostImage(
            imageUrl = recipeModel.imageUrl,
            modifier = Modifier.padding(16.dp)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 10.dp)
        ) {
            PostTitle(recipeModel)
            Author(recipeModel.userModel)
        }
        RightButton(
            modifier = Modifier
                .clearAndSetSemantics {}
                .padding(vertical = 2.dp, horizontal = 6.dp)
                .align(Alignment.CenterVertically)
        )
    }
}

@Composable
fun PostImage(imageUrl: String, modifier: Modifier = Modifier) {
    LoadImage(
        imageUrl = imageUrl,
        modifier = modifier
            .size(40.dp)
            .clip(MaterialTheme.shapes.small),
        contentDescription = null
    )
}

@Composable
fun Author(
    author: UserModel?,
    modifier: Modifier = Modifier
) {
    Row(modifier) {
        Text(
            text = "${author?.name} ${author?.lastName}",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


@Composable
fun PostTitle(recipeModel: RecipeModel) {
    Text(
        text = recipeModel.title,
        style = MaterialTheme.typography.titleMedium,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
fun RightButton(
    modifier: Modifier = Modifier
) {
    Icon(
        modifier = modifier,
        imageVector = Icons.Filled.KeyboardArrowRight,
        contentDescription = null
    )
}