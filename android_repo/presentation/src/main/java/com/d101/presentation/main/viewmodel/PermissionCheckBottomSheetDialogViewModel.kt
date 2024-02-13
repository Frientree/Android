package com.d101.presentation.main.viewmodel

import androidx.lifecycle.ViewModel
import com.d101.domain.usecase.mypage.SetAlarmStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class PermissionCheckBottomSheetDialogViewModel @Inject constructor(
    private val setAlarmStatusUseCase: SetAlarmStatusUseCase,
) : ViewModel() {

    private val _neverShowAgain: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val neverShowAgain: StateFlow<Boolean> = _neverShowAgain.asStateFlow()
}
