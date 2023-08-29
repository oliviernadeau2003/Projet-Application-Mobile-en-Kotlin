package ca.qc.cstj.tenretni.models

import kotlinx.serialization.Serializable

@Serializable
data class Network (
    val nextReboot: String = "",
    val updateDate: String = "",
    val nodes: NetworkNode = NetworkNode()
        )