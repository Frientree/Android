package com.d101.domain.model.status

sealed class PasswordFindErrorStatus : ErrorStatus {
    data class UserNotFound(override val message: String = "해당 이메일로 가입된 계정이 없습니다.") :
        PasswordFindErrorStatus()
}

sealed class PassWordChangeErrorStatus : ErrorStatus {
    data class PasswordPatternMismatch(override val message: String = "") :
        PassWordChangeErrorStatus()
}
