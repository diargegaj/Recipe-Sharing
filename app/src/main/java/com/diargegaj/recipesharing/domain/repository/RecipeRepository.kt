package com.diargegaj.recipesharing.domain.repository

import com.diargegaj.recipesharing.domain.models.RecipeModel
import com.diargegaj.recipesharing.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {

    suspend fun storeRecipe(recipeModel: RecipeModel): Resource<Unit>

    fun observeAllRecipes(query: String, userId: String): Flow<Resource<List<RecipeModel>>>

    suspend fun updateRecipesFromFirestore(): Resource<Unit>

    fun getRecipeDetailsWithId(recipeId: String): Flow<Resource<RecipeModel>>

}
