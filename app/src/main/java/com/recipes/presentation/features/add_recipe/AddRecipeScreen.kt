package com.recipes.presentation.features.add_recipe

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Dimension
import coil.size.Size
import com.recipes.R
import com.recipes.presentation.common.components.keyboardIsOpened
import com.recipes.presentation.common.components.rememberKeyboardHeight
import com.recipes.presentation.common.modifiers.drawNoteSheetOnBackGround
import com.recipes.presentation.common.theme.CianBlueDarker
import com.recipes.presentation.common.theme.DarkGrey
import com.recipes.presentation.common.theme.FontEduAuvicWantHandBold
import com.recipes.presentation.common.theme.FontEduAuvicWantHandMedium
import com.recipes.presentation.common.theme.FontEduAuvicWantHandRegular
import com.recipes.presentation.common.theme.Gray
import com.recipes.presentation.common.theme.GrayLighter
import com.recipes.presentation.common.theme.LightGrey
import com.recipes.presentation.common.theme.MainBgColor
import com.recipes.presentation.common.theme.RecipesTheme
import com.recipes.presentation.common.utils.copyFile
import com.recipes.presentation.common.utils.formatCookingSteps
import com.recipes.presentation.common.utils.formatIngredients
import com.recipes.presentation.common.utils.formatTags
import com.recipes.presentation.features.add_recipe.components.AttachPhotoButton
import com.recipes.presentation.features.add_recipe.components.NotesTextField
import com.recipes.presentation.features.add_recipe.components.Pin
import com.recipes.presentation.features.add_recipe.components.RemoveButton
import com.recipes.presentation.features.add_recipe.components.SaveRecipeButton
import com.recipes.presentation.features.add_recipe.components.TakeChooseButtons
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File


@Composable
fun AddRecipeScreenRoot(
    navController: NavController,
    viewModel: AddRecipeViewModel =
        hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    AddRecipeScreen(
        state = state,
        goBack = {
            navController.navigateUp()
        },
        onAction = viewModel::onAction,
        grantPermissionThroughSettings = {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", context.packageName, null)
            intent.setData(uri)
            context.startActivity(intent)
            Toast.makeText(
                context,
                "Please, grand Camera permissions for taking a photo",
                Toast.LENGTH_SHORT
            ).show()
        }
    )
}

