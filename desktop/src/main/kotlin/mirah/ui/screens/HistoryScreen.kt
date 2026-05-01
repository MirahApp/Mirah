package mirah.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HistoryScreen() {
    val items = List(10) { it }

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
                text = "History",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = OnSurface,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = {}) {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = OnSurface)
            }
            IconButton(onClick = {}) {
                Icon(Icons.Default.DeleteSweep, contentDescription = "Clear History", tint = OnSurface)
            }
        }

        // History Content
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items.chunked(3).forEachIndexed { chunkIndex, chunk ->
                stickyHeader {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF0D0D0D))
                            .padding(start = 16.dp, top = 12.dp, bottom = 4.dp)
                    ) {
                        Text(
                            text = if (chunkIndex == 0) "Today" else "Yesterday",
                            color = OnSurfaceMuted,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                items(chunk) { originalIndex ->
                    HistoryItem(originalIndex)
                }
            }
        }
    }
}

@Composable
private fun HistoryItem(index: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(width = 50.dp, height = 70.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color(0xFF2A2A2A))
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = "Manga Title $index", color = OnSurface, fontSize = 14.sp)
            Text(text = "Chapter ${index + 1}", color = OnSurfaceMuted, fontSize = 12.sp)
            Text(text = "2 hours ago", color = OnSurfaceMuted, fontSize = 11.sp)
        }
        Text(text = "Resume", color = MirahRed, fontSize = 12.sp)
    }
}
