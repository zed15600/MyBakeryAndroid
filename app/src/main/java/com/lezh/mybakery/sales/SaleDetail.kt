package com.lezh.mybakery.sales

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import com.lezh.mybakery.*

class SaleDetail: AppCompatActivity() {
    private lateinit var dateInput: EditText
    private lateinit var vendorPicker: Spinner
    private lateinit var productPicker: Spinner
    private lateinit var amount: EditText
    private lateinit var total: TextView
    private lateinit var sale: Sale
    private lateinit var vendors: Array<Vendor>
    private lateinit var products: Array<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sale_detail)

        sale = intent.getSerializableExtra("sale") as Sale

        total = findViewById(R.id.sale_total_label)

        dateInput = findViewById(R.id.sale_date_input)
        dateInput.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                sale.date = p0.toString()
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        vendorPicker = findViewById(R.id.sale_vendor_picker)
        vendorPicker.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selected = p0?.selectedItem as Vendor
                sale.vendorId = selected.id
            }

        }

        productPicker = findViewById(R.id.sale_product_picker)
        productPicker.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selected = p0?.selectedItem as Product
                sale.productId = selected.id
                updateTotal()
            }

        }

        amount = findViewById(R.id.sale_amount_input)
        amount.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (productPicker.selectedItemPosition != AdapterView.INVALID_POSITION) {
                    sale.ammount = p0.toString().toInt()
                    updateTotal()
                }
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        RequestManager.getInstance(this).requestVendors(
            { response ->
                vendors = ResponseVendors().toObject(response).vendors
                updateVendors()
            },
            {
                //TODO something with this error
            }
        )
        RequestManager.getInstance(this).requestProducts(
            {
                val products = ResponseProducts().toObject(it)
                this.products = products.results
                updateProducts()
            },
            {
                //TODO something with this error
            }
        )

        findViewById<View>(R.id.sale_commit).apply {
            setOnClickListener {
                commitItem(context, sale)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dateInput.setText(sale.date)
        amount.setText("${sale.ammount}")

    }

    private fun updateVendors() {
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, vendors).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        vendorPicker.adapter = aa
        vendorPicker.setSelection(vendors.indexOf(vendors.find { it.id == sale.vendorId }))
    }

    private fun updateProducts() {
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, products).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        productPicker.adapter = aa
        productPicker.setSelection(products.indexOf(products.find { it.id == sale.productId }))
    }

    private fun updateTotal() {
        val value = amount.text.toString().toInt() * (productPicker.selectedItem as Product).price
        sale.total = value
        total.text = "$value"
    }
}
