package ca.qc.cstj.tenretni.data.repositories

import ca.qc.cstj.tenretni.core.ApiResult
import ca.qc.cstj.tenretni.core.Constants
import ca.qc.cstj.tenretni.data.datasources.CustomerDataSource
import ca.qc.cstj.tenretni.data.datasources.GatewayDataSource
import ca.qc.cstj.tenretni.models.Customer
import ca.qc.cstj.tenretni.models.Gateway
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class CustomerRepository {
    private val _customerDataSource = CustomerDataSource()

    fun retrieveOne(href: String) : Flow<ApiResult<Customer>> {
        return flow {
            emit(ApiResult.Loading)
            try {
                emit(ApiResult.Success(_customerDataSource.retrieveOne(href)))
            } catch (ex: Exception) {
                emit(ApiResult.Error(ex))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun retrieveGateways(href: String) : Flow<ApiResult<List<Gateway>>> {
        return flow {
            emit(ApiResult.Loading)
            try {
                emit(ApiResult.Success(_customerDataSource.retrieveGateways(href)))
            } catch (ex: Exception) {
                emit(ApiResult.Error(ex))
            }
        }.flowOn(Dispatchers.IO)
    }
}