package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PhotoItem
import com.example.ui.theme.*

@Composable
fun AetherPhotosScreen(
    photos: List<PhotoItem>,
    selectedPhoto: PhotoItem?,
    magicEnhance: Boolean,
    magicEraser: Boolean,
    filterMode: String,
    onSelectPhoto: (PhotoItem?) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onToggleEnhance: () -> Unit,
    onToggleEraser: () -> Unit,
    onSetFilter: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        if (selectedPhoto != null) {
            // Fullscreen Photo Editor & AI Tools
            PhotoDetailViewer(
                photo = selectedPhoto,
                magicEnhance = magicEnhance,
                magicEraser = magicEraser,
                filterMode = filterMode,
                onBack = { onSelectPhoto(null) },
                onToggleFavorite = { onToggleFavorite(selectedPhoto.id) },
                onToggleEnhance = onToggleEnhance,
                onToggleEraser = onToggleEraser,
                onSetFilter = onSetFilter
            )
        } else {
            // Photos Grid & Memories
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Memories Highlights Carousel
                item {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Aether Memories",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Auto Curated",
                                fontSize = 12.sp,
                                color = AetherAmber,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        val memoryPhotos = photos.filter { it.memoryAlbum != null }
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(memoryPhotos) { photo ->
                                Surface(
                                    shape = RoundedCornerShape(20.dp),
                                    modifier = Modifier
                                        .size(width = 130.dp, height = 170.dp)
                                        .clickable { onSelectPhoto(photo) },
                                    border = BorderStroke(2.dp, AetherAmber.copy(alpha = 0.4f)),
                                    shadowElevation = 3.dp
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(
                                                Brush.verticalGradient(
                                                    listOf(photo.primaryColor, photo.secondaryColor)
                                                )
                                            )
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(
                                                    Brush.verticalGradient(
                                                        listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                                                    )
                                                )
                                                .padding(12.dp),
                                            verticalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Surface(
                                                shape = CircleShape,
                                                color = Color.Black.copy(alpha = 0.3f)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.AutoAwesome,
                                                    contentDescription = "Memory",
                                                    tint = Color.White,
                                                    modifier = Modifier
                                                        .size(24.dp)
                                                        .padding(4.dp)
                                                )
                                            }

                                            Column {
                                                Text(
                                                    text = photo.memoryAlbum ?: "Memory",
                                                    color = Color.White,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    maxLines = 2,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                Text(
                                                    text = photo.dateText,
                                                    color = Color.White.copy(alpha = 0.8f),
                                                    fontSize = 10.sp
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // All Photos Header
                item {
                    Text(
                        text = "Recent Photos",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Photos Grid
                items(photos.chunked(2)) { pair ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        pair.forEach { photo ->
                            PhotoCardItem(
                                photo = photo,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(150.dp)
                                    .clickable { onSelectPhoto(photo) }
                            )
                        }
                        if (pair.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PhotoCardItem(photo: PhotoItem, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 2.dp,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(listOf(photo.primaryColor, photo.secondaryColor))
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f))
                        )
                    )
                    .padding(10.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                if (photo.isFavorite) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Favorite",
                        tint = AetherCoral,
                        modifier = Modifier.size(16.dp)
                    )
                } else {
                    Spacer(modifier = Modifier.size(16.dp))
                }

                Column {
                    Text(
                        text = photo.title,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${photo.location} • ${photo.dateText}",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun PhotoDetailViewer(
    photo: PhotoItem,
    magicEnhance: Boolean,
    magicEraser: Boolean,
    filterMode: String,
    onBack: () -> Unit,
    onToggleFavorite: () -> Unit,
    onToggleEnhance: () -> Unit,
    onToggleEraser: () -> Unit,
    onSetFilter: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Top action bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            Row {
                IconButton(onClick = onToggleFavorite) {
                    Icon(
                        imageVector = if (photo.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (photo.isFavorite) AetherCoral else Color.White
                    )
                }
                IconButton(onClick = { /* Share */ }) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = Color.White
                    )
                }
            }
        }

        // Photo Display Canvas
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(
                    when (filterMode) {
                        "Noir" -> Brush.linearGradient(listOf(Color(0xFF212121), Color(0xFF757575)))
                        "Warm" -> Brush.linearGradient(listOf(Color(0xFFE65100), Color(0xFFFFB300)))
                        "Cool" -> Brush.linearGradient(listOf(Color(0xFF0D47A1), Color(0xFF00ACC1)))
                        "Vivid" -> Brush.linearGradient(listOf(Color(0xFFD50000), Color(0xFF00C853)))
                        else -> Brush.linearGradient(listOf(photo.primaryColor, photo.secondaryColor))
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Landscape,
                    contentDescription = "Photo Preview",
                    tint = Color.White.copy(alpha = 0.85f),
                    modifier = Modifier.size(96.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = photo.title,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "${photo.location} • Taken ${photo.dateText}",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 13.sp
                )

                if (magicEnhance) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = AetherAmber.copy(alpha = 0.9f)
                    ) {
                        Text(
                            text = "✨ AI HDR Enhanced (+40% Contrast)",
                            color = Color.Black,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                if (magicEraser) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = AetherTeal.copy(alpha = 0.9f)
                    ) {
                        Text(
                            text = "🪄 Magic Eraser: Background distractions removed",
                            color = Color.Black,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        // AI Magic Tools Toolbar
        Surface(
            color = Color(0xFF1E2024),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Aether Neural Magic Tools",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Magic Auto-Enhance Button
                    Button(
                        onClick = onToggleEnhance,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (magicEnhance) AetherAmber else Color(0xFF2C3038)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Enhance",
                            tint = if (magicEnhance) Color.Black else Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Enhance",
                            color = if (magicEnhance) Color.Black else Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Magic Object Eraser Button
                    Button(
                        onClick = onToggleEraser,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (magicEraser) AetherTeal else Color(0xFF2C3038)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CleaningServices,
                            contentDescription = "Eraser",
                            tint = if (magicEraser) Color.Black else Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Eraser",
                            color = if (magicEraser) Color.Black else Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Color Preset Filters
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(listOf("Original", "Warm", "Cool", "Vivid", "Noir")) { filter ->
                        val isSelected = filter == filterMode
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) AetherBlue else Color(0xFF2C3038),
                            modifier = Modifier.clickable { onSetFilter(filter) }
                        ) {
                            Text(
                                text = filter,
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
