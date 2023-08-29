package ca.qc.cstj.tenretni.data.datasources

import ca.qc.cstj.tenretni.core.Constants
import ca.qc.cstj.tenretni.core.JsonDataSource
import ca.qc.cstj.tenretni.models.Gateway
import ca.qc.cstj.tenretni.models.Ticket
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

class TicketDataSource : JsonDataSource() {

    fun retrieveAll(): List<Ticket> {
        val (_, _, result) = Constants.BaseURL.TICKETS.httpGet().responseJson()

        return when (result) {
            is Result.Success -> json.decodeFromString(result.value.content)
            is Result.Failure -> throw result.error.exception
        }
    }

    fun retrieveOne(href: String) : Ticket {
        val (_, _, result) = href.httpGet().responseJson()

        return when (result) {
            is Result.Success -> json.decodeFromString(result.value.content)
            is Result.Failure -> throw result.error.exception
        }
    }

    fun solveTicket(href: String) : Ticket {
        return action(href, "solve")
    }

    fun openTicket(href: String) : Ticket {
        return action(href, "open")
    }

    private fun action(href: String, action: String) : Ticket {
        val url = "$href/actions?type=$action"

        val (_,_, result) = url.httpPost().responseJson()

        return when(result) {
            is Result.Success -> json.decodeFromString(result.value.content)
            is Result.Failure -> throw result.error.exception
        }
    }

    fun installGateway(hrefCustomer: String, rawValue: String) : Gateway {
        val url = "$hrefCustomer/gateways"

        val (_,_, result) = url.httpPost().jsonBody(rawValue).responseJson()

        return when(result) {
            is Result.Success -> json.decodeFromString(result.value.content)
            is Result.Failure -> throw result.error.exception
        }
    }
}