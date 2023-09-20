package com.diargegaj.recipesharing.data.repository

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import com.diargegaj.recipesharing.domain.repository.ImageUploadRepository
import com.diargegaj.recipesharing.domain.utils.Resource
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class FirebaseImageUploadRepositoryImpl @Inject constructor(
    private val firebaseStorage: FirebaseStorage
) : ImageUploadRepository {

    override suspend fun storeImage(imageBitmap: ImageBitmap, path: String): Resource<Uri> =
        withContext(Dispatchers.IO) {
            val baos = ByteArrayOutputStream()
            imageBitmap.asAndroidBitmap().compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            val imageRef = firebaseStorage.reference.child(path)

            try {
                imageRef.putBytes(data).await()
                val downloadUrl = imageRef.downloadUrl.await()
                Resource.Success(downloadUrl)
            } catch (e: Exception) {
                Resource.Error(e)
            }
        }
}