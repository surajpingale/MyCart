package com.example.mycart.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dishapp.interfaces.CustomToolbar
import com.example.mycart.application.MyCartApplication
import com.example.mycart.databinding.FragmentCheckoutBinding
import com.example.mycart.model.Address
import com.example.mycart.model.CartItem
import com.example.mycart.model.Order
import com.example.mycart.model.Product
import com.example.mycart.utils.Constants
import com.example.mycart.utils.CustomDialog
import com.example.mycart.utils.Response
import com.example.mycart.viewmodel.UserDetailViewModel
import com.example.mycart.viewmodel.UserViewModelFactory
import com.example.mycart.views.adapter.CartAdapter

class CheckoutFragment : Fragment() {

    private var _binding: FragmentCheckoutBinding? = null

    private val binding get() = _binding!!

    private val viewModel: UserDetailViewModel by viewModels {
        UserViewModelFactory(
            (requireActivity().application as MyCartApplication).fireStoreRepository
        )
    }

    private val args: CheckoutFragmentArgs by navArgs()

    private var address: Address? = null
    private lateinit var productList: List<Product>
    private lateinit var cartItemList: List<CartItem>
    private lateinit var mOrder: Order

    private var subTotal = 0.0
    private var shippingCharge = 0.0
    private var totalAmount = 0.0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)

        setUpToolbar()
        initViewAndListeners()

        return binding.root
    }

    private fun setUpToolbar() {
        activity?.let {
            (activity as CustomToolbar).onToolbarLoad(
                Constants.FRAGMENT_CHECKOUT,
                "Checkout"
            )
        }
    }

    private fun initViewAndListeners() {
        address = args.address
        if (address != null) {
            binding.tvFullNameCheckout.text = address!!.fullName
            binding.tvAddressCheckout.text = "${address!!.address} - ${address!!.zipCode}"
            binding.tvMobileNoCheckout.text = address!!.mobileNumber
        }

        getProductList()

        binding.btnCheckout.setOnClickListener {
            if (address != null) {
                placeOrder()
            }
        }
    }

    private fun getProductList() {
        viewModel.getProductList()
        viewModel.productLiveDataList.observe(viewLifecycleOwner) { state ->

            when (state) {

                is Response.Success -> {
                    productList = state.data!!
                    getCartList()
                }

                is Response.Error -> {
                    CustomDialog.showToast(requireActivity(),"Something wrong")
                }
            }
        }
    }

    private fun getCartList() {
        viewModel.gettingCartList()
        viewModel.cartListData.observe(viewLifecycleOwner) { state ->

            when (state) {
                is Response.Success -> {
                    val cartList = state.data!!
                    for (product in productList) {
                        for (cartItem in cartList) {
                            if (product.productId == cartItem.productId) {
                                cartItem.stockQuantity = product.quantity
                            }
                        }
                    }
                    cartItemList = cartList
                    updateRecyclerView()
                }
                is Response.Error -> {

                }

            }
        }
    }

    private fun updateRecyclerView() {
        val recyclerView = binding.rvProductsCheckout

        val cartAdapter = CartAdapter(requireActivity(), cartItemList, null)

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = cartAdapter
        }


        for (cartItem in cartItemList) {
            val productQuantity = cartItem.stockQuantity.toInt()
            if (productQuantity > 0) {
                val price = cartItem.price.toDouble()
                val quantity = cartItem.cartQuantity.toInt()
                subTotal += (price * quantity)
                shippingCharge += (40 * quantity)
            }
        }
        totalAmount = (subTotal + shippingCharge)
        binding.tvSubtotal.text = "₹$subTotal"
        binding.tvShippingCharges.text = "₹$shippingCharge"
        binding.tvTotal.text = "₹$totalAmount"
    }

    private fun placeOrder() {
        CustomDialog.showDialog(requireActivity())

        val order = Order()
        order.userId = viewModel.getUserId()
        order.cartList = cartItemList
        order.address = address!!
        order.orderName = "Order ${System.currentTimeMillis()}"
        order.subTotal = subTotal.toString()
        order.totalAmount = totalAmount.toString()
        order.dateTime = System.currentTimeMillis()
        order.image = cartItemList[0].image
        order.shippingCharge = shippingCharge.toString()

        mOrder = order
        viewModel.placeOrder(mOrder)
        viewModel.addOrderLive.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Response.Success -> {
                    updateAllData()
                }

                is Response.Error -> {
                    CustomDialog.showToast(requireActivity(),"Something wrong")
                }
            }
        }
    }

    private fun updateAllData() {
        viewModel.updateAllDataAfterPlaceOrder(cartItemList, mOrder)
        viewModel.updateAllDataLive.observe(viewLifecycleOwner) { state ->

            when (state) {
                is Response.Success -> {
                    CustomDialog.hideDialog()
                    CustomDialog.showToast(requireActivity(), "Your order placed successfully")
                    findNavController().navigate(
                        CheckoutFragmentDirections.fragCheckoutToDashboard()
                    )
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