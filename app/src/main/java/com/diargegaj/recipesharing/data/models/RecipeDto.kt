package com.diargegaj.recipesharing.data.models

data class RecipeDto(
    val title: String,
    val description: String,
    val ingredients: List<String>,
    val imageUrl: String,
    val userId: String
)