package ca.qc.cstj.tenretni.data.datasources

import ca.qc.cstj.tenretni.core.Constants
import ca.qc.cstj.tenretni.core.JsonDataSource
import ca.qc.cstj.tenretni.models.Gateway
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result
import kotlinx.serialization.decodeFromString

class GatewayDataSource : JsonDataSource() {

    fun retrieveAll(): List<Gateway> {
        val (_, _, result) = Constants.BaseURL.GATEWAYS.httpGet().responseJson()

        return when (result) {
            is Result.Success -> json.decodeFromString(result.value.content)
            is Result.Failure -> throw result.error.exception
        }
    }

    fun retrieveOne(href: String): Gateway {
        val (_, _, result) = href.httpGet().responseJson()

        return when (result) {
            is Result.Success -> json.decodeFromString(result.value.content)
            is Result.Failure -> throw result.error.exception
        }
    }

    fun update(href: String): Gateway {
        return action(href, "update")
    }

    fun reboot(href: String): Gateway {
        return action(href, "reboot")
    }

    private fun action(href: String, action: String): Gateway {
        val url = "$href/actions?type=$action"

        val (_, _, result) = url.httpPost().responseJson()
        return when (result) {
            is Result.Success -> json.decodeFromString(result.value.content)
            is Result.Failure -> throw result.error.exception
        }
    }
}