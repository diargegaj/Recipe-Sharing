package com.diargegaj.recipesharing.domain.enums

enum class ImagePath(private val pathName: String) {
    RECIPE("recipe_images"),
    PROFILE("profile_images");

    fun getPath(id: String): String {
        return "$pathName/$id.jpg"
    }

}