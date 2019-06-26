package com.lezh.mybakery

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import com.lezh.mybakery.payments.PaymentsFragment
import com.lezh.mybakery.sales.SaleFragment

class MainActivity:
    AppCompatActivity(),
    PaymentsFragment.OnListFragmentInteractionListener,
    SaleFragment.OnListFragmentInteractionListener{
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
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fragments_container, SaleFragment.newInstance(1))
                fragmentTransaction.commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                /*val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.remove(fragmentManager.fragments.first())
                fragmentTransaction.commit()*/
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragments_container, PaymentsFragment.newInstance(1))
        fragmentTransaction.commit()

        val fab = findViewById<View>(R.id.floatingActionButton)
        fab.setOnClickListener { view ->
            val fragment = fragmentManager.fragments.first() as DetailHandler
            if (fragment is PaymentsFragment) {
                val payment = Payment()
                fragment.openDetail(payment)
            }
            if (fragment is SaleFragment) {
                val sale = Sale()
                fragment.openDetail(sale)
            }
        }
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }

    override fun onListFragmentInteraction(item: Payment?) {
        (fragmentManager.fragments.first() as DetailHandler).openDetail(item)
    }

    override fun onListFragmentInteraction(item: Sale?) {
        (fragmentManager.fragments.first() as DetailHandler).openDetail(item)
    }
}
