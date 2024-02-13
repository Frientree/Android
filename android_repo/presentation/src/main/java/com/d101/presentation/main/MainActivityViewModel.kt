package com.d101.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d101.domain.model.Result
import com.d101.domain.model.status.ErrorStatus
import com.d101.domain.model.status.GetUserStatusErrorStatus
import com.d101.domain.usecase.mypage.SetAlarmStatusUseCase
import com.d101.domain.usecase.usermanagement.GetUserInfoUseCase
import com.d101.domain.usecase.usermanagement.ManageUserStatusUseCase
import com.d101.domain.usecase.usermanagement.SetNotificationNeverAskUseCase
import com.d101.domain.usecase.usermanagement.UpdateFcmTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import utils.MutableEventFlow
import utils.asEventFlow
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val updateFcmTokenUseCase: UpdateFcmTokenUseCase,
    private val manageUserStatusUseCase: ManageUserStatusUseCase,
    private val setAlarmStatusUseCase: SetAlarmStatusUseCase,
    private val setNotificationNeverAskUseCase: SetNotificationNeverAskUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
) : ViewModel() {

    private val _currentViewState: MutableStateFlow<MainActivityViewState> =
        MutableStateFlow(MainActivityViewState.TreeView)
    val currentViewState: StateFlow<MainActivityViewState> = _currentViewState.asStateFlow()

    private val _visibility: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val visibility: StateFlow<Boolean> = _visibility.asStateFlow()

    private val _eventFlow = MutableEventFlow<MainActivityEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    init {
        viewModelScope.launch {
            val userInfo = getUserInfoUseCase().first()
            if (userInfo is Result.Success) {
                if (userInfo.data.notificationCheckNeverShow.not()) {
                    emitEvent(MainActivityEvent.ShowAlarmDialogEvent)
                }
            } else {
                emitEvent(MainActivityEvent.ShowErrorEvent("사용자 정보를 가져오는 데 실패했습니다."))
            }
        }
        updateUserStatus()
    }

    private fun emitEvent(event: MainActivityEvent) {
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }

    fun changeViewState(state: MainActivityViewState) {
        _currentViewState.update { state }
    }

    fun finishAnimation(add: Boolean) {
        _visibility.update { add }
    }

    fun showLeafSendDialog() {
        emitEvent(MainActivityEvent.ShowLeafSendDialog)
    }

    fun showLeafReceiveDialog() {
        emitEvent(MainActivityEvent.ShowLeafReceiveDialog)
    }

    fun uploadToken(token: String) {
        viewModelScope.launch {
            updateFcmTokenUseCase(token)
        }
    }

    fun setUserAlarmStatus(status: Boolean) {
        viewModelScope.launch {
            when (val result = setAlarmStatusUseCase(status)) {
                is Result.Success -> {}
                is Result.Failure -> {
                    when (result.errorStatus) {
                        is ErrorStatus.NetworkError -> {
                            emitEvent(MainActivityEvent.ShowErrorEvent("네트워크 에러가 발생했습니다."))
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    private fun updateUserStatus() {
        viewModelScope.launch {
            when (val result = manageUserStatusUseCase.updateUserStatus()) {
                is Result.Success -> {}
                is Result.Failure -> {
                    when (val errorStatus = result.errorStatus) {
                        is ErrorStatus.ServerMaintenance -> {
                            emitEvent(MainActivityEvent.OnServerMaintaining(errorStatus.message))
                        }

                        is GetUserStatusErrorStatus.Fail,
                        -> {
                            emitEvent(MainActivityEvent.ShowErrorEvent(errorStatus.message))
                        }

                        is ErrorStatus.NetworkError -> {
                            emitEvent(MainActivityEvent.ShowErrorEvent(errorStatus.message))
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    fun setNotificationNeverAsk(neverAskAgain: Boolean) {
        viewModelScope.launch {
            when (setNotificationNeverAskUseCase(neverAskAgain)) {
                is Result.Success -> {}
                is Result.Failure -> emitEvent(
                    MainActivityEvent.ShowErrorEvent("알 수 없는 에러가 발생했습니다."),
                )
            }
        }
    }
}
