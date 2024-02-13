package com.d101.domain.model.status

sealed class LeafErrorStatus : ErrorStatus {
    data class LeafCreationFailed(override val message: String = "") : LeafErrorStatus()
    data class NoSendLeaf(override val message: String = "해당 이파리를 찾을 수 없습니다.") : LeafErrorStatus()
    data class LeafNotFound(override val message: String = "해당 이파리를 찾을 수 없습니다.") : LeafErrorStatus()
    data class ServerError(override val message: String = "") : LeafErrorStatus()
}
