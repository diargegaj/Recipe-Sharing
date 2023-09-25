package com.diargegaj.recipesharing.presentation.screens.home.recipes.recipeDetails

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import com.diargegaj.recipesharing.R
import com.diargegaj.recipesharing.domain.models.UserModel
import com.diargegaj.recipesharing.domain.models.emptyUserModel
import com.diargegaj.recipesharing.domain.models.recipe.recipeDetails.FeedbackModel
import com.diargegaj.recipesharing.presentation.navigation.RecipeNavigationActions
import com.diargegaj.recipesharing.presentation.utils.DefaultAppBar
import com.diargegaj.recipesharing.presentation.utils.LoadImage
import com.diargegaj.recipesharing.presentation.viewModel.home.recipes.RecipeDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailsScreen(
    backStackEntry: NavBackStackEntry,
    recipeNavigationActions: RecipeNavigationActions,
    viewModel: RecipeDetailsViewModel = hiltViewModel(backStackEntry)
) {
    val recipe by viewModel.state.collectAsState()
    val currentRating by viewModel.currentRating.collectAsState()
    val feedbackText by viewModel.feedbackText.collectAsState()

    val showDialog = rememberSaveable { mutableStateOf(false) }
    val selectedFeedback = rememberSaveable { mutableStateOf<FeedbackModel?>(null) }

    val messages by viewModel.messages.collectAsState(initial = "")

    if (messages.isNotEmpty()) {
        Toast.makeText(LocalContext.current, messages, Toast.LENGTH_SHORT).show()
    }

    if (showDialog.value && selectedFeedback.value != null) {
        FeedbackInfo(
            feedbackModel = selectedFeedback.value!!,
            onDismiss = {
                showDialog.value = false
                selectedFeedback.value = null
            }
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                DefaultAppBar(
                    title = stringResource(id = R.string.recipe_details),
                    navigationIcon = Icons.Default.ArrowBack,
                    onNavigationClick = { recipeNavigationActions.goBack() },
                    scrollBehavior = scrollBehavior
                )
            },
            content = { paddingValues ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp)
                ) {
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            RecipeImage(
                                modifier = Modifier
                                    .width(250.dp)
                                    .align(CenterHorizontally),
                                imageUrl = recipe.imageUrl
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    item {
                        Text(
                            text = recipe.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    item {
                        UserInfo(recipe.userModel ?: emptyUserModel())

                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    item {
                        Text(
                            text = recipe.description,
                            style = MaterialTheme.typography.labelSmall
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    item {
                        Text(
                            text = "Ingredients",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    items(recipe.ingredients.size) { index ->
                        Text(
                            text = "${index + 1}. ${recipe.ingredients[index]}",
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))

                        RecipeFeedbackSection(
                            currentRating = currentRating,
                            onNewRating = {
                                viewModel.onNewRating(newRating = it)
                            },
                            feedbackText = feedbackText,
                            onNewFeedbackValue = {
                                viewModel.onNewFeedbackValue(feedbackValue = it)
                            },
                            onFeedbackSubmit = {
                                viewModel.onFeedbackSubmit()
                            },
                            usersFeedbacks = recipe.feedbacks,
                            currentUserFeedbackModel = recipe.myFeedbackModel,
                            onFeedbackClicked = { feedbackModel ->
                                selectedFeedback.value = feedbackModel
                                showDialog.value = true
                            }
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun RecipeImage(
    modifier: Modifier = Modifier,
    imageUrl: String
) {
    LoadImage(
        imageUrl = imageUrl,
        modifier = modifier
            .height(200.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.secondary),
        shape = CircleShape,
        contentDescription = "Recipe Image"
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
        LoadImage(
            imageUrl = user.profilePhotoUrl,
            contentDescription = "User Image",
            shape = CircleShape,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary, CircleShape)
                .size(40.dp)
                .clip(CircleShape)
        )

        Column(
            Modifier.padding(start = 10.dp)
        ) {
            Text(text = "${user.name} ${user.lastName}", fontWeight = FontWeight.Bold)
            Text(text = user.email, style = MaterialTheme.typography.labelMedium)
        }
    }
}