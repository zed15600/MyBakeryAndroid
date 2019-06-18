package com.lezh.mybakery

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import kotlinx.serialization.parse

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
}

@Serializable
class Response {
    var results: Array<Payment> = emptyArray()

    fun toObject(stringValue: String): Response {
        val json = Json(JsonConfiguration.Stable)
        return json.parse(Response.serializer(), stringValue)
    }
}

@Serializable
class Vendor {
    var id: Int = 0
    var name: String = ""
    var profit: Int = 0
    var debt: Int = 0

    override fun toString(): String {
        return name
    }
}

@Serializable
class ResponseVendors {
    var vendors: Array<Vendor> = emptyArray()

    fun toObject(stringValue: String): ResponseVendors {
        val json = Json(JsonConfiguration.Stable)
        return json.parse(ResponseVendors.serializer(), stringValue)
    }
}