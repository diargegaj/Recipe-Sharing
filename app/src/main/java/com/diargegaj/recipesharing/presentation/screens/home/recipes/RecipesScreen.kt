package com.diargegaj.recipesharing.presentation.screens.home.recipes

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.diargegaj.recipesharing.R
import com.diargegaj.recipesharing.domain.models.RecipeModel
import com.diargegaj.recipesharing.domain.models.UserModel
import com.diargegaj.recipesharing.domain.models.emptyRecipeModel

@Composable
fun RecipesScreen() {

    val recipes by remember { mutableStateOf(listOf(emptyRecipeModel())) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(recipes.size) {
            RecipePost(
                recipeModel = recipes[it],
                navigateToRecipe = {

                }
            )
        }
    }

}

@Composable
fun RecipePost(
    recipeModel: RecipeModel = emptyRecipeModel(),
    navigateToRecipe: (RecipeModel) -> Unit,
) {
    Row(
        modifier = Modifier
            .clickable(onClick = { navigateToRecipe(recipeModel) })
    ) {
        PostImage(Modifier.padding(16.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 10.dp)
        ) {
            PostTitle(recipeModel)
            Author()
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
fun PostImage(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.ic_launcher_background), // TODO: change
        contentDescription = null,
        modifier = modifier
            .size(40.dp, 40.dp)
            .clip(MaterialTheme.shapes.small)
    )
}

@Composable
fun Author(
    author: UserModel? = null,
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
fun PostTitle(recipeModel: RecipeModel = emptyRecipeModel()) {
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