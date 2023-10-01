package com.diargegaj.recipesharing.presentation.screens.home.recipes.recipeDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.diargegaj.recipesharing.R
import com.diargegaj.recipesharing.domain.models.emptyUserModel
import com.diargegaj.recipesharing.domain.models.recipe.recipeDetails.FeedbackModel

@Composable
fun RecipeFeedbackSection(
    currentRating: Int,
    onNewRating: (rating: Int) -> Unit,
    feedbackText: String,
    onNewFeedbackValue: (feedbackValue: String) -> Unit,
    onFeedbackSubmit: () -> Unit,
    usersFeedbacks: List<FeedbackModel>,
    currentUserFeedbackModel: FeedbackModel? = null,
    onFeedbackClicked: (feedbackModel: FeedbackModel) -> Unit
) {

    Column {
        if (currentUserFeedbackModel != null) {
            Text(text = stringResource(id = R.string.your_feedback))
            Spacer(modifier = Modifier.height(16.dp))
            FeedbackCard(currentUserFeedbackModel, onFeedbackClicked)
        } else {
            Text(text = stringResource(id = R.string.rate_recipe))
            Spacer(modifier = Modifier.height(16.dp))
            AddFeedback(
                currentRating = currentRating,
                onNewRating = onNewRating,
                feedbackText = feedbackText,
                onNewFeedbackValue = onNewFeedbackValue,
                onFeedbackSubmit = onFeedbackSubmit
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = "User Reviews",
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.titleMedium
    )

    if (usersFeedbacks.isEmpty()) {
        Text(text = stringResource(id = R.string.no_feedbacks))
    } else {
        FeedbackList(feedbacks = usersFeedbacks, onFeedbackClicked)
    }

}

@Composable
fun AddFeedback(
    currentRating: Int,
    onNewRating: (rating: Int) -> Unit,
    feedbackText: String,
    onNewFeedbackValue: (feedbackValue: String) -> Unit,
    onFeedbackSubmit: () -> Unit
) {
    RatingInput(
        currentRating = currentRating
    ) { newRating ->
        onNewRating(newRating)
    }

    FeedbackInput(
        feedbackText = feedbackText,
        onNewValue = { newValue ->
            onNewFeedbackValue(newValue)
        },
        onFeedbackSubmit = {
            onFeedbackSubmit()
        }
    )
}

@Composable
fun FeedbackList(
    feedbacks: List<FeedbackModel>,
    onFeedbackClicked: (feedbackModel: FeedbackModel) -> Unit
) {
    feedbacks.forEach { feedback ->
        FeedbackCard(
            feedback = feedback,
            onFeedbackClicked = onFeedbackClicked
        )
    }
}

@Composable
fun FeedbackCard(
    feedback: FeedbackModel,
    onFeedbackClicked: (feedbackModel: FeedbackModel) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(
                color = Color.Gray, shape = MaterialTheme.shapes.medium
            )
            .padding(16.dp)
            .clickable {
                onFeedbackClicked(feedback)
            }
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = feedback.userModel?.getUserFullName() ?: "",
                    style = MaterialTheme.typography.titleMedium
                )

                Repeat(times = feedback.rating) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color.Yellow,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = feedback.feedback, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun Repeat(times: Int, content: @Composable () -> Unit) {
    repeat(times) {
        content()
    }
}


@Composable
fun RatingInput(
    maxStars: Int = 5, currentRating: Int, onRatingSelected: (Int) -> Unit = {}
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
    ) {
        (1..maxStars).forEach { index ->
            Icon(imageVector = if (index <= currentRating) Icons.Default.Star else Icons.TwoTone.Star,
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        onRatingSelected(index)
                    })
        }
    }
}

@Composable
fun FeedbackInput(
    feedbackText: String,
    onNewValue: (feedbackValue: String) -> Unit,
    onFeedbackSubmit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        BasicTextField(
            value = feedbackText,
            onValueChange = {
                onNewValue(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Gray.copy(alpha = 0.1f))
                .padding(12.dp),
            textStyle = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                onFeedbackSubmit()
            }, modifier = Modifier.align(Alignment.End)
        ) {
            Text("Post")
        }
    }
}

@Composable
fun FeedbackInfo(
    feedbackModel: FeedbackModel,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(text = "Feedback Details")
        },
        text = {
            Column {
                UserInfo(feedbackModel.userModel ?: emptyUserModel())

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Repeat(times = feedbackModel.rating) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color.Yellow,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = feedbackModel.feedback,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Close")
            }
        }
    )
}

@Composable
fun AverageRecipeRating(
    averageRating: Int,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Repeat(times = averageRating) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = Color.Yellow,
                modifier = Modifier.size(18.dp)
            )
        }

        Text(
            text = "$averageRating",
            style = MaterialTheme.typography.labelMedium
        )
    }
}