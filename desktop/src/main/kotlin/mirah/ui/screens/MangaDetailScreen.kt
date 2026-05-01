package mirah.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mirah.local.LocalManga
import mirah.ui.MirahRed
import mirah.ui.OnSurface
import mirah.ui.OnSurfaceMuted
import mirah.ui.SurfaceContainer
import org.jetbrains.skia.Image as SkiaImage
import java.util.zip.ZipFile

@Composable
fun MangaDetailScreen(manga: LocalManga, onBack: () -> Unit) {
    var showReader by remember { mutableStateOf(false) }

    if (showReader) {
        ReaderScreen(manga = manga, onBack = { showReader = false })
        return
    }

    val pageCount = remember(manga.file) {
        try {
            ZipFile(manga.file).use { zip ->
                zip.entries().asSequence().count { entry ->
                    val name = entry.name.lowercase()
                    !entry.isDirectory && (
                        name.endsWith(".jpg") || 
                        name.endsWith(".jpeg") || 
                        name.endsWith(".png") || 
                        name.endsWith(".webp")
                    )
                }
            }
        } catch (e: Exception) {
            0
        }
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
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = OnSurface)
            }
            Text(
                text = manga.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = OnSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
        }

        // Hero Section
        Row(
            modifier = Modifier
                .padding(16.dp)
                .height(200.dp)
        ) {
            if (manga.coverImage != null) {
                val bitmap = remember(manga.coverImage) {
                    SkiaImage.makeFromEncoded(manga.coverImage).toComposeImageBitmap()
                }
                Image(
                    bitmap = bitmap,
                    contentDescription = manga.title,
                    modifier = Modifier
                        .width(130.dp)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .width(130.dp)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(8.dp))
                        .background(SurfaceContainer)
                )
            }

            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Text(
                    text = manga.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = OnSurface
                )
                Text(
                    text = manga.file.name,
                    fontSize = 12.sp,
                    color = OnSurfaceMuted
                )
                Text(
                    text = "Local source",
                    fontSize = 12.sp,
                    color = OnSurfaceMuted
                )
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = { showReader = true },
                    colors = ButtonDefaults.buttonColors(containerColor = MirahRed),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Read", color = Color.White)
                }
            }
        }

        // Divider
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(0xFF2A2A2A))
        )

        // Chapter List Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Chapters",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = OnSurface
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "1 chapter",
                fontSize = 12.sp,
                color = OnSurfaceMuted
            )
        }

        // Chapter List
        LazyColumn(modifier = Modifier.weight(1f)) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 16.dp)
                        .clickable { showReader = true },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Chapter 1",
                        fontSize = 14.sp,
                        color = OnSurface
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "$pageCount pages",
                        fontSize = 12.sp,
                        color = OnSurfaceMuted
                    )
                }
            }
        }
    }
}
