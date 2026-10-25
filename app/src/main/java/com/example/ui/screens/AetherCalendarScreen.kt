package com.example.ui.screens

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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CalendarEvent
import com.example.model.OmniTask
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AetherCalendarScreen(
    events: List<CalendarEvent>,
    tasks: List<OmniTask>,
    isCreatingEvent: Boolean,
    onOpenCreateEvent: (Boolean) -> Unit,
    onAddEvent: (title: String, time: String, location: String, color: Color) -> Unit,
    onToggleTask: (String) -> Unit,
    onAddTask: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedDayIndex by remember { mutableStateOf(1) } // 0: Yesterday, 1: Today, 2: Tomorrow, 3: Oct 28
    var newTaskText by remember { mutableStateOf("") }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Month Header & Day Strip
            item {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "October 2026",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        IconButton(onClick = { onOpenCreateEvent(true) }) {
                            Icon(
                                imageVector = Icons.Default.AddCircle,
                                contentDescription = "Add Event",
                                tint = AetherBlue,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Date selector row
                    val days = listOf(
                        Pair("Sun", "25"),
                        Pair("Mon", "26"),
                        Pair("Tue", "27"),
                        Pair("Wed", "28"),
                        Pair("Thu", "29"),
                        Pair("Fri", "30")
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        days.forEachIndexed { idx, pair ->
                            val isSelected = idx == selectedDayIndex
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(
                                        if (isSelected) AetherBlue else MaterialTheme.colorScheme.surface
                                    )
                                    .border(
                                        1.dp,
                                        if (isSelected) AetherBlue else MaterialTheme.colorScheme.outlineVariant,
                                        RoundedCornerShape(16.dp)
                                    )
                                    .clickable { selectedDayIndex = idx }
                                    .padding(vertical = 10.dp, horizontal = 12.dp)
                            ) {
                                Text(
                                    text = pair.first,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = pair.second,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            // Timeline Schedule Header
            item {
                Text(
                    text = "Scheduled Agenda",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Events List
            items(events, key = { it.id }) { event ->
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 1.dp,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .width(4.dp)
                                .height(56.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(event.color)
                        )

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${event.startTime} - ${event.endTime}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = event.color
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = event.title,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = "Location",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = event.location,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            // Integrated Aether Tasks Section
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Aether Tasks",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "${tasks.count { it.isCompleted }}/${tasks.size} Completed",
                                fontSize = 12.sp,
                                color = AetherTeal,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Quick task add input
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = newTaskText,
                                onValueChange = { newTaskText = it },
                                placeholder = { Text("Add a new task...") },
                                singleLine = true,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(
                                onClick = {
                                    if (newTaskText.isNotBlank()) {
                                        onAddTask(newTaskText)
                                        newTaskText = ""
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AddCircle,
                                    contentDescription = "Add Task",
                                    tint = AetherBlue
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        tasks.forEach { task ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onToggleTask(task.id) }
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = task.isCompleted,
                                    onCheckedChange = { onToggleTask(task.id) },
                                    colors = CheckboxDefaults.colors(checkedColor = AetherTeal)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = task.title,
                                    fontSize = 13.sp,
                                    color = if (task.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
        }

        // New Event Creator Dialog
        if (isCreatingEvent) {
            CreateEventSheet(
                onDismiss = { onOpenCreateEvent(false) },
                onAdd = onAddEvent
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateEventSheet(
    onDismiss: () -> Unit,
    onAdd: (title: String, time: String, location: String, color: Color) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("3:00 PM") }
    var location by remember { mutableStateOf("Aether Meet Virtual") }
    var selectedColor by remember { mutableStateOf(AetherBlue) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "New Calendar Event",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Button(
                    onClick = { onAdd(title, time, location, selectedColor) },
                    colors = ButtonDefaults.buttonColors(containerColor = AetherBlue),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.testTag("save_event_button")
                ) {
                    Text("Save")
                }
            }

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Event Title") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = time,
                onValueChange = { time = it },
                label = { Text("Time (e.g. 3:00 PM)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Location or Meet Link") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Text(text = "Color Tag", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                listOf(AetherBlue, AetherTeal, AetherAmber, AetherCoral, AetherPurple).forEach { c ->
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(c)
                            .border(
                                2.dp,
                                if (selectedColor == c) Color.Black else Color.Transparent,
                                CircleShape
                            )
                            .clickable { selectedColor = c }
                    )
                }
            }
        }
    }
}
