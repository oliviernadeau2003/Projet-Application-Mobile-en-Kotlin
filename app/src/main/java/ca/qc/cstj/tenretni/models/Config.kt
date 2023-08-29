package ca.qc.cstj.tenretni.models

import kotlinx.serialization.Serializable

@Serializable
data class Config (
    val mac: String = "",
    val SSID: String = "",
    val version: String = "",
    val kernel: List<String> = emptyList(),
    val kernelRevision: Int = 0
        )