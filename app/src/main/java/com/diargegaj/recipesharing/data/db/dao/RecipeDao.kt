package com.diargegaj.recipesharing.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.diargegaj.recipesharing.data.db.entities.recipes.IngredientEntity
import com.diargegaj.recipesharing.data.db.entities.recipes.RecipeEntity
import com.diargegaj.recipesharing.data.db.entities.recipes.RecipeWithDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredients(ingredients: List<IngredientEntity>)

    @Transaction
    @Query("SELECT * FROM recipes WHERE id = :recipeId")
    fun getRecipeWithIngredients(recipeId: String): Flow<RecipeWithIngredients>

    @Query("SELECT * FROM recipes")
    fun getAllRecipes(): Flow<List<RecipeWithDetails>>

    @Transaction
    suspend fun insertRecipeWithIngredients(
        recipe: RecipeEntity,
        ingredients: List<IngredientEntity>
    ) {
        insertRecipe(recipe)
        insertIngredients(ingredients)
    }
}