package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Obsidian & Dark Canvas
val ObsidianBg = Color(0xFF090A0F)
val ObsidianSurface = Color(0xFF11141D)
val ObsidianCard = Color(0xFF161A26)
val ObsidianElevated = Color(0xFF1D2233)
val ObsidianBorder = Color(0xFF262C40)

// Champagne & Radiant Gold Palette
val GoldLight = Color(0xFFFFE680)
val GoldPrimary = Color(0xFFFFC72C)
val GoldAccent = Color(0xFFE5A910)
val GoldDark = Color(0xFFB37D06)
val GoldMuted = Color(0x33FFC72C)

// Accents
val EmeraldSuccess = Color(0xFF10B981)
val CrimsonAlert = Color(0xFFEF4444)
val VioletCrypto = Color(0xFF8B5CF6)
val BlueVerified = Color(0xFF38BDF8)

// Text Colors
val TextPrimary = Color(0xFFF8FAFC)
val TextSecondary = Color(0xFF94A3B8)
val TextMuted = Color(0xFF64748B)

// Gradients
val GoldGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFFFFE680), Color(0xFFFFC72C), Color(0xFFD99B00))
)
val GoldBadgeGradient = Brush.linearGradient(
    colors = listOf(Color(0xFFFFF1B8), Color(0xFFFFD700), Color(0xFFB8860B))
)
val ObsidianGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFF161A26), Color(0xFF090A0F))
)

