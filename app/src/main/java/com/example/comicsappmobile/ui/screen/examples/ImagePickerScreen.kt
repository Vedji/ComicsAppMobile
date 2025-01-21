package com.example.comicsappmobile.ui.screen.examples

import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.example.comicsappmobile.ui.theme.ComicsAppMobileTheme

@Composable
fun ImagePickerScreen() {
    val context = LocalContext.current
    var selectedImages by remember { mutableStateOf<List<Uri>>(emptyList()) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        selectedImages = uris
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Button(onClick = {
            imagePickerLauncher.launch("image/*")  // Выбор только изображений
        }) {
            androidx.compose.foundation.text.BasicText("Выбрать изображения")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(selectedImages) { uri ->
                ImageItem(uri = uri)
            }
        }
    }
}

@Composable
fun ImageItem(uri: Uri) {
    Column(modifier = Modifier.padding(8.dp)) {
        AsyncImage(
            model = uri,
            contentDescription = "Выбранное изображение",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
    }
}

// Получение имени файла из Uri
fun getFileName(context: android.content.Context, uri: Uri): String {
    var fileName = "Неизвестный файл"
    context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (nameIndex != -1 && cursor.moveToFirst()) {
            fileName = cursor.getString(nameIndex)
        }
    }
    return fileName
}


@Preview
@Composable
fun FilePickerScreenPreview(){
    ComicsAppMobileTheme(3) {
        ImagePickerScreen()
    }
}