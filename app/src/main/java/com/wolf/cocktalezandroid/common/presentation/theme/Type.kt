package com.wolf.cocktalezandroid.common.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.wolf.cocktalezandroid.ui.theme.Typography

val AppTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.quicksand_bold)),
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp
    ),
    displayMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.quicksand_bold)),
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.quicksand_bold)),
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.quicksand_regular)),
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.quicksand_regular)),
        fontSize = 14.sp,
        color = Color.Gray
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.quicksand_bold)),
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
    )
)