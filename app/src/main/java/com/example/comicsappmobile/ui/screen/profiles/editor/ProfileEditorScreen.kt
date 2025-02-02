package com.example.comicsappmobile.ui.screen.profiles.editor

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.twotone.Check
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.comicsappmobile.BuildConfig
import com.example.comicsappmobile.data.mapper.UserMapper
import com.example.comicsappmobile.navigation.Screen
import com.example.comicsappmobile.ui.components.ThemedAlertDialog
import com.example.comicsappmobile.ui.components.ThemedInputField
import com.example.comicsappmobile.ui.presentation.viewmodel.ProfileEditorViewModel
import com.example.comicsappmobile.ui.presentation.viewmodel.UiState
import com.example.comicsappmobile.utils.Logger
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditorScreen(
    navController: NavController,
    profileEditorViewModel: ProfileEditorViewModel = koinViewModel()
) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val authUserState = profileEditorViewModel.userLogin.collectAsState()
    val authUserValue = profileEditorViewModel.globalState.authUser.collectAsState()

    var fetchedDescription: String by remember { mutableStateOf("") }
    var fetchedImageId: Int by remember { mutableIntStateOf(-1) }

    val inputDescription: MutableState<String> = remember { mutableStateOf(fetchedDescription) }
    val selectedImage: MutableState<Uri?> = remember { mutableStateOf<Uri?>(null) }
    var isSetDefaultImage = remember { mutableStateOf(false) }

    var isErrorOnUploadDialog: Boolean by remember { mutableStateOf(false) }
    var isUploadingDialog: Boolean by remember { mutableStateOf(false) }
    var isLoadingDialog: Boolean by remember { mutableStateOf(false) }

    LaunchedEffect(authUserState.value) {
        if (authUserState.value is UiState.Success){
            fetchedDescription = if (authUserState.value is UiState.Success) authUserState.value.data?.userDescription ?: "" else ""
            fetchedImageId = if (authUserState.value is UiState.Success) authUserState.value.data?.userTitleImage ?: -1 else -1
            inputDescription.value = fetchedDescription
            selectedImage.value = null
        }
    }

    if (isErrorOnUploadDialog){
        val onClickErrorDialog = {
            isErrorOnUploadDialog = false
            profileEditorViewModel.setUserState(UiState.Success(data = UserMapper.map(authUserValue.value)))
        }
        ThemedAlertDialog(
            titleText = "Ошибка при загрузке ",
            messageText = authUserState.value.message ?: "",
            onConfirm = onClickErrorDialog,
            onDismiss = onClickErrorDialog,
            onDismissRequest = onClickErrorDialog
        )
    }

    if (isUploadingDialog){
        val onClickUploadDialog = { isUploadingDialog = false }
        ThemedAlertDialog(
            titleText = "Обновление информации в профиле",
            messageText = "Вы уверены, что хотите обновить данные?",
            onConfirm = {
                coroutineScope.launch {
                    isLoadingDialog = true
                    isErrorOnUploadDialog = !profileEditorViewModel.uploadUserInfo(
                        context = context,
                        newUserTitleImageUri = selectedImage.value,
                        newUserDescription = inputDescription.value,
                        isSetDefaultImage = isSetDefaultImage.value
                    )
                    if (!isErrorOnUploadDialog){
                        navController.navigate(Screen.ProfileUserScreen.route)
                    }
                    isLoadingDialog = false
                    onClickUploadDialog()
                }
            },
            onDismiss = onClickUploadDialog,
            onDismissRequest = onClickUploadDialog
        )
    }

    if (isLoadingDialog){
        AlertDialog(
            onDismissRequest = { },
            title = { Text(text = "Идет загрузка подождите") },
            text = { Box(modifier = Modifier.fillMaxWidth())
                { CircularProgressIndicator(modifier = Modifier.align(Alignment.Center)) } },
            backgroundColor = MaterialTheme.colorScheme.primaryContainer,
            confirmButton = { },
            dismissButton = {
                TextButton(
                onClick = {
                    coroutineScope.cancel()
                    isLoadingDialog = false
                }) { Text("Отменить") } }
        )
    }


    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        selectedImage.value = uri
        Logger.debug("Select Uri", "uri = '${selectedImage.value}'")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.background
                ),
                navigationIcon = {
                    IconButton(
                        onClick =
                        { navController.navigate(Screen.ProfileUserScreen.route) },
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Menu",
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                },
                title = {
                    Text(
                        text = "Редактор профиля",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            )
        },
        content = { paddingValues: PaddingValues ->
            if (authUserState.value is UiState.Success && (authUserState.value.data?.userId ?: 0) > 0){
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(12.dp)
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Описание профиля",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        IconButton(
                            onClick = { inputDescription.value = fetchedDescription }
                        ) {
                            Icon(
                                imageVector =
                                if (inputDescription.value == fetchedDescription) Icons.TwoTone.Check
                                else Icons.TwoTone.Refresh,
                                "Has been resource updated?"
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    ThemedInputField(
                        textFieldValue = inputDescription,
                        placeholder = "Введите описание профиля",
                        modifier = Modifier.heightIn(min = 128.dp),
                        textStyle = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.secondary,
                            textAlign = TextAlign.Start
                        ),
                        placeholderStyle = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                            textAlign = TextAlign.Start
                        ),
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        cursorColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        oneLine = false
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    // Profile picture
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Превью изображение",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            IconButton(
                                onClick = {
                                    selectedImage.value = null
                                    isSetDefaultImage.value = true
                                    fetchedImageId = -1
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.TwoTone.Delete,
                                    "Has been resource updated?"
                                )
                            }
                            IconButton(
                                onClick = {
                                    isSetDefaultImage.value = false
                                    selectedImage.value = null
                                    fetchedImageId = authUserValue.value.userTitleImage
                                }
                            ) {
                                Icon(
                                    if (selectedImage.value == null && !isSetDefaultImage.value) Icons.TwoTone.Check else Icons.TwoTone.Refresh,
                                    "Has been resource updated?"
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .width(400.dp)
                            .aspectRatio(0.6f)
                            .clip(MaterialTheme.shapes.large)
                            .clickable { imagePickerLauncher.launch("image/*") }
                            .background(
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                shape = MaterialTheme.shapes.large
                            )
                            .border(
                                1.dp,
                                color = MaterialTheme.colorScheme.outline,
                                shape = MaterialTheme.shapes.large
                            )
                            .padding(6.dp)


                    ) {
                        Text(
                            text = "Выберите изображение",
                            modifier = Modifier.align(Alignment.Center),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.secondary,
                                textAlign = TextAlign.Start
                            ),
                        )
                        AsyncImage(
                            model =
                            if (selectedImage.value != null)
                                selectedImage.value
                            else if (fetchedImageId > 20) "${BuildConfig.API_BASE_URL}/api/v1/file/${fetchedImageId}/get"
                            else if (!isSetDefaultImage.value) "${BuildConfig.API_BASE_URL}/api/v1/file/2/get"
                            else null,
                            contentDescription = "Выбранное изображение",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(MaterialTheme.shapes.large),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Spacer(modifier = Modifier.height(48.dp))
                }
            }
            else if (authUserState.value is UiState.Success && (authUserState.value.data?.userId ?: 0) <= 0){
                val isOpenAlertNoUser = remember { mutableStateOf(true) }
                val navigateOpenAlertNoUser = {
                    isOpenAlertNoUser.value = false
                    navController.navigate(Screen.Catalog.route)
                }
                ThemedAlertDialog(
                    titleText = "Как вы сюда попали?",
                    messageText = "Пользователь загружен и Id <= 0",
                    onConfirm = navigateOpenAlertNoUser,
                    onDismiss = navigateOpenAlertNoUser,
                    onDismissRequest = navigateOpenAlertNoUser
                )
            }
            else{
                Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
        },
        floatingActionButton = {
            SmallFloatingActionButton(
                onClick = {
                    isUploadingDialog = true
                },
                shape = MaterialTheme.shapes.extraLarge,
                modifier = Modifier
                    .size(48.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = MaterialTheme.shapes.extraLarge
                    ),
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    Icons.TwoTone.Check,
                    contentDescription = "Upload edited fields to server",
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    )
}