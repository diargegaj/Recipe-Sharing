package com.diargegaj.recipesharing.domain.enums

enum class ImagePath(private val pathName: String) {
    RECIPE("recipe_images");

    fun getPath(id: String): String {
        return "$pathName/$id.jpg"
    }

}