package com.lezh.mybakery.expenditures

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lezh.mybakery.DetailHandler
import com.lezh.mybakery.Expenditure
import com.lezh.mybakery.R

class ExpenditureFragment : Fragment(), DetailHandler {
    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: Expenditure?)
    }
    companion object {
        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int) = ExpenditureFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_COLUMN_COUNT, columnCount)
            }
        }
    }
    private var columnCount = 1
    private var listener: OnListFragmentInteractionListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_expenditure_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = activity?.baseContext?.let { ExpenditureRecyclerViewAdapter(it, listener) }
            }
        }
        return view
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun <T> openDetail(item: T?) {
        val expenditure = item as Expenditure
        val intent = Intent(this.context, ExpenditureDetail::class.java).apply {
            putExtra("expenditure", expenditure)
        }
        startActivity(intent)
    }
}
