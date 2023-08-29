package ca.qc.cstj.tenretni.ui.gateways.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.qc.cstj.tenretni.core.ApiResult
import ca.qc.cstj.tenretni.data.repositories.GatewayRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GatewaysListViewModel : ViewModel() {

    private val _gatewayRepository = GatewayRepository()
    private val _mainUiState = MutableStateFlow<GatewaysListUiState>(GatewaysListUiState.Loading)

    val mainUiState = _mainUiState.asStateFlow()

    init {
        refreshGateways()
    }

    private fun refreshGateways() {
        viewModelScope.launch {
            _gatewayRepository.retrieveAll().collect {
                _mainUiState.update { _ ->
                    when (it) {
                        is ApiResult.Error -> GatewaysListUiState.Error(it.exception)
                        ApiResult.Loading -> GatewaysListUiState.Loading
                        is ApiResult.Success -> GatewaysListUiState.Success(it.data)
                    }
                }
            }
        }
    }
}

