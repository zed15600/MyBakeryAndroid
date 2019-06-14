package com.lezh.mybakery

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*

@Serializable
class Payment: java.io.Serializable {
    var id: Int = 0
    var vendor: String = ""
    var date: String = ""
    var value: Int = 0

    override fun toString(): String {
        val json = Json(JsonConfiguration.Stable)
        return json.stringify(Payment.serializer(), this)
    }

    fun toObject(stringValue: String): Payment {
        val json = Json(JsonConfiguration.Stable)
        return json.parse(Payment.serializer(), stringValue)
    }
}

@Serializable
class Response {
    var results: Array<Payment> = emptyArray()

    fun toObject(stringValue: String): Response {
        val json = Json(JsonConfiguration.Stable)
        return json.parse(Response.serializer(), stringValue)
    }
}