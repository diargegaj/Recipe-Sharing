package com.diargegaj.recipesharing.data.mappers

import com.diargegaj.recipesharing.data.db.entities.user.UserEntity
import com.diargegaj.recipesharing.data.db.entities.user.UserWithRecipes
import com.diargegaj.recipesharing.data.models.UserDto
import com.diargegaj.recipesharing.domain.models.UserModel

fun UserModel.mapToDto(): UserDto {
    return UserDto(
        userUUID = this.userUUID,
        name = this.name,
        lastName = this.lastName,
        email = this.email,
        profilePhotoUrl = this.profilePhotoUrl
    )
}

fun UserDto.mapToEntity(): UserEntity {
    return UserEntity(
        id = this.userUUID,
        name = this.name,
        lastName = this.lastName,
        email = this.email,
        profilePhotoUrl = this.profilePhotoUrl
    )
}

fun UserDto.mapToDomain(): UserModel {
    return UserModel(
        userUUID = userUUID,
        name = name,
        lastName = lastName,
        email = email,
        profilePhotoUrl = profilePhotoUrl
    )
}

fun UserEntity.mapToUserModel(): UserModel {
    return UserModel(
        userUUID = this.id,
        name = this.name,
        lastName = this.lastName,
        email = this.email,
        profilePhotoUrl = this.profilePhotoUrl
    )
}

fun UserWithRecipes.mapToUserModel(): UserModel {
    return UserModel(
        userUUID = this.user.id,
        name = this.user.name,
        lastName = this.user.lastName,
        email = this.user.email,
        profilePhotoUrl = this.user.profilePhotoUrl,
        userRecipes = this.recipes?.map { it.mapToRecipeModel() } ?: listOf()
    )
}