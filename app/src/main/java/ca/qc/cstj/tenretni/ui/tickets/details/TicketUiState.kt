package ca.qc.cstj.tenretni.ui.tickets.details

import ca.qc.cstj.tenretni.models.Customer
import ca.qc.cstj.tenretni.models.Gateway
import ca.qc.cstj.tenretni.models.Ticket

sealed class TicketUiState {
    object Empty : TicketUiState()
    class Error(val exception: Exception) : TicketUiState()
    class Success(val ticket: Ticket) : TicketUiState()
    class GatewaysLoaded(val gateways: List<Gateway>) : TicketUiState()
    class  GatewayInstalled(val gateway: Gateway) : TicketUiState()
}