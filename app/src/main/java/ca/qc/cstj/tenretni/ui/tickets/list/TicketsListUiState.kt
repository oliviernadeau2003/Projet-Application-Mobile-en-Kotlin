package ca.qc.cstj.tenretni.ui.tickets.list

import ca.qc.cstj.tenretni.models.Ticket
import java.lang.Exception

sealed class TicketsListUiState {
    object Loading: TicketsListUiState()
    class Error(val exception: Exception? = null) : TicketsListUiState()
    class Success(val tickets: List<Ticket>): TicketsListUiState()
}