package com.diargegaj.recipesharing.domain.repository

import com.diargegaj.recipesharing.domain.models.RecipeModel
import com.diargegaj.recipesharing.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {

    suspend fun storeRecipe(recipeModel: RecipeModel): Resource<Unit>

    fun observeAllRecipes(): Flow<Resource<List<RecipeModel>>>

    suspend fun updateRecipesFromFirestore(): Resource<Unit>

}
