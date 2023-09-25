package com.diargegaj.recipesharing.data.db.entities.feedback

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "feedbacks")
data class FeedbackEntity(
    @PrimaryKey val id: String = "",
    val rating: Int = 5,
    val feedback: String = "",
    val recipeId: String = "",
    val userId: String = ""
)
