package ca.qc.cstj.tenretni.models

import kotlinx.serialization.Serializable

@Serializable
data class Connection(
    val status: String = "",
    val ip: String = "",
    val download: Float = 0f,
    val upload: Float  = 0f,
    val signal: Int = 0,
    val ping: Int = 0
)