package mirah.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mirah.local.LocalManga
import mirah.ui.MirahRed
import org.jetbrains.skia.Image as SkiaImage
import java.util.zip.ZipFile

@Composable
fun ReaderScreen(manga: LocalManga, onBack: () -> Unit) {
    var pages by remember { mutableStateOf<List<ByteArray>>(emptyList()) }
    var currentPage by remember { mutableStateOf(0) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val loadedPages = withContext(Dispatchers.IO) {
            try {
                ZipFile(manga.file).use { zip ->
                    zip.entries().asSequence()
                        .filter { entry ->
                            val name = entry.name.lowercase()
                            !entry.isDirectory && (name.endsWith(".jpg") ||
                                name.endsWith(".jpeg") || name.endsWith(".png") ||
                                name.endsWith(".webp"))
                        }
                        .sortedBy { it.name }
                        .map { entry -> zip.getInputStream(entry).use { it.readBytes() } }
                        .toList()
                }
            } catch (e: Exception) {
                emptyList()
            }
        }
        pages = loadedPages
        isLoading = false
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MirahRed
            )
        } else if (pages.isNotEmpty()) {
            // Page Display
            val bitmap = remember(currentPage, pages) {
                SkiaImage.makeFromEncoded(pages[currentPage]).toComposeImageBitmap()
            }
            Image(
                bitmap = bitmap,
                contentDescription = "Page ${currentPage + 1}",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )

            // Navigation Zones
            Box(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.33f)
                        .align(Alignment.CenterStart)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { if (currentPage > 0) currentPage-- }
                        )
                )
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.33f)
                        .align(Alignment.CenterEnd)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { if (currentPage < pages.size - 1) currentPage++ }
                        )
                )
            }
        }

        // Top Overlay
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .background(Color(0xBB000000))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Text(
                    text = manga.title,
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "${if (pages.isEmpty()) 0 else currentPage + 1} / ${pages.size}",
                    color = Color.White,
                    fontSize = 13.sp
                )
            }
        }

        // Bottom Overlay
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color(0xBB000000))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "< Prev",
                    color = Color.White,
                    fontSize = 13.sp,
                    modifier = Modifier.clickable { if (currentPage > 0) currentPage-- }
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Next >",
                    color = Color.White,
                    fontSize = 13.sp,
                    modifier = Modifier.clickable { if (currentPage < pages.size - 1) currentPage++ }
                )
            }
        }
    }
}
