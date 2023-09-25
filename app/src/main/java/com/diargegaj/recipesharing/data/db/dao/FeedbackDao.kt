package com.diargegaj.recipesharing.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.diargegaj.recipesharing.data.db.entities.feedback.FeedbackEntity
import com.diargegaj.recipesharing.data.db.entities.feedback.FeedbackWithUser
import kotlinx.coroutines.flow.Flow

@Dao
interface FeedbackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeedback(recipe: FeedbackEntity): Long

    @Transaction
    @Query("SELECT * FROM feedbacks WHERE feedbacks.recipeId = :recipeId")
    fun getFeedbackWithUser(recipeId: String): Flow<List<FeedbackWithUser>>


}