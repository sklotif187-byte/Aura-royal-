package com.example.ui.chat

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.R
import com.example.data.AppDataStore
import com.example.model.ChatConversation
import com.example.model.ChatMessage
import com.example.model.Creator
import com.example.ui.components.TipCreatorDialog
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun ChatScreen() {
    var selectedConversation by remember { mutableStateOf<ChatConversation?>(null) }
    var activeCallType by remember { mutableStateOf<String?>(null) } // "AUDIO", "VIDEO"
    var tipTargetCreator by remember { mutableStateOf<Creator?>(null) }

    if (selectedConversation != null) {
        ActiveChatView(
            conversation = selectedConversation!!,
            onBack = { selectedConversation = null },
            onStartAudioCall = { activeCallType = "AUDIO" },
            onStartVideoCall = { activeCallType = "VIDEO" },
            onTipClick = { tipTargetCreator = selectedConversation!!.otherUser }
        )
    } else {
        ChatListView(
            conversations = AppDataStore.conversations,
            onSelectConversation = { selectedConversation = it }
        )
    }

    // Audio Call Modal
    if (activeCallType == "AUDIO" && selectedConversation != null) {
        AudioCallDialog(
            creator = selectedConversation!!.otherUser,
            onDismiss = { activeCallType = null }
        )
    }

    // Video Call Modal
    if (activeCallType == "VIDEO" && selectedConversation != null) {
        VideoCallDialog(
            creator = selectedConversation!!.otherUser,
            onDismiss = { activeCallType = null }
        )
    }

    // Tip Modal
    tipTargetCreator?.let { creator ->
        TipCreatorDialog(
            creator = creator,
            onDismiss = { tipTargetCreator = null }
        )
    }
}

