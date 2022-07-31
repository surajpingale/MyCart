package com.example.mycart.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.dishapp.interfaces.CustomToolbar
import com.example.mycart.R
import com.example.mycart.application.MyCartApplication
import com.example.mycart.databinding.FragmentProductDetailsBinding
import com.example.mycart.model.CartItem
import com.example.mycart.model.Product
import com.example.mycart.utils.Constants
import com.example.mycart.utils.CustomDialog
import com.example.mycart.utils.Response
import com.example.mycart.viewmodel.UserDetailViewModel
import com.example.mycart.viewmodel.UserViewModelFactory


class ProductDetailsFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentProductDetailsBinding? = null

    private val binding get() = _binding!!

    private val args: ProductDetailsFragmentArgs by navArgs()
    private lateinit var product: Product

    private val viewModel: UserDetailViewModel by viewModels {
        UserViewModelFactory(
            (requireActivity().application as MyCartApplication).fireStoreRepository
        )
    }

    private var productInCart: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailsBinding.inflate(inflater, container, false)

        setUpToolbar()
        initViewAndListeners()

        return binding.root
    }

    private fun setUpToolbar() {
        activity?.let {
            (activity as CustomToolbar).onToolbarLoad(
                Constants.FRAGMENT_PRODUCT_DETAILS,
                args.productDetails.productName
            )
        }
    }

    private fun initViewAndListeners() {
        product = args.productDetails
        productInCartOrNot()

        if (product.userId == viewModel.getUserId()) {
            binding.btnBuyNow.visibility = View.GONE
            binding.btnAddToCart.visibility = View.GONE
        } else {
            binding.btnBuyNow.visibility = View.VISIBLE
            binding.btnAddToCart.visibility = View.VISIBLE
            binding.btnBuyNow.setOnClickListener(this)
            binding.btnAddToCart.setOnClickListener(this)

        }

        Glide.with(requireActivity())
            .load(product.image)
            .into(binding.ivProductPhoto)

        binding.tvProductName.text = product.productName
        binding.tvProductPrice.text = product.price
        binding.tvProductQuantity.text = product.quantity
        binding.tvProductDescription.text = product.description
    }

    private fun productInCartOrNot() {
        viewModel.checkProductInCartOrNot(product.productId)
        viewModel.existProductInCart.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Response.Success -> {
                    productInCart = state.data!!
                    if (productInCart) {
                        binding.btnAddToCart.text = resources.getString(R.string.go_to_cart)
                    } else {
                        binding.btnAddToCart.text = resources.getString(R.string.add_to_cart)
                    }
                }
                is Response.Error -> {
                    CustomDialog.showToast(requireActivity(), "Something wrong")
                }

            }
        }
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            binding.btnBuyNow.id -> {
                buyNow()
            }
            binding.btnAddToCart.id -> {
                CustomDialog.showDialog(requireActivity())
                if (!productInCart) {
                    addToCart()
                } else {
                    CustomDialog.hideDialog()
                    findNavController().navigate(
                        ProductDetailsFragmentDirections
                            .fragProductDetailsToCart()
                    )
                }

            }
        }
    }

    private fun addToCart() {
        val cartItem = CartItem()
        cartItem.userId = viewModel.getUserId()
        cartItem.productOwnerId = product.userId
        cartItem.productId = product.productId
        cartItem.productName = product.productName
        cartItem.image = product.image
        cartItem.price = product.price
        cartItem.cartQuantity = Constants.DEFAULT_CART_QUANTITY
        cartItem.stockQuantity = product.quantity

        viewModel.addToCart(cartItem)
        viewModel.cartData.observe(viewLifecycleOwner) { state ->

            when (state) {
                is Response.Success -> {
                    CustomDialog.hideDialog()
                    CustomDialog.showToast(
                        requireActivity(),
                        "Added to cart successfully"
                    )
                    binding.btnAddToCart.text = resources.getString(R.string.go_to_cart)
                    productInCart = true
                }
                is Response.Error -> {
                    CustomDialog.hideDialog()
                    CustomDialog.showToast(
                        requireActivity(),
                        "Error in adding to cart"
                    )
                }
            }
        }
    }

    private fun buyNow() {
        findNavController().navigate(
            ProductDetailsFragmentDirections.fragProductDetailsToAddress()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}