package com.example.ui.feed

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
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.AppDataStore
import com.example.model.Creator
import com.example.model.Post
import com.example.model.Story
import com.example.ui.components.StoryViewerDialog
import com.example.ui.components.TipCreatorDialog
import com.example.ui.theme.*

@Composable
fun FeedScreen(
    onOpenStudio: () -> Unit = {},
    onOpenVault: () -> Unit = {}
) {
    var selectedFilter by remember { mutableStateOf("Worldwide Viral") }
    var activeStory by remember { mutableStateOf<Story?>(null) }
    var tipTargetCreator by remember { mutableStateOf<Creator?>(null) }
    var activeCommentPost by remember { mutableStateOf<Post?>(null) }

    val filterOptions = listOf("Worldwide Viral", "For You", "VIP Gated Pass", "Following")

    val displayedPosts = remember(selectedFilter, AppDataStore.posts.size) {
        when (selectedFilter) {
            "VIP Gated Pass" -> AppDataStore.posts.filter { it.isTokenGated }
            "For You" -> AppDataStore.posts.sortedByDescending { it.likesCount }
            else -> AppDataStore.posts
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(ObsidianBg)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            // 1. Stories Carousel Tray (Instagram + Facebook style)
            item {
                StoriesTray(
                    stories = AppDataStore.stories,
                    onStoryClick = { activeStory = it },
                    onAddStory = onOpenStudio
                )
            }

            // 2. Feed Filter Segmented Bar
            item {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filterOptions) { filter ->
                        val isSelected = selectedFilter == filter
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isSelected) GoldPrimary else ObsidianElevated)
                                .border(
                                    1.dp,
                                    if (isSelected) GoldLight else ObsidianBorder,
                                    RoundedCornerShape(20.dp)
                                )
                                .clickable { selectedFilter = filter }
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = filter,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) ObsidianBg else TextSecondary
                            )
                        }
                    }
                }
            }

            // 3. Video Post Feed Items
            items(displayedPosts, key = { it.id }) { post ->
                PostCard(
                    post = post,
                    onTipClick = { tipTargetCreator = post.creator },
                    onCommentClick = { activeCommentPost = post },
                    onUnlockVipClick = onOpenVault
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        // Active Story Viewer Modal
        activeStory?.let { story ->
            StoryViewerDialog(
                story = story,
                onDismiss = { activeStory = null }
            )
        }

        // Active Tip Modal
        tipTargetCreator?.let { creator ->
            TipCreatorDialog(
                creator = creator,
                onDismiss = { tipTargetCreator = null }
            )
        }

        // Comment Sheet
        activeCommentPost?.let { post ->
            CommentSheet(
                post = post,
                onDismiss = { activeCommentPost = null }
            )
        }
    }
}

