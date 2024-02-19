package com.d101.domain.model.status

sealed class TreeErrorStatus : ErrorStatus {
    data class MessageNotFound(override val message: String = "트리 메시지가 더 없어요!") : TreeErrorStatus()
}
