package com.lezh.mybakery.sales

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.lezh.mybakery.*
import com.lezh.mybakery.sales.SaleFragment.OnListFragmentInteractionListener
import kotlinx.android.synthetic.main.fragment_sale.view.*

/**
 * [RecyclerView.Adapter] that can display a [Sale] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 */
class SaleRecyclerViewAdapter(
    context: Context,
    private val mListener: OnListFragmentInteractionListener?
): RecyclerView.Adapter<SaleRecyclerViewAdapter.ViewHolder>() {
    inner class ViewHolder(val mView: View): RecyclerView.ViewHolder(mView) {
        val date: TextView = mView.sale_date
        val product: TextView = mView.sale_product
        val vendor: TextView = mView.sale_vendor
        val amount: TextView = mView.sale_ammount
        val total: TextView = mView.sale_total
    }
    private var mValues: Array<Sale> = emptyArray()
    private var vendors: Array<Vendor> = emptyArray()
    private var products: Array<Product> = emptyArray()
    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Sale
            mListener?.onListFragmentInteraction(item)
        }
        RequestManager.getInstance(context).requestSales(
            { response ->
                val sales = ResponseSales().toObject(response)
                mValues += sales.results
                notifyDataSetChanged()
            },
            {
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
        RequestManager.getInstance(context).requestProducts(
            { response ->
                val products = ResponseProducts().toObject(response)
                this.products = products.results
                notifyDataSetChanged()
            },
            {
                //TODO something with this error
            }
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_sale, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sale = mValues[position]
        holder.date.text = sale.date
        holder.amount.text = "${sale.ammount}"
        holder.total.text = "${sale.total}"
        holder.vendor.text = {
            var ret = ""
            if (vendors.isNotEmpty()) {
                ret = (vendors.find { it.id == sale.vendorId })!!.name
            }
            ret
        }()
        holder.product.text = {
            var ret = ""
            if (products.isNotEmpty()) {
                ret = (products.find { it.id == sale.productId })!!.name
            }
            ret
        }()

        with(holder.mView) {
            tag = sale
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size
}
