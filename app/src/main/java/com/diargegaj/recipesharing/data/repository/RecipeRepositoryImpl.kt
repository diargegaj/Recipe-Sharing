package com.diargegaj.recipesharing.data.repository

import DBCollection
import android.content.res.Resources.NotFoundException
import com.diargegaj.recipesharing.data.db.dao.FeedbackDao
import com.diargegaj.recipesharing.data.db.dao.RecipeDao
import com.diargegaj.recipesharing.data.mappers.mapToDto
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
import com.diargegaj.recipesharing.domain.repository.RecipeRepository
import com.diargegaj.recipesharing.domain.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.roundToInt

class RecipeRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val recipeDao: RecipeDao,
    private val feedbackDao: FeedbackDao
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

    override fun observeAllRecipes(
        query: String,
        userId: String
    ): Flow<Resource<List<RecipeModel>>> {
        return recipeDao.getAllRecipes(query = "%$query%", userId = "%$userId%")
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

    override fun getRecipeDetailsWithId(recipeId: String): Flow<Resource<RecipeDetailsModel>> {
        return recipeDao.getRecipeWithDetails(recipeId)
            .map { recipe ->
                if (recipe == null) {
                    Resource.Error(NotFoundException("No recipes found"))
                } else {
                    Resource.Success(recipe.mapToRecipeDetailsModel())
                }
            }
            .catch { e ->
                emit(Resource.Error(Exception(e.message)))
            }
    }

    override suspend fun addFeedback(
        feedbackModel: FeedbackModel
    ): Resource<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val feedbackDto = feedbackModel.mapToDto()
                firestore
                    .collection(DBCollection.Feedbacks.collectionName)
                    .add(feedbackDto)
                    .await()

                Resource.Success(Unit)
            } catch (e: Exception) {
                Resource.Error(e)
            }
        }

    override suspend fun updateFeedbacksPerRecipe(recipeId: String) =
        withContext(Dispatchers.IO) {
            try {
                val feedbacksRef = firestore.collection(DBCollection.Feedbacks.collectionName)
                val query = feedbacksRef.whereEqualTo(
                    "recipeId",
                    recipeId
                )

                val response = query.get().await()

                val ratings: MutableList<Int> = mutableListOf()

                val feedbacksDto: List<FeedbackDto> = response.documents.mapNotNull { document ->
                    val feedbackDto = document.toObject(FeedbackDto::class.java)
                    feedbackDto?.feedbackId = document.id
                    if (feedbackDto?.rating != null) ratings.add(feedbackDto.rating)
                    feedbackDto
                }

                if (feedbacksDto.isEmpty()) return@withContext Resource.Error(NotFoundException("Data Not Found."))

                feedbacksDto.forEach { dto ->
                    val feedbackEntity = dto.mapToEntity()
                    feedbackDao.insertFeedback(feedbackEntity)
                }

                val recipeWithDetails = recipeDao.getRecipeWithDetails(recipeId).first()
                val recipe = recipeWithDetails?.recipe
                val ingredients = recipeWithDetails?.ingredients
                recipe?.averageRating = ratings.average().roundToInt()

                if (recipe != null && ingredients != null) {
                    recipeDao.insertRecipeWithIngredients(recipe, ingredients)
                }

                Resource.Success(Unit)
            } catch (e: Exception) {
                Resource.Error(e)
            }
        }

    override fun getFeedbacksPerRecipe(recipeId: String): Flow<Resource<List<FeedbackModel>>> {
        return feedbackDao.getFeedbackWithUser(recipeId = recipeId)
            .map { feedbacks ->
                if (feedbacks.isEmpty()) {
                    Resource.Error(NotFoundException("No recipes found"))
                } else {
                    Resource.Success(feedbacks.map { it.mapToFeedbackModel() })
                }
            }
            .catch { e ->
                emit(Resource.Error(Exception(e.message)))
            }
    }
}