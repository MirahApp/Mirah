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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mirah.ui.MirahRed
import mirah.ui.OnSurface
import mirah.ui.OnSurfaceMuted

@Composable
fun MoreScreen() {
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
                text = "More",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = OnSurface
            )
        }

        // Settings List
        LazyColumn(modifier = Modifier.weight(1f)) {
            item {
                SectionHeader("Library")
                SettingsItem(
                    title = "Download location",
                    subtitle = "Not set",
                    icon = Icons.Default.Folder
                )
                SettingsItem(
                    title = "Auto-update library",
                    subtitle = "Every 12 hours",
                    icon = Icons.Default.Sync
                )
            }

            item {
                SectionHeader("Reader")
                SettingsItem(
                    title = "Default reading mode",
                    subtitle = "Right to left",
                    icon = Icons.AutoMirrored.Filled.MenuBook
                )
                SettingsItem(
                    title = "Display mode",
                    subtitle = "Landscape",
                    icon = Icons.Default.Fullscreen
                )
            }

            item {
                SectionHeader("About")
                SettingsItem(
                    title = "Version",
                    subtitle = "0.1.0-alpha",
                    icon = Icons.Default.Info
                )
                SettingsItem(
                    title = "Check for updates",
                    icon = Icons.Default.SystemUpdate
                )
                SettingsItem(
                    title = "Open source licenses",
                    icon = Icons.Default.Code
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        color = MirahRed,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 16.dp, top = 20.dp, bottom = 8.dp)
    )
}

@Composable
private fun SettingsItem(
    title: String,
    subtitle: String? = null,
    icon: ImageVector
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            modifier = Modifier.size(22.dp),
            tint = OnSurfaceMuted
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = title, color = OnSurface, fontSize = 14.sp)
            if (subtitle != null) {
                Text(text = subtitle, color = OnSurfaceMuted, fontSize = 12.sp)
            }
        }
    }
}
