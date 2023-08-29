package ca.qc.cstj.tenretni.ui.gateways.list

import ca.qc.cstj.tenretni.models.Gateway

sealed class GatewaysListUiState{
    object Loading : GatewaysListUiState()
    class Error(val exception: Exception? = null) : GatewaysListUiState()
    class Success(val gateways: List<Gateway>) : GatewaysListUiState()
}