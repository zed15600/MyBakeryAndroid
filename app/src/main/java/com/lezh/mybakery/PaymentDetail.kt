package com.lezh.mybakery

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.fragment_payment_item.*
import java.lang.Exception

class PaymentDetail : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var vendorsPicker: Spinner
    private lateinit var dateInput: EditText
    private lateinit var valueInput: EditText
    private lateinit var payment: Payment
    private lateinit var vendors: Array<Vendor>

    //Lifecycle methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_detail)

        payment = intent.getSerializableExtra("payment") as Payment

        vendorsPicker = findViewById(R.id.vendorsPicker)
        vendorsPicker.onItemSelectedListener = this

        dateInput = findViewById(R.id.paymentDetailDateInput)
        dateInput.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(editable: Editable) {
                payment.date = editable.toString()
            }
        })

        valueInput = findViewById(R.id.paymentDetailValueInput)
        valueInput.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                try {
                    payment.value = p0.toString().toInt()
                } catch (e: Exception) {}
            }
        })

        findViewById<View>(R.id.payment_commit).apply {
            setOnClickListener {
                if (payment.id == 0) {
                    RequestManager.getInstance(context).createNewPayment(
                        payment,
                        { response ->
                            payment.id = Payment().toObject(response).id
                            showAlert(
                                "Request Successful",
                                null,
                                arrayOf(
                                    AlertAction("Ok", AlertActionType.neutral)
                                )
                            )
                        },
                        { error ->
                            showAlert(
                                "Request Failed",
                                String(error.networkResponse.data),
                                arrayOf(
                                    AlertAction("Ok", AlertActionType.neutral)
                                )
                            )
                        }
                    )
                } else if (payment.id > 0) {
                    showAlert(
                        "Confirmation Required",
                        "Are you sure you want to update this payment?",
                        arrayOf(
                            AlertAction(
                                "Confirm",
                                AlertActionType.positive
                            ) { _, _ ->
                                RequestManager.getInstance(context).updatePayment(
                                    payment,
                                    {response ->
                                        showAlert(
                                            "Payment updated successfully.",
                                            null,
                                            arrayOf(
                                                AlertAction("Ok", AlertActionType.neutral)
                                            )
                                        )
                                    },
                                    {error ->
                                        showAlert(
                                            "Update failed",
                                            String(error.networkResponse.data),
                                            arrayOf(
                                                AlertAction("Ok", AlertActionType.neutral)
                                            )
                                        )
                                    }
                                )
                            },
                            AlertAction("Cancel", AlertActionType.negative)
                        )
                    )
                }
            }
        }
        RequestManager.getInstance(this).requestVendors(
            { vendors ->
                this.vendors = vendors
                updateVendorsList()
            },
            {
                //TODO something with this error
            }
        )
    }

    override fun onStart() {
        super.onStart()
        vendorsPicker.setSelection(payment.vendorID-1)
        dateInput.setText(payment.date)
        valueInput.setText(""+payment.value)
    }

    //View delegation methods
    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        payment.vendorID = (p0?.selectedItem as Vendor).id
    }


    //Helper methods
    private fun updateVendorsList() {
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, vendors).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        vendorsPicker.adapter = aa
        vendorsPicker.setSelection(payment.vendorID-1)
    }

    private fun showAlert(title: String, message: String?, actions: Array<AlertAction>) {
        val builder = AlertDialog.Builder(this)
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

    enum class AlertActionType {positive, negative, neutral}
    private class AlertAction(
        val title: String,
        val actionType: AlertActionType,
        val actionHandler: ((DialogInterface, Int) -> Unit)? = null
    )

}
