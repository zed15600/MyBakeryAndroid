package com.lezh.mybakery

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.*
import kotlinx.serialization.toUtf8Bytes
import java.nio.charset.Charset
import kotlin.collections.HashMap

class RequestManager constructor(context: Context) {
    internal class StringRequest(
        method: Int,
        url: String,
        private val heads: MutableMap<String, String>,
        private val pars: String,
        completion: (String) -> Unit,
        errorCompletion: (VolleyError) -> Unit
    ): com.android.volley.toolbox.StringRequest(method, url, completion, errorCompletion) {

        override fun getHeaders(): MutableMap<String, String> {
            return heads
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

    fun request(target: String, completion: (Array<Payment>)->Unit, errorCompletion: (VolleyError)->Unit) {
        val url = baseUrl.format(target)

        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            Response.Listener { response ->
                var resp = Response().toObject(response)
                completion(resp.results)
            },
            Response.ErrorListener { error ->
                errorCompletion(error)
            }
        )
        requestQueue.add(stringRequest)
    }

    fun requestVendors(completion: (Array<Vendor>) -> Unit, errorCompletion: (VolleyError) -> Unit) {
        val url = baseUrl.format("vendors")

        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            Response.Listener { response ->
                var resp = ResponseVendors().toObject(response)
                completion(resp.vendors)
            },
            Response.ErrorListener(errorCompletion)
        )
        requestQueue.add(stringRequest)
    }

    fun createNewPayment(payment: Payment, completion: (String) -> Unit, errorCompletion: (VolleyError) -> Unit) {
        val url = baseUrl.format("payments")

        val headers = HashMap<String, String>().apply {
            put("Content-Type", "application/json")
        }
        val params = "{\"payment\":$payment}"

        val request = StringRequest(Request.Method.POST, url, headers, params, completion, errorCompletion)
        requestQueue.add(request)
    }
}