package com.lezh.mybakery.payments

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.lezh.mybakery.*
import com.lezh.mybakery.payments.PaymentsFragment.OnListFragmentInteractionListener
import kotlinx.android.synthetic.main.fragment_payment_item.view.*

class PaymentsRecyclerViewAdapter(
    context: Context,
    private val mListener: OnListFragmentInteractionListener?
): RecyclerView.Adapter<PaymentsRecyclerViewAdapter.ViewHolder>() {
    inner class ViewHolder(val mView: View): RecyclerView.ViewHolder(mView) {
        val vendor: TextView = mView.vendor
        val date: TextView = mView.date
        val value: TextView = mView.value
    }
    private var mValues: Array<Payment> = emptyArray()
    private var vendors: Array<Vendor> = emptyArray()
    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Payment
            mListener?.onListFragmentInteraction(item)
        }
        RequestManager.getInstance(context).requestPayments(
            { response ->
                mValues += ResponsePayments().toObject(response).results
                notifyDataSetChanged()
            },
            { error ->
                //TODO something with this error
            }
        )
        //TODO optimize this
        RequestManager.getInstance(context).requestVendors(
            { response ->
                this.vendors = ResponseVendors().toObject(response).vendors
                notifyDataSetChanged()
            },
            { error ->
                //TODO something with this error
            }
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_payment_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.vendor.text = {
            var ret = ""
            if (vendors.isNotEmpty()) {
                val vend = vendors.find { it.id == item.vendorID }
                ret = vend?.name!!
            }
            ret
        }()
        holder.date.text = item.date
        holder.value.text = ""+item.value

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size
}
