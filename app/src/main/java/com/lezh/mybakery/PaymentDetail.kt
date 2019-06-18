package com.lezh.mybakery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

class PaymentDetail : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var vendorsPicker: Spinner
    private lateinit var dateInput: EditText
    private lateinit var valueInput: EditText
    private lateinit var payment: Payment
    private lateinit var vendors: Array<Vendor>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_detail)
        vendorsPicker = findViewById(R.id.vendorsPicker)
        dateInput = findViewById(R.id.paymentDetailDateInput)
        valueInput = findViewById(R.id.paymentDetailValueInput)
        payment = intent.getSerializableExtra("payment") as Payment
        RequestManager().requestVendors(
            { vendors ->
                this.vendors = vendors
                updateVendorsList()
            },
            {
                //TODO something with this error
            },
            this
        )
    }

    override fun onStart() {
        super.onStart()
        dateInput.setText(payment.date)
        valueInput.setText(""+payment.value)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun updateVendorsList() {
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, vendors).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        vendorsPicker.adapter = aa
    }
}
