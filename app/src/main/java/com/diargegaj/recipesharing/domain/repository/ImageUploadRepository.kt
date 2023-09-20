package com.diargegaj.recipesharing.domain.repository

import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import com.diargegaj.recipesharing.domain.utils.Resource

interface ImageUploadRepository {

    suspend fun storeImage(imageBitmap: ImageBitmap, path: String): Resource<Uri>

}
