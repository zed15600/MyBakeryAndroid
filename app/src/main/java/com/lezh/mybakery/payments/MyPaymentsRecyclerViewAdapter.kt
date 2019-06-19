package com.lezh.mybakery.payments

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.lezh.mybakery.Payment
import com.lezh.mybakery.R
import com.lezh.mybakery.RequestManager
import com.lezh.mybakery.Vendor

import com.lezh.mybakery.payments.PaymentsFragment.OnListFragmentInteractionListener

import kotlinx.android.synthetic.main.fragment_payment_item.view.*

/**
 * [RecyclerView.Adapter] that can display a [Payment] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class MyPaymentsRecyclerViewAdapter(
    context: Context,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyPaymentsRecyclerViewAdapter.ViewHolder>() {
    private var mValues: Array<Payment> = emptyArray()
    private var vendors: Array<Vendor> = emptyArray()

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val vendor: TextView = mView.vendor
        val date: TextView = mView.date
        val value: TextView = mView.value

        /*override fun toString(): String {
            return super.toString() + " '" + vendor.text + "'"
        }*/
    }

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Payment
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
        RequestManager.getInstance(context).request(
            "payments",
            { payments ->
                mValues += payments
                notifyDataSetChanged()
            },
            { error ->
                Log.d("request", error.message)
            }
        )
        //TODO optimize this
        RequestManager.getInstance(context).requestVendors(
            { vendors ->
                this.vendors = vendors
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
                ret = vendors[item.vendorID - 1].name
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
