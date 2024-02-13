package com.d101.presentation.tree

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d101.domain.model.Fruit
import com.d101.domain.model.Result
import com.d101.domain.model.status.ErrorStatus
import com.d101.domain.model.status.FruitErrorStatus
import com.d101.domain.model.status.TreeErrorStatus
import com.d101.domain.usecase.main.GetMessageFromTreeUseCase
import com.d101.domain.usecase.main.GetTodayFruitUseCase
import com.d101.domain.usecase.usermanagement.ManageUserStatusUseCase
import com.d101.presentation.main.state.TreeFragmentViewState
import com.d101.presentation.main.state.TreeMessageState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import utils.MutableEventFlow
import utils.asEventFlow
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TreeViewModel @Inject constructor(
    private val manageUserStatusUseCase: ManageUserStatusUseCase,
    private val getTodayFruitUseCase: GetTodayFruitUseCase,
    private val getMessageFromTreeUseCase: GetMessageFromTreeUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<TreeFragmentViewState>(
        TreeFragmentViewState.FruitNotCreated("", "", "안녕 난 프렌트리야!"),
    )
    val uiState = _uiState.asStateFlow()

    private val _messageState = MutableStateFlow<TreeMessageState>(
        TreeMessageState.EnableMessage,
    )
    val messageState = _messageState.asStateFlow()

    private val _eventFlow = MutableEventFlow<TreeViewEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    private val localDate: LocalDate = LocalDate.now()

    lateinit var todayFruit: Fruit

    init {
        getUserStatus()
    }

    private fun emitEvent(event: TreeViewEvent) {
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }

    fun enableMessage() {
        _messageState.update { TreeMessageState.EnableMessage }
    }

    fun showFruitDialog() {
        emitEvent(TreeViewEvent.CompleteCreationEvent)
    }

    fun onGetTreeMessage() {
        if (messageState.value is TreeMessageState.EnableMessage) {
            viewModelScope.launch {
                when (val result = getMessageFromTreeUseCase()) {
                    is Result.Success -> {
                        _messageState.update { TreeMessageState.NoAccessMessage }
                        emitEvent(TreeViewEvent.ChangeTreeMessage(result.data))
                    }

                    is Result.Failure -> {
                        when (val errorStatus = result.errorStatus) {
                            is TreeErrorStatus.MessageNotFound -> emitEvent(
                                TreeViewEvent.ShowErrorEvent(
                                    "나무 메세지를 가져올 수 없습니다.",
                                ),
                            )

                            is ErrorStatus.NetworkError -> emitEvent(
                                TreeViewEvent.ShowErrorEvent(
                                    errorStatus.message,
                                ),
                            )

                            ErrorStatus.ServerMaintenance() -> emitEvent(
                                TreeViewEvent.OnServerMaintaining(errorStatus.message),
                            )

                            else -> {}
                        }
                    }
                }
            }
        }
    }

    private fun getTodayFruit() {
        val localDate: LocalDate = LocalDate.now()
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = getTodayFruitUseCase(localDate.toString())) {
                is Result.Success -> {
                    todayFruit = result.data
                    emitEvent(TreeViewEvent.CheckTodayFruitEvent)
                }

                is Result.Failure -> {
                    when (val errorStatus = result.errorStatus) {
                        is ErrorStatus.ServerMaintenance -> emitEvent(
                            TreeViewEvent.OnServerMaintaining(errorStatus.message),
                        )

                        is FruitErrorStatus.LocalGetError -> {
                            emitEvent(TreeViewEvent.ShowErrorEvent(errorStatus.message))
                        }

                        is ErrorStatus.NetworkError -> emitEvent(
                            TreeViewEvent.ShowErrorEvent(
                                errorStatus.message,
                            ),
                        )

                        else -> {}
                    }
                }
            }
        }
    }

    private fun initTodayDate(localDate: LocalDate): String {
        return String.format(
            "%d / %d / %d",
            localDate.year,
            localDate.monthValue,
            localDate.dayOfMonth,
        )
    }

    private fun getUserStatus() {
        viewModelScope.launch {
            manageUserStatusUseCase.getUserStatus().collect { userStatus ->
                _uiState.update { currentState ->
                    if (userStatus.userFruitStatus.not()) {
                        TreeFragmentViewState.FruitCreated(
                            treeName = userStatus.treeName,
                            todayDate = initTodayDate(localDate),
                        )
                    } else {
                        TreeFragmentViewState.FruitNotCreated(
                            treeName = userStatus.treeName,
                            todayDate = initTodayDate(localDate),
                        )
                    }
                }
            }
        }
    }

    fun onLongClickEmotionTrashMode() {
        when (uiState.value) {
            is TreeFragmentViewState.EmotionTrashMode -> {
                // 모드 끄기
                getUserStatus()
            }

            else -> {
                // 감쓰 모드로
                _uiState.update { currentState ->
                    TreeFragmentViewState.EmotionTrashMode(
                        todayDate = currentState.todayDate,
                        treeName = currentState.treeName,
                    )
                }
            }
        }
    }

    fun onButtonClick() {
        when (uiState.value) {
            is TreeFragmentViewState.FruitCreated -> {
                getTodayFruit()
            }

            is TreeFragmentViewState.FruitNotCreated -> {
                emitEvent(TreeViewEvent.MakeFruitEvent)
            }

            is TreeFragmentViewState.EmotionTrashMode -> {
                emitEvent(TreeViewEvent.EmotionTrashEvent)
            }
        }
    }

    fun onTutorialButtonClicked() {
        emitEvent(TreeViewEvent.ShowTutorialEvent)
    }
}
