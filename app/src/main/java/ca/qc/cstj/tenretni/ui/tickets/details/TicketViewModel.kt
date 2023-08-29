package ca.qc.cstj.tenretni.ui.tickets.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import ca.qc.cstj.tenretni.core.ApiResult
import ca.qc.cstj.tenretni.core.Constants
import ca.qc.cstj.tenretni.data.repositories.CustomerRepository
import ca.qc.cstj.tenretni.data.repositories.TicketRepository
import ca.qc.cstj.tenretni.models.Customer
import ca.qc.cstj.tenretni.models.Gateway
import ca.qc.cstj.tenretni.models.Ticket
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TicketViewModel(private val _hrefTicket: String) : ViewModel() {

    private val _ticketRepository = TicketRepository()
    private val _customerRepository = CustomerRepository();
    private val _ticketUiState = MutableStateFlow<TicketUiState>(TicketUiState.Empty)
    val ticketUiState = _ticketUiState.asStateFlow()

    init {
        getTicket()
    }

    class Factory(private val href: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(String::class.java).newInstance(href)
        }
    }

    fun getTicket(hrefTicket:String = ""){
        viewModelScope.launch {
            _ticketRepository.retrieveOne(_hrefTicket).collect { apiResultTicket ->
                _ticketUiState.update {
                    when (apiResultTicket) {
                        is ApiResult.Error -> TicketUiState.Error(apiResultTicket.exception)
                        ApiResult.Loading -> TicketUiState.Empty
                        is ApiResult.Success -> TicketUiState.Success(apiResultTicket.data)
                    }
                }
            }
        }
    }

    fun getGateways(hrefCustomer: String){
        viewModelScope.launch {
            _customerRepository.retrieveGateways(hrefCustomer).collect { apiResultGateways ->
                _ticketUiState.update {
                    when(apiResultGateways){
                        is ApiResult.Error -> TicketUiState.Error(apiResultGateways.exception)
                        ApiResult.Loading -> TicketUiState.Empty
                        is ApiResult.Success -> TicketUiState.GatewaysLoaded(apiResultGateways.data)
                    }
                }
            }
        }
    }

    fun solveTicket(hrefTicket: String) {
        viewModelScope.launch {
            _ticketRepository.solveTicket(hrefTicket).collect { apiResult ->
                _ticketUiState.update {
                    when(apiResult) {
                        is ApiResult.Error -> TicketUiState.Error(apiResult.exception)
                        ApiResult.Loading -> TicketUiState.Empty
                        is ApiResult.Success -> TicketUiState.Success(apiResult.data)
                    }
                }
            }
        }
    }

    fun openTicket(href: String) {
        viewModelScope.launch {
            _ticketRepository.openTicket(href).collect { apiResult ->
                _ticketUiState.update {
                    when(apiResult) {
                        is ApiResult.Error -> TicketUiState.Error(apiResult.exception)
                        ApiResult.Loading -> TicketUiState.Empty
                        is ApiResult.Success -> TicketUiState.Success(apiResult.data)
                    }
                }
            }
        }
    }

    fun installGateway(hrefCustomer: String, rawValue: String) {
        viewModelScope.launch {
            _ticketRepository.installGateway(hrefCustomer, rawValue).collect { apiResult ->
                _ticketUiState.update {
                    when (apiResult) {
                        is ApiResult.Error -> TicketUiState.Error(apiResult.exception)
                        ApiResult.Loading -> TicketUiState.Empty
                        is ApiResult.Success -> TicketUiState.GatewayInstalled(apiResult.data)
                    }
                }
            }
        }
    }
}

