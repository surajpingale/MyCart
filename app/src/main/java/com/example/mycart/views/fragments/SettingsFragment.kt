package com.example.mycart.views.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.dishapp.interfaces.CustomToolbar
import com.example.mycart.R
import com.example.mycart.application.MyCartApplication
import com.example.mycart.databinding.FragmentSettingsBinding
import com.example.mycart.utils.Constants
import com.example.mycart.viewmodel.UserDetailViewModel
import com.example.mycart.viewmodel.UserViewModelFactory
import com.example.mycart.views.activities.LoginActivity


class SettingsFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentSettingsBinding? = null

    private val binding get() = _binding!!

    private val viewModel: UserDetailViewModel by viewModels {
        UserViewModelFactory(
            (requireActivity().application as
                    MyCartApplication).fireStoreRepository
        )
    }

    private lateinit var appClass: MyCartApplication

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        setUpToolbar()
        initViews()

        return binding.root
    }

    private fun initViews() {
        appClass = (requireActivity().application as MyCartApplication)

        loadData()
        binding.tvEdit.setOnClickListener(this)
        binding.btnLogout.setOnClickListener(this)
        binding.tvBtnAddress.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            binding.tvEdit.id -> {
                findNavController().navigate(
                    SettingsFragmentDirections.fragSettingToProfile()
                )
            }

            binding.btnLogout.id -> {
                appClass.firebaseAuth.signOut()
                val intent = Intent(requireActivity(), LoginActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }

            binding.tvBtnAddress.id -> {
                findNavController().navigate(
                    SettingsFragmentDirections.fragSettingToAddress()
                )
            }
        }
    }

    private fun loadData() {
        if (!viewModel.alreadyLoaded) {
            viewModel.getUserDetails()
        }

        viewModel.userLiveData.observe(requireActivity()) { user ->

            Glide.with(this)
                .load(user.image)
                .placeholder(R.drawable.ic_user_placeholder)
                .into(binding.ivProfilePic)

            binding.tvUserName.text = "${user.firstName} ${user.lastName}"
            binding.tvUserEmail.text = user.email
            binding.tvUserMobileNo.text = user.mobileNumber.toString()

        }
    }


    private fun setUpToolbar() {
        activity?.let {
            (activity as CustomToolbar).onToolbarLoad(Constants.FRAGMENT_SETTINGS, "Settings")
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}