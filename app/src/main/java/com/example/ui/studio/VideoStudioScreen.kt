package com.example.ui.studio

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.AppDataStore
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class StudioTab(val label: String) {
    FILTERS("Color Grade"),
    TIMELINE("Trim & Speed"),
    AUDIO("Audio Sync"),
    CAPTIONS("AI Subtitles"),
    VIRAL("AI Viral Tags")
}

@Composable
fun VideoStudioScreen(
    onPublished: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    var selectedAspectRatio by remember { mutableStateOf("9:16") }
    var selectedFilter by remember { mutableStateOf("Cinema Gold") }
    var playbackSpeed by remember { mutableStateOf(1.0f) }
    var timelineProgress by remember { mutableStateOf(0.45f) }
    var isPlaying by remember { mutableStateOf(true) }

    var selectedAudioTrack by remember { mutableStateOf("Tokyo Neon Drift · Synthwave") }
    var audioVolume by remember { mutableStateOf(0.85f) }

    var captionText by remember { mutableStateOf("Capturing the golden essence of midnight Tokyo in 4K HDR ✨") }
    var subtitleStyle by remember { mutableStateOf("Karaoke Gold") }

    var videoTitle by remember { mutableStateOf("Neon Odyssey: Tokyo Masterclass") }
    var selectedTags by remember { mutableStateOf(listOf("#CinemaGold", "#AURAWorldwide", "#TokyoNeo")) }
    var isTokenGated by remember { mutableStateOf(false) }

    var activeTab by remember { mutableStateOf(StudioTab.FILTERS) }

    // Export & Publish State
    var isExporting by remember { mutableStateOf(false) }
    var exportProgress by remember { mutableStateOf(0f) }
    var exportStageText by remember { mutableStateOf("") }

    // Color filter matrix based on filter choice
    val colorFilter = remember(selectedFilter) {
        when (selectedFilter) {
            "Cinema Gold" -> ColorFilter.colorMatrix(ColorMatrix().apply {
                setToScale(1.15f, 1.05f, 0.85f, 1.0f)
            })
            "Noir 35mm" -> ColorFilter.colorMatrix(ColorMatrix().apply {
                setToSaturation(0f)
            })
            "Cyber Neon" -> ColorFilter.colorMatrix(ColorMatrix().apply {
                setToScale(0.9f, 1.2f, 1.3f, 1.0f)
            })
            "Sunset Peach" -> ColorFilter.colorMatrix(ColorMatrix().apply {
                setToScale(1.2f, 0.95f, 0.9f, 1.0f)
            })
            else -> null
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBg)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // Studio Header: Title & Aspect Ratio Switcher
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "AURA Video Studio",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldLight
                        )
                        Text(
                            text = "4K HDR Multi-Track Editor",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }

                    // Aspect Ratio Toggle
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(ObsidianCard)
                            .border(1.dp, ObsidianBorder, RoundedCornerShape(20.dp))
                            .padding(2.dp),
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        listOf("9:16", "1:1", "16:9").forEach { ratio ->
                            val isSelected = selectedAspectRatio == ratio
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (isSelected) GoldPrimary else Color.Transparent)
                                    .clickable { selectedAspectRatio = ratio }
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = ratio,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) ObsidianBg else TextSecondary
                                )
                            }
                        }
                    }
                }
            }

            // Interactive Video Preview Canvas
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(
                            when (selectedAspectRatio) {
                                "16:9" -> 210.dp
                                "1:1" -> 280.dp
                                else -> 350.dp
                            }
                        )
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Black)
                        .border(1.5.dp, GoldPrimary, RoundedCornerShape(16.dp))
                        .clickable { isPlaying = !isPlaying }
                ) {
                    Image(
                        painter = painterResource(
                            id = if (selectedAspectRatio == "16:9")
                                R.drawable.sample_video_cinema_1790280942452
                            else
                                R.drawable.sample_video_fashion_1790280956373
                        ),
                        contentDescription = "Video Canvas",
                        contentScale = ContentScale.Crop,
                        colorFilter = colorFilter,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Scrim
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                androidx.compose.ui.graphics.Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.4f),
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.7f)
                                    )
                                )
                            )
                    )

                    // Active Filter Badge & Speed indicator
                    Row(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "LUT: $selectedFilter",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = ObsidianBg,
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(GoldLight)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                        Text(
                            text = "${playbackSpeed}x",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(ObsidianCard.copy(alpha = 0.8f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    // Dynamic Subtitle Overlay Preview
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 36.dp, start = 16.dp, end = 16.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (subtitleStyle == "Karaoke Gold") GoldPrimary.copy(alpha = 0.9f)
                                else Color.Black.copy(alpha = 0.75f)
                            )
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = captionText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (subtitleStyle == "Karaoke Gold") ObsidianBg else GoldLight,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }

                    // Bottom Seekbar & Timecode inside canvas
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = "Play/Pause",
                            tint = GoldPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Slider(
                            value = timelineProgress,
                            onValueChange = { timelineProgress = it },
                            modifier = Modifier.weight(1f),
                            colors = SliderDefaults.colors(
                                thumbColor = GoldPrimary,
                                activeTrackColor = GoldPrimary,
                                inactiveTrackColor = Color.White.copy(alpha = 0.3f)
                            )
                        )
                        Text(
                            text = "00:14 / 00:30",
                            fontSize = 10.sp,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Studio Feature Segmented Control Tabs
            item {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(StudioTab.values()) { tab ->
                        val isSelected = activeTab == tab
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) GoldPrimary else ObsidianElevated)
                                .border(
                                    1.dp,
                                    if (isSelected) GoldLight else ObsidianBorder,
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { activeTab = tab }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = tab.label,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) ObsidianBg else TextSecondary
                            )
                        }
                    }
                }
            }

            // Tab Content
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ObsidianCard),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .border(1.dp, ObsidianBorder, RoundedCornerShape(16.dp))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        when (activeTab) {
                            StudioTab.FILTERS -> {
                                Text(
                                    text = "Cinematic Color LUTs",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldLight
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    listOf("Cinema Gold", "Noir 35mm", "Cyber Neon", "Sunset Peach").forEach { filter ->
                                        val isSelected = selectedFilter == filter
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(if (isSelected) GoldPrimary else ObsidianElevated)
                                                .clickable { selectedFilter = filter }
                                                .padding(vertical = 10.dp)
                                        ) {
                                            Text(
                                                text = filter,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) ObsidianBg else TextPrimary,
                                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                            )
                                        }
                                    }
                                }
                            }

                            StudioTab.TIMELINE -> {
                                Text(
                                    text = "Playback Speed",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldLight
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    listOf(0.5f, 1.0f, 1.5f, 2.0f).forEach { speed ->
                                        val isSelected = playbackSpeed == speed
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(if (isSelected) GoldPrimary else ObsidianElevated)
                                                .clickable { playbackSpeed = speed }
                                                .padding(vertical = 8.dp)
                                        ) {
                                            Text(
                                                text = "${speed}x",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) ObsidianBg else TextPrimary
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = {},
                                        colors = ButtonDefaults.buttonColors(containerColor = ObsidianElevated),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("✂️ Split Clip", fontSize = 11.sp, color = TextPrimary)
                                    }
                                    Button(
                                        onClick = {},
                                        colors = ButtonDefaults.buttonColors(containerColor = ObsidianElevated),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("🔄 Reverse", fontSize = 11.sp, color = TextPrimary)
                                    }
                                }
                            }

                            StudioTab.AUDIO -> {
                                Text(
                                    text = "Beat Sync Royalty-Free Audio",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldLight
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    listOf(
                                        "Tokyo Neon Drift · Synthwave",
                                        "Golden Hour Acoustic · Studio Mix",
                                        "Cyber Synth Pulse · 128 BPM",
                                        "Midnight Reverie · Anamorphic Lofi"
                                    ).forEach { track ->
                                        val isSelected = selectedAudioTrack == track
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(if (isSelected) ObsidianElevated else ObsidianSurface)
                                                .border(
                                                    1.dp,
                                                    if (isSelected) GoldPrimary else ObsidianBorder,
                                                    RoundedCornerShape(8.dp)
                                                )
                                                .clickable { selectedAudioTrack = track }
                                                .padding(horizontal = 10.dp, vertical = 8.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                Icon(
                                                    Icons.Default.MusicNote,
                                                    contentDescription = null,
                                                    tint = if (isSelected) GoldPrimary else TextSecondary,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Text(
                                                    text = track,
                                                    fontSize = 11.sp,
                                                    color = if (isSelected) GoldLight else TextPrimary
                                                )
                                            }
                                            if (isSelected) {
                                                Text("Synced ⚡", fontSize = 10.sp, color = EmeraldSuccess, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))
                                Text("Music Volume: ${(audioVolume * 100).toInt()}%", fontSize = 11.sp, color = TextSecondary)
                                Slider(
                                    value = audioVolume,
                                    onValueChange = { audioVolume = it },
                                    colors = SliderDefaults.colors(thumbColor = GoldPrimary, activeTrackColor = GoldPrimary)
                                )
                            }

                            StudioTab.CAPTIONS -> {
                                Text(
                                    text = "AI Subtitle Generator & Styles",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldLight
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = captionText,
                                    onValueChange = { captionText = it },
                                    label = { Text("Editable Subtitle Track", fontSize = 11.sp, color = TextMuted) },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = GoldPrimary,
                                        unfocusedBorderColor = ObsidianBorder,
                                        focusedTextColor = TextPrimary,
                                        unfocusedTextColor = TextPrimary
                                    ),
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp)
                                )

                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    listOf("Karaoke Gold", "Bold Pop", "Clean").forEach { style ->
                                        val isSelected = subtitleStyle == style
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(if (isSelected) GoldPrimary else ObsidianElevated)
                                                .clickable { subtitleStyle = style }
                                                .padding(vertical = 8.dp)
                                        ) {
                                            Text(
                                                text = style,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) ObsidianBg else TextSecondary
                                            )
                                        }
                                    }
                                }
                            }

                            StudioTab.VIRAL -> {
                                Text(
                                    text = "AI Viral Title & Token-Gating",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldLight
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = videoTitle,
                                    onValueChange = { videoTitle = it },
                                    label = { Text("Video Title", fontSize = 11.sp, color = TextMuted) },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = GoldPrimary,
                                        unfocusedBorderColor = ObsidianBorder,
                                        focusedTextColor = TextPrimary,
                                        unfocusedTextColor = TextPrimary
                                    ),
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp)
                                )

                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text("Token-Gated VIP Post", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                        Text("Requires Gold Inner Circle Pass to watch", fontSize = 10.sp, color = TextSecondary)
                                    }
                                    Switch(
                                        checked = isTokenGated,
                                        onCheckedChange = { isTokenGated = it },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = ObsidianBg,
                                            checkedTrackColor = GoldPrimary
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Export & Direct Publish Button
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        isExporting = true
                        coroutineScope.launch {
                            exportStageText = "Rendering 4K Cinema Gold Grade..."
                            exportProgress = 0.25f
                            delay(600)
                            exportStageText = "Syncing Beat Waveform Audio..."
                            exportProgress = 0.60f
                            delay(600)
                            exportStageText = "Minting On-chain Metadata..."
                            exportProgress = 0.90f
                            delay(500)
                            exportProgress = 1.0f
                            delay(400)

                            // Add to global post store
                            AppDataStore.addPublishedVideo(
                                title = videoTitle,
                                tags = selectedTags,
                                aspectRatio = selectedAspectRatio,
                                audioTrack = selectedAudioTrack,
                                isTokenGated = isTokenGated
                            )
                            isExporting = false
                            onPublished()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(52.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.Upload,
                            contentDescription = "Publish",
                            tint = ObsidianBg
                        )
                        Text(
                            text = "Render & Publish to Worldwide Feed",
                            color = ObsidianBg,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        // Export Progress Dialog
        if (isExporting) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.85f)),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = ObsidianCard),
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .border(1.5.dp, GoldPrimary, RoundedCornerShape(20.dp))
                        .padding(20.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth().padding(16.dp)
                    ) {
                        CircularProgressIndicator(
                            progress = { exportProgress },
                            color = GoldPrimary,
                            trackColor = ObsidianBorder,
                            modifier = Modifier.size(54.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Exporting 4K Video",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldLight
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = exportStageText,
                            fontSize = 12.sp,
                            color = TextSecondary,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
