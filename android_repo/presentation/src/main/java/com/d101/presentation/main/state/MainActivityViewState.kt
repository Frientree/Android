package com.d101.presentation.main.state

sealed class MainActivityViewState {
    data object TreeView : MainActivityViewState()
    data object CalendarView : MainActivityViewState()

    data object MyPageView : MainActivityViewState()
}
