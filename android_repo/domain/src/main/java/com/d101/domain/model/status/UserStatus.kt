package com.d101.domain.model.status

sealed class SignInErrorStatus : ErrorStatus {
    data class UserNotFound(override val message: String = "존재하지 않는 사용자입니다.") : SignInErrorStatus()
    data class WrongPassword(override val message: String = "잘못된 비밀번호입니다.") : SignInErrorStatus()
}

sealed class GetUserErrorStatus : ErrorStatus {
    data class UserNotFound(override val message: String = "유저 정보를 읽어오는데 실패했습니다.") :
        GetUserErrorStatus()
}

sealed class AuthCodeCreationErrorStatus : ErrorStatus {
    data class EmailDuplicate(override val message: String = "중복된 이메일입니다.") :
        AuthCodeCreationErrorStatus()
}

sealed class GetUserStatusErrorStatus : ErrorStatus {
    data class UserNotFound(override val message: String = "사용자 정보를 찾을 수 없습니다.") :
        GetUserStatusErrorStatus()

    data class Fail(override val message: String = "사용자 정보를 업데이트 하는 데 실패했습니다.") :
        GetUserStatusErrorStatus()
}
