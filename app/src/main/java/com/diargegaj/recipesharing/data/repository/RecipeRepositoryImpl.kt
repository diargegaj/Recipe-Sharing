package com.diargegaj.recipesharing.data.repository

import com.diargegaj.recipesharing.data.mappers.mapToDto
import com.diargegaj.recipesharing.data.utils.DBCollectionUtils.COLLECTIONS.Recipe
import com.diargegaj.recipesharing.domain.models.RecipeModel
import com.diargegaj.recipesharing.domain.repository.RecipeRepository
import com.diargegaj.recipesharing.domain.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : RecipeRepository {

    override suspend fun storeRecipe(recipeModel: RecipeModel): Resource<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val recipeInfo = recipeModel.mapToDto()
                firestore.collection(Recipe.COLLECTION_NAME)
                    .add(recipeInfo).await()

                Resource.Success(Unit)
            } catch (e: Exception) {
                Resource.Error(e)
            }
        }
}