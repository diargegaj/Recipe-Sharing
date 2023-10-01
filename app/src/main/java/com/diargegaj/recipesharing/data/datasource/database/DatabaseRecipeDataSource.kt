package com.diargegaj.recipesharing.data.datasource.database

import com.diargegaj.recipesharing.data.db.dao.FeedbackDao
import com.diargegaj.recipesharing.data.db.dao.RecipeDao
import com.diargegaj.recipesharing.data.mappers.mapToEntity
import com.diargegaj.recipesharing.data.mappers.mapToFeedbackModel
import com.diargegaj.recipesharing.data.mappers.mapToRecipeDetailsModel
import com.diargegaj.recipesharing.data.mappers.mapToRecipeModel
import com.diargegaj.recipesharing.data.mappers.toRecipeEntities
import com.diargegaj.recipesharing.data.models.RecipeDto
import com.diargegaj.recipesharing.data.models.recipe.FeedbackDto
import com.diargegaj.recipesharing.domain.models.recipe.RecipeModel
import com.diargegaj.recipesharing.domain.models.recipe.recipeDetails.FeedbackModel
import com.diargegaj.recipesharing.domain.models.recipe.recipeDetails.RecipeDetailsModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DatabaseRecipeDataSource @Inject constructor(
    private val recipeDao: RecipeDao,
    private val feedbackDao: FeedbackDao
) {

    fun observeAllRecipes(query: String, userId: String): Flow<List<RecipeModel>> {
        return recipeDao.getAllRecipes(query = "%$query%", userId = "%$userId%")
            .map { recipes ->
                recipes.map {
                    it.mapToRecipeModel()
                }
            }
    }

    suspend fun insertRecipesWithIngredientsFromDtos(recipeDtos: List<RecipeDto>) {
        recipeDtos.forEach { dto ->
            val (recipeEntity, ingredientEntities) = dto.toRecipeEntities()
            recipeDao.insertRecipeWithIngredients(recipeEntity, ingredientEntities)
        }
    }

    fun observeRecipeDetails(recipeId: String): Flow<RecipeDetailsModel?> {
        return recipeDao.getRecipeWithDetails(recipeId).map {
            it?.mapToRecipeDetailsModel()
        }
    }

    suspend fun updateRecipeRating(recipeId: String, averageRating: Int) {
        val recipeWithDetails = recipeDao.getRecipeWithDetails(recipeId).first()
        val recipe = recipeWithDetails?.recipe
        val ingredients = recipeWithDetails?.ingredients
        recipe?.averageRating = averageRating
        if (recipe != null && ingredients != null) {
            recipeDao.insertRecipeWithIngredients(recipe, ingredients)
        }
    }

    fun observeFeedbacksForRecipe(recipeId: String): Flow<List<FeedbackModel>> {
        return feedbackDao.getFeedbackWithUser(recipeId = recipeId).map { feedbacks ->
            feedbacks.map {
                it.mapToFeedbackModel()
            }
        }
    }

    suspend fun insertFeedbacksFromDtos(feedbacksDto: List<FeedbackDto>) {
        feedbacksDto.forEach { dto ->
            val feedbackEntity = dto.mapToEntity()
            feedbackDao.insertFeedback(feedbackEntity)
        }
    }

    suspend fun deleteRecipe(recipeId: String) {
        recipeDao.deleteRecipe(recipeId)
    }
}
