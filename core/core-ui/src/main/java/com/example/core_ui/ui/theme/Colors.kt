package com.example.core_ui.ui.theme

import androidx.compose.ui.graphics.Color

// ----------------------------------------------------------------------------
// BRAND & ROLE COLORS
// ----------------------------------------------------------------------------

val TeacherPrimary = Color(0xFF2553EB)
val StudentPrimary = Color(0xFF2333EA)
val CommunityPeers = Color(0xFF4F46E5)



// ----------------------------------------------------------------------------
// TEXT COLORS
// ----------------------------------------------------------------------------

// Primary text (headings, titles, main content)
val TextPrimary = Color(0xFF111827)   // Almost black (high contrast)

// Secondary text (subtitles, labels, hints)
val TextSecondary = Color(0xFF4B5563) // Muted gray

// Tertiary / disabled text
val TextDisabled = Color(0xFF9CA3AF)  // Light gray

// Inverse text (on dark backgrounds)
val TextInverse = Color.White

// Link text
val TextLink = TeacherPrimary


// Optional: muted UI labels
val TextHint = Color(0xFF6B7280)
val TextCaption = Color(0xFF6D7280)


// ----------------------------------------------------------------------------
// STATUS & FUNCTIONAL COLORS
// ----------------------------------------------------------------------------

val SuccessGood = Color(0xFF16A34A)
val SuccessAlt = Color(0xFF047867)
val WarningPending = Color(0xFFF97316)
val DangerOverdue = Color(0xFFEF4444)
val Highlight = Color(0xFFEAB308)


// ----------------------------------------------------------------------------
// CHART & GRAPH COLORS
// ----------------------------------------------------------------------------

val EmeraldGreen = Color(0xFF108981)
val ChartBlue     = Color(0xFF3382F6)
val Amber         = Color(0xFFF53308)
val ChartOrange   = Color(0xFFF79718)
val ChartRed      = Color(0xFFE44444)
val ChartLightGray = Color(0xFFE5E7EB)
val GridGray       = Color(0xFF8E74B)


// ----------------------------------------------------------------------------
// BACKGROUNDS & NEUTRALS
// ----------------------------------------------------------------------------

val AppBackground = Color(0xFFF8F4FB)
val CardBackground = Color(0xFFFFFFFF)


// ----------------------------------------------------------------------------
// GRADIENT COLORS
// ----------------------------------------------------------------------------

val BlueGradientStart = Color(0xFF3902FF)
val BlueGradientEnd   = Color(0xFF3829FF)

val TealGradientStart = Color(0xFF1F766E)
val TealGradientEnd   = Color(0xFF109388)

val RedGradientStart  = Color(0xFFEF4444)
val RedGradientEnd    = Color(0xFFC81919)

val PurpleGradientStart = Color(0xFF8B5CF6)
val PurpleGradientEnd   = Color(0xFF7B22CE)


// ----------------------------------------------------------------------------
// DEFAULT THEME MAPPINGS (MATERIAL COLORS)
// ----------------------------------------------------------------------------

val Primary = TeacherPrimary
val Secondary = StudentPrimary
val Tertiary = CommunityPeers

val Error = DangerOverdue
val Success = SuccessGood
val Warning = WarningPending
val HighlightColor = Highlight

// Status-based text
val TextSuccess = SuccessGood
val TextWarning = WarningPending
val TextError = DangerOverdue


val Background = AppBackground
val Surface = CardBackground
val OnPrimary = Color.White
val OnSurface = Color(0xFF111827)
