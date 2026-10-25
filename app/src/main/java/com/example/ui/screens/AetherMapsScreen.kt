package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MapPlace
import com.example.model.NavigationStep
import com.example.model.PlaceCategory
import com.example.ui.theme.*

@Composable
fun AetherMapsScreen(
    places: List<MapPlace>,
    selectedPlace: MapPlace?,
    selectedCategory: PlaceCategory,
    isNavigating: Boolean,
    navStepIndex: Int,
    navSteps: List<NavigationStep>,
    onSelectPlace: (MapPlace?) -> Unit,
    onSelectCategory: (PlaceCategory) -> Unit,
    onStartNavigation: (MapPlace) -> Unit,
    onStopNavigation: () -> Unit,
    onNextNavStep: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        // Custom Interactive Vector Canvas Map
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(places) {
                    detectTapGestures { tapOffset ->
                        val w = size.width
                        val h = size.height
                        // Check if tap hit any place marker within 36dp
                        val hit = places.firstOrNull { place ->
                            val px = place.xPos * w
                            val py = place.yPos * h
                            val dist = kotlin.math.hypot((tapOffset.x - px).toDouble(), (tapOffset.y - py).toDouble())
                            dist < 60
                        }
                        onSelectPlace(hit)
                    }
                }
        ) {
            val w = size.width
            val h = size.height

            // Base Map Land Surface
            drawRect(color = Color(0xFFF4F6F9))

            // Water Body (Bay at top right)
            val waterPath = Path().apply {
                moveTo(w * 0.7f, 0f)
                cubicTo(w * 0.65f, h * 0.2f, w * 0.85f, h * 0.35f, w, h * 0.32f)
                lineTo(w, 0f)
                close()
            }
            drawPath(path = waterPath, color = Color(0xFFD6E4F0))

            // Green Parks
            drawRoundRect(
                color = Color(0xFFE2F0D9),
                topLeft = Offset(w * 0.68f, h * 0.22f),
                size = androidx.compose.ui.geometry.Size(w * 0.26f, h * 0.16f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(24f, 24f)
            )
            drawRoundRect(
                color = Color(0xFFE2F0D9),
                topLeft = Offset(w * 0.12f, h * 0.15f),
                size = androidx.compose.ui.geometry.Size(w * 0.22f, h * 0.18f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(24f, 24f)
            )

            // Street Grid Network
            val roadColor = Color(0xFFFFFFFF)
            val roadOutline = Color(0xFFE5E9F0)

            // Horizontal Streets
            listOf(0.18f, 0.34f, 0.50f, 0.65f, 0.82f).forEach { yRatio ->
                drawLine(
                    color = roadOutline,
                    start = Offset(0f, h * yRatio),
                    end = Offset(w, h * yRatio),
                    strokeWidth = 14f
                )
                drawLine(
                    color = roadColor,
                    start = Offset(0f, h * yRatio),
                    end = Offset(w, h * yRatio),
                    strokeWidth = 10f
                )
            }

            // Vertical Avenues
            listOf(0.18f, 0.38f, 0.58f, 0.78f).forEach { xRatio ->
                drawLine(
                    color = roadOutline,
                    start = Offset(w * xRatio, 0f),
                    end = Offset(w * xRatio, h),
                    strokeWidth = 14f
                )
                drawLine(
                    color = roadColor,
                    start = Offset(w * xRatio, 0f),
                    end = Offset(w * xRatio, h),
                    strokeWidth = 10f
                )
            }

            // Curved Arterial Highway
            val highwayPath = Path().apply {
                moveTo(0f, h * 0.75f)
                cubicTo(w * 0.35f, h * 0.70f, w * 0.5f, h * 0.45f, w, h * 0.38f)
            }
            drawPath(
                path = highwayPath,
                color = Color(0xFFFFD54F),
                style = Stroke(width = 12f, cap = StrokeCap.Round)
            )

            // If navigating: Draw Glowing Blue Route
            if (isNavigating && selectedPlace != null) {
                val routePath = Path().apply {
                    moveTo(w * 0.48f, h * 0.82f) // Starting point
                    lineTo(w * 0.38f, h * 0.82f)
                    lineTo(w * 0.38f, h * 0.50f)
                    lineTo(selectedPlace.xPos * w, selectedPlace.yPos * h)
                }
                drawPath(
                    path = routePath,
                    color = AetherBlue.copy(alpha = 0.3f),
                    style = Stroke(width = 18f, cap = StrokeCap.Round)
                )
                drawPath(
                    path = routePath,
                    color = AetherBlue,
                    style = Stroke(
                        width = 8f,
                        cap = StrokeCap.Round,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(25f, 12f))
                    )
                )
            }

            // User Current Location Pulse Beacon
            val userPos = Offset(w * 0.48f, h * 0.82f)
            drawCircle(color = AetherBlue.copy(alpha = 0.2f), radius = 28f, center = userPos)
            drawCircle(color = Color.White, radius = 12f, center = userPos)
            drawCircle(color = AetherBlue, radius = 8f, center = userPos)

            // Place POI Pins
            places.forEach { place ->
                val pos = Offset(place.xPos * w, place.yPos * h)
                val isSelected = place.id == selectedPlace?.id
                val pinColor = when (place.category) {
                    PlaceCategory.TECH -> AetherBlue
                    PlaceCategory.CAFES -> AetherAmber
                    PlaceCategory.PARKS -> AetherTeal
                    PlaceCategory.RESTAURANTS -> AetherCoral
                    PlaceCategory.SIGHTS -> AetherPurple
                    else -> AetherBlue
                }

                if (isSelected) {
                    drawCircle(color = pinColor.copy(alpha = 0.3f), radius = 32f, center = pos)
                }
                drawCircle(color = Color.White, radius = 16f, center = pos)
                drawCircle(color = pinColor, radius = 12f, center = pos)
            }
        }

        // Top Category Filter Chips (Hidden during active Navigation HUD)
        if (!isNavigating) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(PlaceCategory.entries.toTypedArray()) { cat ->
                        val isSelected = cat == selectedCategory
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) AetherTeal else MaterialTheme.colorScheme.surface,
                            shadowElevation = 2.dp,
                            border = if (!isSelected) BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant) else null,
                            modifier = Modifier.clickable { onSelectCategory(cat) }
                        ) {
                            Text(
                                text = cat.name.lowercase().replaceFirstChar { it.uppercase() },
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        }

        // Navigation Mode Active HUD Banner
        if (isNavigating && selectedPlace != null) {
            val step = navSteps[navStepIndex.coerceIn(0, navSteps.size - 1)]
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.TopCenter)
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = AetherTeal,
                    shadowElevation = 6.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = when (step.iconType) {
                                    "turn_right" -> Icons.Default.TurnRight
                                    "turn_left" -> Icons.Default.TurnLeft
                                    "destination" -> Icons.Default.Place
                                    else -> Icons.Default.Straight
                                },
                                contentDescription = "Step Icon",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = step.instruction,
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "In ${step.distanceText} • Step ${navStepIndex + 1} of ${navSteps.size}",
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Navigation Controls Bar
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "ETA: 8 min (${selectedPlace.distanceMiles} mi)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Fastest route • Light traffic",
                                fontSize = 11.sp,
                                color = AetherTeal
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            if (navStepIndex < navSteps.size - 1) {
                                Button(
                                    onClick = onNextNavStep,
                                    colors = ButtonDefaults.buttonColors(containerColor = AetherTeal),
                                    shape = RoundedCornerShape(50)
                                ) {
                                    Text("Next Step")
                                }
                            }
                            OutlinedButton(
                                onClick = onStopNavigation,
                                shape = RoundedCornerShape(50)
                            ) {
                                Text("Exit")
                            }
                        }
                    }
                }
            }
        }

        // Place Detail Card (Shown when place is selected & not yet navigating)
        if (selectedPlace != null && !isNavigating) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 8.dp,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = selectedPlace.name,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "${selectedPlace.rating} ★",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AetherAmber
                                )
                                Text(
                                    text = " (${selectedPlace.reviewCount} reviews) • ${selectedPlace.distanceMiles} mi away",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        IconButton(
                            onClick = { onSelectPlace(null) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = selectedPlace.description,
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "${selectedPlace.openStatus} • ${selectedPlace.address}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { onStartNavigation(selectedPlace) },
                            colors = ButtonDefaults.buttonColors(containerColor = AetherTeal),
                            shape = RoundedCornerShape(50),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("start_navigation_button")
                        ) {
                            Icon(imageVector = Icons.Default.Navigation, contentDescription = "Navigate")
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Start GPS Route")
                        }

                        OutlinedButton(
                            onClick = { /* Save POI */ },
                            shape = RoundedCornerShape(50)
                        ) {
                            Icon(imageVector = Icons.Default.BookmarkBorder, contentDescription = "Save")
                        }
                    }
                }
            }
        }
    }
}
