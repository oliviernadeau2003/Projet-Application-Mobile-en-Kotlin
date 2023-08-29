package ca.qc.cstj.tenretni.data.repositories

import ca.qc.cstj.tenretni.core.ApiResult
import ca.qc.cstj.tenretni.core.Constants
import ca.qc.cstj.tenretni.data.datasources.TicketDataSource
import ca.qc.cstj.tenretni.models.Gateway
import ca.qc.cstj.tenretni.models.Ticket
import kotlinx.android.parcel.RawValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class TicketRepository {
    private val _ticketDataSource = TicketDataSource()

    fun retrieveAll() : Flow<ApiResult<List<Ticket>>> {
        return flow {
            while(true) {
                emit(ApiResult.Loading)

                try {
                    emit(ApiResult.Success(_ticketDataSource.retrieveAll()))
                } catch (ex: Exception) {
                    emit(ApiResult.Error(ex))
                }

                delay(Constants.RefreshDelay.TICKET_DELAY)
            }
        }.flowOn(Dispatchers.IO)
    }

    fun retrieveOne(href: String) : Flow<ApiResult<Ticket>> {
        return flow {
            while(true){
                emit(ApiResult.Loading)
                try {
                    emit(ApiResult.Success(_ticketDataSource.retrieveOne(href)))
                } catch (ex: Exception) {
                    emit(ApiResult.Error(ex))
                }
                delay(Constants.RefreshDelay.TICKET_DELAY)
            }
        }.flowOn(Dispatchers.IO)
    }

    fun solveTicket(href: String) : Flow<ApiResult<Ticket>> {
        return flow {
            try {
                emit(ApiResult.Success(_ticketDataSource.solveTicket(href)))
            } catch (ex: Exception) {
                emit(ApiResult.Error(ex))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun openTicket(href: String) : Flow<ApiResult<Ticket>> {
        return flow {
            try {
                emit(ApiResult.Success(_ticketDataSource.openTicket(href)))
            } catch (ex: Exception) {
                emit(ApiResult.Error(ex))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun installGateway(hrefCustomer: String, rawValue: String) : Flow<ApiResult<Gateway>> {
        return flow {
            try {
                emit(ApiResult.Success(_ticketDataSource.installGateway(hrefCustomer, rawValue)))
            } catch (ex: Exception) {
                emit(ApiResult.Error(ex))
            }
        }.flowOn(Dispatchers.IO)
    }
}
