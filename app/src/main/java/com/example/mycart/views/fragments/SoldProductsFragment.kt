package com.example.mycart.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dishapp.interfaces.CustomToolbar
import com.example.mycart.application.MyCartApplication
import com.example.mycart.databinding.FragmentSoldProductsBinding
import com.example.mycart.model.SoldProduct
import com.example.mycart.utils.Constants
import com.example.mycart.utils.CustomDialog
import com.example.mycart.utils.Response
import com.example.mycart.viewmodel.UserDetailViewModel
import com.example.mycart.viewmodel.UserViewModelFactory
import com.example.mycart.views.adapter.SoldProductAdapter


class SoldProductsFragment : Fragment(), SoldProductAdapter.OnSoldProductClick {

    private var _binding: FragmentSoldProductsBinding? = null

    private val binding get() = _binding!!

    private val viewModel: UserDetailViewModel by viewModels {
        UserViewModelFactory(
            (requireActivity().application as MyCartApplication).fireStoreRepository
        )
    }

    private lateinit var soldProductList: List<SoldProduct>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSoldProductsBinding.inflate(inflater, container, false)

        setUpToolbar()
        initViews()
        return binding.root
    }

    private fun setUpToolbar() {
        activity?.let {
            (activity as CustomToolbar).onToolbarLoad(
                Constants.FRAGMENT_SOLD_PRODUCTS,
                "Sold Products"
            )
        }
    }

    private fun initViews() {
        viewModel.getSoldProducts()
        viewModel.soldProductListLive.observe(viewLifecycleOwner) { state ->

            when (state) {
                is Response.Success -> {
                    soldProductList = state.data!!
                    updateRecyclerView()
                }
                is Response.Error -> {
                    CustomDialog.showToast(requireActivity(), "Something wrong")
                }
            }
        }
    }

    private fun updateRecyclerView() {
        if (soldProductList.isNotEmpty()) {
            binding.tvNoSoldProductFound.visibility = View.GONE
            binding.rvSoldProduct.visibility = View.VISIBLE
            val recyclerView = binding.rvSoldProduct
            val soldProductAdapter = SoldProductAdapter(requireActivity(), soldProductList, this)

            recyclerView.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireActivity())
                adapter = soldProductAdapter
            }
        } else {
            binding.tvNoSoldProductFound.visibility = View.VISIBLE
            binding.rvSoldProduct.visibility = View.GONE
        }

    }

    override fun onSoldProductClicked(soldProduct: SoldProduct) {
        findNavController().navigate(
            SoldProductsFragmentDirections.fragSoldProductsToSoldProductDetails(soldProduct)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}