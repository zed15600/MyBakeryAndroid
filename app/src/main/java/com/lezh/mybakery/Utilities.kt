package com.lezh.mybakery

import android.content.Context
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

interface Item {
    var id: Int
}

interface DetailHandler {
    fun <T> openDetail(item: T?)
}

fun <T: Item> commitItem(context: Context, item: T) {
    var itemType = ""
    when (item::class) {
        Payment::class -> itemType = "payment"
        Sale::class -> itemType = "sale"
    }
    if (item.id == 0) {
        RequestManager.getInstance(context).createNewItem(
            item,
            { response ->
                item.id = (@Serializable object: Item {
                    override var id: Int = 0

                    fun toObject(stringValue: String): Item {
                        return Json.nonstrict.parse(serializer(), stringValue)
                    }
                }).toObject(response).id//Payment().toObject(response).id
                AlertManager.showAlert(
                    context,
                    "Request Successful",
                    null,
                    arrayOf(
                        AlertManager.AlertAction(
                            "Ok",
                            AlertManager.AlertActionType.neutral
                        )
                    )
                )
            },
            { error ->
                AlertManager.showAlert(
                    context,
                    "Request Failed",
                    String(error.networkResponse.data),
                    arrayOf(
                        AlertManager.AlertAction(
                            "Ok",
                            AlertManager.AlertActionType.neutral
                        )
                    )
                )
            }
        )
    } else if (item.id > 0) {
        AlertManager.showAlert(
            context,
            "Confirmation Required",
            "Are you sure you want to update this $itemType?",
            arrayOf(
                AlertManager.AlertAction(
                    "Confirm",
                    AlertManager.AlertActionType.positive
                ) { _, _ ->
                    RequestManager.getInstance(context).updateItem(
                        item,
                        { response ->
                            AlertManager.showAlert(
                                context,
                                "$itemType updated successfully.",
                                null,
                                arrayOf(
                                    AlertManager.AlertAction(
                                        "Ok",
                                        AlertManager.AlertActionType.neutral
                                    )
                                )
                            )
                        },
                        { error ->
                            AlertManager.showAlert(
                                context,
                                "Update failed",
                                String(error.networkResponse.data),
                                arrayOf(
                                    AlertManager.AlertAction(
                                        "Ok",
                                        AlertManager.AlertActionType.neutral
                                    )
                                )
                            )
                        }
                    )
                },
                AlertManager.AlertAction(
                    "Cancel",
                    AlertManager.AlertActionType.negative
                )
            )
        )
    }
}
