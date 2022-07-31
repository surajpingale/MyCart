package com.example.mycart.views.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.dishapp.interfaces.CustomToolbar
import com.example.mycart.R
import com.example.mycart.databinding.ActivityLoginBinding
import com.example.mycart.utils.Constants

class LoginActivity : AppCompatActivity(), CustomToolbar {

    private var _binding: ActivityLoginBinding? = null

    private val binding: ActivityLoginBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onToolbarLoad(from: String, title: String) {
        val toolbar = binding.toolbar

        when (from) {
            Constants.FRAGMENT_LOGIN -> {
                binding.toolbar.visibility = View.GONE
            }
            Constants.FRAGMENT_REGISTER -> {
                binding.toolbar.background = ContextCompat.getDrawable(this, R.color.white)
                binding.btnBackPressed.setImageDrawable(
                    ContextCompat.getDrawable(
                        this, R.drawable.ic_back
                    )
                )
                binding.toolbar.visibility = View.VISIBLE
                binding.toolbarTitle.text = title


            }
            Constants.FRAGMENT_FORGOT_PASSWORD -> {
                binding.toolbar.visibility = View.VISIBLE
                binding.toolbar.background =
                    ContextCompat.getDrawable(this, R.drawable.bg_gradient)
                binding.btnBackPressed.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_back_white
                    )
                )
                binding.toolbarTitle.text = title
            }
        }

        binding.btnBackPressed.setOnClickListener {
            onBackPressed()
        }

        setSupportActionBar(toolbar)
        toolbar.showOverflowMenu()
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}