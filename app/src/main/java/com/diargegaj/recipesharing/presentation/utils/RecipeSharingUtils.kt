package com.diargegaj.recipesharing.presentation.utils

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import java.io.InputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    titleText: String,
    navigationIcon: ImageVector,
    navigationIconCLick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(text = titleText)
        },
        navigationIcon = {
            IconButton(onClick = { navigationIconCLick() }) {
                Icon(
                    imageVector = navigationIcon,
                    contentDescription = "Navigation icon"
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@Composable
fun ImagePicker(
    modifier: Modifier = Modifier,
    image: ImageBitmap?,
    onImagePicked: (ImageBitmap) -> Unit,
    editView: @Composable () -> Unit
) {
    val context = LocalContext.current
    val startForResult =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri = result.data?.data
                imageUri?.let {
                    val imageStream: InputStream? = context.contentResolver.openInputStream(it)
                    val selectedImage = BitmapFactory.decodeStream(imageStream)
                    onImagePicked(selectedImage.asImageBitmap())
                }
            }
        }

    Box(
        modifier = modifier
            .clickable {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startForResult.launch(intent)
            },
        contentAlignment = Alignment.Center
    ) {
        image?.let {
            Image(bitmap = it, contentDescription = "Image")
        } ?: editView()
    }
}