package ca.qc.cstj.tenretni.models

import kotlinx.serialization.Serializable

@Serializable
data class Ticket(
    val href: String,
    val ticketNumber: String = "",
    val createdDate: String = "",
    val priority: String = "",
    var status: String = "",
    val customer: Customer = Customer()
)