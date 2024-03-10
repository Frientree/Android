package com.d101.presentation.model

import android.os.Parcelable
import com.d101.domain.utils.FruitEmotion
import kotlinx.parcelize.Parcelize

@Parcelize
data class FruitUiModel(
    val date: Long,
    val name: String,
    val description: String,
    val imageUrl: String,
    val calendarImageUrl: String,
    val fruitEmotion: FruitEmotion,
    val score: Int,
) : Parcelable
