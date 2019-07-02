package com.lezh.mybakery

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.*
import kotlinx.serialization.toUtf8Bytes
import kotlin.collections.HashMap

class RequestManager constructor(val context: Context) {
    internal class StringRequest(
        method: Int,
        url: String,
        private val pars: String,
        completion: (String) -> Unit,
        errorCompletion: (VolleyError) -> Unit
    ): com.android.volley.toolbox.StringRequest(method, url, completion, errorCompletion) {

        override fun getHeaders(): MutableMap<String, String> {
            return HashMap<String, String>().apply {
                put("Content-Type", "application/json")
            }
        }
        override fun getBody(): ByteArray {
            return pars.toUtf8Bytes()
        }
    }
    companion object {
        @Volatile
        private var INSTANCE: RequestManager? = null
        fun getInstance(context: Context) = INSTANCE ?: synchronized(this) {
            INSTANCE ?: RequestManager(context).also {
                INSTANCE = it
            }
        }
    }
    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }
    private val baseUrl = "http://54.233.228.170:3000/%s.json"

    fun requestPayments(completion: (String) -> Unit, errorCompletion: (VolleyError)->Unit) {
        request("", "payments", Request.Method.GET, completion, errorCompletion)
    }

    fun requestVendors(completion: (String) -> Unit, errorCompletion: (VolleyError) -> Unit) {
        request("", "vendors", Request.Method.GET, completion, errorCompletion)
    }

    fun requestSales(completion: (String) -> Unit, errorCompletion: (VolleyError) -> Unit) {
        request("", "sales", Request.Method.GET, completion, errorCompletion)
    }

    fun requestProducts(completion: (String) -> Unit, errorCompletion: (VolleyError) -> Unit) {
        request("", "products", Request.Method.GET, completion, errorCompletion)
    }

    fun requestExpenditures(completion: (String) -> Unit, errorCompletion: (VolleyError) -> Unit) {
        request("", "expenditures", Request.Method.GET, completion, errorCompletion)
    }

    fun requestFeedstocks(completion: (String) -> Unit, errorCompletion: (VolleyError) -> Unit) {
        request("", "feedstocks", Request.Method.GET, completion, errorCompletion)
    }

    fun requestExpenditureDetails(expenditure: Expenditure, completion: (String) -> Unit, errorCompletion: (VolleyError) -> Unit) {
        request("", "expenditures/${expenditure.id}", Request.Method.GET, completion, errorCompletion)
    }

    fun createNewItem(item: Item, completion: (String) -> Unit, errorCompletion: (VolleyError) -> Unit) {
        when (item::class) {
            Payment::class -> createNewPayment(item as Payment, completion, errorCompletion)
            Sale::class -> createNewSale(item as Sale, completion, errorCompletion)
            Expenditure::class -> createNewExpenditure(item as Expenditure, completion, errorCompletion)
        }
    }

    fun updateItem(item: Item, completion: (String) -> Unit, errorCompletion: (VolleyError) -> Unit) {
        when (item::class) {
            Payment::class -> updatePayment(item as Payment, completion, errorCompletion)
            Sale::class -> updateSale(item as Sale, completion, errorCompletion)
            Expenditure::class -> updateExpenditure(item as Expenditure, completion, errorCompletion)
        }
    }

    private fun createNewPayment(payment: Payment, completion: (String) -> Unit, errorCompletion: (VolleyError) -> Unit) {
        val params = "{\"payment\":${payment.toJsonString()}}"
        request(params, "payments", Request.Method.POST, completion, errorCompletion)
    }

    private fun createNewSale(sale: Sale, completion: (String) -> Unit, errorCompletion: (VolleyError) -> Unit) {
        val params = "{\"sale\":${sale.toJsonString()}}"
        request(params, "sales", Request.Method.POST, completion, errorCompletion)
    }

    private fun createNewExpenditure(expenditure: Expenditure, completion: (String) -> Unit, errorCompletion: (VolleyError) -> Unit) {
        val params = "{\"expenditure\":${expenditure.toJsonString()}}"
        request(params, "expenditures", Request.Method.POST, completion, errorCompletion)
    }

    private fun updatePayment(payment: Payment, completion: (String) -> Unit, errorCompletion: (VolleyError) -> Unit) {
        val params = "{\"payment\":${payment.toJsonString()}}"
        request(params, "payments/${payment.id}", Request.Method.PATCH, completion, errorCompletion)
    }

    private fun updateSale(sale: Sale, completion: (String) -> Unit, errorCompletion: (VolleyError) -> Unit) {
        val params = "{\"sale\":${sale.toJsonString()}}"
        request(params, "sales/${sale.id}", Request.Method.PATCH, completion, errorCompletion)
    }

    private fun updateExpenditure(expenditure: Expenditure, completion: (String) -> Unit, errorCompletion: (VolleyError) -> Unit) {
        val params = "{\"expenditure\":${expenditure.toJsonString()}}"
        request(params, "expenditures/${expenditure.id}", Request.Method.PATCH, completion, errorCompletion)
    }

    private fun request(
        params: String,
        target: String,
        method: Int,
        completion: (String) -> Unit,
        errorCompletion: (VolleyError) -> Unit) {
        val url = baseUrl.format(target)

        val request = StringRequest(method, url, params, completion, errorCompletion)
        requestQueue.add(request)
    }
}