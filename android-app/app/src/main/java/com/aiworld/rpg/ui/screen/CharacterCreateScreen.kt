package com.aiworld.rpg.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterCreateScreen(
    worldTypeName: String,
    onCharacterCreated: (String) -> Unit,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var selectedRace by remember { mutableStateOf("人類") }
    var selectedClass by remember { mutableStateOf("戰士") }
    var backstory by remember { mutableStateOf("") }

    val races = listOf("精靈", "矮人", "人類", "獸人", "龍族")
    val classes = listOf("戰士", "法師", "盜賊", "牧師", "遊俠")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("創建角色") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "世界類型：$worldTypeName",
                style = MaterialTheme.typography.titleMedium
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("角色名稱") },
                modifier = Modifier.fillMaxWidth()
            )

            var raceExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = raceExpanded,
                onExpandedChange = { raceExpanded = !raceExpanded }
            ) {
                OutlinedTextField(
                    value = selectedRace,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("種族") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = raceExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = raceExpanded,
                    onDismissRequest = { raceExpanded = false }
                ) {
                    races.forEach { race ->
                        DropdownMenuItem(
                            text = { Text(race) },
                            onClick = {
                                selectedRace = race
                                raceExpanded = false
                            }
                        )
                    }
                }
            }

            var classExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = classExpanded,
                onExpandedChange = { classExpanded = !classExpanded }
            ) {
                OutlinedTextField(
                    value = selectedClass,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("職業") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = classExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = classExpanded,
                    onDismissRequest = { classExpanded = false }
                ) {
                    classes.forEach { className ->
                        DropdownMenuItem(
                            text = { Text(className) },
                            onClick = {
                                selectedClass = className
                                classExpanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = backstory,
                onValueChange = { backstory = it },
                label = { Text("背景故事（可選）") },
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val saveId = "save-${System.currentTimeMillis()}"
                    onCharacterCreated(saveId)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = name.isNotBlank()
            ) {
                Text("開始冒險")
            }
        }
    }
}
