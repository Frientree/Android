package com.d101.presentation.fruit

sealed class FruitViewEvent {
    data object SelectInputTypeViewEvent : FruitViewEvent()
    data object FruitCreationByTextViewEvent : FruitViewEvent()
    data object FruitCreationBySpeechViewEvent : FruitViewEvent()
    data object FruitCreationLoadingViewEvent : FruitViewEvent()
    data object AfterFruitCreationViewEvent : FruitViewEvent()
    data class CardFlipEvent(
        val color: Int,
    ) : FruitViewEvent()

    data class ShowErrorToastEvent(
        val message: String,
    ) : FruitViewEvent()

    data class AppleEvent(
        val isApple: Boolean,
    ) : FruitViewEvent()

    data class OnServerMaintaining(val message: String) : FruitViewEvent()
}
