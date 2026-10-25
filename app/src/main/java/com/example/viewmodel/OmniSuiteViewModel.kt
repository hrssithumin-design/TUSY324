package com.example.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.model.*
import com.example.ui.theme.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AetherSuiteViewModel : ViewModel() {

    // ==================== APP & ECOSYSTEM SHELL ====================
    private val _activeApp = MutableStateFlow(AetherAppType.HOME)
    val activeApp: StateFlow<AetherAppType> = _activeApp.asStateFlow()

    private val _appHistory = MutableStateFlow<List<AetherAppType>>(listOf(AetherAppType.HOME))

    private val _isWaffleOpen = MutableStateFlow(false)
    val isWaffleOpen: StateFlow<Boolean> = _isWaffleOpen.asStateFlow()

    private val _isAccountDialogOpen = MutableStateFlow(false)
    val isAccountDialogOpen: StateFlow<Boolean> = _isAccountDialogOpen.asStateFlow()

    // Multiple Aether Accounts
    val availableUsers = listOf(
        UserProfile(
            id = "user_1",
            name = "Alex Mercer",
            email = "alex.mercer@aether.cloud",
            initials = "AM",
            avatarBgColor = AetherBlue,
            usedStorageGb = 9.8f,
            totalStorageGb = 15.0f,
            isWorkAccount = false
        ),
        UserProfile(
            id = "user_2",
            name = "Alex Mercer (Aether Enterprise)",
            email = "alex@aether-systems.io",
            initials = "AE",
            avatarBgColor = AetherTeal,
            usedStorageGb = 38.4f,
            totalStorageGb = 100.0f,
            isWorkAccount = true
        )
    )

    private val _currentUser = MutableStateFlow(availableUsers[0])
    val currentUser: StateFlow<UserProfile> = _currentUser.asStateFlow()

    fun switchAccount(user: UserProfile) {
        _currentUser.value = user
        _isAccountDialogOpen.value = false
    }

    fun openApp(app: AetherAppType) {
        if (_activeApp.value != app) {
            _appHistory.update { it + app }
            _activeApp.value = app
        }
        _isWaffleOpen.value = false
    }

    fun handleBack(): Boolean {
        if (_isAccountDialogOpen.value) {
            _isAccountDialogOpen.value = false
            return true
        }
        if (_isWaffleOpen.value) {
            _isWaffleOpen.value = false
            return true
        }
        if (_selectedEmail.value != null) {
            _selectedEmail.value = null
            return true
        }
        if (_isComposingMail.value) {
            _isComposingMail.value = false
            return true
        }
        if (_activeDoc.value != null) {
            closeDoc()
            return true
        }
        if (_isNavigating.value) {
            _isNavigating.value = false
            return true
        }
        if (_selectedPlace.value != null) {
            _selectedPlace.value = null
            return true
        }
        if (_selectedPhoto.value != null) {
            _selectedPhoto.value = null
            return true
        }
        if (_isCreatingEvent.value) {
            _isCreatingEvent.value = false
            return true
        }
        if (_isCreatingNote.value) {
            _isCreatingNote.value = false
            return true
        }
        if (_searchQuery.value.isNotEmpty()) {
            _searchQuery.value = ""
            return true
        }
        if (_appHistory.value.size > 1) {
            val updated = _appHistory.value.dropLast(1)
            _appHistory.value = updated
            _activeApp.value = updated.last()
            return true
        }
        return false
    }

    fun toggleWaffle(open: Boolean? = null) {
        _isWaffleOpen.value = open ?: !_isWaffleOpen.value
    }

    fun toggleAccountDialog(open: Boolean? = null) {
        _isAccountDialogOpen.value = open ?: !_isAccountDialogOpen.value
    }

    // ==================== SEARCH & PULSE ====================
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _discoverArticles = MutableStateFlow(
        listOf(
            DiscoverArticle(
                id = "art_1",
                title = "Silicon Photonics Accelerators Deliver 10x Bandwidth for Edge AI",
                source = "Aether Tech Pulse",
                timeAgo = "15m ago",
                category = "Computing",
                summary = "Integrated optical waveguides directly inside standard chip packages demonstrate near-zero latency data transfer at 20% typical power consumption.",
                readMinutes = 4
            ),
            DiscoverArticle(
                id = "art_2",
                title = "Deep-Field Space Observatory Detects Water Vapor Atmosphere on Exoplanet K2-18",
                source = "Astro Science",
                timeAgo = "1h ago",
                category = "Astronomy",
                summary = "Spectroscopic analysis reveals clear signatures of carbon dioxide and methane alongside significant marine cloud coverage in the habitable zone.",
                readMinutes = 5
            ),
            DiscoverArticle(
                id = "art_3",
                title = "Minimalist Spatial UI: The Evolution of Fluid Surfaces and Micro-Interactions",
                source = "Design Journal",
                timeAgo = "2h ago",
                category = "Design",
                summary = "Why high-clarity monochrome typography paired with intentional color punctuation outperforms heavy desktop skeumorphism in modern multi-form-factor apps.",
                readMinutes = 3
            ),
            DiscoverArticle(
                id = "art_4",
                title = "Solid-State Sodium Batteries Enter Pilot Electric Vehicle Fleet Production",
                source = "Green Mobility",
                timeAgo = "4h ago",
                category = "Clean Energy",
                summary = "Eliminating rare earth metals while offering sub-10-minute fast charging in winter conditions below zero degrees Celsius.",
                readMinutes = 6
            )
        )
    )
    val discoverArticles: StateFlow<List<DiscoverArticle>> = _discoverArticles.asStateFlow()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleSaveArticle(articleId: String) {
        _discoverArticles.update { list ->
            list.map { if (it.id == articleId) it.copy(isSaved = !it.isSaved) else it }
        }
    }

    // ==================== MAIL ====================
    private val _mailCategory = MutableStateFlow(MailCategory.PRIMARY)
    val mailCategory: StateFlow<MailCategory> = _mailCategory.asStateFlow()

    private val _selectedEmail = MutableStateFlow<EmailMessage?>(null)
    val selectedEmail: StateFlow<EmailMessage?> = _selectedEmail.asStateFlow()

    private val _isComposingMail = MutableStateFlow(false)
    val isComposingMail: StateFlow<Boolean> = _isComposingMail.asStateFlow()

    private val _emails = MutableStateFlow(
        listOf(
            EmailMessage(
                id = "em_1",
                senderName = "Dr. Elena Rostova",
                senderEmail = "elena.r@aether-labs.org",
                subject = "Aether Systems Q4 Architecture Blueprint & Specs",
                preview = "Attached is the consolidated spec for our cross-app data synchronization...",
                body = "Hello Alex,\n\nI have reviewed the engineering benchmarks for the Aether Cloud ecosystem. The unified data pipeline between Aether Mail, Docs, and Chrono calendar is operating with sub-50ms local cache updates.\n\nPlease inspect the attached document and let's finalize the rollout schedule during our sync.\n\nWarm regards,\nElena",
                timestamp = "10:15 AM",
                category = MailCategory.PRIMARY,
                isUnread = true,
                isStarred = true,
                avatarColor = AetherIndigo,
                hasAttachment = true,
                attachmentName = "Aether_Q4_Blueprint.docx"
            ),
            EmailMessage(
                id = "em_2",
                senderName = "Aether Shield Security",
                senderEmail = "security@aether.cloud",
                subject = "Security Audit Verified: Zero Alerts",
                preview = "Your encrypted cloud vault passed all integrity checks across 3 active terminals...",
                body = "Hello Alex,\n\nYour biometric credentials and end-to-end encrypted storage partitions were validated at 06:00 UTC. All active sessions are fully protected by Aether Shield.\n\n- Android Emulator Device (Current session)\n- Workstation Aether Pro\n- Aether Cloud Backup Vault\n\nNo further verification is needed.",
                timestamp = "8:30 AM",
                category = MailCategory.UPDATES,
                isUnread = true,
                isStarred = false,
                avatarColor = AetherTeal
            ),
            EmailMessage(
                id = "em_3",
                senderName = "Marcus Vance",
                senderEmail = "marcus.v@vectoraudio.com",
                subject = "Synthesizer Presets & Spatial Audio Soundscapes",
                preview = "Hey Alex! Exported the ambient audio tracks for the Aether Canvas photo memories...",
                body = "Hey Alex,\n\nHere are the binaural nature ambient audio stems for the Photos memories slideshow. They automatically adjust reverb depending on whether the photo is tagged Nature, Travel, or Urban.\n\nGive them a listen!\n\nBest,\nMarcus",
                timestamp = "Yesterday",
                category = MailCategory.PRIMARY,
                isUnread = false,
                isStarred = false,
                avatarColor = AetherAmber
            ),
            EmailMessage(
                id = "em_4",
                senderName = "Aether Flight Concierge",
                senderEmail = "boarding@aether-aero.com",
                subject = "Flight Confirmation: AC-924 to Zurich",
                preview = "Gate E12 • Terminal 1 International • On-time departure 09:45 AM...",
                body = "Alex, your flight AC-924 to Zurich Airport is confirmed on schedule.\n\n- Flight: Aether Aero AC-924\n- Gate: E12\n- Seat: 4F (Window / Power Outlets)\n\nTap attachment below to preview flight itinerary and baggage vouchers in Aether Docs.",
                timestamp = "Oct 24",
                category = MailCategory.UPDATES,
                isUnread = false,
                isStarred = true,
                avatarColor = AetherCoral,
                hasAttachment = true,
                attachmentName = "Zurich_Itinerary_AC924.pdf"
            )
        )
    )
    val emails: StateFlow<List<EmailMessage>> = _emails.asStateFlow()

    fun setMailCategory(category: MailCategory) {
        _mailCategory.value = category
    }

    fun selectEmail(email: EmailMessage?) {
        _selectedEmail.value = email
        if (email != null && email.isUnread) {
            _emails.update { list ->
                list.map { if (it.id == email.id) it.copy(isUnread = false) else it }
            }
        }
    }

    fun toggleStarEmail(emailId: String) {
        _emails.update { list ->
            list.map { if (it.id == emailId) it.copy(isStarred = !it.isStarred) else it }
        }
        if (_selectedEmail.value?.id == emailId) {
            _selectedEmail.update { it?.copy(isStarred = !it.isStarred) }
        }
    }

    fun deleteEmail(emailId: String) {
        _emails.update { list -> list.filter { it.id != emailId } }
        if (_selectedEmail.value?.id == emailId) {
            _selectedEmail.value = null
        }
    }

    fun openComposeMail(open: Boolean) {
        _isComposingMail.value = open
    }

    fun sendEmail(to: String, subject: String, body: String) {
        if (to.isBlank() && subject.isBlank()) return
        val newEmail = EmailMessage(
            id = "sent_${System.currentTimeMillis()}",
            senderName = "Me (${_currentUser.value.name})",
            senderEmail = _currentUser.value.email,
            subject = subject.ifBlank { "(No Subject)" },
            preview = body.take(70),
            body = body,
            timestamp = "Just now",
            category = MailCategory.PRIMARY,
            isUnread = false,
            avatarColor = _currentUser.value.avatarBgColor
        )
        _emails.update { listOf(newEmail) + it }
        _isComposingMail.value = false
    }

    // ==================== DRIVE & DOCS ====================
    private val _driveFiles = MutableStateFlow(
        listOf(
            DriveFile(
                id = "doc_1",
                name = "Aether Systems Q4 Architecture Blueprint",
                type = DriveFileType.DOC,
                modifiedDate = "Today, 10:15 AM",
                owner = "Dr. Elena Rostova",
                size = "48 KB",
                isStarred = true,
                docContent = "# Aether Systems Architecture Blueprint\n\n## 1. Executive Summary\nAether Cloud bridges communication, document collaboration, spatial telemetry, and creative media into an interconnected, zero-latency mobile workspace.\n\n## 2. Core Pillars\n- **Unified Identity**: Single sign-on across all Aether tools with instantaneous storage allocation.\n- **Adaptive Navigation**: Triple-tier navigation (Dashboard, Bottom Dock, 9-Dot Switcher).\n- **On-Device AI**: Local neural filters for photos and instant AI overview synthesis for queries.\n\n## 3. Milestones\n- Phase 1: Core Modular Shell & Unified Theme\n- Phase 2: Live Canvas Map Navigation & Vector Routing\n- Phase 3: Collaborative Docs with Real-time Token Sync\n"
            ),
            DriveFile(
                id = "doc_2",
                name = "Aether 2026 Fiscal Forecast & Model",
                type = DriveFileType.SHEET,
                modifiedDate = "Yesterday, 2:40 PM",
                owner = "Finance Group",
                size = "132 KB",
                isStarred = true,
                docContent = "Quarterly Revenue Summary:\nQ1: $6.8M (+24% YoY)\nQ2: $8.2M (+28% YoY)\nQ3: $10.1M (+31% YoY)\nQ4 Projected: $12.5M\nOperating Margin: 44.8%"
            ),
            DriveFile(
                id = "doc_3",
                name = "Zurich Itinerary AC924.pdf",
                type = DriveFileType.PDF,
                modifiedDate = "Oct 24, 2026",
                owner = "Aether Aero",
                size = "1.8 MB",
                isStarred = true,
                docContent = "Flight Confirmation: AC-924\nDeparture: 09:45 AM (Terminal 1 Gate E12)\nArrival: Zurich Airport (ZRH)\nHotel: Grand Alpine Pavilion Zurich\nTrain Pass: Swiss Travel Pass Gold"
            ),
            DriveFile(
                id = "doc_4",
                name = "Design System Tokens & Typography",
                type = DriveFileType.FOLDER,
                modifiedDate = "Oct 21, 2026",
                owner = "Me",
                size = "--",
                isStarred = false
            ),
            DriveFile(
                id = "doc_5",
                name = "Aether Keynote: Next-Gen Creative Suite",
                type = DriveFileType.SLIDE,
                modifiedDate = "Oct 19, 2026",
                owner = "Alex Mercer",
                size = "16.4 MB",
                isStarred = false,
                docContent = "Slide 1: Simplicity at Scale\nSlide 2: Beyond Desktop Windows\nSlide 3: One Ecosystem, Infinite Workflows"
            )
        )
    )
    val driveFiles: StateFlow<List<DriveFile>> = _driveFiles.asStateFlow()

    private val _activeDoc = MutableStateFlow<DriveFile?>(null)
    val activeDoc: StateFlow<DriveFile?> = _activeDoc.asStateFlow()

    private val _editingDocTitle = MutableStateFlow("")
    val editingDocTitle: StateFlow<String> = _editingDocTitle.asStateFlow()

    private val _editingDocContent = MutableStateFlow("")
    val editingDocContent: StateFlow<String> = _editingDocContent.asStateFlow()

    fun openDoc(file: DriveFile) {
        _activeDoc.value = file
        _editingDocTitle.value = file.name
        _editingDocContent.value = file.docContent ?: ""
    }

    fun openDocByName(docName: String) {
        val found = _driveFiles.value.firstOrNull { it.name.contains(docName.substringBefore("."), ignoreCase = true) }
        if (found != null) {
            openApp(AetherAppType.DRIVE)
            openDoc(found)
        }
    }

    fun closeDoc() {
        val current = _activeDoc.value
        if (current != null) {
            _driveFiles.update { list ->
                list.map {
                    if (it.id == current.id) {
                        it.copy(
                            name = _editingDocTitle.value.ifBlank { "Untitled Document" },
                            docContent = _editingDocContent.value,
                            modifiedDate = "Just now"
                        )
                    } else it
                }
            }
        }
        _activeDoc.value = null
    }

    fun updateDocTitle(title: String) {
        _editingDocTitle.value = title
    }

    fun updateDocContent(content: String) {
        _editingDocContent.value = content
    }

    fun createNewDoc() {
        val newFile = DriveFile(
            id = "doc_${System.currentTimeMillis()}",
            name = "Untitled Document",
            type = DriveFileType.DOC,
            modifiedDate = "Just now",
            owner = _currentUser.value.name,
            size = "1 KB",
            docContent = "# New Document\n\nStart typing your ideas here..."
        )
        _driveFiles.update { listOf(newFile) + it }
        openDoc(newFile)
    }

    fun toggleStarFile(fileId: String) {
        _driveFiles.update { list ->
            list.map { if (it.id == fileId) it.copy(isStarred = !it.isStarred) else it }
        }
    }

    // ==================== MAPS & GUIDE ====================
    private val _mapPlaces = MutableStateFlow(
        listOf(
            MapPlace(
                id = "pl_1",
                name = "Aether Innovation Center",
                category = PlaceCategory.TECH,
                address = "100 Aether Way, Silicon Gateway",
                rating = 4.9f,
                reviewCount = 1840,
                distanceMiles = 0.6f,
                openStatus = "Open • Closes 8 PM",
                description = "Flagship architectural campus showcasing optical computing, interactive spatial pavilions, and organic cafes.",
                xPos = 0.48f,
                yPos = 0.40f
            ),
            MapPlace(
                id = "pl_2",
                name = "Lumina Roasters & Espresso Lab",
                category = PlaceCategory.CAFES,
                address = "42 Cypress Boulevard",
                rating = 4.8f,
                reviewCount = 720,
                distanceMiles = 0.4f,
                openStatus = "Open • Closes 7 PM",
                description = "Single-origin pourovers, cold brew flights, and tranquil outdoor cedar terrace with fast fiber internet.",
                xPos = 0.32f,
                yPos = 0.58f
            ),
            MapPlace(
                id = "pl_3",
                name = "Bayview Botanical Conservatory",
                category = PlaceCategory.PARKS,
                address = "1 Marina Promenade",
                rating = 4.9f,
                reviewCount = 3800,
                distanceMiles = 1.1f,
                openStatus = "Open 24 Hours",
                description = "Expansive glass biodome with tropical foliage, elevated canopy bridges, and illuminated evening koi ponds.",
                xPos = 0.74f,
                yPos = 0.28f
            ),
            MapPlace(
                id = "pl_4",
                name = "Kuroshio Artisan Ramen & Robata",
                category = PlaceCategory.RESTAURANTS,
                address = "88 Lantern Alley",
                rating = 4.9f,
                reviewCount = 940,
                distanceMiles = 0.7f,
                openStatus = "Open • Closes 11 PM",
                description = "Slow-simmered rich broths, handmade noodles, and seasonal robata skewers in an intimate ambiance.",
                xPos = 0.64f,
                yPos = 0.68f
            ),
            MapPlace(
                id = "pl_5",
                name = "Horizon Skydeck Observatory",
                category = PlaceCategory.SIGHTS,
                address = "500 Horizon Ridge Road",
                rating = 4.8f,
                reviewCount = 5600,
                distanceMiles = 2.2f,
                openStatus = "Open • Sunset viewing recommended",
                description = "Panoramic 360-degree overlook above the coastal fog, city skyline, and Golden Gate Bridge.",
                xPos = 0.22f,
                yPos = 0.22f
            )
        )
    )
    val mapPlaces: StateFlow<List<MapPlace>> = _mapPlaces.asStateFlow()

    private val _selectedPlace = MutableStateFlow<MapPlace?>(null)
    val selectedPlace: StateFlow<MapPlace?> = _selectedPlace.asStateFlow()

    private val _selectedPlaceCategory = MutableStateFlow(PlaceCategory.ALL)
    val selectedPlaceCategory: StateFlow<PlaceCategory> = _selectedPlaceCategory.asStateFlow()

    private val _isNavigating = MutableStateFlow(false)
    val isNavigating: StateFlow<Boolean> = _isNavigating.asStateFlow()

    private val _currentNavStepIndex = MutableStateFlow(0)
    val currentNavStepIndex: StateFlow<Int> = _currentNavStepIndex.asStateFlow()

    val navigationSteps = listOf(
        NavigationStep("Head north on Aether Way toward 2nd Ave", "400 ft", "straight"),
        NavigationStep("Turn right onto Cypress Boulevard", "0.3 mi", "turn_right"),
        NavigationStep("Merge onto Bayview Expressway North", "0.8 mi", "turn_left"),
        NavigationStep("Arrive at destination on your right", "100 ft", "destination")
    )

    fun selectPlace(place: MapPlace?) {
        _selectedPlace.value = place
        _isNavigating.value = false
    }

    fun setPlaceCategory(category: PlaceCategory) {
        _selectedPlaceCategory.value = category
    }

    fun startNavigation(place: MapPlace) {
        _selectedPlace.value = place
        _isNavigating.value = true
        _currentNavStepIndex.value = 0
    }

    fun stopNavigation() {
        _isNavigating.value = false
    }

    fun nextNavStep() {
        if (_currentNavStepIndex.value < navigationSteps.size - 1) {
            _currentNavStepIndex.value += 1
        }
    }

    // ==================== PHOTOS & CANVAS ====================
    private val _photos = MutableStateFlow(
        listOf(
            PhotoItem("ph_1", "Alpine Mist at Emerald Lake", "Lake Tahoe", "Today", "Nature", Color(0xFF1E88E5), Color(0xFFFFB300), isFavorite = true, memoryAlbum = "High Sierra Escapes"),
            PhotoItem("ph_2", "Geometric Glass Pavilion", "Downtown District", "Today", "Architecture", Color(0xFF546E7A), Color(0xFF90A4AE)),
            PhotoItem("ph_3", "Golden Hour Suspension Bridge", "San Francisco Bay", "Yesterday", "Travel", Color(0xFFE53935), Color(0xFFFB8C00), isFavorite = true, memoryAlbum = "Pacific Coast Memories"),
            PhotoItem("ph_4", "Hand-Poured Cortado & Brioche", "Lumina Roasters", "Yesterday", "Food", Color(0xFF6D4C41), Color(0xFFD7CCC8)),
            PhotoItem("ph_5", "Neon Cyberpunk Alleyways", "Tokyo Shinjuku", "3 days ago", "Travel", Color(0xFF8E24AA), Color(0xFF00ACC1), isFavorite = true, memoryAlbum = "Tokyo After Dark"),
            PhotoItem("ph_6", "Cathedral Grove Redwood Canopies", "Muir Woods", "Last week", "Nature", Color(0xFF2E7D32), Color(0xFF81C784)),
            PhotoItem("ph_7", "Golden Retriever on Sunset Beach", "Pacifica Coast", "Last week", "Pets", Color(0xFF0288D1), Color(0xFFFFCC80), isFavorite = true),
            PhotoItem("ph_8", "Starlight over Joshua Tree", "Mojave Desert", "2 weeks ago", "Nature", Color(0xFF1A237E), Color(0xFF7E57C2), memoryAlbum = "Desert Stargazing")
        )
    )
    val photos: StateFlow<List<PhotoItem>> = _photos.asStateFlow()

    private val _selectedPhoto = MutableStateFlow<PhotoItem?>(null)
    val selectedPhoto: StateFlow<PhotoItem?> = _selectedPhoto.asStateFlow()

    private val _photoMagicEnhance = MutableStateFlow(false)
    val photoMagicEnhance: StateFlow<Boolean> = _photoMagicEnhance.asStateFlow()

    private val _photoMagicEraser = MutableStateFlow(false)
    val photoMagicEraser: StateFlow<Boolean> = _photoMagicEraser.asStateFlow()

    private val _photoFilterMode = MutableStateFlow("Original")
    val photoFilterMode: StateFlow<String> = _photoFilterMode.asStateFlow()

    fun selectPhoto(photo: PhotoItem?) {
        _selectedPhoto.value = photo
        _photoMagicEnhance.value = false
        _photoMagicEraser.value = false
        _photoFilterMode.value = "Original"
    }

    fun togglePhotoFavorite(photoId: String) {
        _photos.update { list ->
            list.map { if (it.id == photoId) it.copy(isFavorite = !it.isFavorite) else it }
        }
        if (_selectedPhoto.value?.id == photoId) {
            _selectedPhoto.update { it?.copy(isFavorite = !it.isFavorite) }
        }
    }

    fun toggleMagicEnhance() {
        _photoMagicEnhance.value = !_photoMagicEnhance.value
    }

    fun toggleMagicEraser() {
        _photoMagicEraser.value = !_photoMagicEraser.value
    }

    fun setPhotoFilter(filter: String) {
        _photoFilterMode.value = filter
    }

    // ==================== CALENDAR & TASKS ====================
    private val _calendarEvents = MutableStateFlow(
        listOf(
            CalendarEvent(
                id = "ev_1",
                title = "Aether Systems Q4 Roadmap Review",
                startTime = "10:00 AM",
                endTime = "11:30 AM",
                dateText = "Today",
                color = AetherBlue,
                location = "Turing Pavilion & Aether Meet",
                attendees = listOf("Alex", "Dr. Elena", "Marcus", "Sarah"),
                notes = "Review synchronized cross-app data layer, offline vector map engine, and on-device photo filters."
            ),
            CalendarEvent(
                id = "ev_2",
                title = "Design Sync: Minimalist Clean Spatial Tokens",
                startTime = "1:00 PM",
                endTime = "1:45 PM",
                dateText = "Today",
                color = AetherTeal,
                location = "Design Lab Room 4A",
                attendees = listOf("Alex", "Kenji", "Priya")
            ),
            CalendarEvent(
                id = "ev_3",
                title = "1-on-1 with Dr. Elena Rostova",
                startTime = "2:30 PM",
                endTime = "3:15 PM",
                dateText = "Today",
                color = AetherAmber,
                location = "Aether Meet: meet.aether.cloud/elena-sync",
                attendees = listOf("Alex", "Elena")
            ),
            CalendarEvent(
                id = "ev_4",
                title = "Flight AC-924 to Zurich Airport",
                startTime = "09:45 AM",
                endTime = "06:30 AM (+1)",
                dateText = "Tomorrow",
                color = AetherCoral,
                location = "Terminal 1 Gate E12",
                attendees = listOf("Alex")
            )
        )
    )
    val calendarEvents: StateFlow<List<CalendarEvent>> = _calendarEvents.asStateFlow()

    private val _omniTasks = MutableStateFlow(
        listOf(
            OmniTask("tk_1", "Approve Q4 Architecture Blueprint in Aether Docs", "Today", isCompleted = false),
            OmniTask("tk_2", "Verify turn-by-turn routing steps for Silicon Gateway", "Today", isCompleted = false),
            OmniTask("tk_3", "Download Zurich boarding pass offline", "Tomorrow", isCompleted = true),
            OmniTask("tk_4", "Curate Pacific Coast photo memories album", "Oct 27", isCompleted = false)
        )
    )
    val omniTasks: StateFlow<List<OmniTask>> = _omniTasks.asStateFlow()

    private val _isCreatingEvent = MutableStateFlow(false)
    val isCreatingEvent: StateFlow<Boolean> = _isCreatingEvent.asStateFlow()

    fun openCreateEvent(open: Boolean) {
        _isCreatingEvent.value = open
    }

    fun addCalendarEvent(title: String, time: String, location: String, color: Color) {
        if (title.isBlank()) return
        val newEvent = CalendarEvent(
            id = "ev_${System.currentTimeMillis()}",
            title = title,
            startTime = time.ifBlank { "3:30 PM" },
            endTime = "4:30 PM",
            dateText = "Today",
            color = color,
            location = location.ifBlank { "Aether Meet Virtual" },
            attendees = listOf("Me", "Elena")
        )
        _calendarEvents.update { it + newEvent }
        _isCreatingEvent.value = false
    }

    fun toggleTask(taskId: String) {
        _omniTasks.update { list ->
            list.map { if (it.id == taskId) it.copy(isCompleted = !it.isCompleted) else it }
        }
    }

    fun addTask(title: String) {
        if (title.isBlank()) return
        val newTask = OmniTask(
            id = "tk_${System.currentTimeMillis()}",
            title = title,
            dueText = "Today"
        )
        _omniTasks.update { listOf(newTask) + it }
    }

    // Keep / Notes
    private val _isCreatingNote = MutableStateFlow(false)
    val isCreatingNote: StateFlow<Boolean> = _isCreatingNote.asStateFlow()
}
