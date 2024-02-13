package com.d101.domain.model.status

sealed class LeafErrorStatus : ErrorStatus {
    data object LeafCreationFailed : LeafErrorStatus()
    data object NoSendLeaf : LeafErrorStatus()
    data object LeafNotFound : LeafErrorStatus()
    data object ServerError : LeafErrorStatus()
}
