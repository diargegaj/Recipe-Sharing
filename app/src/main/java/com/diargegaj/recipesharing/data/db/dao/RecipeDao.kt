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
    fun getRecipeWithDetails(recipeId: String): Flow<RecipeWithDetails?>

    @Query("SELECT * FROM recipes WHERE (title LIKE :query OR description LIKE :query) AND userId LIKE :userId")
    fun getAllRecipes(query: String = "%%", userId: String = "%%"): Flow<List<RecipeWithDetails>>

    @Transaction
    suspend fun insertRecipeWithIngredients(
        recipe: RecipeEntity,
        ingredients: List<IngredientEntity>
    ) {
        insertRecipe(recipe)
        insertIngredients(ingredients)
    }

    @Query("DELETE FROM recipes WHERE id = :recipeId ")
    suspend fun deleteRecipe(recipeId: String)

}