package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.AppDataStore
import com.example.ui.theme.*

@Composable
fun AppTopBar(
    onWalletClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {}
) {
    Surface(
        color = ObsidianSurface,
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Golden Logo & Brand Title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, GoldPrimary, CircleShape)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.golden_logo_icon_1790280881946),
                            contentDescription = "AURA Golden Logo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "AURA",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                color = GoldPrimary,
                                letterSpacing = 1.5.sp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "ROYALE",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldLight,
                                modifier = Modifier
                                    .background(ObsidianElevated, RoundedCornerShape(4.dp))
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                        Text(
                            text = "Worldwide Creator Network",
                            fontSize = 10.sp,
                            color = TextSecondary
                        )
                    }
                }

                // Action Controls: Search, Web3 Wallet Pill, Notifications
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = onSearchClick,
                        modifier = Modifier
                            .size(36.dp)
                            .background(ObsidianCard, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = TextPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Web3 Wallet Pill
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(ObsidianElevated)
                            .border(1.dp, GoldMuted, RoundedCornerShape(20.dp))
                            .clickable { onWalletClick() }
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(EmeraldSuccess)
                        )
                        Text(
                            text = "$${AppDataStore.wallet.usdcBalance.toInt()} USDC",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = GoldLight
                        )
                    }

                    IconButton(
                        onClick = onNotificationClick,
                        modifier = Modifier
                            .size(36.dp)
                            .background(ObsidianCard, CircleShape)
                    ) {
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = GoldPrimary,
                                    contentColor = ObsidianBg
                                ) {
                                    Text("3", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Notifications,
                                contentDescription = "Notifications",
                                tint = TextPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
            HorizontalDivider(color = ObsidianBorder, thickness = 1.dp)
        }
    }
}

enum class NavigationTab(val title: String) {
    FEED("Feed"),
    STUDIO("Studio"),
    CHAT("Chat & Calls"),
    ANALYTICS("Analytics"),
    VAULT("Vault")
}

@Composable
fun AppBottomNav(
    currentTab: NavigationTab,
    onTabSelected: (NavigationTab) -> Unit
) {
    Surface(
        color = ObsidianSurface,
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {
        Column {
            HorizontalDivider(color = ObsidianBorder, thickness = 1.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 1. Worldwide Feed
                NavItem(
                    icon = if (currentTab == NavigationTab.FEED) Icons.Filled.PlayCircle else Icons.Outlined.PlayCircleOutline,
                    label = "Feed",
                    selected = currentTab == NavigationTab.FEED,
                    onClick = { onTabSelected(NavigationTab.FEED) }
                )

                // 2. Creator Studio Editor
                NavItem(
                    icon = if (currentTab == NavigationTab.STUDIO) Icons.Filled.VideoCameraBack else Icons.Outlined.VideoCameraBack,
                    label = "Studio",
                    selected = currentTab == NavigationTab.STUDIO,
                    isSpecial = true,
                    onClick = { onTabSelected(NavigationTab.STUDIO) }
                )

                // 3. Real-time Chat & Calls (WhatsApp style)
                NavItem(
                    icon = if (currentTab == NavigationTab.CHAT) Icons.Filled.ChatBubble else Icons.Outlined.ChatBubbleOutline,
                    label = "Chat",
                    selected = currentTab == NavigationTab.CHAT,
                    badgeCount = 1,
                    onClick = { onTabSelected(NavigationTab.CHAT) }
                )

                // 4. Personalized Creator Analytics
                NavItem(
                    icon = if (currentTab == NavigationTab.ANALYTICS) Icons.Filled.BarChart else Icons.Outlined.BarChart,
                    label = "Analytics",
                    selected = currentTab == NavigationTab.ANALYTICS,
                    onClick = { onTabSelected(NavigationTab.ANALYTICS) }
                )

                // 5. Blockchain Vault & Monetization
                NavItem(
                    icon = if (currentTab == NavigationTab.VAULT) Icons.Filled.AccountBalanceWallet else Icons.Outlined.AccountBalanceWallet,
                    label = "Vault",
                    selected = currentTab == NavigationTab.VAULT,
                    onClick = { onTabSelected(NavigationTab.VAULT) }
                )
            }
        }
    }
}

@Composable
private fun NavItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    selected: Boolean,
    isSpecial: Boolean = false,
    badgeCount: Int = 0,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        if (isSpecial) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(if (selected) GoldPrimary else ObsidianElevated)
                    .border(1.dp, if (selected) GoldLight else GoldMuted, CircleShape)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = if (selected) ObsidianBg else GoldPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        } else {
            BadgedBox(
                badge = {
                    if (badgeCount > 0) {
                        Badge(containerColor = CrimsonAlert, contentColor = Color.White) {
                            Text("$badgeCount", fontSize = 8.sp)
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = if (selected) GoldPrimary else TextSecondary,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = if (selected) GoldLight else TextMuted
        )
    }
}