@Composable
fun ChatListView(
    conversations: List<ChatConversation>,
    onSelectConversation: (ChatConversation) -> Unit
) {
    var selectedTab by remember { mutableStateOf("All Chats") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBg)
            .padding(bottom = 80.dp)
    ) {
        // Chat Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "AURA Encrypted Chat",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldLight
                )
                Text(
                    text = "WhatsApp-grade P2P & Group Vaults",
                    fontSize = 11.sp,
                    color = TextSecondary
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(ObsidianElevated)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Icon(Icons.Default.Lock, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(12.dp))
                Text("E2E Encrypted", fontSize = 10.sp, color = EmeraldSuccess, fontWeight = FontWeight.Bold)
            }
        }

        // Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("All Chats", "VIP Backstage", "Creator Collabs").forEach { tab ->
                val isSelected = selectedTab == tab
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(18.dp))
                        .background(if (isSelected) GoldPrimary else ObsidianElevated)
                        .clickable { selectedTab = tab }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = tab,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) ObsidianBg else TextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Conversations List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(conversations) { conv ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(ObsidianCard)
                        .border(1.dp, ObsidianBorder, RoundedCornerShape(14.dp))
                        .clickable { onSelectConversation(conv) }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box {
                        Image(
                            painter = painterResource(id = conv.otherUser.avatarRes),
                            contentDescription = conv.otherUser.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .border(1.5.dp, GoldPrimary, CircleShape)
                        )
                        if (conv.otherUser.isLive) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(EmeraldSuccess)
                                    .border(1.5.dp, ObsidianBg, CircleShape)
                            )
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = conv.otherUser.name,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                if (conv.isVipChannel) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("👑", fontSize = 11.sp)
                                }
                            }
                            Text(
                                text = conv.lastMessageTime,
                                fontSize = 10.sp,
                                color = TextMuted
                            )
                        }

                        Spacer(modifier = Modifier.height(3.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = conv.lastMessage,
                                fontSize = 12.sp,
                                color = TextSecondary,
                                maxLines = 1,
                                modifier = Modifier.weight(1f)
                            )
                            if (conv.unreadCount > 0) {
                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clip(CircleShape)
                                        .background(GoldPrimary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${conv.unreadCount}",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ObsidianBg
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ActiveChatView(
    conversation: ChatConversation,
    onBack: () -> Unit,
    onStartAudioCall: () -> Unit,
    onStartVideoCall: () -> Unit,
    onTipClick: () -> Unit
) {
    var messageInput by remember { mutableStateOf("") }
    var isRecordingVoice by remember { mutableStateOf(false) }
    var recordingDuration by remember { mutableStateOf(0) }

    // Live Voice recording timer
    LaunchedEffect(isRecordingVoice) {
        if (isRecordingVoice) {
            recordingDuration = 0
            while (isRecordingVoice) {
                delay(1000)
                recordingDuration++
            }
        }
    }

    val messages = remember { mutableStateListOf(*conversation.messages.toTypedArray()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBg)
    ) {
        // Active Chat Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(ObsidianSurface)
                .statusBarsPadding()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = onBack, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                }

                Image(
                    painter = painterResource(id = conversation.otherUser.avatarRes),
                    contentDescription = conversation.otherUser.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .border(1.dp, GoldPrimary, CircleShape)
                )

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = conversation.otherUser.name,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(13.dp))
                    }
                    Text("Online · Tokyo Studio", fontSize = 10.sp, color = EmeraldSuccess)
                }
            }

            // Real-time Action buttons: Audio Call, Video Call, Tip
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(onClick = onTipClick, modifier = Modifier.size(34.dp)) {
                    Icon(Icons.Default.Bolt, contentDescription = "Tip", tint = GoldLight, modifier = Modifier.size(20.dp))
                }
                IconButton(onClick = onStartAudioCall, modifier = Modifier.size(34.dp)) {
                    Icon(Icons.Default.Call, contentDescription = "Audio Call", tint = TextPrimary, modifier = Modifier.size(20.dp))
                }
                IconButton(onClick = onStartVideoCall, modifier = Modifier.size(34.dp)) {
                    Icon(Icons.Default.Videocam, contentDescription = "Video Call", tint = GoldPrimary, modifier = Modifier.size(22.dp))
                }
            }
        }

        HorizontalDivider(color = ObsidianBorder, thickness = 1.dp)

        // Chat Bubble Feed
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 14.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // End-to-End Encryption Notice
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🔒 Messages & calls are end-to-end encrypted. No one outside of this chat can read or listen.",
                        fontSize = 10.sp,
                        color = TextMuted,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(ObsidianCard)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            items(messages) { msg ->
                ChatBubble(message = msg)
            }
        }

        // Bottom Input Row & Voice Recording Controller
        Surface(
            color = ObsidianSurface,
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (isRecordingVoice) {
                    // Active Recording Visualizer & Timer
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(24.dp))
                            .background(CrimsonAlert.copy(alpha = 0.15f))
                            .border(1.dp, CrimsonAlert, RoundedCornerShape(24.dp))
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(CrimsonAlert)
                            )
                            Text(
                                text = "Recording: 0:${if (recordingDuration < 10) "0$recordingDuration" else "$recordingDuration"}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = CrimsonAlert
                            )
                        }

                        Text(
                            text = "Release to send",
                            fontSize = 10.sp,
                            color = TextSecondary
                        )
                    }

                    // Stop & Send Voice Note
                    IconButton(
                        onClick = {
                            isRecordingVoice = false
                            val newVoiceNote = ChatMessage(
                                id = "vn_" + System.currentTimeMillis(),
                                senderId = "creator_me",
                                text = null,
                                timestamp = "Just now",
                                isFromMe = true,
                                isVoiceNote = true,
                                voiceDurationSeconds = recordingDuration.coerceAtLeast(3),
                                voiceWaveform = listOf(0.3f, 0.6f, 0.9f, 0.4f, 0.8f, 1.0f, 0.7f, 0.5f, 0.8f, 0.3f)
                            )
                            messages.add(newVoiceNote)
                        },
                        modifier = Modifier
                            .size(42.dp)
                            .background(GoldPrimary, CircleShape)
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "Send Voice", tint = ObsidianBg, modifier = Modifier.size(18.dp))
                    }
                } else {
                    // Regular Text & Mic Input
                    IconButton(
                        onClick = {},
                        modifier = Modifier
                            .size(36.dp)
                            .background(ObsidianElevated, CircleShape)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Attach", tint = TextSecondary, modifier = Modifier.size(20.dp))
                    }

                    OutlinedTextField(
                        value = messageInput,
                        onValueChange = { messageInput = it },
                        placeholder = { Text("Message...", fontSize = 12.sp, color = TextMuted) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GoldPrimary,
                            unfocusedBorderColor = ObsidianBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(24.dp)
                    )

                    if (messageInput.isNotBlank()) {
                        IconButton(
                            onClick = {
                                val newMsg = ChatMessage(
                                    id = "msg_" + System.currentTimeMillis(),
                                    senderId = "creator_me",
                                    text = messageInput,
                                    timestamp = "Just now",
                                    isFromMe = true
                                )
                                messages.add(newMsg)
                                messageInput = ""
                            },
                            modifier = Modifier
                                .size(40.dp)
                                .background(GoldPrimary, CircleShape)
                        ) {
                            Icon(Icons.Default.Send, contentDescription = "Send", tint = ObsidianBg, modifier = Modifier.size(18.dp))
                        }
                    } else {
                        // Voice Message Record Button
                        IconButton(
                            onClick = { isRecordingVoice = true },
                            modifier = Modifier
                                .size(40.dp)
                                .background(ObsidianElevated, CircleShape)
                                .border(1.dp, GoldPrimary, CircleShape)
                        ) {
                            Icon(Icons.Default.Mic, contentDescription = "Record Voice Note", tint = GoldLight, modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    var isPlayingVoice by remember { mutableStateOf(false) }
    var playbackSpeed by remember { mutableStateOf("1x") }

    val alignment = if (message.isFromMe) Alignment.End else Alignment.Start
    val bubbleColor = if (message.isFromMe) ObsidianElevated else ObsidianCard
    val borderColor = if (message.isFromMe) GoldMuted else ObsidianBorder

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = alignment
    ) {
        Card(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (message.isFromMe) 16.dp else 2.dp,
                bottomEnd = if (message.isFromMe) 2.dp else 16.dp
            ),
            colors = CardDefaults.cardColors(containerColor = bubbleColor),
            modifier = Modifier
                .widthIn(max = 280.dp)
                .border(1.dp, borderColor, RoundedCornerShape(16.dp))
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                if (message.isVoiceNote) {
                    // WhatsApp-style Voice Note Player
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = { isPlayingVoice = !isPlayingVoice },
                            modifier = Modifier
                                .size(36.dp)
                                .background(GoldPrimary, CircleShape)
                        ) {
                            Icon(
                                imageVector = if (isPlayingVoice) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = "Play Voice Note",
                                tint = ObsidianBg,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // Simulated audio wave bars
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            (message.voiceWaveform.ifEmpty { listOf(0.4f, 0.7f, 1f, 0.6f, 0.8f, 0.3f, 0.7f, 0.5f) }).forEach { barHeight ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height((barHeight * 22).dp)
                                        .clip(RoundedCornerShape(2.dp))
                                        .background(if (isPlayingVoice) GoldPrimary else TextMuted)
                                )
                            }
                        }

                        // Speed toggle
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(ObsidianSurface)
                                .clickable {
                                    playbackSpeed = when (playbackSpeed) {
                                        "1x" -> "1.5x"
                                        "1.5x" -> "2x"
                                        else -> "1x"
                                    }
                                }
                                .padding(horizontal = 5.dp, vertical = 2.dp)
                        ) {
                            Text(playbackSpeed, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = GoldLight)
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "0:${message.voiceDurationSeconds}",
                        fontSize = 10.sp,
                        color = TextSecondary
                    )
                } else if (message.tipAmountUsd != null) {
                    // Direct Crypto Tip Bubble
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(GoldPrimary.copy(alpha = 0.15f))
                            .padding(8.dp)
                    ) {
                        Text("🪙", fontSize = 20.sp)
                        Column {
                            Text(
                                text = "On-Chain Tip Sent",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldLight
                            )
                            Text(
                                text = "+$${message.tipAmountUsd.toInt()} USDC Transferred",
                                fontSize = 11.sp,
                                color = TextPrimary
                            )
                        }
                    }
                    message.text?.let {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(it, fontSize = 12.sp, color = TextPrimary)
                    }
                } else {
                    message.text?.let {
                        Text(it, fontSize = 13.sp, color = TextPrimary, lineHeight = 18.sp)
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Timestamp & WhatsApp Double Checkmark
                Row(
                    modifier = Modifier.align(Alignment.End),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Text(
                        text = message.timestamp,
                        fontSize = 9.sp,
                        color = TextMuted
                    )
                    if (message.isFromMe) {
                        // Double Gold Ticks
                        Row {
                            Icon(Icons.Default.DoneAll, contentDescription = "Read", tint = GoldPrimary, modifier = Modifier.size(13.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VideoCallDialog(
    creator: Creator,
    onDismiss: () -> Unit
) {
    var isMuted by remember { mutableStateOf(false) }
    var isCameraOff by remember { mutableStateOf(false) }
    var isGoldFilterOn by remember { mutableStateOf(true) }
    var callDurationSeconds by remember { mutableStateOf(42) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            callDurationSeconds++
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            // Video Stream
            Image(
                painter = painterResource(id = R.drawable.sample_video_fashion_1790280956373),
                contentDescription = "Video Call Feed",
                contentScale = ContentScale.Crop,
                colorFilter = if (isGoldFilterOn) ColorFilter.colorMatrix(ColorMatrix().apply {
                    setToScale(1.15f, 1.05f, 0.85f, 1.0f)
                }) else null,
                modifier = Modifier.fillMaxSize()
            )

            // Top Header: Duration & Security
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = creator.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(14.dp))
                    }
                    Text(
                        text = "0${callDurationSeconds / 60}:${if (callDurationSeconds % 60 < 10) "0${callDurationSeconds % 60}" else "${callDurationSeconds % 60}"} · 4K HDR",
                        fontSize = 11.sp,
                        color = GoldLight
                    )
                }

                // PiP Self Camera Preview
                Box(
                    modifier = Modifier
                        .size(width = 80.dp, height = 110.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.DarkGray)
                        .border(1.5.dp, GoldPrimary, RoundedCornerShape(12.dp))
                ) {
                    Image(
                        painter = painterResource(id = AppDataStore.currentUser.avatarRes),
                        contentDescription = "Self Camera",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            // Bottom Call Controls (WhatsApp / FaceTime style)
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Gold Lens Filter Toggle
                IconButton(
                    onClick = { isGoldFilterOn = !isGoldFilterOn },
                    modifier = Modifier
                        .size(50.dp)
                        .background(if (isGoldFilterOn) GoldPrimary else Color.White.copy(alpha = 0.25f), CircleShape)
                ) {
                    Icon(
                        Icons.Default.AutoAwesome,
                        contentDescription = "Gold Filter",
                        tint = if (isGoldFilterOn) ObsidianBg else Color.White
                    )
                }

                // Camera Toggle
                IconButton(
                    onClick = { isCameraOff = !isCameraOff },
                    modifier = Modifier
                        .size(50.dp)
                        .background(if (isCameraOff) CrimsonAlert else Color.White.copy(alpha = 0.25f), CircleShape)
                ) {
                    Icon(
                        if (isCameraOff) Icons.Default.VideocamOff else Icons.Default.Videocam,
                        contentDescription = "Camera",
                        tint = Color.White
                    )
                }

                // Mic Mute Toggle
                IconButton(
                    onClick = { isMuted = !isMuted },
                    modifier = Modifier
                        .size(50.dp)
                        .background(if (isMuted) CrimsonAlert else Color.White.copy(alpha = 0.25f), CircleShape)
                ) {
                    Icon(
                        if (isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                        contentDescription = "Mute",
                        tint = Color.White
                    )
                }

                // End Call Button
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(54.dp)
                        .background(CrimsonAlert, CircleShape)
                ) {
                    Icon(
                        Icons.Default.CallEnd,
                        contentDescription = "End Call",
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AudioCallDialog(
    creator: Creator,
    onDismiss: () -> Unit
) {
    var isMuted by remember { mutableStateOf(false) }
    var isSpeakerOn by remember { mutableStateOf(true) }
    var callDurationSeconds by remember { mutableStateOf(18) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            callDurationSeconds++
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ObsidianBg),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(24.dp)
            ) {
                // Pulsing concentric gold ring avatar
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(130.dp)
                        .clip(CircleShape)
                        .background(GoldMuted)
                ) {
                    Image(
                        painter = painterResource(id = creator.avatarRes),
                        contentDescription = creator.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .border(2.dp, GoldPrimary, CircleShape)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = creator.name,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldLight
                )

                Text(
                    text = "AURA High-Definition Audio Call",
                    fontSize = 12.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "0${callDurationSeconds / 60}:${if (callDurationSeconds % 60 < 10) "0${callDurationSeconds % 60}" else "${callDurationSeconds % 60}"}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldSuccess
                )

                Spacer(modifier = Modifier.height(48.dp))

                // Bottom Call Controls
                Row(
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { isSpeakerOn = !isSpeakerOn },
                        modifier = Modifier
                            .size(52.dp)
                            .background(if (isSpeakerOn) GoldPrimary else ObsidianElevated, CircleShape)
                    ) {
                        Icon(
                            Icons.Default.VolumeUp,
                            contentDescription = "Speaker",
                            tint = if (isSpeakerOn) ObsidianBg else TextPrimary
                        )
                    }

                    IconButton(
                        onClick = { isMuted = !isMuted },
                        modifier = Modifier
                            .size(52.dp)
                            .background(if (isMuted) CrimsonAlert else ObsidianElevated, CircleShape)
                    ) {
                        Icon(
                            if (isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                            contentDescription = "Mute",
                            tint = Color.White
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(56.dp)
                            .background(CrimsonAlert, CircleShape)
                    ) {
                        Icon(
                            Icons.Default.CallEnd,
                            contentDescription = "End Call",
                            tint = Color.White,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
            }
        }
    }
}
