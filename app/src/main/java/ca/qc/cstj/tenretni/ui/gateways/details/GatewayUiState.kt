package ca.qc.cstj.tenretni.ui.gateways.details

import ca.qc.cstj.tenretni.models.Gateway

sealed class GatewayUiState {
    object Empty : GatewayUiState()
    class Error(val exception: Exception) : GatewayUiState()
    class Success(val gateway: Gateway) : GatewayUiState()
}