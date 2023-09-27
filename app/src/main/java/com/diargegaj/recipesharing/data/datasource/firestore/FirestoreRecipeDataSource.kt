package com.diargegaj.recipesharing.data.datasource.firestore

import com.diargegaj.recipesharing.data.models.RecipeDto
import com.diargegaj.recipesharing.data.models.recipe.FeedbackDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreRecipeDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun storeRecipeToFirestore(recipeInfo: RecipeDto) {
        firestore.collection(DBCollection.Recipe.collectionName)
            .add(recipeInfo).await()
    }

    suspend fun fetchAllRecipesFromFirestore(): List<RecipeDto> {
        val response = firestore.collection(DBCollection.Recipe.collectionName)
            .get()
            .await()
        return response.documents.mapNotNull { document ->
            val recipeDto = document.toObject(RecipeDto::class.java)
            recipeDto?.recipeId = document.id
            recipeDto
        }
    }

    suspend fun storeFeedbackToFirestore(feedbackDto: FeedbackDto) {
        firestore.collection(DBCollection.Feedbacks.collectionName)
            .add(feedbackDto).await()
    }

    suspend fun fetchFeedbacksForRecipeFromFirestore(recipeId: String): List<FeedbackDto> {
        val feedbacksRef = firestore.collection(DBCollection.Feedbacks.collectionName)
        val query = feedbacksRef.whereEqualTo("recipeId", recipeId)
        val response = query.get().await()
        return response.documents.mapNotNull { document ->
            val feedbackDto = document.toObject(FeedbackDto::class.java)
            feedbackDto?.feedbackId = document.id
            feedbackDto
        }
    }
}
