package com.lezh.mybakery.payments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import com.lezh.mybakery.*
import java.lang.Exception

class PaymentDetail : AppCompatActivity() {
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
        vendorsPicker.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                payment.vendorID = (p0?.selectedItem as Vendor).id
            }
        }

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
                commitItem(context, payment)
            }
        }
        RequestManager.getInstance(this).requestVendors(
            { response ->
                this.vendors = ResponseVendors().toObject(response).vendors
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

    //Helper methods
    private fun updateVendorsList() {
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, vendors).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        vendorsPicker.adapter = aa
        vendorsPicker.setSelection(payment.vendorID-1)
    }
}
