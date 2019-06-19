package com.lezh.mybakery

import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
class Payment: java.io.Serializable {
    @Serializer(forClass = Payment::class)
    companion object: KSerializer<Payment> {
        override fun serialize(encoder: Encoder, obj: Payment) {
            val elemOutput = encoder.beginStructure(descriptor)
            elemOutput.encodeIntElement(descriptor, 1, obj.vendorID)
            elemOutput.encodeStringElement(descriptor, 2, obj.date)
            elemOutput.encodeIntElement(descriptor, 3, obj.value)
            elemOutput.endStructure(descriptor)
        }
    }

    var id: Int = 0
    @SerialName("vendor_id")
    var vendorID: Int = 0
    var date: String = ""
    var value: Int = 0

    override fun toString(): String {
        val json = Json(JsonConfiguration.Stable)
        return json.stringify(Payment.serializer(), this)
    }

    fun toObject(stringValue: String): Payment {
        return com.lezh.mybakery.toObject(Payment.serializer(), stringValue)
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

private fun <T> toObject(serializer: DeserializationStrategy<T>, stringValue: String): T {
    val json = Json(JsonConfiguration.Stable)
    return json.parse(serializer, stringValue)
}