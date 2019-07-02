package com.lezh.mybakery.expenditures

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.volley.Response
import com.lezh.mybakery.Expenditure
import com.lezh.mybakery.R
import com.lezh.mybakery.RequestManager
import com.lezh.mybakery.ResponseExpenditures
import com.lezh.mybakery.expenditures.ExpenditureFragment.OnListFragmentInteractionListener
import kotlinx.android.synthetic.main.fragment_expenditure.view.*

class ExpenditureRecyclerViewAdapter(
    context: Context,
    private val mListener: OnListFragmentInteractionListener?
): RecyclerView.Adapter<ExpenditureRecyclerViewAdapter.ViewHolder>() {
    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val date: TextView = mView.expenditure_date
        val total: TextView = mView.expenditure_total
    }
    private var mValues: Array<Expenditure> = emptyArray()
    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Expenditure
            mListener?.onListFragmentInteraction(item)
        }
        RequestManager.getInstance(context).requestExpenditures(
            { response ->
                mValues = ResponseExpenditures().toObject(response).results
                notifyDataSetChanged()
            },
            {
                //TODO something with this error
            }
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_expenditure, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.date.text = item.date
        holder.total.text = "${item.total}"

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size
}
