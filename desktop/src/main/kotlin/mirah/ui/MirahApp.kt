package mirah.ui

import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MirahApp() {
    val mirahRed = Color(0xFFCC0000)
    val sidebarBg = Color(0xFF111111)
    val contentBg = Color(0xFF161616)
    val inactiveIcon = Color(0xFF888888)
    val textColor = Color(0xFFEEEEEE)

    val colorScheme = darkColorScheme(
        primary = mirahRed,
        background = sidebarBg,
        surface = contentBg
    )

    var selectedItem by remember { mutableStateOf(0) }
    val navItems = listOf(
        NavEntry("Library", Icons.Default.CollectionsBookmark),
        NavEntry("Browse", Icons.Default.Explore),
        NavEntry("History", Icons.Default.History),
        NavEntry("More", Icons.Default.Settings)
    )

    MaterialTheme(colorScheme = colorScheme) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.fillMaxSize()) {
                // Sidebar
                Column(
                    modifier = Modifier
                        .width(56.dp)
                        .fillMaxHeight()
                        .background(sidebarBg),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "म",
                        color = mirahRed,
                        fontSize = 22.sp,
                        modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
                    )

                    androidx.compose.foundation.layout.Spacer(modifier = Modifier.weight(1f))

                    // Middle items (Library, Browse, History)
                    listOf(0, 1, 2).forEach { index ->
                        val item = navItems[index]
                        val isSelected = selectedItem == index
                        TooltipArea(
                            tooltip = {
                                Surface(
                                    modifier = Modifier.padding(8.dp),
                                    shape = RoundedCornerShape(4.dp),
                                    color = Color.DarkGray
                                ) {
                                    Text(
                                        text = item.name,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        color = Color.White,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) mirahRed else Color.Transparent)
                                    .clickable { selectedItem = index },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.name,
                                    modifier = Modifier.size(24.dp),
                                    tint = if (isSelected) Color.White else inactiveIcon
                                )
                            }
                        }
                        if (index < 2) {
                            androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(8.dp))
                        }
                    }

                    androidx.compose.foundation.layout.Spacer(modifier = Modifier.weight(1f))

                    // Bottom item (Settings)
                    val settingsIndex = 3
                    val settingsItem = navItems[settingsIndex]
                    val isSettingsSelected = selectedItem == settingsIndex
                    TooltipArea(
                        tooltip = {
                            Surface(
                                modifier = Modifier.padding(8.dp),
                                shape = RoundedCornerShape(4.dp),
                                color = Color.DarkGray
                            ) {
                                Text(
                                    text = settingsItem.name,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    color = Color.White,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSettingsSelected) mirahRed else Color.Transparent)
                                .clickable { selectedItem = settingsIndex },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = settingsItem.icon,
                                contentDescription = settingsItem.name,
                                modifier = Modifier.size(24.dp),
                                tint = if (isSettingsSelected) Color.White else inactiveIcon
                            )
                        }
                    }
                    androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(16.dp))
                }

                // Content Area
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(contentBg),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = navItems[selectedItem].name,
                        color = textColor,
                        fontSize = 24.sp
                    )
                }
            }
        }
    }
}

private data class NavEntry(val name: String, val icon: ImageVector)
(val name: String, val icon: ImageVector)
