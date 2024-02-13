package com.d101.presentation.leaf

sealed class LeafReceiveViewEvent {
    data object ShakingViewLeafPage : LeafReceiveViewEvent()

    data object ReadyToReceiveView : LeafReceiveViewEvent()

    data object ReportViewLeafComplete : LeafReceiveViewEvent()

    data class ShowErrorToast(
        val message: String,
    ) : LeafReceiveViewEvent()

    data class OnServerMaintaining(val message: String) : LeafReceiveViewEvent()
}
