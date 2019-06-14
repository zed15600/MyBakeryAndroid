package com.lezh.mybakery

import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.lezh.mybakery.payments.PaymentsFragment

class MainActivity : AppCompatActivity(), PaymentsFragment.OnListFragmentInteractionListener {
    //private lateinit var fragments: Array<Fragment>
    private val fragmentManager = supportFragmentManager
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                //val fragmentTransaction = fragmentManager.beginTransaction()
                //fragmentTransaction.replace(R.id.fragments_container, fragments[0])
                //fragmentTransaction.commit()
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
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        //fragments = arrayOf(Payments.newInstance())
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }

    override fun onListFragmentInteraction(item: Payment?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
