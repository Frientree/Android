package com.d101.presentation.tree

sealed class TreeViewEvent {
    data object MakeFruitEvent : TreeViewEvent()
    data object CheckTodayFruitEvent : TreeViewEvent()

    data object EmotionTrashEvent : TreeViewEvent()

    data object CompleteCreationEvent : TreeViewEvent()

    data class ShowErrorEvent(
        val message: String,
    ) : TreeViewEvent()

    data class ChangeTreeMessage(
        val message: String,
    ) : TreeViewEvent()

    data object ShowTutorialEvent : TreeViewEvent()

    data class OnServerMaintaining(val message: String) : TreeViewEvent()
}
