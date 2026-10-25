package com.example.ui.components

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.model.AetherAppType

@Composable
fun AetherBottomNav(
    activeApp: AetherAppType,
    onSelectApp: (AetherAppType) -> Unit,
    onOpenWaffle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val navItems = listOf(
        AetherAppType.HOME,
        AetherAppType.SEARCH,
        AetherAppType.MAIL,
        AetherAppType.DRIVE,
        AetherAppType.MAPS
    )

    NavigationBar(
        modifier = modifier
            .navigationBarsPadding()
            .testTag("aether_bottom_nav"),
        tonalElevation = 3.dp,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        navItems.forEach { app ->
            val selected = activeApp == app
            NavigationBarItem(
                selected = selected,
                onClick = { onSelectApp(app) },
                icon = {
                    BadgedBox(
                        badge = {
                            if (app.badgeCount > 0) {
                                Badge { Text("${app.badgeCount}") }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = app.icon,
                            contentDescription = app.title,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                label = { Text(app.title.removePrefix("Aether ")) },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = app.brandColor.copy(alpha = 0.16f),
                    selectedIconColor = app.brandColor,
                    selectedTextColor = app.brandColor,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }

        // 6th item: All Apps (Waffle)
        NavigationBarItem(
            selected = false,
            onClick = onOpenWaffle,
            icon = {
                Icon(
                    imageVector = Icons.Default.Apps,
                    contentDescription = "All Aether Apps",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text("All Apps") },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}
