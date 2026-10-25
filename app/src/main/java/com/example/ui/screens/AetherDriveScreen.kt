package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DriveFile
import com.example.model.DriveFileType
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AetherDriveScreen(
    files: List<DriveFile>,
    activeDoc: DriveFile?,
    editingTitle: String,
    editingContent: String,
    onOpenDoc: (DriveFile) -> Unit,
    onCloseDoc: () -> Unit,
    onUpdateTitle: (String) -> Unit,
    onUpdateContent: (String) -> Unit,
    onCreateDoc: () -> Unit,
    onToggleStar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        if (activeDoc != null) {
            // Interactive Full Document Editor
            AetherDocEditor(
                title = editingTitle,
                content = editingContent,
                onTitleChange = onUpdateTitle,
                onContentChange = onUpdateContent,
                onClose = onCloseDoc
            )
        } else {
            // Drive Files Repository
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                // Top Storage Summary Bar
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 1.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CloudQueue,
                                contentDescription = "Storage",
                                tint = AetherPurple,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Aether Drive • 9.8 GB / 15 GB Used",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = AetherPurple.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = "${files.size} Files",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = AetherPurple,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                // Files List
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        Text(
                            text = "Cloud Workspace Files",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    items(files, key = { it.id }) { file ->
                        DriveFileCard(
                            file = file,
                            onClick = { onOpenDoc(file) },
                            onToggleStar = { onToggleStar(file.id) }
                        )
                    }
                }
            }

            // Create New Doc FAB
            FloatingActionButton(
                onClick = onCreateDoc,
                containerColor = AetherPurple,
                contentColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(4.dp),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp)
                    .testTag("create_doc_fab")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "New Document")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("New Doc", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun DriveFileCard(
    file: DriveFile,
    onClick: () -> Unit,
    onToggleStar: () -> Unit
) {
    val typeColor = when (file.type) {
        DriveFileType.DOC -> AetherBlue
        DriveFileType.SHEET -> AetherTeal
        DriveFileType.SLIDE -> AetherAmber
        DriveFileType.PDF -> AetherCoral
        DriveFileType.FOLDER -> AetherIndigo
        DriveFileType.IMAGE -> AetherPurple
    }

    val typeIcon = when (file.type) {
        DriveFileType.DOC -> Icons.Default.Description
        DriveFileType.SHEET -> Icons.Default.TableChart
        DriveFileType.SLIDE -> Icons.Default.Slideshow
        DriveFileType.PDF -> Icons.Default.PictureAsPdf
        DriveFileType.FOLDER -> Icons.Default.Folder
        DriveFileType.IMAGE -> Icons.Default.Image
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(typeColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = typeIcon,
                    contentDescription = file.type.name,
                    tint = typeColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = file.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${file.modifiedDate} • ${file.owner} • ${file.size}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(onClick = onToggleStar) {
                Icon(
                    imageVector = if (file.isStarred) Icons.Default.Star else Icons.Default.StarBorder,
                    contentDescription = "Star",
                    tint = if (file.isStarred) AetherAmber else MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun AetherDocEditor(
    title: String,
    content: String,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onClose: () -> Unit
) {
    val wordCount = remember(content) {
        content.trim().split("\\s+".toRegex()).count { it.isNotBlank() }
    }
    val charCount = remember(content) { content.length }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Document Top Bar
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back to Drive"
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextField(
                        value = title,
                        onValueChange = onTitleChange,
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        textStyle = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(AetherTeal)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Saved to Aether Cloud • $wordCount words",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Done",
                        tint = AetherTeal
                    )
                }
            }
        }

        // Formatting Quick Bar
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.fillMaxWidth()
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                item {
                    EditorFormatChip("H1") {
                        onContentChange("$content\n\n# ")
                    }
                }
                item {
                    EditorFormatChip("H2") {
                        onContentChange("$content\n\n## ")
                    }
                }
                item {
                    EditorFormatChip("Bold") {
                        onContentChange("$content**bold**")
                    }
                }
                item {
                    EditorFormatChip("Bullet") {
                        onContentChange("$content\n- ")
                    }
                }
                item {
                    EditorFormatChip("Task") {
                        onContentChange("$content\n[ ] ")
                    }
                }
            }
        }

        // Document Body Editor
        TextField(
            value = content,
            onValueChange = onContentChange,
            placeholder = { Text("Start typing your document content here...") },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
        )
    }
}

@Composable
private fun EditorFormatChip(label: String, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}
