package com.d101.presentation.leaf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d101.domain.model.Leaf
import com.d101.domain.model.Result
import com.d101.domain.model.status.ErrorStatus
import com.d101.domain.model.status.LeafErrorStatus
import com.d101.domain.usecase.main.GetRandomLeafUseCase
import com.d101.domain.usecase.main.ReportLeafUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import utils.MutableEventFlow
import utils.asEventFlow
import javax.inject.Inject

@HiltViewModel
class LeafReceiveViewModel @Inject constructor(
    private val getRandomLeafUseCase: GetRandomLeafUseCase,
    private val reportLeafUseCase: ReportLeafUseCase,
) : ViewModel() {

    var checkedChipId: Int = 0

    lateinit var leaf: Leaf
    private val _leafEventFlow = MutableEventFlow<LeafReceiveViewEvent>()
    val leafEventFlow = _leafEventFlow.asEventFlow()

    init {
        viewModelScope.launch {
            emitEvent(LeafReceiveViewEvent.ShakingViewLeafPage)
        }
    }

    private fun emitEvent(event: LeafReceiveViewEvent) {
        viewModelScope.launch {
            _leafEventFlow.emit(event)
        }
    }

    fun onReadyToReceive() {
        viewModelScope.launch {
            when (val result = getRandomLeafUseCase(checkedChipId)) {
                is Result.Success -> {
                    leaf = result.data
                    emitEvent(LeafReceiveViewEvent.ReadyToReceiveView)
                }

                is Result.Failure -> {
                    when (val errorStatus = result.errorStatus) {
                        is ErrorStatus.ServerMaintenance -> emitEvent(
                            LeafReceiveViewEvent.OnServerMaintaining(errorStatus.message),
                        )

                        ErrorStatus.NetworkError() -> {
                            emitEvent(LeafReceiveViewEvent.ShowErrorToast(errorStatus.message))
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    fun onReportLeaf() {
        viewModelScope.launch {
            when (val result = reportLeafUseCase(leaf.leafNum)) {
                is Result.Success -> {
                    emitEvent(LeafReceiveViewEvent.ReportViewLeafComplete)
                }

                is Result.Failure -> {
                    when (val errorStatus = result.errorStatus) {
                        LeafErrorStatus.LeafNotFound() -> {
                            emitEvent(LeafReceiveViewEvent.ShowErrorToast(errorStatus.message))
                        }

                        LeafErrorStatus.ServerError() -> {
                            emitEvent(LeafReceiveViewEvent.ShowErrorToast(errorStatus.message))
                        }

                        is ErrorStatus.NetworkError -> emitEvent(
                            LeafReceiveViewEvent.ShowErrorToast(errorStatus.message),
                        )

                        else -> {}
                    }
                }
            }
        }
    }
}
