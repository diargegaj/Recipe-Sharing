package com.diargegaj.recipesharing.data.utils

import com.diargegaj.recipesharing.domain.utils.Resource

suspend fun <T> safeCall(call: suspend () -> Resource<T>): Resource<T> {
    return try {
        call()
    } catch (e: Exception) {
        Resource.Error(e)
    }
}