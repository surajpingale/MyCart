package com.example.mycart.views.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.dishapp.interfaces.CustomToolbar
import com.example.mycart.R
import com.example.mycart.databinding.ActivityMainBinding
import com.example.mycart.utils.Constants

class MainActivity : AppCompatActivity(), CustomToolbar {

    private var _binding: ActivityMainBinding? = null

    private val binding: ActivityMainBinding
        get() = _binding!!

    private lateinit var from: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpBottomNavView()
    }

    override fun onToolbarLoad(from: String, title: String) {
        val toolbar = binding.toolbar
        this.from = from

        if (from == Constants.FRAGMENT_DASHBOARD) {
            binding.btnBackPressed.visibility = View.GONE
            binding.toolbarTitle.text = title
        }
        if (from == Constants.FRAGMENT_PRODUCTS) {
            binding.btnBackPressed.visibility = View.VISIBLE
            binding.toolbarTitle.text = title
        }
        if (from == Constants.FRAGMENT_ORDERS) {
            binding.btnBackPressed.visibility = View.VISIBLE
            binding.toolbarTitle.text = title
        }
        if (from == Constants.FRAGMENT_SETTINGS) {
            binding.btnBackPressed.visibility = View.VISIBLE
            binding.toolbarTitle.text = title
        }
        if (from == Constants.FRAGMENT_PRODUCT_DETAILS) {
            binding.btnBackPressed.visibility = View.VISIBLE
            binding.toolbarTitle.text = title
        }
        if (from == Constants.FRAGMENT_CART) {
            binding.btnBackPressed.visibility = View.VISIBLE
            binding.toolbarTitle.text = title
        }
        if (from == Constants.FRAGMENT_ADDRESS) {
            binding.btnBackPressed.visibility = View.VISIBLE
            binding.toolbarTitle.text = title
        }
        if (from == Constants.FRAGMENT_ADD_ADDRESS) {
            binding.btnBackPressed.visibility = View.VISIBLE
            binding.toolbarTitle.text = title
        }
        if (from == Constants.FRAGMENT_CHECKOUT) {
            binding.btnBackPressed.visibility = View.VISIBLE
            binding.toolbarTitle.text = title
        }
        if (from == Constants.FRAGMENT_ORDER_DETAILS) {
            binding.btnBackPressed.visibility = View.VISIBLE
            binding.toolbarTitle.text = title
        }
        if (from == Constants.FRAGMENT_ADD_PRODUCT) {
            binding.btnBackPressed.visibility = View.VISIBLE
            binding.toolbarTitle.text = title
        }
        if (from == Constants.FRAGMENT_PROFILE) {
            binding.btnBackPressed.visibility = View.VISIBLE
            binding.toolbarTitle.text = title
        }
        if (from == Constants.FRAGMENT_SOLD_PRODUCTS) {
            binding.btnBackPressed.visibility = View.VISIBLE
            binding.toolbarTitle.text = title
        }
        if (from == Constants.FRAGMENT_SOLD_PRODUCTS_DETAILS) {
            binding.btnBackPressed.visibility = View.VISIBLE
            binding.toolbarTitle.text = title
        }




        binding.btnBackPressed.setOnClickListener {
            onBackPressed()
        }

        setSupportActionBar(toolbar)
        toolbar.showOverflowMenu()
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun setUpBottomNavView() {
        val navHost = supportFragmentManager.findFragmentById(binding.fragmentContainerView.id)
                as NavHostFragment
        binding.bottomNavView.setupWithNavController(navHost.navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (from == Constants.FRAGMENT_DASHBOARD) {
            menuInflater.inflate(R.menu.main_menu, menu)
        }
        if (from == Constants.FRAGMENT_PRODUCTS) {
            menuInflater.inflate(R.menu.add_product_menu, menu)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navHost = supportFragmentManager.findFragmentById(
            binding.fragmentContainerView.id
        ) as NavHostFragment

        when (item.itemId) {
            R.id.menu_settings -> {
                navHost.navController.navigate(R.id.fragment_setting)
                hideBottomNavigationBar()
            }
            R.id.menu_add_product -> {
                navHost.navController.navigate(R.id.fragment_add_product)
                hideBottomNavigationBar()
            }
            R.id.menu_cart -> {
                navHost.navController.navigate(R.id.fragment_cart)
                hideBottomNavigationBar()
            }
        }
        return true
    }

    fun showBottomNavigationBar() {
        binding.bottomNavView.clearAnimation()
        binding.bottomNavView.animate().translationY(0f).duration = 300
        binding.bottomNavView.visibility = View.VISIBLE
    }

    fun hideBottomNavigationBar() {
        binding.bottomNavView.clearAnimation()
        binding.bottomNavView.animate().translationY(binding.bottomNavView.height.toFloat())
            .duration = 300

        binding.bottomNavView.visibility = View.GONE

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}