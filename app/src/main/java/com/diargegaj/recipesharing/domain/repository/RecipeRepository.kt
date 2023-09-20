package com.diargegaj.recipesharing.domain.repository

import com.diargegaj.recipesharing.domain.models.RecipeModel
import com.diargegaj.recipesharing.domain.utils.Resource

interface RecipeRepository {

    suspend fun storeRecipe(recipeModel: RecipeModel): Resource<Unit>

}
