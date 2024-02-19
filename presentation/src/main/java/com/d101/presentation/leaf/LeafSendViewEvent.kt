package com.d101.presentation.leaf

sealed class LeafSendViewEvent {
    data object SendLeaf : LeafSendViewEvent()
    data object ReadyToSend : LeafSendViewEvent()
    data object FirstPage : LeafSendViewEvent()
    data class ShowErrorToast(
        val message: String,
    ) : LeafSendViewEvent()

    data class OnServerMaintaining(val message: String) : LeafSendViewEvent()
}
