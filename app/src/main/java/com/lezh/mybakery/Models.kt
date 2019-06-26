package com.lezh.mybakery

import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
class Payment: Item, java.io.Serializable {
    override var id: Int = 0
    @SerialName("vendor_id")
    var vendorID: Int = 0
    var date: String = ""
    var value: Int = 0

    fun toJsonString(): String {
        return toJsonString(serializer(), this)
    }

    fun toObject(stringValue: String): Payment {
        return toObject(serializer(), stringValue)
    }
}

@Serializable
class ResponsePayments {
    var results: Array<Payment> = emptyArray()

    fun toObject(stringValue: String): ResponsePayments {
        return toObject(serializer(), stringValue)
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
        return toObject(serializer(), stringValue)
    }
}

@Serializable
class Sale: Item, java.io.Serializable {
    override var id: Int = 0
    var date: String = ""
    @SerialName("product_id")
    var productId: Int = 0
    var ammount: Int = 0
    @SerialName("vendor_id")
    var vendorId: Int = 0
    @SerialName("total_value")
    var total: Int = 0

    fun toJsonString(): String {
        return toJsonString(serializer(), this)
    }
}

@Serializable
class ResponseSales {
    var results: Array<Sale> = emptyArray()

    fun toObject(stringValue: String): ResponseSales {
        return toObject(serializer(), stringValue)
    }
}

@Serializable
class Product {
    var id: Int = 0
    var name: String = ""
    var description: String = ""
    var price: Int = 0

    override fun toString(): String {
        return name
    }
}

@Serializable
class ResponseProducts {
    var results: Array<Product> = emptyArray()

    fun toObject(stringValue: String): ResponseProducts {
        return toObject(serializer(), stringValue)
    }
}

private fun <T> toObject(serializer: DeserializationStrategy<T>, stringValue: String): T {
    return Json.nonstrict.parse(serializer, stringValue)
}

private fun <T> toJsonString(serializer: SerializationStrategy<T>, obj: T): String {
    val json = Json(JsonConfiguration.Stable)
    return json.stringify(serializer, obj)
}