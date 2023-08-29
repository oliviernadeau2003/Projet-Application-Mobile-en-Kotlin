package ca.qc.cstj.tenretni.ui.tickets.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.qc.cstj.tenretni.core.ApiResult
import ca.qc.cstj.tenretni.data.repositories.TicketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TicketsListViewModel : ViewModel() {

    private val _ticketRepository = TicketRepository()
    private val _mainUiState = MutableStateFlow<TicketsListUiState>(TicketsListUiState.Loading)

    val mainUiState = _mainUiState.asStateFlow()

    init {
        refreshTickets()
    }

    private fun refreshTickets() {
        viewModelScope.launch {
            _ticketRepository.retrieveAll().collect {
                _mainUiState.update { _ ->
                    when(it) {
                        is ApiResult.Error -> TicketsListUiState.Error(it.exception)
                        ApiResult.Loading -> TicketsListUiState.Loading
                        is ApiResult.Success -> TicketsListUiState.Success(it.data)
                    }
                }
            }
        }
    }
}