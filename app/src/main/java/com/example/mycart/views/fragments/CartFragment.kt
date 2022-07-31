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
import com.example.mycart.R
import com.example.mycart.application.MyCartApplication
import com.example.mycart.databinding.FragmentCartBinding
import com.example.mycart.model.CartItem
import com.example.mycart.utils.Constants
import com.example.mycart.utils.CustomDialog
import com.example.mycart.utils.Response
import com.example.mycart.viewmodel.UserDetailViewModel
import com.example.mycart.viewmodel.UserViewModelFactory
import com.example.mycart.views.adapter.CartAdapter


class CartFragment : Fragment(), CartAdapter.OnCartItemClick, View.OnClickListener {

    private var _binding: FragmentCartBinding? = null

    private val binding get() = _binding!!
    private lateinit var cartList: List<CartItem>
    private lateinit var cartAdapter: CartAdapter

    private val viewModel: UserDetailViewModel by viewModels {
        UserViewModelFactory(
            (requireActivity().application as MyCartApplication).fireStoreRepository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)

        setUpToolbar()
        initViewAndListeners()

        return binding.root
    }

    private fun initViewAndListeners() {

        binding.include.btnCheckout.setOnClickListener(this)

        CustomDialog.showDialog(requireActivity())
        gettingCartList()
        CustomDialog.hideDialog()
    }

    override fun onClick(view: View?) {
        val include = binding.include
        when(view!!.id)
        {
            include.btnCheckout.id -> {
                findNavController().navigate(CartFragmentDirections.fragCartToAddress())
            }
        }
    }

    private fun gettingCartList() {
        updateRecyclerView()
        viewModel.gettingCartList()
        viewModel.cartListData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Response.Success -> {
                    val list = state.data!!
                    cartAdapter.updateCartList(list)
                    CustomDialog.hideDialog()
                    if (list.isNotEmpty()) {
                        binding.tvNoCartItemFound.visibility = View.GONE
                        binding.include.rootCartLayout.visibility = View.VISIBLE
                        showingPrice(list)
                    } else {
                        binding.tvNoCartItemFound.visibility = View.VISIBLE
                        binding.include.rootCartLayout.visibility = View.GONE
                    }
                }
                is Response.Error -> {
                    CustomDialog.hideDialog()
                    CustomDialog.showToast(requireActivity(), "Something wrong")
                }
            }
        }
    }

    private fun showingPrice(cartList: List<CartItem>) {
        var subtotal = 0
        val shippingCharges = "40"

        for (cartItem in cartList) {
            subtotal += (cartItem.price.toInt()) * cartItem.cartQuantity.toInt()
        }

        val totalPrice: Int = subtotal + shippingCharges.toInt()

        binding.include.tvSubtotal.text = subtotal.toString()
        binding.include.tvShippingCharges.text = "₹40"
        binding.include.tvTotal.text = totalPrice.toString()
    }

    private fun updateRecyclerView() {
        cartList = ArrayList()
        val recyclerView = binding.include.rvCartItems

        cartAdapter = CartAdapter(requireActivity(), cartList, this)
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = cartAdapter
        }
    }

    private fun setUpToolbar() {
        activity?.let {
            (activity as CustomToolbar).onToolbarLoad(
                Constants.FRAGMENT_CART,
                "Cart"
            )
        }
    }

    override fun onCartDeleteClicked(cartItem: CartItem) {
        viewModel.deleteItemFromCart(cartItem.id)
        viewModel.deletedProductFromCart.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Response.Success -> {
                    CustomDialog.showToast(requireActivity(), "Deleted Successfully")
                    viewModel.gettingCartList()
                }
            }
        }
    }

    override fun onCartItemClicked(cartItem: CartItem) {

    }

    override fun onCartAddClicked(cartItem: CartItem) {
        CustomDialog.showDialog(requireActivity())
        val cartQuantity = cartItem.cartQuantity.toInt()
        if (cartQuantity < cartItem.stockQuantity.toInt()) {
            val hashMap = HashMap<String, Any>()
            val updated = (cartQuantity + 1)
            hashMap[Constants.CART_QUANTITY] = updated.toString()

            viewModel.updateCartQuantity(cartItem.id, hashMap)

            viewModel.updateCartLiveData.observe(viewLifecycleOwner) { state ->
                when (state) {
                    is Response.Success -> {
                        CustomDialog.hideDialog()
                    }
                    is Response.Error -> {
                        CustomDialog.hideDialog()
                    }
                }
            }
        } else {
            CustomDialog.hideDialog()
            CustomDialog.showToast(
                requireActivity(),
                resources.getString(R.string.stock_for_available, cartItem.stockQuantity)
            )
        }
        viewModel.gettingCartList()
    }

    override fun onCartRemoveClicked(cartItem: CartItem) {
        val cartQuantity = cartItem.cartQuantity.toInt()
        if (cartQuantity == 1) {
            viewModel.deleteItemFromCart(cartItem.id)
        } else {
            val hashMap = HashMap<String, Any>()

            hashMap[Constants.CART_QUANTITY] = (cartQuantity - 1).toString()
            viewModel.updateCartQuantity(cartItem.id, hashMap)
        }
        viewModel.gettingCartList()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}