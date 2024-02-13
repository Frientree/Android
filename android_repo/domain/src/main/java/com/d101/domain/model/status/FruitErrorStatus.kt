package com.d101.domain.model.status

sealed class FruitErrorStatus : ErrorStatus {
    data class ApiError(override val message: String = "문제가 발생했습니다. 잠시 후 다시 시도해주세요!") :
        FruitErrorStatus()

    data class FruitNotFound(override val message: String = "열매 저장에 실패했어요!") : FruitErrorStatus()

    data class UserModifyException(override val message: String = "열매 저장에 실패했어요!") :
        FruitErrorStatus()

    data class LocalInsertError(override val message: String = "결과 저장에 실패했어요.") : FruitErrorStatus()
    data class LocalGetError(override val message: String = "열매를 불러오는 데 실패했습니다.") :
        FruitErrorStatus()

    data class UserFruitNotFound(override val message: String = "오늘 열매가 아직 없어요!") :
        FruitErrorStatus()
}
