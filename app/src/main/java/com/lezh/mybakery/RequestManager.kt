package com.lezh.mybakery

import android.content.Context
import android.net.nsd.NsdManager
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.serialization.json.JSON
import kotlinx.serialization.parse
import java.util.*

class RequestManager {
    private val baseUrl = "http://54.233.228.170:3000/%s?format=json"

    fun request(target: String, completion: (Array<Payment>)->Unit, errorCompletion: (VolleyError)->Unit, context: Context) {
        val queue = Volley.newRequestQueue(context)
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
        queue.add(stringRequest)
    }

    fun requestVendors(completion: (Array<Vendor>) -> Unit, errorCompletion: (VolleyError) -> Unit, context: Context) {
        val queue = Volley.newRequestQueue(context)
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
        queue.add(stringRequest)
    }
}