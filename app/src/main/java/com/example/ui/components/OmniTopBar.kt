package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AetherAppType
import com.example.model.UserProfile
import com.example.ui.theme.*

@Composable
fun AetherTopBar(
    activeApp: AetherAppType,
    currentUser: UserProfile,
    onOpenWaffle: () -> Unit,
    onOpenAccount: () -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left: Aether Brand Emblem & Current App Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onOpenWaffle() }
                    .padding(horizontal = 6.dp, vertical = 4.dp)
            ) {
                AetherBrandEmblem(size = 28.dp)
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Aether",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (activeApp != AetherAppType.HOME) {
                            Text(
                                text = " • " + activeApp.title.removePrefix("Aether "),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 15.sp,
                                color = activeApp.brandColor
                            )
                        } else {
                            Text(
                                text = " Cloud",
                                fontWeight = FontWeight.Normal,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Right: Search, 9-Dot Waffle Menu, and Account Avatar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                IconButton(
                    onClick = onSearchClick,
                    modifier = Modifier
                        .size(40.dp)
                        .testTag("top_bar_search_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(
                    onClick = onOpenWaffle,
                    modifier = Modifier
                        .size(40.dp)
                        .testTag("waffle_menu_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Apps,
                        contentDescription = "Aether Apps Launcher",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(currentUser.avatarBgColor)
                        .clickable { onOpenAccount() }
                        .testTag("account_avatar_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = currentUser.initials,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
fun AetherBrandEmblem(size: androidx.compose.ui.unit.Dp = 24.dp, modifier: Modifier = Modifier) {
    // Minimalist geometric overlapping rings
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(
                brush = Brush.linearGradient(
                    listOf(AetherBlue, AetherTeal, AetherPurple)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(size * 0.45f)
                .clip(CircleShape)
                .background(Color.White)
        )
    }
}
