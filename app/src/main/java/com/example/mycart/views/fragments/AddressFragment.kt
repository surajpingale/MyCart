package com.example.mycart.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dishapp.interfaces.CustomToolbar
import com.example.mycart.application.MyCartApplication
import com.example.mycart.databinding.FragmentAddressBinding
import com.example.mycart.model.Address
import com.example.mycart.utils.Constants
import com.example.mycart.utils.CustomDialog
import com.example.mycart.utils.Response
import com.example.mycart.viewmodel.UserDetailViewModel
import com.example.mycart.viewmodel.UserViewModelFactory
import com.example.mycart.views.adapter.AddressAdapter


class AddressFragment : Fragment(), View.OnClickListener, AddressAdapter.OnAddressClick {

    private var _binding: FragmentAddressBinding? = null

    private val binding get() = _binding!!

    private val viewModel: UserDetailViewModel by viewModels {
        UserViewModelFactory(
            (requireActivity().application as MyCartApplication).fireStoreRepository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddressBinding.inflate(inflater, container, false)

        setUpToolbar()
        initViewAndListeners()


        return binding.root
    }

    private fun initViewAndListeners() {
        binding.btnAddAddress.setOnClickListener(this)
        gettingAddressList()
    }

    private fun gettingAddressList() {
        CustomDialog.showDialog(requireActivity())
        viewModel.getAddressList()
        viewModel.addressLiveData.observe(viewLifecycleOwner) { state ->

            when (state) {
                is Response.Success -> {
                    CustomDialog.hideDialog()
                    val list = state.data!!

                    if (list.isNotEmpty()) {
                        binding.tvNoCartItemFound.visibility = View.GONE
                        binding.rvAddress.visibility = View.VISIBLE
                        updateRecyclerView(list)
                    } else {
                        binding.tvNoCartItemFound.visibility = View.VISIBLE
                        binding.rvAddress.visibility = View.GONE
                    }

                }

                is Response.Error -> {
                    CustomDialog.hideDialog()
                }
            }

        }
    }

    private fun setUpToolbar() {
        activity?.let {
            (activity as CustomToolbar).onToolbarLoad(
                Constants.FRAGMENT_ADDRESS,
                "Address list"
            )
        }
    }

    private fun updateRecyclerView(list: List<Address>) {
        val recyclerView = binding.rvAddress

        val addressAdapter = AddressAdapter(requireActivity(), list, this)

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = addressAdapter
        }
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            binding.btnAddAddress.id -> {
                findNavController().navigate(
                    AddressFragmentDirections.fragAddressToAddAddress(null)
                )
            }
        }
    }

    override fun onAddressClicked(address: Address) {
        findNavController().navigate(
            AddressFragmentDirections.fragAddressToCheckout(address)
        )
    }

    override fun onAddressEditClicked(address: Address) {
        findNavController().navigate(
            AddressFragmentDirections.fragAddressToAddAddress(address)
        )
    }

    override fun onAddressDeleteClicked(address: Address) {
        CustomDialog.showDialog(requireActivity())
        viewModel.deleteAddress(address.addressId)
        viewModel.deleteAddressLive.observe(viewLifecycleOwner) { state ->

            when (state) {
                is Response.Success -> {
                    CustomDialog.hideDialog()
                    CustomDialog.showToast(requireActivity(), "Deleted successfully")
                }
                is Response.Error -> {
                    CustomDialog.hideDialog()
                    CustomDialog.showToast(requireActivity(), "Something wrong")
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}