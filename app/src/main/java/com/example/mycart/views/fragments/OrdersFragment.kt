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
import com.example.mycart.databinding.FragmentOrdersBinding
import com.example.mycart.model.Order
import com.example.mycart.utils.Constants
import com.example.mycart.utils.Response
import com.example.mycart.viewmodel.UserDetailViewModel
import com.example.mycart.viewmodel.UserViewModelFactory
import com.example.mycart.views.adapter.OrdersAdapter


class OrdersFragment : Fragment(), OrdersAdapter.OnOrderClick {

    private var _binding: FragmentOrdersBinding? = null

    private val binding get() = _binding!!

    private val viewModel: UserDetailViewModel by viewModels {
        UserViewModelFactory(
            (requireActivity().application as MyCartApplication).fireStoreRepository
        )
    }

    private lateinit var ordersList: List<Order>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)

        setUpToolbar()
        initViewAndListeners()

        return binding.root
    }

    private fun setUpToolbar() {
        activity?.let {
            (activity as CustomToolbar).onToolbarLoad(
                Constants.FRAGMENT_ORDERS,
                "Orders"
            )
        }
    }

    private fun initViewAndListeners() {
        getOrdersList()

    }

    private fun getOrdersList() {
        viewModel.getOrdersList()
        viewModel.orderListLive.observe(viewLifecycleOwner) { state ->

            when (state) {
                is Response.Success -> {
                    ordersList = state.data!!
                    updateRecyclerView()
                }
                is Response.Error -> {

                }
            }
        }
    }

    private fun updateRecyclerView() {
        if (ordersList.isNotEmpty()) {
            binding.rvOrders.visibility = View.VISIBLE
            binding.tvNoOrderFound.visibility = View.GONE

            val recyclerView = binding.rvOrders

            val ordersAdapter = OrdersAdapter(requireActivity(), ordersList, this)

            recyclerView.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireActivity())
                adapter = ordersAdapter
            }

        } else {
            binding.rvOrders.visibility = View.GONE
            binding.tvNoOrderFound.visibility = View.VISIBLE
        }
    }

    override fun onOrderClicked(order: Order) {
        findNavController().navigate(
            OrdersFragmentDirections.fragOrdersToOrderDetails(order)
        )
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}