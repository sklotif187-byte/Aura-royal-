package com.example.ui.analytics

import androidx.compose.animation.*
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppDataStore
import com.example.ui.theme.*

@Composable
fun AnalyticsScreen(
    onOpenStudioWithTrend: (String) -> Unit = {}
) {
    var selectedTimeframe by remember { mutableStateOf("30 Days") }
    val analytics = AppDataStore.analytics

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBg)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Creator Intelligence",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldLight
                    )
                    Text(
                        text = "Worldwide Virality & Engagement Radar",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }

                // Timeframe Selector
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(ObsidianElevated)
                        .padding(2.dp)
                ) {
                    listOf("7D", "30D", "90D").forEach { tf ->
                        val isSelected = (selectedTimeframe.startsWith(tf.replace("D", "")))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isSelected) GoldPrimary else Color.Transparent)
                                .clickable { selectedTimeframe = "$tf Days" }
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = tf,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) ObsidianBg else TextSecondary
                            )
                        }
                    }
                }
            }
        }

        // Key KPI 4-Card Grid
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard(
                        title = "Worldwide Reach",
                        value = analytics.totalImpressions,
                        delta = "+28.4%",
                        isPositive = true,
                        icon = "🌐",
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "Video Views",
                        value = analytics.totalViews,
                        delta = "+34.1%",
                        isPositive = true,
                        icon = "👁️",
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard(
                        title = "Watch Time",
                        value = analytics.watchHours,
                        delta = "+18.2%",
                        isPositive = true,
                        icon = "⏱️",
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "On-Chain Revenue",
                        value = "$${analytics.monthlyRevenueUsd.toInt()}",
                        delta = "+42.5%",
                        isPositive = true,
                        icon = "🪙",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Virality Index Banner
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ObsidianCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, GoldPrimary, RoundedCornerShape(16.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Virality Index Score",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldLight
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("⚡ Apex Tier", fontSize = 10.sp, color = EmeraldSuccess, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Top 0.8% of global creators on AURA network",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(GoldPrimary)
                    ) {
                        Text(
                            text = "${analytics.viralityScore}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = ObsidianBg
                        )
                    }
                }
            }
        }

        // Retention & Daily Engagement Bar Chart
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ObsidianCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, ObsidianBorder, RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Weekly Engagement Curve", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text("Peak engagement on Thursday & Saturday", fontSize = 11.sp, color = TextMuted)
                        }
                        Text("88.4% Retention", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoldLight)
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Simulated 7-day Bar Chart
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        val days = listOf("Mon" to 0.45f, "Tue" to 0.60f, "Wed" to 0.55f, "Thu" to 0.90f, "Fri" to 0.75f, "Sat" to 0.98f, "Sun" to 0.70f)
                        days.forEach { (day, value) ->
                            val isPeak = value > 0.85f
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Bottom,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(22.dp)
                                        .fillMaxHeight(value)
                                        .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                        .background(if (isPeak) GoldPrimary else ObsidianElevated)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = day,
                                    fontSize = 10.sp,
                                    fontWeight = if (isPeak) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isPeak) GoldLight else TextSecondary
                                )
                            }
                        }
                    }
                }
            }
        }

        // Worldwide Geographic Hotspots
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ObsidianCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, ObsidianBorder, RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Worldwide Audience Hotspots", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text("6 Continents", fontSize = 11.sp, color = TextMuted)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        analytics.hotspots.forEach { spot ->
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Text(spot.flag, fontSize = 14.sp)
                                        Text(spot.country, fontSize = 12.sp, color = TextPrimary, fontWeight = FontWeight.Medium)
                                    }
                                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Text(spot.views, fontSize = 11.sp, color = TextSecondary)
                                        Text("${spot.percentage}%", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoldLight)
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                LinearProgressIndicator(
                                    progress = { spot.percentage / 100f },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(4.dp)
                                        .clip(RoundedCornerShape(2.dp)),
                                    color = GoldPrimary,
                                    trackColor = ObsidianElevated
                                )
                            }
                        }
                    }
                }
            }
        }

        // Viral Trend Radar (Audio & Hashtags)
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ObsidianCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, ObsidianBorder, RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Viral Trend Radar", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = GoldLight)
                    Text("Real-time algorithmic surges across AURA feed", fontSize = 11.sp, color = TextMuted)

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("🔥 Breakout Audio Sounds", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Spacer(modifier = Modifier.height(6.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        analytics.trendingAudio.forEach { track ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(ObsidianElevated)
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(track, fontSize = 11.sp, color = TextPrimary)
                                Icon(Icons.Default.TrendingUp, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(16.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text("🏷️ Surging Hashtags", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        analytics.trendingHashtags.take(3).forEach { tag ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(ObsidianElevated)
                                    .border(1.dp, GoldMuted, RoundedCornerShape(12.dp))
                                    .clickable { onOpenStudioWithTrend(tag) }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(tag, fontSize = 10.sp, color = GoldLight, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    delta: String,
    isPositive: Boolean,
    icon: String,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = ObsidianCard),
        modifier = modifier.border(1.dp, ObsidianBorder, RoundedCornerShape(14.dp))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, fontSize = 11.sp, color = TextSecondary)
                Text(icon, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(value, fontSize = 18.sp, fontWeight = FontWeight.Black, color = TextPrimary)
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (isPositive) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                    contentDescription = null,
                    tint = if (isPositive) EmeraldSuccess else CrimsonAlert,
                    modifier = Modifier.size(12.dp)
                )
                Text(
                    text = delta,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isPositive) EmeraldSuccess else CrimsonAlert
                )
                Text(" this week", fontSize = 9.sp, color = TextMuted)
            }
        }
    }
}
