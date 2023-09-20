package com.diargegaj.recipesharing.data.models

data class UserDto(
    val userUUID: String = "",
    val name: String,
    val lastName: String,
    val email: String,
    val profilePhotoUrl: String
)