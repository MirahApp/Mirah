package mirah.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mirah.ui.screens.LibraryScreen
import mirah.ui.screens.BrowseScreen
import mirah.ui.screens.HistoryScreen

val MirahRed = Color(0xFFCC0000)
val SurfaceDark = Color(0xFF0D0D0D)
val SurfaceContainer = Color(0xFF1A1A1A)
val OnSurfaceMuted = Color(0xFF938F99)
val OnSurface = Color(0xFFE6E1E5)

@Composable
fun MirahApp() {
    val colorScheme = darkColorScheme(
        primary = MirahRed,
        background = SurfaceDark,
        surface = SurfaceContainer,
        onSurface = OnSurface,
        onSurfaceVariant = OnSurfaceMuted
    )

    var selectedItem by remember { mutableStateOf(0) }
    val navItems = listOf(
        NavEntry("Library", Icons.Default.CollectionsBookmark),
        NavEntry("Browse", Icons.Default.Explore),
        NavEntry("History", Icons.Default.History),
        NavEntry("More", Icons.Default.Settings)
    )

    MaterialTheme(colorScheme = colorScheme) {
        Surface(modifier = Modifier.fillMaxSize(), color = SurfaceDark) {
            Row(modifier = Modifier.fillMaxSize()) {
                // Sidebar
                Column(
                    modifier = Modifier
                        .width(72.dp)
                        .fillMaxHeight()
                        .background(SurfaceDark),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource("icon-1024.png"),
                        contentDescription = "Mirah",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .padding(top = 16.dp, bottom = 24.dp)
                            .size(32.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Main nav items
                    SidebarItem(
                        entry = navItems[0],
                        isSelected = selectedItem == 0,
                        onClick = { selectedItem = 0 }
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    SidebarItem(
                        entry = navItems[1],
                        isSelected = selectedItem == 1,
                        onClick = { selectedItem = 1 }
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    SidebarItem(
                        entry = navItems[2],
                        isSelected = selectedItem == 2,
                        onClick = { selectedItem = 2 }
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    // Bottom item
                    SidebarItem(
                        entry = navItems[3],
                        isSelected = selectedItem == 3,
                        onClick = { selectedItem = 3 }
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // Vertical Divider
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .fillMaxHeight()
                        .background(SurfaceContainer)
                )

                // Content Area
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(SurfaceDark)
                ) {
                    when (selectedItem) {
                        0 -> LibraryScreen()
                        1 -> BrowseScreen()
                        2 -> HistoryScreen()
                        else -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = navItems[selectedItem].name,
                                    color = OnSurface,
                                    style = MaterialTheme.typography.headlineLarge
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SidebarItem(
    entry: NavEntry,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val contentColor = if (isSelected) MirahRed else OnSurfaceMuted
    
    Column(
        modifier = Modifier
            .width(72.dp)
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(width = 56.dp, height = 40.dp)
                .clip(RoundedCornerShape(50))
                .background(if (isSelected) MirahRed.copy(alpha = 0.15f) else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = entry.icon,
                contentDescription = entry.name,
                modifier = Modifier.size(24.dp),
                tint = contentColor
            )
        }
        
        Text(
            text = entry.name,
            color = contentColor,
            fontSize = 10.sp,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

private data class NavEntry(val name: String, val icon: ImageVector)
