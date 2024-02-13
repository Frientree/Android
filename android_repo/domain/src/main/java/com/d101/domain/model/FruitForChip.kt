package com.d101.domain.model

import com.d101.domain.utils.FruitEmotion

data class FruitForChip(
    var fruitNum: Long = -1,
    var fruitName: String = "",
    val fruitDescription: String = "",
    val fruitImageUrl: String = "",
    val fruitFeel: FruitEmotion = FruitEmotion.UNKNOWN,
)
