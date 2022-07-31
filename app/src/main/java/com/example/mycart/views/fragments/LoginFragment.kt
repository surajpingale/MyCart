package com.example.mycart.views.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dishapp.interfaces.CustomToolbar
import com.example.mycart.R
import com.example.mycart.application.MyCartApplication
import com.example.mycart.databinding.FragmentLoginBinding
import com.example.mycart.interfaces.FirebaseLoginListener
import com.example.mycart.model.User
import com.example.mycart.utils.Constants
import com.example.mycart.utils.CustomDialog
import com.example.mycart.views.activities.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class LoginFragment : Fragment(), View.OnClickListener, FirebaseLoginListener {

    private var _binding: FragmentLoginBinding? = null

    private val binding: FragmentLoginBinding
        get() = _binding!!

    private lateinit var appClass: MyCartApplication
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseFireStore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        setUpToolbar()
        initViewsAndListeners()

        return binding.root
    }

    private fun initViewsAndListeners() {
        appClass = (requireActivity().application as MyCartApplication)
        firebaseAuth = appClass.firebaseAuth
        firebaseFireStore = appClass.firebaseFirestore

        val include = binding.include

        include.tvRegister.setOnClickListener(this)
        include.btnLogin.setOnClickListener(this)
        include.tvForgotPassword.setOnClickListener(this)

    }

    private fun setUpToolbar() {
        activity?.let {
            (activity as CustomToolbar).onToolbarLoad(
                Constants.FRAGMENT_LOGIN,
                ""
            )
        }
    }

    override fun onClick(view: View?) {

        val tvRegisterId = binding.include.tvRegister.id
        val btnLoginId = binding.include.btnLogin.id
        val tvForgotPasswordId = binding.include.tvForgotPassword.id

        when (view!!.id) {
            btnLoginId -> {
                CustomDialog.showDialog(requireActivity())
                if (validateEntries()) {
                    logInUser()
                }
            }

            tvRegisterId -> {
                findNavController().navigate(
                    LoginFragmentDirections.fragLoginToFragRegister()
                )
            }

            tvForgotPasswordId -> {
                findNavController().navigate(
                    LoginFragmentDirections.fragLoginToFragForgotPassword()
                )
            }
        }
    }

    private fun logInUser() {
        val include = binding.include

        val emailId = include.etTvEmail.editText.text.toString()
        val password = include.etTvPassword.editText.text.toString()

        firebaseAuth.signInWithEmailAndPassword(emailId, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                appClass.fireStore.getCurrentUserDetails(
                    this,
                    firebaseFireStore,
                    firebaseAuth
                )
            } else {
                CustomDialog.hideDialog()
                Toast.makeText(requireActivity(), "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateEntries(): Boolean {

        val include = binding.include

        val emailId = include.etTvEmail.editText.text.toString().trim { it <= ' ' }
        val password = include.etTvPassword.editText.text.toString().trim { it <= ' ' }

        if (emailId.isEmpty()) {
            include.etTvEmail.errorText.visibility = View.VISIBLE
            include.etTvEmail.errorText.text = resources.getString(
                R.string.error_msg_email_id
            )
            return false
        } else {
            include.etTvEmail.errorText.visibility = View.INVISIBLE
        }

        if (password.isEmpty()) {
            include.etTvPassword.errorText.visibility = View.VISIBLE
            include.etTvPassword.errorText.text = resources.getString(
                R.string.error_msg_password
            )
            return false
        } else {
            include.etTvPassword.errorText.visibility = View.INVISIBLE
        }
        return true
    }

    override fun onSuccessListener(user: User) {
        CustomDialog.hideDialog()
        val intent = Intent(requireActivity(), MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    override fun onFailureListener(exception: Exception) {
        CustomDialog.hideDialog()
        Toast.makeText(requireActivity(), "Something wrong", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}