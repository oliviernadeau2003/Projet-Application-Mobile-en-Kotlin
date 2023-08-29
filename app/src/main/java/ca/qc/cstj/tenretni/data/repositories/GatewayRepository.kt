package ca.qc.cstj.tenretni.data.repositories

import ca.qc.cstj.tenretni.core.ApiResult
import ca.qc.cstj.tenretni.core.Constants
import ca.qc.cstj.tenretni.data.datasources.GatewayDataSource
import ca.qc.cstj.tenretni.models.Gateway
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GatewayRepository {
    private val _gatewayDataSource = GatewayDataSource()

    fun retrieveAll(): Flow<ApiResult<List<Gateway>>> {
        return flow {
            while (true) {
                emit(ApiResult.Loading)

                try {
                    emit(ApiResult.Success(_gatewayDataSource.retrieveAll()))
                } catch (ex: Exception) {
                    emit(ApiResult.Error(ex))
                }

                delay(Constants.RefreshDelay.GATEWAY_DELAY)
            }
        }.flowOn(Dispatchers.IO)
    }

    fun retrieveOne(href: String): Flow<ApiResult<Gateway>> {
        return flow {
            while (true) {
                emit(ApiResult.Loading)
                try {
                    emit(ApiResult.Success(_gatewayDataSource.retrieveOne(href)))
                } catch (ex: Exception) {
                    emit(ApiResult.Error(ex))
                }
                delay(Constants.RefreshDelay.GATEWAY_DELAY)
            }
        }.flowOn(Dispatchers.IO)
    }

    fun update(href: String): Flow<ApiResult<Gateway>> {
        return flow {
            try {
                emit(ApiResult.Success(_gatewayDataSource.update(href)))
            } catch (ex: Exception) {
                emit(ApiResult.Error(ex))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun reboot(href: String): Flow<ApiResult<Gateway>> {
        return flow {
            try {
                emit(ApiResult.Success(_gatewayDataSource.reboot(href)))
            } catch (ex: Exception) {
                emit(ApiResult.Error(ex))
            }
        }.flowOn(Dispatchers.IO)
    }
}