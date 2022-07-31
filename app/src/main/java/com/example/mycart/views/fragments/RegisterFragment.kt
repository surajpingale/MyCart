package com.example.mycart.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.dishapp.interfaces.CustomToolbar
import com.example.mycart.R
import com.example.mycart.application.MyCartApplication
import com.example.mycart.databinding.FragmentRegisterBinding
import com.example.mycart.interfaces.FirebaseRegisterListener
import com.example.mycart.model.User
import com.example.mycart.utils.Constants
import com.example.mycart.utils.CustomDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class RegisterFragment : Fragment(), View.OnClickListener, FirebaseRegisterListener {

    private var _binding: FragmentRegisterBinding? = null

    private val binding: FragmentRegisterBinding
        get() = _binding!!

    // Firebase instance
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var appClass: MyCartApplication
    private lateinit var firebaseFireStore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        setUpToolbar()
        initViewsAndListeners()

        return binding.root
    }

    private fun initViewsAndListeners() {
        appClass = (requireActivity().application as MyCartApplication)
        firebaseAuth = appClass.firebaseAuth
        firebaseFireStore = appClass.firebaseFirestore

        binding.include.btnRegister.setOnClickListener(this)
        binding.include.tvLogin.setOnClickListener(this)
    }

    private fun setUpToolbar() {
        activity?.let {
            (activity as CustomToolbar).onToolbarLoad(
                Constants.FRAGMENT_REGISTER,
                "Register"
            )
        }
    }

    override fun onClick(view: View?) {
        val btnRegisterId = binding.include.btnRegister.id
        val tvLoginId = binding.include.tvLogin.id

        when (view!!.id) {

            btnRegisterId -> {
                if (validateRegistration()) {
                    CustomDialog.showDialog(requireActivity())
                    registerUser()
                }
            }

            tvLoginId -> {
                activity?.onBackPressed()
            }

        }
    }

    /**
     * Will register user from
     */
    private fun registerUser() {
        val include = binding.include

        val emailId = include.etTvEmailId.editText.text.toString().trim { it <= ' ' }
        val password = include.etTvPassword.editText.text.toString().trim { it <= ' ' }
        val firstName = include.etTvFirstName.editText.text.toString().trim { it <= ' ' }
        val lastName = include.etTvLastName.editText.text.toString().trim { it <= ' ' }

        firebaseAuth.createUserWithEmailAndPassword(emailId, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val firebaseUser = task.result.user!!

                    val user = User()
                    user.id = firebaseUser.uid
                    user.email = emailId
                    user.firstName = firstName
                    user.lastName = lastName

                    appClass.fireStore.registerUser(
                        this, firebaseFireStore,
                        user
                    )

                } else {
                    showToast("Error in register..")
                }
            }
    }

    override fun onSuccessListener() {
        CustomDialog.hideDialog()
        firebaseAuth.signOut()
        activity?.onBackPressed()
        showToast("Registered successfully")
    }

    override fun onFailureListener() {
        CustomDialog.hideDialog()
        showToast("Error in register..")
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show()
    }

    /**
     * This fun will validate entries of user
     */
    private fun validateRegistration(): Boolean {
        val include = binding.include

        val firstName = include.etTvFirstName.editText.text.toString().trim { it <= ' ' }
        val lastName = include.etTvLastName.editText.text.toString().trim { it <= ' ' }
        val emailId = include.etTvEmailId.editText.text.toString().trim { it <= ' ' }
        val password = include.etTvPassword.editText.text.toString().trim { it <= ' ' }
        val confirmPassword =
            include.etTvConfirmPassword.editText.text.toString().trim { it <= ' ' }

        if (firstName.isEmpty()) {
            include.etTvFirstName.errorText.visibility = View.VISIBLE
            include.etTvFirstName.errorText.text = resources.getString(
                R.string.error_msg_first_name
            )
            return false
        } else {
            include.etTvFirstName.errorText.visibility = View.INVISIBLE
        }

        if (lastName.isEmpty()) {
            include.etTvLastName.errorText.visibility = View.VISIBLE
            include.etTvLastName.errorText.text = resources.getString(
                R.string.error_msg_last_name
            )
            return false
        } else {
            include.etTvLastName.errorText.visibility = View.INVISIBLE
        }

        if (emailId.isEmpty()) {
            include.etTvEmailId.errorText.visibility = View.VISIBLE
            include.etTvEmailId.errorText.text = resources.getString(
                R.string.error_msg_email_id
            )
            return false
        } else {
            include.etTvEmailId.errorText.visibility = View.INVISIBLE
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

        if (confirmPassword.isEmpty()) {
            include.etTvConfirmPassword.errorText.visibility = View.VISIBLE
            include.etTvConfirmPassword.errorText.text = resources.getString(
                R.string.error_msg_confirm_password
            )
            return false
        } else {
            include.etTvConfirmPassword.errorText.visibility = View.INVISIBLE
        }

        if (password != confirmPassword) {
            showToast("Passwords not matching")
            return false
        }
        return true

    }

    override fun onDestroyView() {
        super.onDestroy()
        _binding = null
    }
}