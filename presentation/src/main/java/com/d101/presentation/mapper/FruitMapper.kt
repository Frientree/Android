package com.d101.presentation.mapper

import com.d101.domain.model.Fruit
import com.d101.presentation.model.FruitUiModel

object FruitMapper {
    fun Fruit.toFruitUiModel() = FruitUiModel(
        date = date,
        name = name,
        description = description,
        imageUrl = imageUrl,
        calendarImageUrl = calendarImageUrl,
        fruitEmotion = fruitEmotion,
        score = score,
    )

}
