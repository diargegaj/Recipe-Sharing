package com.diargegaj.recipesharing.data.mappers

import com.diargegaj.recipesharing.data.db.entities.recipes.IngredientEntity
import com.diargegaj.recipesharing.data.db.entities.recipes.RecipeEntity
import com.diargegaj.recipesharing.data.db.entities.recipes.RecipeWithDetails
import com.diargegaj.recipesharing.data.models.RecipeDto
import com.diargegaj.recipesharing.domain.models.RecipeForViewModel
import com.diargegaj.recipesharing.domain.models.RecipeModel
import com.diargegaj.recipesharing.domain.models.RecipeUIModel
import com.diargegaj.recipesharing.domain.models.UserModel

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
        userId = this.recipe.userId
    )
}

fun RecipeWithDetails.mapToRecipeForViewModel(): RecipeForViewModel {
    return RecipeForViewModel(
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
        userId = this.userId
    )
    val ingredientEntities = this.ingredients.map {
        IngredientEntity(
            recipeId = this.recipeId,
            ingredient = it
        )
    }
    return recipeEntity to ingredientEntities
}

fun RecipeModel.toUiModel(userInfo: UserModel): RecipeUIModel {
    return RecipeUIModel(
        recipeId = this.recipeId,
        title = this.title,
        description = this.description,
        imageUrl = this.imageUrl,
        userModel = userInfo
    )
}