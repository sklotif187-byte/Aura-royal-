package com.example.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.R
import com.example.model.*

object AppDataStore {

    val currentUser = Creator(
        id = "creator_me",
        name = "Aria Sterling",
        handle = "@ariasterling",
        avatarRes = R.drawable.creator_avatar_elena_1790280911154,
        isVerified = true,
        followers = "1.4M",
        bio = "Worldwide Visual Storyteller & Cyber Cinema Director · Tokyo / London",
        isLive = false,
        walletAddress = "0x71C839...4A2D"
    )

    val creatorElena = Creator(
        id = "creator_elena",
        name = "Elena Vance",
        handle = "@elenavance",
        avatarRes = R.drawable.creator_avatar_elena_1790280911154,
        isVerified = true,
        followers = "2.8M",
        bio = "Cyber Fashion & 4K Cinema Producer · Tokyo Studio",
        isLive = true,
        walletAddress = "0x892F...18E4"
    )

    val creatorMarcus = Creator(
        id = "creator_marcus",
        name = "Marcus Thorne",
        handle = "@marcusthorne",
        avatarRes = R.drawable.creator_avatar_marcus_1790280929238,
        isVerified = true,
        followers = "980K",
        bio = "Cinematic Drone Expeditions & Architectural Visions",
        isLive = false,
        walletAddress = "0x34A1...98B2"
    )

    val creatorSophia = Creator(
        id = "creator_sophia",
        name = "Sophia Chen",
        handle = "@sophiachen",
        avatarRes = R.drawable.creator_avatar_elena_1790280911154,
        isVerified = true,
        followers = "3.2M",
        bio = "Global High Fashion & Web3 Creative Collective",
        isLive = false,
        walletAddress = "0x67DC...E110"
    )

    val stories = mutableStateListOf(
        Story(
            id = "story_1",
            creator = creatorElena,
            mediaRes = R.drawable.sample_video_fashion_1790280956373,
            caption = "Live backstage at Tokyo Neo-Fashion Week 2026 ✨",
            timeAgo = "12m ago",
            hasUnseen = true
        ),
        Story(
            id = "story_2",
            creator = creatorMarcus,
            mediaRes = R.drawable.sample_video_cinema_1790280942452,
            caption = "Golden Hour drone master shots over the harbor 🌅",
            timeAgo = "45m ago",
            hasUnseen = true
        ),
        Story(
            id = "story_3",
            creator = creatorSophia,
            mediaRes = R.drawable.sample_video_fashion_1790280956373,
            caption = "New Director's Cut dropping in 2 hours for Pass Holders 👑",
            timeAgo = "2h ago",
            hasUnseen = false
        )
    )

    val posts = mutableStateListOf(
        Post(
            id = "post_1",
            creator = creatorElena,
            caption = "Tokyo Night Reverie in 4K HDR. Shot with anamorphic primes & color graded in AURA Studio using Cinema Gold LUT. What do you think of this color palette? 🌆✨",
            tags = listOf("#TokyoNeo", "#CinemaGold", "#AURAWorldwide", "#CyberAesthetic"),
            mediaRes = R.drawable.sample_video_fashion_1790280956373,
            videoAspectRatio = "9:16",
            musicTrack = "Tokyo Neon Drift · Synthwave (Original Audio)",
            likesCount = 28410,
            fireCount = 9420,
            trophyCount = 1850,
            insightfulCount = 740,
            commentsCount = 1420,
            tipsTotalUsd = 1450.0,
            isTokenGated = false,
            timeAgo = "18 min ago",
            userLiked = true,
            userReaction = "FIRE"
        ),
        Post(
            id = "post_2",
            creator = creatorMarcus,
            caption = "Golden Sunset across the Pacific Metropolis. 8K Aerial Hyperlapse captured from 1,200ft. Full behind-the-scenes editing workflow available for Gold Pass subscribers! 🎬🚁",
            tags = listOf("#DroneCinema", "#GoldenHour", "#FilmMaking", "#Metropolis"),
            mediaRes = R.drawable.sample_video_cinema_1790280942452,
            videoAspectRatio = "16:9",
            musicTrack = "Golden Horizon Symphony · Marcus Thorne",
            likesCount = 45900,
            fireCount = 14200,
            trophyCount = 4890,
            insightfulCount = 2100,
            commentsCount = 2830,
            tipsTotalUsd = 3890.0,
            isTokenGated = true,
            requiredTokenTier = "Gold Inner Circle Pass",
            timeAgo = "2 hours ago",
            userLiked = false
        ),
        Post(
            id = "post_3",
            creator = creatorSophia,
            caption = "Exclusive Preview: Neo-Tokyo Runway Collection 2026. Interactive audio-reactive garments woven with smart photonic fiber. Direct tips on this post go towards student filmmaker grants! 🪙",
            tags = listOf("#Runway2026", "#Web3Creators", "#SmartTextiles", "#TokyoFashion"),
            mediaRes = R.drawable.sample_video_fashion_1790280956373,
            videoAspectRatio = "1:1",
            musicTrack = "Parisian Nights · Ambient Deep House",
            likesCount = 19800,
            fireCount = 7200,
            trophyCount = 2100,
            insightfulCount = 1120,
            commentsCount = 940,
            tipsTotalUsd = 2150.0,
            isTokenGated = false,
            timeAgo = "4 hours ago",
            userLiked = false
        )
    )

