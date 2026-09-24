package com.example.model

data class Creator(
    val id: String,
    val name: String,
    val handle: String,
    val avatarRes: Int,
    val isVerified: Boolean = true,
    val followers: String,
    val bio: String,
    val isLive: Boolean = false,
    val walletAddress: String
)

data class Post(
    val id: String,
    val creator: Creator,
    val caption: String,
    val tags: List<String>,
    val mediaRes: Int,
    val videoAspectRatio: String = "9:16", // "9:16", "1:1", "16:9"
    val musicTrack: String,
    val likesCount: Int,
    val fireCount: Int,
    val trophyCount: Int,
    val insightfulCount: Int,
    val commentsCount: Int,
    val tipsTotalUsd: Double,
    val isTokenGated: Boolean = false,
    val requiredTokenTier: String? = null,
    val timeAgo: String,
    val userLiked: Boolean = false,
    val userReaction: String? = null, // "HEART", "FIRE", "TROPHY", "INSIGHTFUL"
    val isPlaying: Boolean = false
)

data class Story(
    val id: String,
    val creator: Creator,
    val mediaRes: Int,
    val caption: String,
    val timeAgo: String,
    val hasUnseen: Boolean = true
)

data class Comment(
    val id: String,
    val author: String,
    val authorAvatarRes: Int,
    val content: String,
    val timeAgo: String,
    val likes: Int = 0
)

data class ChatMessage(
    val id: String,
    val senderId: String,
    val text: String? = null,
    val timestamp: String,
    val isFromMe: Boolean,
    val isVoiceNote: Boolean = false,
    val voiceDurationSeconds: Int = 0,
    val voiceWaveform: List<Float> = emptyList(),
    val tipAmountUsd: Double? = null,
    val readStatus: String = "READ" // "SENT", "DELIVERED", "READ"
)

data class ChatConversation(
    val id: String,
    val otherUser: Creator,
    val lastMessage: String,
    val lastMessageTime: String,
    val unreadCount: Int = 0,
    val isVipChannel: Boolean = false,
    val isGroup: Boolean = false,
    val messages: List<ChatMessage> = emptyList()
)

data class HotspotCountry(
    val country: String,
    val flag: String,
    val percentage: Int,
    val views: String
)

data class AnalyticsSummary(
    val totalImpressions: String,
    val totalViews: String,
    val watchHours: String,
    val monthlyRevenueUsd: Double,
    val viralityScore: Double,
    val hotspots: List<HotspotCountry>,
    val trendingAudio: List<String>,
    val trendingHashtags: List<String>
)

data class SubscriptionTier(
    val id: String,
    val name: String,
    val description: String,
    val priceUsd: Double,
    val priceEth: Double,
    val perks: List<String>,
    val activeSubscribers: Int,
    val isSubscribed: Boolean = false
)

data class CryptoWallet(
    val ethAddress: String,
    val solAddress: String,
    val auraBalance: Double,
    val ethBalance: Double,
    val solBalance: Double,
    val usdcBalance: Double
)

data class BlockchainTx(
    val hash: String,
    val blockNumber: Long,
    val type: String, // "CREATOR_TIP", "PASS_MINT", "REVENUE_SHARE", "WITHDRAW"
    val amount: String,
    val token: String,
    val from: String,
    val to: String,
    val timestamp: String,
    val status: String = "CONFIRMED"
)
