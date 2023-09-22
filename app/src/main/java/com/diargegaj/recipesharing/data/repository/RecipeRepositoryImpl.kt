package com.diargegaj.recipesharing.data.repository

import DBCollection
import android.content.res.Resources.NotFoundException
import com.diargegaj.recipesharing.data.db.dao.RecipeDao
import com.diargegaj.recipesharing.data.mappers.mapToDto
import com.diargegaj.recipesharing.data.mappers.mapToRecipeModel
import com.diargegaj.recipesharing.data.mappers.toRecipeEntities
import com.diargegaj.recipesharing.data.models.RecipeDto
import com.diargegaj.recipesharing.domain.models.RecipeModel
import com.diargegaj.recipesharing.domain.repository.RecipeRepository
import com.diargegaj.recipesharing.domain.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val recipeDao: RecipeDao
) : RecipeRepository {

    override suspend fun storeRecipe(recipeModel: RecipeModel): Resource<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val recipeInfo = recipeModel.mapToDto()
                firestore.collection(DBCollection.Recipe.collectionName)
                    .add(recipeInfo).await()

                Resource.Success(Unit)
            } catch (e: Exception) {
                Resource.Error(e)
            }
        }

    override fun observeAllRecipes(): Flow<Resource<List<RecipeModel>>> {
        return recipeDao.getAllRecipes()
            .map { recipes ->
                if (recipes.isEmpty()) {
                    Resource.Error(NotFoundException("No recipes found"))
                } else {
                    Resource.Success(recipes.map { it.mapToRecipeModel() })
                }
            }
            .catch { e ->
                emit(Resource.Error(Exception(e.message)))
            }

    }

    override suspend fun updateRecipesFromFirestore(): Resource<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val response = firestore.collection(DBCollection.Recipe.collectionName)
                    .get()
                    .await()

                val recipeDtos: List<RecipeDto> = response.documents.mapNotNull { document ->
                    val recipeDto = document.toObject(RecipeDto::class.java)
                    recipeDto?.recipeId = document.id
                    recipeDto
                }

                if (recipeDtos.isEmpty()) return@withContext Resource.Error(NotFoundException("Data Not Found."))

                recipeDtos.forEach { dto ->
                    val (recipeEntity, ingredientEntities) = dto.toRecipeEntities()
                    recipeDao.insertRecipeWithIngredients(recipeEntity, ingredientEntities)
                }

                Resource.Success(Unit)
            } catch (e: Exception) {
                Resource.Error(e)
            }
        }

    override fun getRecipeDetailsWithId(recipeId: String): Flow<Resource<RecipeModel>> {
        return recipeDao.getRecipeWithDetails(recipeId)
            .map { recipe ->
                if (recipe == null) {
                    Resource.Error(NotFoundException("No recipes found"))
                } else {
                    Resource.Success(recipe.mapToRecipeModel())
                }
            }
            .catch { e ->
                emit(Resource.Error(Exception(e.message)))
            }
    }
}