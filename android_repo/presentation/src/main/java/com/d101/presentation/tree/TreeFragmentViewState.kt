package com.d101.presentation.tree

sealed class TreeFragmentViewState {
    abstract val todayDate: String
    abstract val treeName: String
    abstract val treeMessage: String
    abstract val fruitStatusText: String
    abstract val buttonText: String

    data class FruitCreated(
        override val todayDate: String,
        override val treeName: String,
        override val treeMessage: String = "안녕 난 프렌트리야!",
        override val fruitStatusText: String = "오늘 만든 열매를 확인해보세요!",
        override val buttonText: String = "오늘의 열매 확인하기",
    ) : TreeFragmentViewState()

    data class FruitNotCreated(
        override val todayDate: String,
        override val treeName: String,
        override val treeMessage: String = "안녕 난 프렌트리야!",
        override val fruitStatusText: String = "오늘의 감정을 기록해보세요!",
        override val buttonText: String = "오늘의 열매 생성하기",
    ) : TreeFragmentViewState()

    data class EmotionTrashMode(
        override val todayDate: String,
        override val treeName: String,
        override val treeMessage: String = "여기서 마음껏 이야기해봐!",
        override val fruitStatusText: String = "힘든 일이 있으신가요?",
        override val buttonText: String = "감정 토로하기",
    ) : TreeFragmentViewState()
}
