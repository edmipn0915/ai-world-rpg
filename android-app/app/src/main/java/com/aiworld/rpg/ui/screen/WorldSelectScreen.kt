package com.aiworld.rpg.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aiworld.rpg.domain.model.WorldType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorldSelectScreen(
    onWorldSelected: (WorldType) -> Unit,
    onLoadGame: () -> Unit,
    onSettings: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("幻境 AI World") },
                actions = {
                    IconButton(onClick = onSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "設定")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "選擇你的世界",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            WorldTypeCard(
                title = "奇幻世界",
                description = "魔法、龍、中世紀冒險",
                icon = Icons.Default.Star,
                onClick = { onWorldSelected(WorldType.FANTASY) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            WorldTypeCard(
                title = "科幻世界",
                description = "太空、未來科技、賽博龐克",
                icon = Icons.Default.Public,
                onClick = { onWorldSelected(WorldType.SCI_FI) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            WorldTypeCard(
                title = "現實世界",
                description = "模擬現實生活、社交關係",
                icon = Icons.Default.Cloud,
                onClick = { onWorldSelected(WorldType.REALITY) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            WorldTypeCard(
                title = "自訂世界",
                description = "自由定義你的世界觀",
                icon = Icons.Default.Storage,
                onClick = { onWorldSelected(WorldType.CUSTOM) }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onLoadGame,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("讀取存檔")
            }
        }
    }
}

@Composable
private fun WorldTypeCard(
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
