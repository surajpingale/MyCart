package com.example.mycart.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dishapp.interfaces.CustomToolbar
import com.example.mycart.R
import com.example.mycart.databinding.FragmentOrderDetailsBinding
import com.example.mycart.utils.Constants
import com.example.mycart.views.adapter.CartAdapter
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class OrderDetailsFragment : Fragment() {

    private var _binding: FragmentOrderDetailsBinding? = null

    private val binding get() = _binding!!

    private val args: OrderDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderDetailsBinding.inflate(inflater, container, false)

        setUpToolbar()
        initViews()

        return binding.root
    }

    private fun setUpToolbar() {
        activity?.let {
            (activity as CustomToolbar).onToolbarLoad(
                Constants.FRAGMENT_ORDER_DETAILS,
                "Order Details"
            )
        }
    }


    private fun initViews() {
        val order = args.order

        if (order != null) {
            binding.tvOrderId.text = order.orderId

            val dateFormat = "dd-MM-yyyy HH:mm"
            val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
            val calender = Calendar.getInstance()
            calender.timeInMillis = order.dateTime
            val orderDateTime = formatter.format(calender.time)

            binding.tvOrderDate.text = orderDateTime

            val diffInMilliSeconds = System.currentTimeMillis() - order.dateTime
            val diffHours = TimeUnit.MILLISECONDS.toHours(diffInMilliSeconds)


            when (diffHours) {
                in 0..2 -> {
                    binding.tvOrderStatus.text = resources.getString(R.string.pending)
                    binding.tvOrderStatus.setTextColor(
                        ContextCompat.getColor(
                            requireActivity(),
                            R.color.colorThemePink
                        )
                    )
                }

                in 2..3 -> {
                    binding.tvOrderStatus.text = resources.getString(R.string.in_process)
                    binding.tvOrderStatus.setTextColor(
                        ContextCompat.getColor(
                            requireActivity(),
                            R.color.colorThemeOrange
                        )
                    )
                }

                else -> {
                    binding.tvOrderStatus.text = resources.getString(R.string.delivered)
                    binding.tvOrderStatus.setTextColor(
                        ContextCompat.getColor(
                            requireActivity(),
                            R.color.green
                        )
                    )
                }
            }

            binding.tvFullNameOrderDetails.text = order.address.fullName
            binding.tvAddressOrderDetails.text =
                "${order.address.address} - ${order.address.zipCode}"
            binding.tvMobileNoOrderDetails.text = order.address.mobileNumber
            binding.tvSubtotal.text = order.subTotal
            binding.tvShippingCharges.text = order.shippingCharge
            binding.tvTotalAmount.text = order.totalAmount

        }

        val recyclerView = binding.rvProductsOrderDetails

        val cartAdapter = CartAdapter(requireActivity(), order.cartList, null)

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = cartAdapter
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}