package com.lezh.mybakery

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

class AlertManager {
    companion object {
        @JvmStatic
        fun showAlert(context: Context, title: String, message: String?, actions: Array<AlertAction>) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(title)
            builder.setMessage(message)
            actions.forEach {
                when (it.actionType) {
                    AlertActionType.positive -> builder.setPositiveButton(it.title, it.actionHandler)
                    AlertActionType.negative -> builder.setNegativeButton(it.title, it.actionHandler)
                    AlertActionType.neutral -> builder.setNeutralButton(it.title, it.actionHandler)
                }
            }
            builder.create().show()
        }
    }

    enum class AlertActionType {positive, negative, neutral}
    class AlertAction(
        val title: String,
        val actionType: AlertActionType,
        val actionHandler: ((DialogInterface, Int) -> Unit)? = null
    )
}