@Composable
fun StoriesTray(
    stories: List<Story>,
    onStoryClick: (Story) -> Unit,
    onAddStory: () -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // "Your Story / Create" Item
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { onAddStory() }
            ) {
                Box(
                    modifier = Modifier
                        .size(66.dp)
                        .clip(CircleShape)
                        .background(ObsidianElevated)
                        .border(1.5.dp, GoldPrimary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = AppDataStore.currentUser.avatarRes),
                        contentDescription = "Your Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(58.dp)
                            .clip(CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(GoldPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Add Story",
                            tint = ObsidianBg,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Your Studio",
                    fontSize = 11.sp,
                    color = TextPrimary,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Active Creator Stories
        items(stories) { story ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { onStoryClick(story) }
            ) {
                Box(
                    modifier = Modifier
                        .size(66.dp)
                        .clip(CircleShape)
                        .background(if (story.hasUnseen) GoldPrimary else ObsidianBorder)
                        .padding(2.5.dp)
                ) {
                    Image(
                        painter = painterResource(id = story.creator.avatarRes),
                        contentDescription = story.creator.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .border(1.5.dp, ObsidianBg, CircleShape)
                    )

                    if (story.creator.isLive) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .clip(RoundedCornerShape(4.dp))
                                .background(CrimsonAlert)
                                .padding(horizontal = 4.dp, vertical = 1.dp)
                        ) {
                            Text("LIVE", fontSize = 8.sp, fontWeight = FontWeight.Black, color = Color.White)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = story.creator.name.split(" ").first(),
                    fontSize = 11.sp,
                    color = TextPrimary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun PostCard(
    post: Post,
    onTipClick: () -> Unit,
    onCommentClick: () -> Unit,
    onUnlockVipClick: () -> Unit
) {
    var isLiked by remember { mutableStateOf(post.userLiked) }
    var likesCount by remember { mutableStateOf(post.likesCount) }
    var activeReaction by remember { mutableStateOf(post.userReaction) }
    var fireCount by remember { mutableStateOf(post.fireCount) }
    var trophyCount by remember { mutableStateOf(post.trophyCount) }
    var isPlaying by remember { mutableStateOf(true) }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = ObsidianCard),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .border(1.dp, ObsidianBorder, RoundedCornerShape(18.dp))
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header: Creator Profile, Handle & Verified Badge
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Image(
                        painter = painterResource(id = post.creator.avatarRes),
                        contentDescription = post.creator.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, GoldPrimary, CircleShape)
                    )
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = post.creator.name,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = "Verified Creator",
                                tint = GoldPrimary,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                        Text(
                            text = "${post.creator.handle} · ${post.timeAgo}",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    // Direct Tip Button
                    Button(
                        onClick = onTipClick,
                        colors = ButtonDefaults.buttonColors(containerColor = ObsidianElevated),
                        shape = RoundedCornerShape(14.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.border(1.dp, GoldPrimary, RoundedCornerShape(14.dp))
                    ) {
                        Icon(
                            Icons.Default.Bolt,
                            contentDescription = "Tip",
                            tint = GoldLight,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text("Tip", fontSize = 11.sp, color = GoldLight, fontWeight = FontWeight.Bold)
                    }

                    IconButton(onClick = {}, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More", tint = TextMuted)
                    }
                }
            }

            // Media Preview Stage with Play/Pause & Audio Badge
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (post.videoAspectRatio == "16:9") 220.dp else 360.dp)
                    .background(Color.Black)
                    .clickable { isPlaying = !isPlaying }
            ) {
                Image(
                    painter = painterResource(id = post.mediaRes),
                    contentDescription = "Video Thumbnail",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Aspect Ratio indicator
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.Black.copy(alpha = 0.6f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "4K HDR · ${post.videoAspectRatio}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldLight
                    )
                }

                // If Token-gated
                if (post.isTokenGated) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.65f))
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(GoldPrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Lock, contentDescription = "Locked", tint = ObsidianBg)
                            }
                            Text(
                                text = "Exclusive VIP Director's Cut",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldLight
                            )
                            Text(
                                text = "Token-Gated: Requires ${post.requiredTokenTier}",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                            Button(
                                onClick = onUnlockVipClick,
                                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Unlock with Web3 Pass", color = ObsidianBg, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                } else {
                    // Play / Pause Overlay Icon
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.5f))
                            .border(1.dp, GoldLight.copy(alpha = 0.6f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = "Playback state",
                            tint = GoldPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                // Audio Track Pill at Bottom Left
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Black.copy(alpha = 0.7f))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        Icons.Default.MusicNote,
                        contentDescription = "Music Track",
                        tint = GoldPrimary,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = post.musicTrack,
                        fontSize = 10.sp,
                        color = Color.White
                    )
                }
            }

            // Reactions & Social Bar (FB + Instagram amalgamation)
            Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Reaction Cluster: Heart, Fire, Trophy
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Heart Like
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                isLiked = !isLiked
                                if (isLiked) {
                                    likesCount++
                                    activeReaction = "HEART"
                                } else {
                                    likesCount--
                                    activeReaction = null
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (isLiked) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "Like",
                                tint = if (isLiked) CrimsonAlert else TextSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "$likesCount",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                        }

                        // Fire Reaction
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                fireCount++
                                activeReaction = "FIRE"
                            }
                        ) {
                            Text("🔥", fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "$fireCount",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                        }

                        // Trophy Reaction
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                trophyCount++
                                activeReaction = "TROPHY"
                            }
                        ) {
                            Text("🏆", fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "$trophyCount",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                        }

                        // Comments
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable(onClick = onCommentClick)
                        ) {
                            Icon(
                                Icons.Outlined.ChatBubbleOutline,
                                contentDescription = "Comments",
                                tint = TextSecondary,
                                modifier = Modifier.size(19.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${post.commentsCount}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                        }
                    }

                    // Total Tips Accumulated
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(ObsidianElevated)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("🪙", fontSize = 11.sp)
                        Text(
                            text = "$${post.tipsTotalUsd.toInt()} Tipped",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldLight
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Caption & Hashtags
                Text(
                    text = post.caption,
                    fontSize = 13.sp,
                    color = TextPrimary,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    post.tags.forEach { tag ->
                        Text(
                            text = tag,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = GoldPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CommentSheet(
    post: Post,
    onDismiss: () -> Unit
) {
    var newCommentText by remember { mutableStateOf("") }
    val sampleComments = remember {
        mutableStateListOf(
            "The anamorphic lenses combined with Cinema Gold grading is breathtaking! 🤩",
            "Just sent a 25 USDC tip! Keep up the brilliant masterclasses.",
            "Can you drop the DaVinci node tree in the Gold Inner Circle pass?",
            "Top tier audio sync. Best video on AURA today!"
        )
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = ObsidianCard),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.75f)
                .border(1.dp, ObsidianBorder, RoundedCornerShape(20.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Comments (${sampleComments.size})",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldLight
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                HorizontalDivider(color = ObsidianBorder, thickness = 1.dp, modifier = Modifier.padding(vertical = 10.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(sampleComments) { comment ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(ObsidianElevated)
                                    .border(1.dp, GoldPrimary, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🎬", fontSize = 12.sp)
                            }
                            Column {
                                Text("Creator Colleague · 5m ago", fontSize = 10.sp, color = TextMuted)
                                Text(comment, fontSize = 12.sp, color = TextPrimary)
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = newCommentText,
                        onValueChange = { newCommentText = it },
                        placeholder = { Text("Add comment...", fontSize = 12.sp, color = TextMuted) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GoldPrimary,
                            unfocusedBorderColor = ObsidianBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(20.dp)
                    )

                    IconButton(
                        onClick = {
                            if (newCommentText.isNotBlank()) {
                                sampleComments.add(0, newCommentText)
                                newCommentText = ""
                            }
                        },
                        modifier = Modifier
                            .size(42.dp)
                            .background(GoldPrimary, CircleShape)
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "Send", tint = ObsidianBg, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}
