package com.lezh.mybakery

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.lezh.mybakery.payments.PaymentsFragment

class MainActivity : AppCompatActivity(), PaymentsFragment.OnListFragmentInteractionListener {
    private val fragmentManager = supportFragmentManager
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fragments_container, PaymentsFragment.newInstance(1))
                fragmentTransaction.commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                /*val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.remove(fragments[0])
                fragmentTransaction.commit()*/
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                /*val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.remove(fragments[0])
                fragmentTransaction.commit()*/
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fab = findViewById<View>(R.id.floatingActionButton)
        fab.setOnClickListener { view ->
            val payment = Payment()
            val intent = Intent(this, PaymentDetail::class.java).apply {
                putExtra("payment", payment)
            }
            startActivity(intent)
        }
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        //fragments = arrayOf(Payments.newInstance())
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }

    override fun onListFragmentInteraction(item: Payment?) {
        val intent = Intent(this, PaymentDetail::class.java).apply {
            putExtra("payment", item)
        }
        startActivity(intent)
    }
}
