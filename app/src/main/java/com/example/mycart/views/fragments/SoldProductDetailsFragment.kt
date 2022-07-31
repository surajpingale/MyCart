package com.example.mycart.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.dishapp.interfaces.CustomToolbar
import com.example.mycart.R
import com.example.mycart.databinding.FragmentSoldProductDetailsBinding
import com.example.mycart.utils.Constants
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class SoldProductDetailsFragment : Fragment() {

    private var _binding: FragmentSoldProductDetailsBinding? = null

    private val binding get() = _binding!!

    private val args: SoldProductDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSoldProductDetailsBinding.inflate(inflater, container, false)

        setUpToolbar()
        initViews()

        return binding.root
    }

    private fun setUpToolbar() {
        activity?.let {
            (activity as CustomToolbar).onToolbarLoad(
                Constants.FRAGMENT_SOLD_PRODUCTS_DETAILS,
                "Sold Product Details"
            )
        }
    }

    private fun initViews() {
        val soldProduct = args.soldProduct

        if (soldProduct != null) {
            binding.tvOrderId.text = soldProduct.orderId

            val dateFormat = "dd-MM-yyyy HH:mm"
            val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
            val calender = Calendar.getInstance()
            calender.timeInMillis = soldProduct.orderDate
            val orderDateTime = formatter.format(calender.time)

            binding.tvOrderDate.text = orderDateTime

            val diffInMilliSeconds = System.currentTimeMillis() - soldProduct.orderDate
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

            binding.tvFullNameOrderDetails.text = soldProduct.address.fullName
            binding.tvAddressOrderDetails.text =
                "${soldProduct.address.address} - ${soldProduct.address.zipCode}"
            binding.tvMobileNoOrderDetails.text = soldProduct.address.mobileNumber
            binding.tvSubtotal.text = soldProduct.subTotal
            binding.tvShippingCharges.text = soldProduct.shippingCharge
            binding.tvTotalAmount.text = soldProduct.totalAmount

        }

        val include = binding.includeCartItem
        include.ivCartDelete.visibility = View.GONE
        include.ivAddProductToCart.visibility = View.GONE
        include.ivRemoveProductFromCart.visibility = View.GONE

        Glide.with(requireActivity())
            .load(soldProduct.image)
            .into(include.ivCartProduct)
        include.tvCartProductName.text = soldProduct.title
        include.tvProductInCart.text = soldProduct.soldQuantity
        include.tvCartProductPrice.text = soldProduct.price

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}