@Composable
private fun AddRecipeScreen(
    goBack: () -> Unit,
    state: AddRecipeState,
    onAction: (AddRecipeScreenActions) -> Unit,
    grantPermissionThroughSettings: () -> Unit
) {
    val context = LocalContext.current
    val d = LocalDensity.current
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    var showAttachPhotoButton by rememberSaveable {
        mutableStateOf(true)
    }
    var showPhotoPopUp by rememberSaveable {
        mutableStateOf(false)
    }
    var showPhoto by rememberSaveable {
        mutableStateOf(false)
    }
    var showPhotoFullScreen by rememberSaveable {
        mutableStateOf(false)
    }
    var titleText by rememberSaveable {
        mutableStateOf("")
    }
    var descriptionText by rememberSaveable {
        mutableStateOf("")
    }
    var folderText by rememberSaveable {
        mutableStateOf("")
    }
    val foldersToShow by remember(state.foldersList) {
        derivedStateOf {
            if (folderText.isEmpty()) {
                state.foldersList
            } else {
                state.foldersList.filter { it.lowercase().contains(folderText.lowercase()) }
            }
        }
    }
    var showFoldersPopUp by rememberSaveable {
        mutableStateOf(false)
    }
    var foldersOffset by remember {
        mutableStateOf(Offset(0f, 0f))
    }
    var foldersPopupHeight by remember {
        mutableIntStateOf(0)
    }
    var statusBarHeight by remember {
        mutableStateOf(0.dp)
    }
    val foldersMaxHeight by remember(foldersOffset) {
        derivedStateOf {
            with(d) { foldersOffset.y.toDp() - statusBarHeight }
        }
    }
    var folderTextHandler: (textFromRemote: String) -> Unit = {}
    var tagsText by rememberSaveable {
        mutableStateOf("")
    }
    var ingredientsText by rememberSaveable {
        mutableStateOf("")
    }
    var cookingStepsText by rememberSaveable {
        mutableStateOf("")
    }
    val keyboardHeight by rememberKeyboardHeight()
    val keyboardIsOpened by keyboardIsOpened()

    val showSaveButton by remember {
        derivedStateOf {
            titleText.isNotEmpty() &&
                    descriptionText.isNotEmpty() &&
                    folderText.isNotEmpty() &&
                    tagsText.isNotEmpty() &&
                    ingredientsText.isNotEmpty() &&
                    cookingStepsText.isNotEmpty()
        }
    }

    LaunchedEffect(keyboardIsOpened) {
        if (keyboardIsOpened) {
            showPhotoPopUp = false
        }
    }

    val cacheDirectory = File(context.cacheDir, "images")
    val fileDirectory = File(context.filesDir, "images")

    var tempUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var photoUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var photoFile by rememberSaveable { mutableStateOf<File?>(null) }
    val authority = stringResource(id = R.string.fileprovider)

    val getTempFile: () -> File = {
        cacheDirectory.mkdirs()
        val file = File.createTempFile(
            "image_" + System.currentTimeMillis().toString(),
            ".jpg",
            cacheDirectory
        )
        file
    }
    val getStableFile: () -> File = {
        fileDirectory.mkdirs()
        val file = File(fileDirectory, "image_" + System.currentTimeMillis().toString() + ".jpg")
        file
    }
    val getUri: (file: File) -> Uri? = { file ->
        FileProvider.getUriForFile(
            context,
            authority,
            file
        )
    }

    val takePhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { isSaved ->
            if (isSaved) {
                val newStableFile = getStableFile().also { photoFile = it }
                val uri = getUri(newStableFile)

                scope.launch(Dispatchers.IO) {
                    tempUri?.let {
                        copyFile(context, tempUri, uri)
                        photoUri = uri
                        showPhoto = true
                        showAttachPhotoButton = false
                    }
                }
            }
        }
    )

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            getUri(getTempFile())?.let { uri ->
                tempUri = uri
                takePhotoLauncher.launch(uri)
            }
        } else {
            if (state.permissionsWasAsked) {
                grantPermissionThroughSettings()
            } else {
                onAction(AddRecipeScreenActions.PermissionsAsked)
            }
        }
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { imageUri ->
            imageUri?.let {
                scope.launch(Dispatchers.IO) {
                    val newStableFile = getStableFile().also { photoFile = it }
                    val uri = getUri(newStableFile)

                    copyFile(context, it, uri)
                    photoUri = uri
                    showPhoto = true
                    showAttachPhotoButton = false
                }
            }
        }
    )

    Scaffold { paddingValues ->
        statusBarHeight = paddingValues.calculateTopPadding()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MainBgColor)
                .padding(paddingValues)
                .clickable(
                    indication = null,
                    interactionSource = null
                ) {
                    showPhotoPopUp = false
                }
                .then(
                    if (showPhotoFullScreen) Modifier.blur(4.dp)
                    else Modifier
                )
        ) {
            // Screen title
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    fontFamily = FontEduAuvicWantHandBold,
                    text = "Add a new recipe",
                    modifier = Modifier
                        .drawBehind {
                            val strokeWidth = 3.dp.toPx()
                            drawLine(
                                color = DarkGrey,
                                start = Offset(0f - 12.dp.toPx(), size.height + 6.dp.toPx()),
                                end = Offset(size.width + 12.dp.toPx(), size.height + 2.dp.toPx()),
                                strokeWidth = strokeWidth,
                                cap = StrokeCap.Round
                            )
                        }
                        .align(Alignment.Center),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    color = DarkGrey
                )
                RemoveButton(
                    onClicked = {
                        goBack()
                    },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 12.dp, top = 12.dp)
                )
            }

            // Note content
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(top = 6.dp)
                    .padding(8.dp)
                    .drawNoteSheetOnBackGround()
                    .padding(start = 30.dp, end = 8.dp, bottom = 8.dp)
            ) {
                var imageWidth by remember {
                    mutableStateOf(0.dp)
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onPlaced {
                            imageWidth = with(d) { (it.size.width.toFloat() * 0.8f).toDp() }
                        }
                        .padding(
                            bottom = run {
                                val padding =
                                    with(d) { keyboardHeight.toDp() } - paddingValues.calculateBottomPadding()
                                if (padding >= 0.dp) padding else 0.dp
                            }
                        )
                ) {
                    // Photo
                    Box(
                        modifier = Modifier
                    ) {
                        androidx.compose.animation.AnimatedVisibility(
                            visible = showAttachPhotoButton,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            AttachPhotoButton(
                                onButtonClicked = {
                                    showPhotoPopUp = true
                                },
                                modifier = Modifier
                                    .padding(6.dp)
                                    .rotate(-3f)
                                    .padding(top = 6.dp)
                            )
                        }

                        androidx.compose.animation.AnimatedVisibility(
                            visible = showPhoto,
                            enter = slideIn(
                                initialOffset = { IntOffset(-it.height / 2, -it.height * 2) },
                                animationSpec = spring(
                                    stiffness = Spring.StiffnessMediumLow,
                                    visibilityThreshold = IntOffset.VisibilityThreshold
                                )
                            ),
                            exit = slideOut(
                                targetOffset = { IntOffset(-it.height / 2, -it.height * 2) },
                                animationSpec = spring(
                                    stiffness = Spring.StiffnessMediumLow,
                                    visibilityThreshold = IntOffset.VisibilityThreshold
                                )
                            )
                        ) {
                            Box {
                                AsyncImage(
                                    model = ImageRequest.Builder(context)
                                        .memoryCachePolicy(CachePolicy.ENABLED)
                                        .data(photoUri)
                                        .size(
                                            Size(
                                                with(LocalDensity.current) {
                                                    162.dp.toPx().toInt()
                                                },
                                                Dimension.Undefined
                                            )
                                        )
                                        .build(),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .height(162.dp)
                                        .width(imageWidth)
                                        .rotate(-3f)
                                        .padding(top = 12.dp)
                                        .clickable(
                                            indication = null,
                                            interactionSource = null
                                        ) {
                                            showPhotoFullScreen = true
                                        }
                                )

                                RemoveButton(
                                    onClicked = {
                                        showPhoto = false
                                        showAttachPhotoButton = true

                                        scope.launch(Dispatchers.IO) {
                                            photoFile?.let { photoFile ->
                                                if (photoFile.exists()) {
                                                    photoFile.delete()
                                                }
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .padding(top = 12.dp, end = 8.dp)
                                        .rotate(-3f)
                                        .align(Alignment.TopEnd)
                                )
                            }
                        }

                        androidx.compose.animation.AnimatedVisibility(
                            visible = showPhoto,
                            enter = slideIn(
                                initialOffset = { IntOffset(-it.height / 2, -it.height * 3) },
                                animationSpec = spring(
                                    stiffness = Spring.StiffnessMediumLow,
                                    visibilityThreshold = IntOffset.VisibilityThreshold
                                )
                            ),
                            exit = slideOut(
                                targetOffset = { IntOffset(-it.height / 2, -it.height * 3) },
                                animationSpec = spring(
                                    stiffness = Spring.StiffnessMediumLow,
                                    visibilityThreshold = IntOffset.VisibilityThreshold
                                )
                            )
                        ) {
                            Pin(
                                modifier = Modifier
                                    .size(width = 30.dp, height = 70.dp)
                                    .rotate(3f)
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .padding(top = 18.dp)
                            .verticalScroll(scrollState)
                    ) {
                        var folderHeight by remember {
                            mutableStateOf(0)
                        }
                        var tagsHeight by remember {
                            mutableStateOf(0)
                        }
                        var ingredientsHeight by remember {
                            mutableStateOf(0)
                        }
                        var cookingStepsHeight by remember {
                            mutableStateOf(0)
                        }
                        var saveRecipeHeight by remember {
                            mutableStateOf(0)
                        }

                        // Title
                        Row {
                            Text(
                                fontFamily = FontEduAuvicWantHandRegular,
                                text = "title:",
                                modifier = Modifier
                                    .align(Alignment.Bottom)
                                    .padding(end = 4.dp),
                                textAlign = TextAlign.Start,
                                fontSize = 18.sp,
                                color = DarkGrey
                            )

                            NotesTextField(
                                onTextChanged = { txt ->
                                    titleText = txt
                                },
                                placeHolder = "Enter a title",
                                maxLines = Int.MAX_VALUE,
                                maxLength = 100,
                                modifier = Modifier
                                    .widthIn(min = imageWidth / 1.4f),
                                fontSize = 20.sp,
                                fontFamily = FontEduAuvicWantHandMedium,
                                allowNewLines = false
                            )
                        }

                        // Description
                        NotesTextField(
                            onTextChanged = { txt ->
                                descriptionText = txt
                                scope.launch {
                                    delay(100)
                                    scrollState.animateScrollTo(
                                        scrollState.maxValue - saveRecipeHeight - cookingStepsHeight - ingredientsHeight - tagsHeight - folderHeight
                                    )
                                }
                            },
                            onFocusChanged = { focused ->
                                if (focused) {
                                    scope.launch {
                                        delay(400)
                                        scrollState.animateScrollTo(
                                            scrollState.maxValue - saveRecipeHeight - cookingStepsHeight - ingredientsHeight - tagsHeight - folderHeight
                                        )
                                    }
                                }
                            },
                            placeHolder = "Description",
                            maxLines = Int.MAX_VALUE,
                            maxLength = 500,
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .fillMaxWidth(),
                            fontSize = 16.sp,
                            fontFamily = FontEduAuvicWantHandMedium,
                        )

                        // Folder field
                        Row(
                            modifier = Modifier
                                .onPlaced {
                                    folderHeight = it.size.height
                                }
                                .padding(top = 8.dp)
                                .onGloballyPositioned {
                                    foldersOffset = it
                                        .localToRoot(
                                            Offset(
                                                with(d) { 36.dp.toPx() },
                                                0f
                                            )
                                        )
                                }
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_folder),
                                colorFilter = ColorFilter.tint(DarkGrey),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(end = 4.dp)
                                    .size(32.dp)
                                    .align(Alignment.CenterVertically)
                            )
                            NotesTextField(
                                onTextChanged = { txt ->
                                    folderText = txt
                                },
                                onFocusChanged = { focused ->
                                    showFoldersPopUp = focused
                                    if (focused) {
                                        scope.launch {
                                            delay(400)
                                            scrollState.animateScrollTo(
                                                scrollState.maxValue - saveRecipeHeight - cookingStepsHeight - ingredientsHeight - tagsHeight
                                            )
                                        }
                                    }
                                },
                                placeHolder = "Recipe folder",
                                maxLines = 1,
                                maxLength = 50,
                                modifier = Modifier
                                    .padding(top = 8.dp)
                                    .widthIn(min = imageWidth / 1.4f),
                                fontSize = 16.sp,
                                fontFamily = FontEduAuvicWantHandMedium,
                                textFromRemoteHandler = {
                                    folderTextHandler = it
                                },
                                allowNewLines = false
                            )
                        }

                        // Tags
                        NotesTextField(
                            onTextChanged = { txt ->
                                tagsText = txt
                                scope.launch {
                                    delay(100)
                                    scrollState.animateScrollTo(
                                        scrollState.maxValue - saveRecipeHeight - cookingStepsHeight - ingredientsHeight
                                    )
                                }
                            },
                            onFocusChanged = { focused ->
                                if (focused) {
                                    scope.launch {
                                        delay(400)
                                        scrollState.animateScrollTo(
                                            scrollState.maxValue - saveRecipeHeight - cookingStepsHeight - ingredientsHeight
                                        )
                                    }
                                }
                            },
                            placeHolder = "Enter tags separated by a comma",
                            maxLines = Int.MAX_VALUE,
                            maxLength = 100,
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .fillMaxWidth()
                                .onPlaced {
                                    tagsHeight = it.size.height
                                },
                            fontSize = 20.sp,
                            fontFamily = FontEduAuvicWantHandMedium,
                            customTextDrawComposable = { tagsStr ->
                                Text(
                                    fontFamily = FontEduAuvicWantHandMedium,
                                    text = tagsStr.formatTags(),
                                    modifier = Modifier
                                        .onPlaced {
                                            tagsHeight = it.size.height
                                        },
                                    textAlign = TextAlign.Start,
                                    fontSize = 20.sp,
                                    color = CianBlueDarker
                                )
                            },
                            allowNewLines = false
                        )

                        // Ingredients
                        Column(
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .onPlaced {
                                    ingredientsHeight = it.size.height
                                }
                        ) {
                            var ingredientsTitleHeight by remember {
                                mutableIntStateOf(0)
                            }
                            Text(
                                fontFamily = FontEduAuvicWantHandRegular,
                                text = "ingredients:",
                                modifier = Modifier
                                    .onPlaced {
                                        ingredientsTitleHeight = it.size.height
                                    },
                                textAlign = TextAlign.Start,
                                fontSize = 14.sp,
                                color = DarkGrey
                            )
                            NotesTextField(
                                onTextChanged = { txt ->
                                    ingredientsText = txt
                                    scope.launch {
                                        delay(100)
                                        scrollState.animateScrollTo(
                                            scrollState.maxValue - saveRecipeHeight - cookingStepsHeight
                                        )
                                    }
                                },
                                onFocusChanged = { focused ->
                                    if (focused) {
                                        scope.launch {
                                            delay(400)
                                            scrollState.animateScrollTo(
                                                scrollState.maxValue - saveRecipeHeight - cookingStepsHeight
                                            )
                                        }
                                    }
                                },
                                placeHolder = "Enter ingredients separated by a comma or new line",
                                maxLines = Int.MAX_VALUE,
                                maxLength = 500,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                fontSize = 16.sp,
                                fontFamily = FontEduAuvicWantHandMedium,
                                customTextDrawComposable = { ingredientsStr ->
                                    Text(
                                        fontFamily = FontEduAuvicWantHandMedium,
                                        text = ingredientsStr.formatIngredients(),
                                        modifier = Modifier
                                            .onPlaced {
                                                ingredientsHeight =
                                                    it.size.height + ingredientsTitleHeight
                                            },
                                        textAlign = TextAlign.Start,
                                        fontSize = 16.sp,
                                        color = DarkGrey
                                    )
                                },
                                allowNewLines = true
                            )
                        }

                        // Cooking steps
                        Column(
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .onPlaced {
                                    cookingStepsHeight = it.size.height
                                }
                        ) {
                            Text(
                                fontFamily = FontEduAuvicWantHandRegular,
                                text = "Cooking steps",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                fontSize = 18.sp,
                                color = DarkGrey
                            )
                            NotesTextField(
                                onTextChanged = { txt ->
                                    cookingStepsText = txt
                                    scope.launch {
                                        delay(100)
                                        scrollState.animateScrollTo(
                                            scrollState.maxValue - saveRecipeHeight
                                        )
                                    }
                                },
                                onFocusChanged = { focused ->
                                    if (focused) {
                                        scope.launch {
                                            delay(400)
                                            scrollState.animateScrollTo(
                                                scrollState.maxValue - saveRecipeHeight
                                            )
                                        }
                                    }
                                },
                                placeHolder = "Enter cooking steps separated by doubled new line",
                                maxLines = Int.MAX_VALUE,
                                maxLength = 5000,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                fontSize = 16.sp,
                                fontFamily = FontEduAuvicWantHandMedium,
                                customTextDrawComposable = { stepsStr ->
                                    Text(
                                        fontFamily = FontEduAuvicWantHandMedium,
                                        text = stepsStr.formatCookingSteps(),
                                        modifier = Modifier,
                                        textAlign = TextAlign.Start,
                                        fontSize = 16.sp,
                                        color = DarkGrey
                                    )
                                },
                                allowNewLines = true
                            )
                        }

                        // Save recipe
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 14.dp)
                                .onPlaced {
                                    saveRecipeHeight = it.size.height
                                }
                        ) {
                            androidx.compose.animation.AnimatedVisibility(
                                visible = !showSaveButton,
                                enter = fadeIn(),
                                exit = fadeOut(),
                                modifier = Modifier
                                    .align(Alignment.Center)
                            ) {
                                Text(
                                    fontFamily = FontEduAuvicWantHandRegular,
                                    text = "Fill all info",
                                    modifier = Modifier
                                        .padding(bottom = 30.dp)
                                        .drawBehind {
                                            val strokeWidth = 1.dp.toPx()
                                            drawLine(
                                                color = Color.Red,
                                                start = Offset(
                                                    0f - 4.dp.toPx(),
                                                    size.height + 3.dp.toPx()
                                                ),
                                                end = Offset(
                                                    size.width + 4.dp.toPx(),
                                                    size.height + 1.dp.toPx()
                                                ),
                                                strokeWidth = strokeWidth,
                                                cap = StrokeCap.Round
                                            )
                                        },
                                    textAlign = TextAlign.Center,
                                    fontSize = 14.sp,
                                    color = Color.Red
                                )
                            }
                            androidx.compose.animation.AnimatedVisibility(
                                visible = showSaveButton,
                                enter = fadeIn(),
                                exit = fadeOut(),
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                            ) {
                                SaveRecipeButton(
                                    onButtonClicked = {
                                        onAction(
                                            AddRecipeScreenActions.SaveRecipe(
                                                photoUri?.toString(),
                                                titleText,
                                                descriptionText,
                                                folderText,
                                                ingredientsText,
                                                cookingStepsText,
                                                tagsText
                                            )
                                        )
                                        goBack()
                                    },
                                    modifier = Modifier
                                        .padding(end = 22.dp)
                                        .rotate(-3f)
                                        .padding(top = 8.dp, bottom = 8.dp)
                                )
                            }
                        }
                    }
                }

                // Take or choose photo popup
                androidx.compose.animation.AnimatedVisibility(
                    visible = showPhotoPopUp,
                    enter = scaleIn(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    ),
                    exit = scaleOut(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    )
                ) {
                    TakeChooseButtons(
                        onTakePhotoClicked = {
                            showPhotoPopUp = false

                            val permission = Manifest.permission.CAMERA
                            if (
                                ContextCompat.checkSelfPermission(
                                    context,
                                    permission
                                ) == PackageManager.PERMISSION_GRANTED
                            ) {
                                getUri(getTempFile())?.let { uri ->
                                    tempUri = uri
                                    takePhotoLauncher.launch(uri)
                                }
                            } else {
                                cameraPermissionLauncher.launch(permission)
                            }
                        },
                        onChooseFromGalleryClicked = {
                            showPhotoPopUp = false

                            imagePicker.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        },
                        modifier = Modifier
                            .padding(top = 6.dp)
                    )
                }
            }
        }

        // Folders pop up
        AnimatedVisibility(
            visible = showFoldersPopUp,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = foldersOffset.x.toInt(),
                        y = foldersOffset.y.toInt() - foldersPopupHeight
                    )
                }
        ) {
            var lineWidth by remember {
                mutableStateOf(0.dp)
            }

            LazyColumn(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Gray)
                    .onPlaced {
                        lineWidth = with(d) { it.size.width.toDp() }
                        foldersPopupHeight = it.size.height
                    }
                    .heightIn(max = foldersMaxHeight)
            ) {
                itemsIndexed(foldersToShow) { index, folder ->
                    Row(
                        modifier = Modifier
                            .widthIn(min = lineWidth)
                            .clickable {
                                folderTextHandler(folder)
                                focusManager.clearFocus()
                            }
                            .padding(
                                bottom = if (index == foldersToShow.count() - 1) 8.dp else 4.dp,
                                top = if (index == 0) 8.dp else 4.dp,
                                start = 8.dp,
                                end = 8.dp
                            )
                    ) {
                        Text(
                            fontFamily = FontEduAuvicWantHandMedium,
                            text = folder,
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp,
                            color = LightGrey
                        )
                    }
                    if (index != foldersToShow.count() - 1) {
                        Spacer(
                            modifier = Modifier
                                .height(1.dp)
                                .background(GrayLighter)
                                .width(lineWidth)
                        )
                    }
                }
            }
        }

        // Photo pop up
        AnimatedVisibility(
            visible = showPhotoFullScreen,
            enter = scaleIn(),
            exit = scaleOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = paddingValues.calculateTopPadding(),
                        bottom = paddingValues.calculateBottomPadding()
                    )
                    .clickable(
                        indication = null,
                        interactionSource = null
                    ) { }
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .dispatcher(Dispatchers.IO)
                            .data(photoUri)
                            .build(),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )

                    RemoveButton(
                        onClicked = {
                            showPhotoFullScreen = false
                        },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(end = 12.dp, top = 12.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun AddRecipeScreenPreview() {
    val foldersList = mutableListOf<String>()
    for (i in 1..1000) {
        foldersList.add("folder $i")
    }
    RecipesTheme {
        AddRecipeScreen(
            state = AddRecipeState(
                foldersList = foldersList
            ),
            goBack = {},
            onAction = {},
            grantPermissionThroughSettings = {}
        )
    }
}