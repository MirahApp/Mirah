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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mirah.extensions.ApkExtensionInstaller
import mirah.extensions.ExtensionRepository
import mirah.extensions.RemoteExtension
import mirah.ui.MirahRed
import mirah.ui.OnSurface
import mirah.ui.OnSurfaceMuted
import mirah.ui.SurfaceContainer
import java.io.File

@Composable
fun BrowseScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Sources", "Extensions", "Migrate")

    var extensions by remember { mutableStateOf<List<RemoteExtension>>(emptyList()) }
    var isLoadingExtensions by remember { mutableStateOf(false) }
    var installedPackageNames by remember { mutableStateOf<Set<String>>(emptySet()) }
    var installingPackage by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        isLoadingExtensions = true
        val fetched: List<RemoteExtension> = withContext(Dispatchers.IO) {
            ExtensionRepository.fetchExtensions()
        }
        val installed: Set<String> = withContext(Dispatchers.IO) {
            ApkExtensionInstaller.getInstalledPackageNames().toSet()
        }
        extensions = fetched
        installedPackageNames = installed
        isLoadingExtensions = false
    }

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
                1 -> {
                    ExtensionsTab(
                        extensions = extensions,
                        isLoading = isLoadingExtensions,
                        installedPackageNames = installedPackageNames,
                        installingPackage = installingPackage,
                        errorMessage = errorMessage,
                        onInstall = { extension ->
                            scope.launch {
                                try {
                                    installingPackage = extension.packageName
                                    val downloadedApk = withContext(Dispatchers.IO) {
                                        ExtensionRepository.downloadApk(
                                            extension,
                                            File(System.getProperty("user.home"), ".mirah/downloads")
                                        )
                                    }
                                    withContext(Dispatchers.IO) {
                                        ApkExtensionInstaller.install(downloadedApk)
                                    }
                                    installedPackageNames = installedPackageNames + extension.packageName
                                    installingPackage = null
                                } catch (e: Exception) {
                                    errorMessage = "Failed to install ${extension.name}: ${e.message}"
                                    installingPackage = null
                                }
                            }
                        }
                    )
                }
                2 -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "Migrate", color = OnSurfaceMuted)
                    }
                }
            }
        }
    }
}

@Composable
private fun ExtensionsTab(
    extensions: List<RemoteExtension>,
    isLoading: Boolean,
    installedPackageNames: Set<String>,
    installingPackage: String?,
    errorMessage: String?,
    onInstall: (RemoteExtension) -> Unit
) {
    when {
        isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MirahRed)
            }
        }
        errorMessage != null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = errorMessage, color = OnSurfaceMuted)
            }
        }
        extensions.isEmpty() -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "No extensions found", color = OnSurfaceMuted)
            }
        }
        else -> {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(extensions) { extension ->
                    ExtensionItem(
                        extension = extension,
                        isInstalled = installedPackageNames.contains(extension.packageName),
                        isInstalling = installingPackage == extension.packageName,
                        onInstall = { onInstall(extension) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ExtensionItem(
    extension: RemoteExtension,
    isInstalled: Boolean,
    isInstalling: Boolean,
    onInstall: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(SurfaceContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = extension.lang.first().uppercase(),
                color = OnSurfaceMuted,
                fontSize = 14.sp
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = extension.name, color = OnSurface, fontSize = 14.sp)
            Text(text = "${extension.lang} • ${extension.versionName}", color = OnSurfaceMuted, fontSize = 12.sp)
        }
        
        when {
            isInstalling -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MirahRed,
                    strokeWidth = 2.dp
                )
            }
            isInstalled -> {
                Text(text = "Installed", color = OnSurfaceMuted, fontSize = 12.sp)
            }
            else -> {
                TextButton(onClick = onInstall) {
                    Text(text = "Install", color = MirahRed)
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
