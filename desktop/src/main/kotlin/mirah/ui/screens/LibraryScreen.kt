package mirah.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mirah.local.LocalLibraryScanner
import mirah.local.LocalManga
import mirah.ui.MirahRed
import mirah.ui.OnSurface
import mirah.ui.OnSurfaceMuted
import mirah.ui.SurfaceContainer
import org.jetbrains.skia.Image as SkiaImage
import java.io.File

@Composable
fun LibraryScreen() {
    var mangaList by remember { mutableStateOf<List<LocalManga>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var selectedManga by remember { mutableStateOf<LocalManga?>(null) }

    LaunchedEffect(Unit) {
        isLoading = true
        val userHome = System.getProperty("user.home")
        val libraryFolder = File(userHome, "Mirah/Library")
        if (!libraryFolder.exists()) {
            libraryFolder.mkdirs()
        }
        val result = withContext(Dispatchers.IO) {
            LocalLibraryScanner.scanFolder(libraryFolder)
        }
        mangaList = result
        isLoading = false
    }

    if (selectedManga != null) {
        MangaDetailScreen(
            manga = selectedManga!!,
            onBack = { selectedManga = null }
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0D0D0D))
        ) {
            // Manual Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Library",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = OnSurface,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Search, contentDescription = "Search", tint = OnSurface)
                }
                IconButton(onClick = {}) {
                    Icon(Icons.Default.FilterList, contentDescription = "Filter", tint = OnSurface)
                }
            }

            if (mangaList.isEmpty() && !isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Your library is empty",
                            color = OnSurfaceMuted,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Add CBZ or ZIP files to ~/Mirah/Library",
                            color = OnSurfaceMuted,
                            fontSize = 13.sp
                        )
                    }
                }
            } else {
                // Grid
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 160.dp),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(0.dp),
                    verticalArrangement = Arrangement.spacedBy(3.dp),
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    items(mangaList) { manga ->
                        MangaGridItem(
                            title = manga.title,
                            unreadCount = 0,
                            coverImage = manga.coverImage,
                            onClick = { selectedManga = manga }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MangaGridItem(
    title: String,
    unreadCount: Int,
    coverImage: ByteArray?,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(2f / 3f)
            .clip(RoundedCornerShape(8.dp))
            .background(SurfaceContainer)
            .clickable(onClick = onClick)
    ) {
        // Cover Image
        if (coverImage != null) {
            val bitmap = remember(coverImage) {
                SkiaImage.makeFromEncoded(coverImage).toComposeImageBitmap()
            }
            Image(
                bitmap = bitmap,
                contentDescription = title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Scrim and Title
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, Color(0xDD000000))
                    )
                ),
            contentAlignment = Alignment.BottomStart
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(6.dp)
            )
        }

        // Unread Badge
        if (unreadCount > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 6.dp, top = 6.dp)
                    .defaultMinSize(minWidth = 24.dp, minHeight = 16.dp)
                    .background(MirahRed, RoundedCornerShape(50))
                    .padding(horizontal = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = unreadCount.toString(),
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
