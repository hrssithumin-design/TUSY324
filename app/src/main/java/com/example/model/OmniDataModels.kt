package com.example.model

import androidx.compose.ui.graphics.Color

// ==================== SEARCH & DISCOVER ====================
data class DiscoverArticle(
    val id: String,
    val title: String,
    val source: String,
    val timeAgo: String,
    val category: String,
    val summary: String,
    val readMinutes: Int,
    val isSaved: Boolean = false
)

data class SearchResultItem(
    val title: String,
    val url: String,
    val snippet: String,
    val category: String
)

data class AiOverview(
    val query: String,
    val answer: String,
    val keyPoints: List<String>,
    val sources: List<String>
)

// ==================== MAIL ====================
enum class MailCategory {
    PRIMARY, SOCIAL, UPDATES, PROMOTIONS
}

data class EmailMessage(
    val id: String,
    val senderName: String,
    val senderEmail: String,
    val subject: String,
    val preview: String,
    val body: String,
    val timestamp: String,
    val category: MailCategory = MailCategory.PRIMARY,
    val isUnread: Boolean = true,
    val isStarred: Boolean = false,
    val avatarColor: Color = Color(0xFF1A73E8),
    val hasAttachment: Boolean = false,
    val attachmentName: String? = null
)

// ==================== DRIVE & DOCS ====================
enum class DriveFileType {
    DOC, SHEET, SLIDE, FOLDER, PDF, IMAGE
}

data class DriveFile(
    val id: String,
    val name: String,
    val type: DriveFileType,
    val modifiedDate: String,
    val owner: String,
    val size: String,
    val isStarred: Boolean = false,
    val docContent: String? = null
)

// ==================== MAPS ====================
enum class PlaceCategory {
    ALL, RESTAURANTS, CAFES, TECH, PARKS, SIGHTS
}

data class MapPlace(
    val id: String,
    val name: String,
    val category: PlaceCategory,
    val address: String,
    val rating: Float,
    val reviewCount: Int,
    val distanceMiles: Float,
    val openStatus: String,
    val description: String,
    val xPos: Float, // relative 0.0 - 1.0 on canvas
    val yPos: Float  // relative 0.0 - 1.0 on canvas
)

data class NavigationStep(
    val instruction: String,
    val distanceText: String,
    val iconType: String // "turn_left", "turn_right", "straight", "destination"
)

// ==================== PHOTOS ====================
data class PhotoItem(
    val id: String,
    val title: String,
    val location: String,
    val dateText: String,
    val tag: String,
    val primaryColor: Color,
    val secondaryColor: Color,
    val isFavorite: Boolean = false,
    val memoryAlbum: String? = null
)

// ==================== CALENDAR & TASKS ====================
data class CalendarEvent(
    val id: String,
    val title: String,
    val startTime: String,
    val endTime: String,
    val dateText: String,
    val color: Color,
    val location: String = "Omni Meet Virtual",
    val attendees: List<String> = listOf("Alex", "Sarah", "Liam"),
    val notes: String = ""
)

data class OmniTask(
    val id: String,
    val title: String,
    val dueText: String,
    val isCompleted: Boolean = false
)

// ==================== KEEP NOTES ====================
data class NoteChecklistItem(
    val text: String,
    val isChecked: Boolean = false
)

data class KeepNote(
    val id: String,
    val title: String,
    val content: String,
    val bgColorHex: Long = 0xFFFFFFFF,
    val isPinned: Boolean = false,
    val tags: List<String> = emptyList(),
    val checklist: List<NoteChecklistItem> = emptyList(),
    val updatedAt: String = "Today"
)
