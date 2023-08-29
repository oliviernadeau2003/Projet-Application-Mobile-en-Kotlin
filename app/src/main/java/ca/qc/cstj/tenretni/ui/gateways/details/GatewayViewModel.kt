package ca.qc.cstj.tenretni.ui.gateways.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import ca.qc.cstj.tenretni.core.ApiResult
import ca.qc.cstj.tenretni.data.repositories.GatewayRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GatewayViewModel(private val _href: String) : ViewModel() {
    private val _gatewayRepository = GatewayRepository()
    private val _gatewayUiState = MutableStateFlow<GatewayUiState>(GatewayUiState.Empty)
    val gatewayUiState = _gatewayUiState.asStateFlow()

    init {
        getGateway()
    }

    fun getGateway() {
        viewModelScope.launch {
            _gatewayRepository.retrieveOne(_href).collect { apiResult ->
                _gatewayUiState.update {
                    when (apiResult) {
                        is ApiResult.Error -> GatewayUiState.Error(apiResult.exception)
                        ApiResult.Loading -> GatewayUiState.Empty
                        is ApiResult.Success -> GatewayUiState.Success(apiResult.data)
                    }
                }
            }
        }
    }

    class Factory(private val href: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(String::class.java).newInstance(href)
        }
    }

    fun updateGateway(hrefGateway: String) {
        viewModelScope.launch {
            _gatewayRepository.update(hrefGateway).collect { apiResult ->
                _gatewayUiState.update {
                    when (apiResult) {
                        is ApiResult.Error -> GatewayUiState.Error(apiResult.exception)
                        ApiResult.Loading -> GatewayUiState.Empty
                        is ApiResult.Success -> GatewayUiState.Success(apiResult.data)
                    }
                }
            }
        }
    }

    fun rebootGateway(hrefGateway: String) {
        viewModelScope.launch {
            _gatewayRepository.reboot(hrefGateway).collect { apiResult ->
                _gatewayUiState.update {
                    when (apiResult) {
                        is ApiResult.Error -> GatewayUiState.Error(apiResult.exception)
                        ApiResult.Loading -> GatewayUiState.Empty
                        is ApiResult.Success -> GatewayUiState.Success(apiResult.data)
                    }
                }
            }
        }
    }
}