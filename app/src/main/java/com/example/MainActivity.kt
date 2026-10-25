package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.AetherAppType
import com.example.ui.components.AetherAccountDialog
import com.example.ui.components.AetherBottomNav
import com.example.ui.components.AetherTopBar
import com.example.ui.components.AetherWaffleSheet
import com.example.ui.screens.*
import com.example.ui.theme.AetherTheme
import com.example.viewmodel.AetherSuiteViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AetherTheme {
                AetherAppRoot()
            }
        }
    }
}

@Composable
fun AetherAppRoot(vm: AetherSuiteViewModel = viewModel()) {
    val activeApp by vm.activeApp.collectAsStateWithLifecycle()
    val isWaffleOpen by vm.isWaffleOpen.collectAsStateWithLifecycle()
    val isAccountDialogOpen by vm.isAccountDialogOpen.collectAsStateWithLifecycle()
    val currentUser by vm.currentUser.collectAsStateWithLifecycle()

    // Search state
    val searchQuery by vm.searchQuery.collectAsStateWithLifecycle()
    val discoverArticles by vm.discoverArticles.collectAsStateWithLifecycle()

    // Mail state
    val emails by vm.emails.collectAsStateWithLifecycle()
    val mailCategory by vm.mailCategory.collectAsStateWithLifecycle()
    val selectedEmail by vm.selectedEmail.collectAsStateWithLifecycle()
    val isComposingMail by vm.isComposingMail.collectAsStateWithLifecycle()

    // Drive & Docs state
    val driveFiles by vm.driveFiles.collectAsStateWithLifecycle()
    val activeDoc by vm.activeDoc.collectAsStateWithLifecycle()
    val editingDocTitle by vm.editingDocTitle.collectAsStateWithLifecycle()
    val editingDocContent by vm.editingDocContent.collectAsStateWithLifecycle()

    // Maps state
    val mapPlaces by vm.mapPlaces.collectAsStateWithLifecycle()
    val selectedPlace by vm.selectedPlace.collectAsStateWithLifecycle()
    val selectedPlaceCategory by vm.selectedPlaceCategory.collectAsStateWithLifecycle()
    val isNavigating by vm.isNavigating.collectAsStateWithLifecycle()
    val currentNavStepIndex by vm.currentNavStepIndex.collectAsStateWithLifecycle()

    // Photos state
    val photos by vm.photos.collectAsStateWithLifecycle()
    val selectedPhoto by vm.selectedPhoto.collectAsStateWithLifecycle()
    val magicEnhance by vm.photoMagicEnhance.collectAsStateWithLifecycle()
    val magicEraser by vm.photoMagicEraser.collectAsStateWithLifecycle()
    val photoFilterMode by vm.photoFilterMode.collectAsStateWithLifecycle()

    // Calendar state
    val calendarEvents by vm.calendarEvents.collectAsStateWithLifecycle()
    val omniTasks by vm.omniTasks.collectAsStateWithLifecycle()
    val isCreatingEvent by vm.isCreatingEvent.collectAsStateWithLifecycle()

    // Back handling across all screens and sheets
    BackHandler(enabled = true) {
        val handled = vm.handleBack()
        if (!handled) {
            // Reached root
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            // Only hide top bar when in full distraction-free doc editor or fullscreen photo
            if (activeDoc == null && selectedPhoto == null) {
                AetherTopBar(
                    activeApp = activeApp,
                    currentUser = currentUser,
                    onOpenWaffle = { vm.toggleWaffle(true) },
                    onOpenAccount = { vm.toggleAccountDialog(true) },
                    onSearchClick = { vm.openApp(AetherAppType.SEARCH) }
                )
            }
        },
        bottomBar = {
            // Only hide bottom bar when in full distraction-free doc editor or fullscreen photo
            if (activeDoc == null && selectedPhoto == null) {
                AetherBottomNav(
                    activeApp = activeApp,
                    onSelectApp = { vm.openApp(it) },
                    onOpenWaffle = { vm.toggleWaffle(true) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = activeApp,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "AetherAppTransition"
            ) { app ->
                when (app) {
                    AetherAppType.HOME -> {
                        AetherHomeScreen(
                            currentUser = currentUser,
                            emails = emails,
                            driveFiles = driveFiles,
                            calendarEvents = calendarEvents,
                            photos = photos,
                            tasks = omniTasks,
                            onOpenApp = { vm.openApp(it) },
                            onOpenDoc = { vm.openDoc(it) },
                            onSelectEmail = {
                                vm.openApp(AetherAppType.MAIL)
                                vm.selectEmail(it)
                            },
                            onToggleTask = { vm.toggleTask(it) }
                        )
                    }

                    AetherAppType.SEARCH -> {
                        AetherSearchScreen(
                            searchQuery = searchQuery,
                            discoverArticles = discoverArticles,
                            onSearchQueryChange = { vm.updateSearchQuery(it) },
                            onSaveArticle = { vm.toggleSaveArticle(it) },
                            onOpenApp = { vm.openApp(it) }
                        )
                    }

                    AetherAppType.MAIL -> {
                        AetherMailScreen(
                            emails = emails,
                            selectedCategory = mailCategory,
                            selectedEmail = selectedEmail,
                            isComposing = isComposingMail,
                            onSelectCategory = { vm.setMailCategory(it) },
                            onSelectEmail = { vm.selectEmail(it) },
                            onToggleStar = { vm.toggleStarEmail(it) },
                            onDeleteEmail = { vm.deleteEmail(it) },
                            onOpenCompose = { vm.openComposeMail(it) },
                            onSendEmail = { to: String, sub: String, body: String -> vm.sendEmail(to, sub, body) },
                            onOpenDriveAttachment = { docName: String -> vm.openDocByName(docName) }
                        )
                    }

                    AetherAppType.DRIVE -> {
                        AetherDriveScreen(
                            files = driveFiles,
                            activeDoc = activeDoc,
                            editingTitle = editingDocTitle,
                            editingContent = editingDocContent,
                            onOpenDoc = { vm.openDoc(it) },
                            onCloseDoc = { vm.closeDoc() },
                            onUpdateTitle = { vm.updateDocTitle(it) },
                            onUpdateContent = { vm.updateDocContent(it) },
                            onCreateDoc = { vm.createNewDoc() },
                            onToggleStar = { vm.toggleStarFile(it) }
                        )
                    }

                    AetherAppType.MAPS -> {
                        AetherMapsScreen(
                            places = mapPlaces,
                            selectedPlace = selectedPlace,
                            selectedCategory = selectedPlaceCategory,
                            isNavigating = isNavigating,
                            navStepIndex = currentNavStepIndex,
                            navSteps = vm.navigationSteps,
                            onSelectPlace = { vm.selectPlace(it) },
                            onSelectCategory = { vm.setPlaceCategory(it) },
                            onStartNavigation = { vm.startNavigation(it) },
                            onStopNavigation = { vm.stopNavigation() },
                            onNextNavStep = { vm.nextNavStep() }
                        )
                    }

                    AetherAppType.PHOTOS -> {
                        AetherPhotosScreen(
                            photos = photos,
                            selectedPhoto = selectedPhoto,
                            magicEnhance = magicEnhance,
                            magicEraser = magicEraser,
                            filterMode = photoFilterMode,
                            onSelectPhoto = { vm.selectPhoto(it) },
                            onToggleFavorite = { vm.togglePhotoFavorite(it) },
                            onToggleEnhance = { vm.toggleMagicEnhance() },
                            onToggleEraser = { vm.toggleMagicEraser() },
                            onSetFilter = { vm.setPhotoFilter(it) }
                        )
                    }

                    AetherAppType.CALENDAR -> {
                        AetherCalendarScreen(
                            events = calendarEvents,
                            tasks = omniTasks,
                            isCreatingEvent = isCreatingEvent,
                            onOpenCreateEvent = { vm.openCreateEvent(it) },
                            onAddEvent = { title, time, loc, color ->
                                vm.addCalendarEvent(title, time, loc, color)
                            },
                            onToggleTask = { vm.toggleTask(it) },
                            onAddTask = { vm.addTask(it) }
                        )
                    }
                }
            }
        }
    }

    // 9-Dot Aether Waffle Launcher Sheet
    if (isWaffleOpen) {
        AetherWaffleSheet(
            activeApp = activeApp,
            currentUser = currentUser,
            onSelectApp = { vm.openApp(it) },
            onDismiss = { vm.toggleWaffle(false) }
        )
    }

    // Aether Account Switcher Dialog
    if (isAccountDialogOpen) {
        AetherAccountDialog(
            currentUser = currentUser,
            allUsers = vm.availableUsers,
            onSwitchUser = { vm.switchAccount(it) },
            onDismiss = { vm.toggleAccountDialog(false) }
        )
    }
}
