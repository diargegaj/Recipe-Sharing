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
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.diargegaj.recipesharing.R
import java.io.InputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultAppBar(
    title: String,
    navigationIcon: ImageVector,
    onNavigationClick: () -> Unit,
    actions: @Composable() (RowScope.() -> Unit) = {},
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    CenterAlignedTopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(onClick = onNavigationClick) {
                Icon(imageVector = navigationIcon, contentDescription = "Navigation icon")
            }
        },
        actions = actions,
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAppBar(
    searchQuery: String,
    onSearchChanged: (String) -> Unit,
    onSearchAction: () -> Unit,
    navigationIcon: ImageVector,
    onNavigationClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    CenterAlignedTopAppBar(
        title = {
            TextField(
                value = searchQuery,
                onValueChange = onSearchChanged,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(text = stringResource(id = R.string.search)) },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    onSearchAction()
                }),
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent)
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavigationClick) {
                Icon(imageVector = navigationIcon, contentDescription = "Navigation icon")
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

@Composable
fun LoadImage(
    imageUrl: String,
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    contentDescription: String? = null,
    placeholder: Int = R.drawable.ic_downloading,
    errorImage: Int = R.drawable.ic_downloading
) {
    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .placeholder(placeholder)
            .error(errorImage)
            .build()
    )

    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier.clip(shape),
        contentScale = ContentScale.Crop
    )
}