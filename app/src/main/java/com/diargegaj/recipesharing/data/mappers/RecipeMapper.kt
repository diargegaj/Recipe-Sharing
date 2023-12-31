package com.diargegaj.recipesharing.data.mappers

import com.diargegaj.recipesharing.data.db.entities.recipes.IngredientEntity
import com.diargegaj.recipesharing.data.db.entities.recipes.RecipeEntity
import com.diargegaj.recipesharing.data.db.entities.recipes.RecipeWithDetails
import com.diargegaj.recipesharing.data.models.RecipeDto
import com.diargegaj.recipesharing.domain.models.recipe.RecipeModel
import com.diargegaj.recipesharing.domain.models.recipe.recipeDetails.RecipeDetailsModel

fun RecipeModel.mapToDto(): RecipeDto {
    return RecipeDto(
        title = this.title,
        description = this.description,
        ingredients = this.ingredients,
        imageUrl = this.imageUrl,
        userId = this.userId
    )
}

fun RecipeWithDetails.mapToRecipeModel(): RecipeModel {
    return RecipeModel(
        recipeId = this.recipe.id,
        title = this.recipe.title,
        description = this.recipe.description,
        ingredients = this.ingredients.map { it.mapToIngredient() },
        imageUrl = this.recipe.imageUrl,
        userId = this.recipe.userId,
        userModel = this.user?.mapToUserModel()
    )
}

fun IngredientEntity.mapToIngredient() = this.ingredient

fun RecipeDto.toRecipeEntities(): Pair<RecipeEntity, List<IngredientEntity>> {
    val recipeEntity = RecipeEntity(
        id = this.recipeId,
        title = this.title,
        description = this.description,
        imageUrl = this.imageUrl,
        userId = this.userId,
        averageRating = 0
    )
    val ingredientEntities = this.ingredients.map {
        IngredientEntity(
            recipeId = this.recipeId,
            ingredient = it
        )
    }
    return recipeEntity to ingredientEntities
}

fun RecipeWithDetails.mapToRecipeDetailsModel(): RecipeDetailsModel {
    return RecipeDetailsModel(
        recipeId = this.recipe.id,
        title = this.recipe.title,
        description = this.recipe.description,
        ingredients = this.ingredients.map { it.mapToIngredient() },
        imageUrl = this.recipe.imageUrl,
        userModel = this.user?.mapToUserModel(),
        feedbacks = listOf(),
        averageRating = this.recipe.averageRating
    )
}

fun RecipeEntity.mapToRecipeModel(): RecipeModel {
    return RecipeModel(
        recipeId = this.id,
        title = this.title,
        description = this.description,
        ingredients = listOf(),
        imageUrl = this.imageUrl,
        userId = this.userId
    )
}

fun RecipeDetailsModel.mapToDto(): RecipeDto {
    return RecipeDto(
        recipeId = this.recipeId,
        title = this.title,
        description = this.description,
        ingredients = this.ingredients,
        imageUrl = this.imageUrl,
        userId = this.userModel?.userUUID ?: ""
    )
}