    var wallet by mutableStateOf(
        CryptoWallet(
            ethAddress = "0x71C8392F8A4b29C4...614A",
            solAddress = "AuRa9k2LmPx7B1k4Qw...9dZ1",
            auraBalance = 14250.0,
            ethBalance = 5.42,
            solBalance = 78.5,
            usdcBalance = 8450.0
        )
    )

    val subscriptionTiers = mutableStateListOf(
        SubscriptionTier(
            id = "tier_1",
            name = "Gold Inner Circle",
            description = "Access to exclusive 4K BTS footage, raw project timelines & monthly live Q&A sessions.",
            priceUsd = 19.0,
            priceEth = 0.007,
            perks = listOf(
                "Unlock all Token-Gated Creator Posts",
                "Direct VIP Discord / AURA Chat Priority",
                "Exclusive LUTs & Sound FX Packs Download",
                "Monthly Worldwide Filmmakers Zoom Call"
            ),
            activeSubscribers = 482,
            isSubscribed = true
        ),
        SubscriptionTier(
            id = "tier_2",
            name = "Director's Cut Pass",
            description = "Complete project source files, DaVinci/Premiere color nodes, and commercial asset rights.",
            priceUsd = 49.0,
            priceEth = 0.018,
            perks = listOf(
                "All Gold Inner Circle perks included",
                "Full Uncompressed 8K Raw Footage Downloads",
                "Commercial Video Licensing Rights",
                "Priority Collaboration Inquiries"
            ),
            activeSubscribers = 194,
            isSubscribed = false
        ),
        SubscriptionTier(
            id = "tier_3",
            name = "1-on-1 Mentorship VIP",
            description = "Bi-weekly 45-minute private creative consulting and portfolio critique with Elena Vance.",
            priceUsd = 149.0,
            priceEth = 0.055,
            perks = listOf(
                "All Director's Cut perks included",
                "Two 1-on-1 Video Calls per month via AURA",
                "Direct WhatsApp / AURA VIP Hotline",
                "Personalized Sponsor & Brand Pitch Deck Review"
            ),
            activeSubscribers = 28,
            isSubscribed = false
        )
    )

    val transactions = mutableStateListOf(
        BlockchainTx(
            hash = "0x89ab...712c",
            blockNumber = 20491823,
            type = "CREATOR_TIP",
            amount = "+50.00",
            token = "USDC",
            from = "0x41e...889b",
            to = "Elena Vance (@elenavance)",
            timestamp = "Just now",
            status = "CONFIRMED"
        ),
        BlockchainTx(
            hash = "0x34fd...001a",
            blockNumber = 20491798,
            type = "PASS_MINT",
            amount = "+0.007",
            token = "ETH",
            from = "0x71c...614a",
            to = "AURA Smart Contract",
            timestamp = "14m ago",
            status = "CONFIRMED"
        ),
        BlockchainTx(
            hash = "0xaa91...55be",
            blockNumber = 20491650,
            type = "CREATOR_TIP",
            amount = "+150.00",
            token = "AURA",
            from = "0x28c...33df",
            to = "Marcus Thorne (@marcusthorne)",
            timestamp = "1h ago",
            status = "CONFIRMED"
        ),
        BlockchainTx(
            hash = "0x11ce...44a7",
            blockNumber = 20491500,
            type = "REVENUE_SHARE",
            amount = "+420.50",
            token = "USDC",
            from = "AURA Worldwide Pool",
            to = "Aria Sterling (@ariasterling)",
            timestamp = "5h ago",
            status = "CONFIRMED"
        )
    )

