package com.d101.presentation.tree

sealed class TreeMessageState {
    data object EnableMessage : TreeMessageState()
    data object NoAccessMessage : TreeMessageState()
}
