package com.lezh.mybakery.expenditures

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.lezh.mybakery.*
import kotlinx.android.synthetic.main.expenditure_feedstock_item.view.*
import java.time.Instant
import java.util.*

class ExpenditureDetail : AppCompatActivity() {
    inner class ViewHolder(val mView: View) {
        val feedstock: Spinner = mView.expenditure_feedstock_picker
        val amount: EditText = mView.expenditure_feedstock_amount
        val total: TextView = mView.expenditure_feedstock_total
        lateinit var feed: ExpenditureFeedstock

        fun updateTotal() {
            val totalValue =  feed.amount * (feedstock.selectedItem as Feedstock).price
            feed.total = totalValue
            total.text = "$totalValue"
            updateTotalValue()
        }
    }
    private lateinit var date: EditText
    private lateinit var totalValue: TextView
    private lateinit var feedstocksHolder: LinearLayout
    private lateinit var expenditure: Expenditure
    private lateinit var feedstocks: Array<Feedstock>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expenditure_detail)

        expenditure = intent.getSerializableExtra("expenditure") as Expenditure

        date = findViewById(R.id.expenditure_date_input)
        date.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                val str = p0.toString()
                expenditure.date = str
                //TODO this is useless here, just trying a way to do it for future functionality somewhere else
                if(str.length == 10 && str.matches(Regex("\\d{4}-\\d{2}-\\d{2}"))) {
                    expenditure.dDate = Date.from(Instant.parse(str + "T05:00:00.00Z"))
                } else {
                    expenditure.dDate = null
                }
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
        date.setText(expenditure.date)

        totalValue = findViewById(R.id.expenditure_total_label)
        totalValue.text = "${expenditure.total}"

        feedstocksHolder = findViewById(R.id.expenditure_feedstocks)

        RequestManager.getInstance(this).requestFeedstocks(
            { response ->
                feedstocks = ResponseFeedstocks().toObject(response).results
                //Request expenditure details
                if (expenditure.id > 0) {
                    RequestManager.getInstance(this).requestExpenditureDetails(
                        expenditure,
                        { response2 ->
                            expenditure = expenditure.toObject(response2)
                            expenditure.feedstocks.forEach {
                                addFeedstockView(it)
                            }
                        },
                        {
                            //TODO something with this error
                        }
                    )
                }
            },
            {
                //TODO something with this error
            }
        )

        findViewById<View>(R.id.expenditure_add_feedstock).apply {
            setOnClickListener {
                val feed = ExpenditureFeedstock()
                expenditure.feedstocks += feed
                addFeedstockView(feed)
            }
        }

        findViewById<View>(R.id.expenditure_remove_feedstock).apply {
            setOnClickListener {
                if(expenditure.feedstocks.isNotEmpty()) {
                    expenditure.feedstocks = expenditure.feedstocks.sliceArray(0..expenditure.feedstocks.size - 2)
                    feedstocksHolder.removeViewAt(feedstocksHolder.childCount - 1)
                    updateTotalValue()
                }
            }
        }

        findViewById<View>(R.id.expenditure_commit).apply {
            setOnClickListener {
                commitItem(context, expenditure)
            }
        }
    }

    fun createViewHolder(): ViewHolder {
        val view = LayoutInflater.from(feedstocksHolder.context).inflate(R.layout.expenditure_feedstock_item, feedstocksHolder, false)
        return ViewHolder(view)
    }

    fun bindViewHolder(holder: ViewHolder, feed: ExpenditureFeedstock) {
        holder.feed = feed

        holder.feedstock.adapter = {
            ArrayAdapter(this, android.R.layout.simple_spinner_item, feedstocks).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
        }()
        holder.feedstock.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                feed.feedstockID = (p0?.selectedItem as Feedstock).id
                holder.updateTotal()
            }
        }
        holder.feedstock.setSelection(
            {
               feedstocks.indexOf(
                   feedstocks.find {
                       it.id == feed.feedstockID
                   }
               )
            }()
        )

        holder.amount.setText("${feed.amount}")
        holder.amount.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                feed.amount = if (p0!!.isNotEmpty()) p0.toString().toInt() else 0
                holder.updateTotal()
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
        holder.total.text = "${feed.total}"

        with(holder.mView) {
            tag = feed
        }
    }

    fun updateTotalValue() {
        var totalValue = 0
        expenditure.feedstocks.forEach {
            totalValue += it.total
        }
        this.totalValue.text = totalValue.toString()
        expenditure.total = totalValue
    }

    fun addFeedstockView(feed: ExpenditureFeedstock) {
        val view = createViewHolder()
        bindViewHolder(view, feed)
        feedstocksHolder.addView(view.mView)
    }
}
