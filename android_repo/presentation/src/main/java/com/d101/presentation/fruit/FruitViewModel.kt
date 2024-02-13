package com.d101.presentation.fruit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d101.domain.model.AppleData
import com.d101.domain.model.FruitForChip
import com.d101.domain.model.Result
import com.d101.domain.model.status.ErrorStatus
import com.d101.domain.model.status.FruitErrorStatus
import com.d101.domain.usecase.main.DecideTodayFruitUseCase
import com.d101.domain.usecase.main.MakeFruitBySpeechUseCase
import com.d101.domain.usecase.main.MakeFruitByTextUseCase
import com.d101.domain.usecase.usermanagement.ManageUserStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import utils.MutableEventFlow
import utils.asEventFlow
import java.io.File
import javax.inject.Inject

@HiltViewModel
class FruitViewModel @Inject constructor(
    private val makeFruitByTextUseCase: MakeFruitByTextUseCase,
    private val makeFruitBySpeechUseCase: MakeFruitBySpeechUseCase,
    private val decideTodayFruitUseCase: DecideTodayFruitUseCase,
    private val manageUserStatusUseCase: ManageUserStatusUseCase,
) : ViewModel() {
    private val _todayFruitList: MutableStateFlow<List<FruitForChip>> = MutableStateFlow(listOf())
    val todayFruitList: StateFlow<List<FruitForChip>> = _todayFruitList.asStateFlow()

    private val _selectedFruit: MutableStateFlow<FruitForChip> = MutableStateFlow(FruitForChip())
    val selectedFruit: StateFlow<FruitForChip> = _selectedFruit.asStateFlow()

    val inputText = MutableStateFlow("")
    private lateinit var audioFile: File

    var isTextInput = true

    private val _eventFlow = MutableEventFlow<FruitViewEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    private var _apple: AppleData? = null
    val apple get() = _apple!!

    fun setTodayFruitList() {
        viewModelScope.launch {
            val delay = async {
                delay(3_000L)
            }

            val fruitResult = if (isTextInput) {
                async {
                    makeFruitByTextUseCase(inputText.value)
                }
            } else {
                async {
                    makeFruitBySpeechUseCase(audioFile)
                }
            }
            delay.await()
            when (val result = fruitResult.await()) {
                is Result.Success -> {
                    _todayFruitList.update { result.data }
                }

                is Result.Failure -> {
                    when (val errorStatus = result.errorStatus) {
                        is FruitErrorStatus.LocalInsertError -> emitEvent(
                            FruitViewEvent.ShowErrorToastEvent(
                                errorStatus.message,
                            ),
                        )

                        is ErrorStatus.ServerMaintenance -> emitEvent(
                            FruitViewEvent.OnServerMaintaining(errorStatus.message),
                        )

                        is ErrorStatus.NetworkError -> emitEvent(
                            FruitViewEvent.ShowErrorToastEvent(
                                errorStatus.message,
                            ),
                        )

                        else -> {}
                    }
                }
            }
        }
    }

    private val _appleUiState: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val appleUiState: StateFlow<Boolean> = _appleUiState.asStateFlow()

    fun setAppleViewVisibility(flip: Boolean) {
        _appleUiState.update { flip }
    }

    fun cardFlip(fruitColorValue: Int) {
        emitEvent(FruitViewEvent.CardFlipEvent(fruitColorValue))
    }

    fun setAudioFile(file: File) {
        audioFile = file
    }

    fun setSelectedFruit(fruit: FruitForChip) {
        _selectedFruit.update { fruit }
    }

    fun saveSelectedFruit() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = decideTodayFruitUseCase(selectedFruit.value.fruitNum)) {
                is Result.Success -> {
                    manageUserStatusUseCase.updateUserStatus()
                    if (result.data.isApple) {
                        _apple = result.data
                        emitEvent(FruitViewEvent.AppleEvent(true))
                    } else {
                        emitEvent(FruitViewEvent.AppleEvent(false))
                    }
                }

                is Result.Failure -> {
                    when (val errorStatus = result.errorStatus) {
                        is ErrorStatus.ServerMaintenance -> emitEvent(
                            FruitViewEvent.OnServerMaintaining(errorStatus.message),
                        )

                        is FruitErrorStatus.LocalInsertError -> emitEvent(
                            FruitViewEvent.ShowErrorToastEvent(
                                "결과가 저장되지 못했습니다.",
                            ),
                        )

                        is ErrorStatus.NetworkError -> emitEvent(
                            FruitViewEvent.ShowErrorToastEvent(
                                errorStatus.message,
                            ),
                        )

                        else -> {}
                    }
                }
            }
        }
    }

    private fun emitEvent(event: FruitViewEvent) {
        viewModelScope.launch { _eventFlow.emit(event) }
    }

    fun onGoSelectInputTypeView() {
        emitEvent(FruitViewEvent.SelectInputTypeViewEvent)
    }

    fun onGoFruitLoadingView() {
        emitEvent(FruitViewEvent.FruitCreationLoadingViewEvent)
    }

    fun onGoReultView() {
        emitEvent(FruitViewEvent.AfterFruitCreationViewEvent)
    }

    fun onGoCreateionByTextView() {
        emitEvent(FruitViewEvent.FruitCreationByTextViewEvent)
    }

    fun onGoCreateionBySpeechView() {
        emitEvent(FruitViewEvent.FruitCreationBySpeechViewEvent)
    }
}
