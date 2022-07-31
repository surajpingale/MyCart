package com.example.mycart.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.dishapp.interfaces.CustomToolbar
import com.example.mycart.R
import com.example.mycart.application.MyCartApplication
import com.example.mycart.databinding.FragmentAddAddressBinding
import com.example.mycart.model.Address
import com.example.mycart.utils.Constants
import com.example.mycart.utils.CustomDialog
import com.example.mycart.utils.Response
import com.example.mycart.viewmodel.UserDetailViewModel
import com.example.mycart.viewmodel.UserViewModelFactory


class AddAddressFragment : Fragment() {

    private var _binding: FragmentAddAddressBinding? = null

    private val binding get() = _binding!!

    private val viewModel: UserDetailViewModel by viewModels {
        UserViewModelFactory(
            (requireActivity().application as MyCartApplication).fireStoreRepository
        )
    }

    private val args: AddAddressFragmentArgs by navArgs()
    private lateinit var addressDetails: Address

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddAddressBinding.inflate(inflater, container, false)

        setUpToolbar()
        initViewsAndListeners()

        return binding.root
    }

    private fun setUpToolbar() {
        activity?.let {
            if (args.address != null) {
                (activity as CustomToolbar).onToolbarLoad(
                    Constants.FRAGMENT_ADD_ADDRESS,
                    "Edit Address"
                )
            } else {
                (activity as CustomToolbar).onToolbarLoad(
                    Constants.FRAGMENT_ADD_ADDRESS,
                    "Add Address"
                )
            }
        }
    }

    private fun initViewsAndListeners() {

        if (args.address != null) {
            addressDetails = args.address!!
            binding.etTvFullName.editText.text = addressDetails.fullName
            binding.etTvAddress.editText.text = addressDetails.address
            binding.etTvMobileNo.editText.text = addressDetails.mobileNumber
            binding.etTvZipCode.editText.text = addressDetails.zipCode

            binding.btnAddAddress.text = resources.getString(R.string.update)
        }

        binding.btnAddAddress.setOnClickListener {
            if (validateEntries()) {
                if (args.address != null) {
                    CustomDialog.showDialog(requireActivity())
                    updateAddress()
                } else {
                    CustomDialog.showDialog(requireActivity())
                    addAddress()
                }
            }
        }
    }

    private fun updateAddress() {
        val fullName = binding.etTvFullName.editText.text.toString().trim { it <= ' ' }
        val mobileNumber = binding.etTvMobileNo.editText.text.toString().trim { it <= ' ' }
        val address = binding.etTvAddress.editText.text.toString().trim { it <= ' ' }
        val zipCode = binding.etTvZipCode.editText.text.toString().trim { it <= ' ' }

        val hashMap = HashMap<String, Any>()

        hashMap[Constants.FULL_NAME] = fullName
        hashMap[Constants.ADDRESS] = address
        hashMap[Constants.MOBILE_NUMBER] = mobileNumber
        hashMap[Constants.ZIP_CODE] = zipCode


        viewModel.updateAddress(addressDetails.addressId, hashMap)
        viewModel.updateAddressLive.observe(viewLifecycleOwner) { state ->

            when (state) {
                is Response.Success -> {
                    CustomDialog.hideDialog()
                    CustomDialog.showToast(requireActivity(), "Updated successfully")
                    activity?.onBackPressed()
                }
                is Response.Error -> {
                    CustomDialog.hideDialog()
                    CustomDialog.showToast(requireActivity(), "Something wrong")
                }
            }
        }
    }

    private fun addAddress() {
        val fullName = binding.etTvFullName.editText.text.toString().trim { it <= ' ' }
        val mobileNumber = binding.etTvMobileNo.editText.text.toString().trim { it <= ' ' }
        val address = binding.etTvAddress.editText.text.toString().trim { it <= ' ' }
        val zipCode = binding.etTvZipCode.editText.text.toString().trim { it <= ' ' }

        val addAddress = Address()
        addAddress.userId = viewModel.getUserId()
        addAddress.fullName = fullName
        addAddress.mobileNumber = mobileNumber
        addAddress.address = address
        addAddress.zipCode = zipCode

        viewModel.addAddress(addAddress)
        viewModel.addAddressLiveData.observe(viewLifecycleOwner) { state ->

            when (state) {
                is Response.Success -> {
                    CustomDialog.hideDialog()
                    CustomDialog.showToast(requireActivity(), "Address added successfully")
                    activity?.onBackPressed()
                }

                is Response.Error -> {
                    CustomDialog.hideDialog()
                }
                else -> CustomDialog.hideDialog()
            }
        }

    }


    private fun validateEntries(): Boolean {
        val fullName = binding.etTvFullName.editText.text.toString().trim { it <= ' ' }
        val mobileNumber = binding.etTvMobileNo.editText.text.toString().trim { it <= ' ' }
        val address = binding.etTvAddress.editText.text.toString().trim { it <= ' ' }
        val zipCode = binding.etTvZipCode.editText.text.toString().trim { it <= ' ' }


        if (fullName.isEmpty()) {
            binding.etTvFullName.errorText.visibility = View.VISIBLE
            binding.etTvFullName.errorText.text = resources.getString(R.string.error_msg_full_name)
            return false
        } else {
            binding.etTvFullName.errorText.visibility = View.INVISIBLE
        }

        if (mobileNumber.isEmpty()) {
            binding.etTvMobileNo.errorText.visibility = View.VISIBLE
            binding.etTvMobileNo.errorText.text =
                resources.getString(R.string.error_msg_mobile_number)
            return false
        } else {
            binding.etTvMobileNo.errorText.visibility = View.INVISIBLE
        }

        if (address.isEmpty()) {
            binding.etTvAddress.errorText.visibility = View.VISIBLE
            binding.etTvAddress.errorText.text = resources.getString(R.string.error_msg_address)
            return false
        } else {
            binding.etTvAddress.errorText.visibility = View.INVISIBLE
        }

        if (zipCode.isEmpty()) {
            binding.etTvZipCode.errorText.visibility = View.VISIBLE
            binding.etTvZipCode.errorText.text = resources.getString(R.string.error_msg_zip_code)
            return false
        } else {
            binding.etTvZipCode.errorText.visibility = View.INVISIBLE
        }

        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}