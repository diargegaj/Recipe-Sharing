package com.diargegaj.recipesharing.domain.repository

import com.diargegaj.recipesharing.domain.models.recipe.RecipeModel
import com.diargegaj.recipesharing.domain.models.recipe.recipeDetails.FeedbackModel
import com.diargegaj.recipesharing.domain.models.recipe.recipeDetails.RecipeDetailsModel
import com.diargegaj.recipesharing.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {

    suspend fun storeRecipe(recipeModel: RecipeModel): Resource<Unit>

    fun observeAllRecipes(query: String, userId: String): Flow<Resource<List<RecipeModel>>>

    suspend fun updateRecipesFromFirestore(): Resource<Unit>

    fun getRecipeDetailsWithId(recipeId: String): Flow<Resource<RecipeDetailsModel>>

    suspend fun addFeedback(feedbackModel: FeedbackModel): Resource<Unit>

    suspend fun updateFeedbacksPerRecipe(recipeId: String): Resource<Unit>

    fun getFeedbacksPerRecipe(recipeId: String): Flow<Resource<List<FeedbackModel>>>

    suspend fun updateRecipe(recipe: RecipeDetailsModel): Resource<Unit>

    suspend fun deleteRecipe(recipeId: String): Resource<Unit>

}
