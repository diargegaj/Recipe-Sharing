package com.diargegaj.recipesharing.domain.models

data class UserModel(
    var userUUID: String = "",
    val name: String,
    val lastName: String,
    val email: String,
    val profilePhotoUrl: String = ""
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