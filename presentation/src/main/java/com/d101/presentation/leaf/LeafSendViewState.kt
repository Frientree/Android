package com.d101.presentation.leaf

sealed class LeafSendViewState {
    abstract val leafSendTitle: String
    abstract val leftLeavesCount: Int

    data class NoSendLeafSendViewState(
        override val leafSendTitle: String = "다른 사람들에게 힘이 되는 이파리를 날려보세요!",
        override val leftLeavesCount: Int = 0,
    ) : LeafSendViewState()

    data class ZeroViewLeafSendViewState(
        override val leafSendTitle: String = "당신의 이파리가 날아다니는 중이에요!",
        override val leftLeavesCount: Int,
    ) : LeafSendViewState()

    data class SomeViewLeafSateSendView(
        override val leafSendTitle: String = "",
        override val leftLeavesCount: Int,
    ) : LeafSendViewState()

    data class AlreadySendState(
        override val leafSendTitle: String,
        override val leftLeavesCount: Int = 0,
    ) : LeafSendViewState()
}
