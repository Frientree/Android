package com.d101.presentation.main.event

sealed class MainActivityEvent {
    data class ShowErrorEvent(
        val message: String,
    ) : MainActivityEvent()
    data object ShowAlarmDialogEvent : MainActivityEvent()
    data object ShowLeafSendDialog : MainActivityEvent()
    data object ShowLeafReceiveDialog : MainActivityEvent()
    data class OnServerMaintaining(val message: String) : MainActivityEvent()
}
