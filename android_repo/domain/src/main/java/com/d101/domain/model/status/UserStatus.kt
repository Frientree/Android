package com.d101.domain.model.status

sealed class SignInErrorStatus : ErrorStatus {
    data class UserNotFound(override val message: String = "존재하지 않는 사용자입니다.") : SignInErrorStatus()
    data class WrongPassword(override val message: String = "잘못된 비밀번호입니다.") : SignInErrorStatus()
}

sealed class GetUserErrorStatus : ErrorStatus {
    data class UserNotFound(override val message: String = "") : GetUserErrorStatus()
}

sealed class AuthCodeCreationErrorStatus : ErrorStatus {
    data class EmailDuplicate(override val message: String = "중복된 이메일입니다.") :
        AuthCodeCreationErrorStatus()
}

sealed class GetUserStatusErrorStatus : ErrorStatus {
    data class UserNotFound(override val message: String = "") : GetUserStatusErrorStatus()
    data class Fail(override val message: String = "") : GetUserStatusErrorStatus()
}
