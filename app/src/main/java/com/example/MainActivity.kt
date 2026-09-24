package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.analytics.AnalyticsScreen
import com.example.ui.chat.ChatScreen
import com.example.ui.components.AppBottomNav
import com.example.ui.components.AppTopBar
import com.example.ui.components.NavigationTab
import com.example.ui.feed.FeedScreen
import com.example.ui.monetization.Web3VaultScreen
import com.example.ui.studio.VideoStudioScreen
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppScaffold()
            }
        }
    }
}

@Composable
fun MainAppScaffold() {
    var currentTab by remember { mutableStateOf(NavigationTab.FEED) }
    var showSearchDialog by remember { mutableStateOf(false) }
    var showNotificationsDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBg)
    ) {
        Scaffold(
            topBar = {
                AppTopBar(
                    onWalletClick = { currentTab = NavigationTab.VAULT },
                    onSearchClick = { showSearchDialog = true },
                    onNotificationClick = { showNotificationsDialog = true }
                )
            },
            bottomBar = {
                AppBottomNav(
                    currentTab = currentTab,
                    onTabSelected = { currentTab = it }
                )
            },
            containerColor = ObsidianBg
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (currentTab) {
                    NavigationTab.FEED -> FeedScreen(
                        onOpenStudio = { currentTab = NavigationTab.STUDIO },
                        onOpenVault = { currentTab = NavigationTab.VAULT }
                    )
                    NavigationTab.STUDIO -> VideoStudioScreen(
                        onPublished = { currentTab = NavigationTab.FEED }
                    )
                    NavigationTab.CHAT -> ChatScreen()
                    NavigationTab.ANALYTICS -> AnalyticsScreen(
                        onOpenStudioWithTrend = { currentTab = NavigationTab.STUDIO }
                    )
                    NavigationTab.VAULT -> Web3VaultScreen()
                }
            }
        }

        // Global Search Dialog
        if (showSearchDialog) {
            SearchModal(onDismiss = { showSearchDialog = false })
        }

        // Notifications Dialog
        if (showNotificationsDialog) {
            NotificationsModal(onDismiss = { showNotificationsDialog = false })
        }
    }
}

@Composable
fun SearchModal(onDismiss: () -> Unit) {
    var searchQuery by remember { mutableStateOf("") }
    val trendingSearches = listOf("#CinemaGold", "Elena Vance Tokyo", "Anamorphic Lenses", "Web3 Creator Grant", "Director's Cut")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = ObsidianCard),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, GoldPrimary, RoundedCornerShape(20.dp))
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Worldwide Search", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = GoldLight)
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search creators, tags, sounds...", fontSize = 12.sp, color = TextMuted) },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(20.dp))
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldPrimary,
                        unfocusedBorderColor = ObsidianBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text("TRENDING SEARCHES", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    trendingSearches.forEach { term ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(ObsidianElevated)
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(term, fontSize = 12.sp, color = TextPrimary)
                            Text("↗", fontSize = 12.sp, color = GoldPrimary)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationsModal(onDismiss: () -> Unit) {
    val alerts = listOf(
        "🪙 Elena Vance sent you a 50 USDC tip on Tokyo Runway post",
        "👑 New VIP Member joined your Gold Inner Circle pass (+19 USDC)",
        "🔥 Your video \"Neon Odyssey\" is surging on Worldwide Viral feed",
        "🎙️ Marcus Thorne sent you an audio voice message"
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = ObsidianCard),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, ObsidianBorder, RoundedCornerShape(20.dp))
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("AURA Notifications", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = GoldLight)
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    alerts.forEach { alert ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(ObsidianElevated)
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(alert, fontSize = 12.sp, color = TextPrimary, lineHeight = 16.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Close", color = ObsidianBg, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
