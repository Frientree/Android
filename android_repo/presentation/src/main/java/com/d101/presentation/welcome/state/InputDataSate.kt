package com.d101.presentation.welcome.state

import android.text.InputType
import androidx.annotation.StringRes
import com.d101.presentation.R
import com.d101.presentation.welcome.model.DescriptionType

sealed class InputDataSate {
    abstract val label: Int
    abstract val hint: Int
    abstract val confirmVisible: Boolean
    abstract val confirmEnabled: Boolean
    abstract val description: Int
    abstract val descriptionType: DescriptionType
    abstract val inputType: Int

    data class IdInputState(
        @StringRes override val label: Int = R.string.id_upper_case,
        @StringRes override val hint: Int = R.string.id_example,
        override val confirmVisible: Boolean = true,
        override val confirmEnabled: Boolean = false,
        @StringRes override val description: Int = R.string.empty_text,
        override val descriptionType: DescriptionType = DescriptionType.DEFAULT,
        override val inputType: Int = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS,
    ) : InputDataSate()

    data class AuthNumberInputState(
        @StringRes override val label: Int = R.string.input_auth_number,
        @StringRes override val hint: Int = R.string.please_input_auth_number,
        override val confirmVisible: Boolean = true,
        override val confirmEnabled: Boolean = false,
        @StringRes override val description: Int = R.string.empty_text,
        override val descriptionType: DescriptionType = DescriptionType.DEFAULT,
        override val inputType: Int = InputType.TYPE_CLASS_TEXT,
    ) : InputDataSate()

    data class NickNameInputState(
        @StringRes override val label: Int = R.string.nickname,
        @StringRes override val hint: Int = R.string.example_nickname,
        override val confirmVisible: Boolean = true,
        override val confirmEnabled: Boolean = true,
        @StringRes override val description: Int = R.string.empty_text,
        override val descriptionType: DescriptionType = DescriptionType.DEFAULT,
        override val inputType: Int = InputType.TYPE_CLASS_TEXT,
    ) : InputDataSate()

    data class PasswordInputState(
        @StringRes override val label: Int = R.string.password,
        @StringRes override val hint: Int = R.string.example_password,
        override val confirmVisible: Boolean = false,
        override val confirmEnabled: Boolean = false,
        @StringRes override val description: Int = R.string.empty_text,
        override val descriptionType: DescriptionType = DescriptionType.DEFAULT,
        override val inputType: Int = InputType.TYPE_TEXT_VARIATION_PASSWORD,
    ) : InputDataSate()

    data class PasswordCheckInputState(
        @StringRes override val label: Int = R.string.password_check,
        @StringRes override val hint: Int = R.string.empty_text,
        override val confirmVisible: Boolean = false,
        override val confirmEnabled: Boolean = false,
        @StringRes override val description: Int = R.string.empty_text,
        override val descriptionType: DescriptionType = DescriptionType.DEFAULT,
        override val inputType: Int = InputType.TYPE_TEXT_VARIATION_PASSWORD,
    ) : InputDataSate()
}
