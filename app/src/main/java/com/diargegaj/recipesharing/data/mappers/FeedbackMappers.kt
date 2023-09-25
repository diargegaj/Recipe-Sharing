package com.diargegaj.recipesharing.data.mappers

import com.diargegaj.recipesharing.data.db.entities.feedback.FeedbackEntity
import com.diargegaj.recipesharing.data.db.entities.feedback.FeedbackWithUser
import com.diargegaj.recipesharing.data.models.recipe.FeedbackDto
import com.diargegaj.recipesharing.domain.models.recipe.recipeDetails.FeedbackModel

fun FeedbackModel.mapToDto(): FeedbackDto {
    return FeedbackDto(
        rating = this.rating,
        feedback = this.feedback,
        recipeId = this.recipeId,
        userId = this.userId
    )
}

fun FeedbackDto.mapToEntity(): FeedbackEntity {
    return FeedbackEntity(
        id = this.feedbackId,
        rating = this.rating,
        feedback = this.feedback,
        recipeId = this.recipeId,
        userId = this.userId
    )
}

fun FeedbackWithUser.mapToFeedbackModel(): FeedbackModel {
    return FeedbackModel(
        this.feedback.id,
        rating = this.feedback.rating,
        feedback = this.feedback.feedback,
        userId = this.feedback.userId,
        userModel = this.user?.mapToUserModel()
    )
}