package com.diargegaj.recipesharing.domain.models

import com.diargegaj.recipesharing.domain.models.recipe.RecipeModel

data class UserModel(
    var userUUID: String = "",
    val name: String,
    val lastName: String,
    val email: String,
    val profilePhotoUrl: String = "",
    val userRecipes: List<RecipeModel> = listOf(),
    val followersCount: Int = 0,
    val followingCount: Int = 0
) {
    fun getUserFullName() = "$name $lastName"
}

fun emptyUserModel(): UserModel {
    return UserModel(
        userUUID = "",
        name = "",
        lastName = "",
        email = "",
        profilePhotoUrl = ""
    )
}