    val analytics = AnalyticsSummary(
        totalImpressions = "4.82M",
        totalViews = "1.24M",
        watchHours = "186.4K",
        monthlyRevenueUsd = 18450.0,
        viralityScore = 96.4,
        hotspots = listOf(
            HotspotCountry("Tokyo, Japan", "🇯🇵", 32, "396.8K views"),
            HotspotCountry("New York, USA", "🇺🇸", 26, "322.4K views"),
            HotspotCountry("London, UK", "🇬🇧", 18, "223.2K views"),
            HotspotCountry("Dubai, UAE", "🇦🇪", 12, "148.8K views"),
            HotspotCountry("São Paulo, Brazil", "🇧🇷", 8, "99.2K views"),
            HotspotCountry("Berlin, Germany", "🇩🇪", 4, "49.6K views")
        ),
        trendingAudio = listOf(
            "Tokyo Neon Drift · Synthwave (+480% virality)",
            "Cinematic Gold Sunset · Orchestral (+310% virality)",
            "Cyber Beats 2026 · Future Bass (+225% virality)",
            "Midnight Reverie · Anamorphic Lofi (+180% virality)"
        ),
        trendingHashtags = listOf(
            "#AURAWorldwide", "#CinemaGold", "#CyberAesthetic", "#TokyoNeoRunway", "#CreatorRoyale"
        )
    )

    // Chat data
    val conversations = mutableStateListOf(
        ChatConversation(
            id = "chat_elena",
            otherUser = creatorElena,
            lastMessage = "🎙️ Voice message (0:18)",
            lastMessageTime = "12:44 PM",
            unreadCount = 1,
            isVipChannel = true,
            messages = listOf(
                ChatMessage(
                    id = "m1",
                    senderId = creatorElena.id,
                    text = "Hey Aria! Loving the color grading on your latest Tokyo runway cut! Which LUT did you apply from the AURA suite?",
                    timestamp = "12:35 PM",
                    isFromMe = false
                ),
                ChatMessage(
                    id = "m2",
                    senderId = "creator_me",
                    text = "Thank you Elena! I used the Cinema Gold preset with 45% warm highlight diffusion and dialed the anamorphic flare to 1.2x. Works like magic with 4K HDR footage.",
                    timestamp = "12:38 PM",
                    isFromMe = true
                ),
                ChatMessage(
                    id = "m3",
                    senderId = "creator_me",
                    text = "Sent 50 USDC direct tip for your masterclass advice! 🪙",
                    timestamp = "12:39 PM",
                    isFromMe = true,
                    tipAmountUsd = 50.0
                ),
                ChatMessage(
                    id = "m4",
                    senderId = creatorElena.id,
                    text = "You're amazing! Listen to this preview of the sound track we're syncing for tomorrow's live drop:",
                    timestamp = "12:43 PM",
                    isFromMe = false
                ),
                ChatMessage(
                    id = "m5",
                    senderId = creatorElena.id,
                    text = null,
                    timestamp = "12:44 PM",
                    isFromMe = false,
                    isVoiceNote = true,
                    voiceDurationSeconds = 18,
                    voiceWaveform = listOf(0.2f, 0.4f, 0.8f, 0.6f, 0.9f, 0.5f, 0.7f, 1.0f, 0.8f, 0.4f, 0.6f, 0.9f, 0.3f, 0.5f, 0.7f, 0.6f, 0.4f, 0.2f)
                )
            )
        ),
        ChatConversation(
            id = "chat_marcus",
            otherUser = creatorMarcus,
            lastMessage = "Let's do a joint video call tomorrow to plan the Tokyo aerial shoot!",
            lastMessageTime = "11:15 AM",
            unreadCount = 0,
            isVipChannel = false,
            messages = listOf(
                ChatMessage(
                    id = "m_marc_1",
                    senderId = creatorMarcus.id,
                    text = "Yo Aria! Just wrapped up the drone flight permissions over Shinjuku. We have clean airspace at 6:30 PM for sunset.",
                    timestamp = "11:10 AM",
                    isFromMe = false
                ),
                ChatMessage(
                    id = "m_marc_2",
                    senderId = "creator_me",
                    text = "Incredible! I'll bring the RED Komodo and the 35mm anamorphic setup.",
                    timestamp = "11:12 AM",
                    isFromMe = true
                ),
                ChatMessage(
                    id = "m_marc_3",
                    senderId = creatorMarcus.id,
                    text = "Let's do a joint video call tomorrow to plan the Tokyo aerial shoot!",
                    timestamp = "11:15 AM",
                    isFromMe = false
                )
            )
        ),
        ChatConversation(
            id = "chat_vip_circle",
            otherUser = creatorSophia,
            lastMessage = "🎙️ Voice message (0:32)",
            lastMessageTime = "Yesterday",
            unreadCount = 0,
            isVipChannel = true,
            isGroup = true,
            messages = listOf(
                ChatMessage(
                    id = "m_vip_1",
                    senderId = creatorSophia.id,
                    text = "Welcome to the Gold Inner Circle mastermind! 👑 Here is the exclusive raw footage link for this week's drop.",
                    timestamp = "Yesterday",
                    isFromMe = false
                )
            )
        )
    )

