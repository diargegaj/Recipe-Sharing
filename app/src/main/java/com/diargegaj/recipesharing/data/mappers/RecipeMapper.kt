package com.diargegaj.recipesharing.data.mappers

import com.diargegaj.recipesharing.data.models.RecipeDto
import com.diargegaj.recipesharing.domain.models.RecipeModel

fun RecipeModel.mapToDto(): RecipeDto {
    return RecipeDto(
        title = this.title,
        description = this.description,
        ingredients = this.ingredients,
        imageUrl = this.imageUrl,
        userId = this.userId
    )
}