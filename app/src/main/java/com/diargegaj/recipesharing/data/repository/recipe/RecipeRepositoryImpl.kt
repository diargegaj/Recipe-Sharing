package com.diargegaj.recipesharing.data.repository.recipe

import com.diargegaj.recipesharing.data.datasource.database.DatabaseRecipeDataSource
import com.diargegaj.recipesharing.data.datasource.firestore.FirestoreRecipeDataSource
import com.diargegaj.recipesharing.data.mappers.mapToDto
import com.diargegaj.recipesharing.domain.models.recipe.RecipeModel
import com.diargegaj.recipesharing.domain.models.recipe.recipeDetails.FeedbackModel
import com.diargegaj.recipesharing.domain.models.recipe.recipeDetails.RecipeDetailsModel
import com.diargegaj.recipesharing.domain.repository.RecipeRepository
import com.diargegaj.recipesharing.domain.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val firestoreDataSource: FirestoreRecipeDataSource,
    private val databaseDataSource: DatabaseRecipeDataSource
) : RecipeRepository {

    override suspend fun storeRecipe(recipeModel: RecipeModel): Resource<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val recipeDto = recipeModel.mapToDto()
                firestoreDataSource.storeRecipeToFirestore(recipeDto)
                Resource.Success(Unit)
            } catch (e: Exception) {
                Resource.Error(e)
            }
        }

    override fun observeAllRecipes(
        query: String,
        userId: String
    ): Flow<Resource<List<RecipeModel>>> {
        return databaseDataSource.observeAllRecipes(query, userId)
            .map { recipes ->
                if (recipes.isEmpty()) {
                    Resource.Error(Exception("No recipes found"))
                } else {
                    Resource.Success(recipes)
                }
            }
            .catch { e ->
                Resource.Error(Exception(e.message))
            }
    }

    override suspend fun updateRecipesFromFirestore(): Resource<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val recipeDtos = firestoreDataSource.fetchAllRecipesFromFirestore()
                if (recipeDtos.isEmpty()) Resource.Error(Exception("Data Not Found"))
                else {
                    databaseDataSource.insertRecipesWithIngredientsFromDtos(recipeDtos)
                    Resource.Success(Unit)
                }
            } catch (e: Exception) {
                Resource.Error(e)
            }
        }

    override fun getRecipeDetailsWithId(recipeId: String): Flow<Resource<RecipeDetailsModel>> {
        return databaseDataSource.observeRecipeDetails(recipeId)
            .map { recipeDetails ->
                recipeDetails?.let {
                    Resource.Success(it)
                } ?: Resource.Error(Exception("No recipe details found"))
            }
            .catch { e ->
                Resource.Error(Exception(e.message))
            }
    }

    override suspend fun addFeedback(feedbackModel: FeedbackModel): Resource<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val feedbackDto = feedbackModel.mapToDto()
                firestoreDataSource.storeFeedbackToFirestore(feedbackDto)
                Resource.Success(Unit)
            } catch (e: Exception) {
                Resource.Error(e)
            }
        }

    override suspend fun updateFeedbacksPerRecipe(recipeId: String): Resource<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val feedbacksDto =
                    firestoreDataSource.fetchFeedbacksForRecipeFromFirestore(recipeId)
                if (feedbacksDto.isEmpty()) Resource.Error(Exception("Data Not Found"))
                else {
                    val ratings = feedbacksDto.map { it.rating }
                    val averageRating = ratings.average().toInt()
                    databaseDataSource.insertFeedbacksFromDtos(feedbacksDto)
                    databaseDataSource.updateRecipeRating(recipeId, averageRating)
                    Resource.Success(Unit)
                }
            } catch (e: Exception) {
                Resource.Error(e)
            }
        }

    override fun getFeedbacksPerRecipe(recipeId: String): Flow<Resource<List<FeedbackModel>>> {
        return databaseDataSource.observeFeedbacksForRecipe(recipeId)
            .map { feedbacks ->
                if (feedbacks.isEmpty()) {
                    Resource.Error(Exception("No feedback found"))
                } else {
                    Resource.Success(feedbacks)
                }
            }
            .catch { e ->
                Resource.Error(Exception(e.message))
            }
    }
}