    fun sendTip(creator: Creator, amountUsd: Double, token: String, messageText: String) {
        val newTx = BlockchainTx(
            hash = "0x" + (1000..9999).random().toString(16) + "..." + (1000..9999).random().toString(16),
            blockNumber = 20491850L + (1..50).random().toLong(),
            type = "CREATOR_TIP",
            amount = "+$amountUsd",
            token = token,
            from = wallet.ethAddress,
            to = "${creator.name} (${creator.handle})",
            timestamp = "Just now",
            status = "CONFIRMED"
        )
        transactions.add(0, newTx)

        // Deduct from wallet
        if (token == "USDC") {
            wallet = wallet.copy(usdcBalance = (wallet.usdcBalance - amountUsd).coerceAtLeast(0.0))
        } else if (token == "AURA") {
            wallet = wallet.copy(auraBalance = (wallet.auraBalance - amountUsd).coerceAtLeast(0.0))
        } else if (token == "ETH") {
            wallet = wallet.copy(ethBalance = (wallet.ethBalance - 0.015).coerceAtLeast(0.0))
        }

        // Add tipping chat message
        val chatIndex = conversations.indexOfFirst { it.otherUser.id == creator.id }
        if (chatIndex != -1) {
            val conv = conversations[chatIndex]
            val newMsg = ChatMessage(
                id = "tip_" + System.currentTimeMillis(),
                senderId = "creator_me",
                text = "Tipped $amountUsd $token on-chain: \"$messageText\"",
                timestamp = "Just now",
                isFromMe = true,
                tipAmountUsd = amountUsd
            )
            conversations[chatIndex] = conv.copy(
                messages = conv.messages + newMsg,
                lastMessage = "Sent $$amountUsd Tip 🪙",
                lastMessageTime = "Just now"
            )
        }
    }

    fun addPublishedVideo(
        title: String,
        tags: List<String>,
        aspectRatio: String,
        audioTrack: String,
        isTokenGated: Boolean
    ) {
        val newPost = Post(
            id = "post_" + System.currentTimeMillis(),
            creator = currentUser,
            caption = "$title ✨ Edited in AURA Video Studio with Cinema Gold grade.",
            tags = tags,
            mediaRes = if (aspectRatio == "16:9") R.drawable.sample_video_cinema_1790280942452 else R.drawable.sample_video_fashion_1790280956373,
            videoAspectRatio = aspectRatio,
            musicTrack = audioTrack,
            likesCount = 1,
            fireCount = 1,
            trophyCount = 0,
            insightfulCount = 0,
            commentsCount = 0,
            tipsTotalUsd = 0.0,
            isTokenGated = isTokenGated,
            requiredTokenTier = if (isTokenGated) "Gold Inner Circle Pass" else null,
            timeAgo = "Just now",
            userLiked = true,
            userReaction = "HEART"
        )
        posts.add(0, newPost)
    }
}
