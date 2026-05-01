package mirah.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mirah.ui.MirahRed
import mirah.ui.OnSurface
import mirah.ui.OnSurfaceMuted

@Composable
fun BrowseScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Sources", "Extensions", "Migrate")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0D0D))
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Browse",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = OnSurface,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = {}) {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = OnSurface)
            }
            IconButton(onClick = {}) {
                Icon(Icons.Default.Settings, contentDescription = "Settings", tint = OnSurface)
            }
        }

        // Tab Row
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color(0xFF0D0D0D),
            contentColor = MirahRed,
            divider = {},
            indicator = { tabPositions ->
                if (selectedTab < tabPositions.size) {
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = MirahRed
                    )
                }
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            color = if (selectedTab == index) OnSurface else OnSurfaceMuted,
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                )
            }
        }

        // Tab Content
        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            when (selectedTab) {
                0 -> SourcesTab()
                else -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = tabs[selectedTab], color = OnSurfaceMuted)
                    }
                }
            }
        }
    }
}

@Composable
private fun SourcesTab() {
    val sources = listOf("MangaDex", "MangaPlus", "Viz", "ComiXology", "ReadComicOnline")
    
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(sources) { source ->
            SourceItem(source)
        }
    }
}

@Composable
private fun SourceItem(name: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF2A2A2A))
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = name, color = OnSurface, fontSize = 14.sp)
            Text(text = "English", color = OnSurfaceMuted, fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(text = "Latest", color = MirahRed, fontSize = 13.sp)
    }
}
