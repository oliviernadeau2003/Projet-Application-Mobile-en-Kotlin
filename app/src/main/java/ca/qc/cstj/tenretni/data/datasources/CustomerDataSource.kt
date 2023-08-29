package ca.qc.cstj.tenretni.data.datasources

import ca.qc.cstj.tenretni.core.Constants
import ca.qc.cstj.tenretni.core.JsonDataSource
import ca.qc.cstj.tenretni.models.Customer
import ca.qc.cstj.tenretni.models.Gateway
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result
import kotlinx.serialization.decodeFromString

class CustomerDataSource : JsonDataSource() {

    fun retrieveOne(href: String): Customer {
        val (_, _, result) = href.httpGet().responseJson()

        return when (result) {
            is Result.Success -> json.decodeFromString(result.value.content)
            is Result.Failure -> throw result.error.exception
        }
    }

    fun retrieveGateways(href: String): List<Gateway> {
        val request = "$href/gateways"
        val (_, _, result) = request.httpGet().responseJson()

        return when (result) {
            is Result.Success -> json.decodeFromString(result.value.content)
            is Result.Failure -> throw result.error.exception
        }
    }
}