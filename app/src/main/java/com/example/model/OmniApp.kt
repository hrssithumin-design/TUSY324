package com.example.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.ui.theme.AetherAmber
import com.example.ui.theme.AetherBlue
import com.example.ui.theme.AetherCoral
import com.example.ui.theme.AetherIndigo
import com.example.ui.theme.AetherPurple
import com.example.ui.theme.AetherTeal

enum class AetherAppType(
    val title: String,
    val subtitle: String,
    val brandColor: Color,
    val icon: ImageVector,
    val badgeCount: Int = 0
) {
    HOME(
        title = "Aether Hub",
        subtitle = "Dashboard & Briefing",
        brandColor = AetherIndigo,
        icon = Icons.Default.Dashboard
    ),
    SEARCH(
        title = "Aether Pulse",
        subtitle = "Search & AI Overview",
        brandColor = AetherBlue,
        icon = Icons.Default.Search
    ),
    MAIL(
        title = "Aether Mail",
        subtitle = "Priority Inbox",
        brandColor = AetherCoral,
        icon = Icons.Default.Email,
        badgeCount = 3
    ),
    DRIVE(
        title = "Aether Docs",
        subtitle = "Drive & Document Editor",
        brandColor = AetherPurple,
        icon = Icons.Default.Description
    ),
    MAPS(
        title = "Aether Guide",
        subtitle = "Maps & Turn-by-Turn GPS",
        brandColor = AetherTeal,
        icon = Icons.Default.Explore
    ),
    PHOTOS(
        title = "Aether Canvas",
        subtitle = "Photos & AI Magic Tools",
        brandColor = AetherAmber,
        icon = Icons.Default.PhotoLibrary
    ),
    CALENDAR(
        title = "Aether Chrono",
        subtitle = "Calendar & Task Planner",
        brandColor = AetherBlue,
        icon = Icons.Default.CalendarMonth,
        badgeCount = 2
    )
}

data class UserProfile(
    val id: String,
    val name: String,
    val email: String,
    val initials: String,
    val avatarBgColor: Color,
    val usedStorageGb: Float,
    val totalStorageGb: Float = 15.0f,
    val isWorkAccount: Boolean = false
)
