package com.d101.domain.model.status

sealed interface ErrorStatus {

    val message: String

    data class BadRequest(override val message: String = "잘못된 요청입니다.") : ErrorStatus
    data class NetworkError(override val message: String = "네트워크 에러가 발생했습니다.") : ErrorStatus
    data class UnknownError(override val message: String = "알수 없는 에러가 발생했습니다.") : ErrorStatus
    data class ServerMaintenance(override val message: String = "서버 점검중입니다. 잠시 후 다시 시도해주세요") :
        ErrorStatus
}
