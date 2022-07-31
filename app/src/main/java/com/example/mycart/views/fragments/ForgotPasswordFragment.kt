package com.example.mycart.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.dishapp.interfaces.CustomToolbar
import com.example.mycart.R
import com.example.mycart.application.MyCartApplication
import com.example.mycart.databinding.FragmentForgotPasswordBinding
import com.example.mycart.utils.Constants
import com.example.mycart.utils.CustomDialog
import com.google.firebase.auth.FirebaseAuth


class ForgotPasswordFragment : Fragment() {

    private var _binding: FragmentForgotPasswordBinding? = null

    private val binding: FragmentForgotPasswordBinding
        get() = _binding!!

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)

        setUpToolbar()
        initViews()

        return binding.root
    }

    private fun setUpToolbar() {
        activity?.let {
            (activity as CustomToolbar).onToolbarLoad(
                Constants.FRAGMENT_FORGOT_PASSWORD, "")
        }
    }

    private fun initViews() {
        firebaseAuth = (requireActivity().application as MyCartApplication).firebaseAuth

        val include = binding.include

        include.btnSendEmail.setOnClickListener {
            CustomDialog.showDialog(requireActivity())
            if (validateEmailId()) {
                forgotPassword()
            }
        }
    }

    private fun forgotPassword() {

        val include = binding.include
        val emailId = include.etTvEmail.editText.text.toString().trim { it <= ' ' }

        firebaseAuth.sendPasswordResetEmail(emailId).addOnCompleteListener { task ->
            CustomDialog.hideDialog()
            if (task.isSuccessful) {
                showToast("Email sent on your email id")
            } else {
                showToast("Something wrong.")
            }
            activity?.onBackPressed()
        }
    }

    private fun validateEmailId(): Boolean {
        val include = binding.include

        val emailId = include.etTvEmail.editText.text.toString().trim { it <= ' ' }

        return if (emailId.isEmpty()) {
            include.etTvEmail.errorText.visibility = View.VISIBLE
            include.etTvEmail.errorText.text = resources.getString(R.string.error_msg_email_id)
            false
        } else {
            include.etTvEmail.errorText.visibility = View.INVISIBLE
            true
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}