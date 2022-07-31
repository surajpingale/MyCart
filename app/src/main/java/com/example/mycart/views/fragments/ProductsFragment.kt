package com.example.mycart.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dishapp.interfaces.CustomToolbar
import com.example.mycart.application.MyCartApplication
import com.example.mycart.databinding.FragmentProductsBinding
import com.example.mycart.model.Product
import com.example.mycart.utils.Constants
import com.example.mycart.utils.CustomDialog
import com.example.mycart.utils.Response
import com.example.mycart.viewmodel.ProductViewModel
import com.example.mycart.viewmodel.ProductViewModelFactory
import com.example.mycart.views.activities.MainActivity
import com.example.mycart.views.adapter.ProductsAdapter

class ProductsFragment : Fragment(), ProductsAdapter.OnItemClick {

    private var _binding: FragmentProductsBinding? = null

    private val binding get() = _binding!!

    private lateinit var productList: List<Product>
    private lateinit var productAdapter: ProductsAdapter


    private val viewModel: ProductViewModel by viewModels {
        ProductViewModelFactory(
            (requireActivity().application
                    as MyCartApplication).fireStoreRepository
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)

        setUpToolbar()
        initViewsAndListeners()

        return binding.root
    }

    private fun initViewsAndListeners() {
        productList = ArrayList()

        val recyclerView = binding.rvUserProducts

        productAdapter = ProductsAdapter(requireActivity(), productList, this)

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = productAdapter
        }

        viewModel.getProductList()

        viewModel.productLiveData.observe(viewLifecycleOwner) { state ->

            when (state) {
                is Response.Loading -> {
                    CustomDialog.showDialog(requireActivity())
                }
                is Response.Success -> {
                    CustomDialog.hideDialog()
                    val list = state.data!!
                    productAdapter.updateList(list)
                }

                is Response.Error -> {
                    CustomDialog.hideDialog()
                }
            }
        }

    }

    override fun onItemClicked(product: Product) {
        findNavController().navigate(
            ProductsFragmentDirections
                .fragProductsToProductDetails(product)
        )
    }

    override fun onPopUpDeleteClicked(product: Product) {
        viewModel.productDelete(product.productId)
        viewModel.productDeleteLiveData.observe(viewLifecycleOwner){state ->
            when(state)
            {
                is Response.Success -> {
                    Toast.makeText(
                        requireActivity(), "Product deleted successfully.", Toast.LENGTH_SHORT
                    ).show()
                }
                is Response.Error -> {
                    Toast.makeText(
                        requireActivity(), "Something wrong.", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        viewModel.getProductList()
    }

    override fun onPopUpEditClicked(product: Product) {
        findNavController().navigate(
            ProductsFragmentDirections
                .fragProductsToAddProduct()
        )
    }

    private fun setUpToolbar() {
        activity?.let {
            (activity as CustomToolbar).onToolbarLoad(
                Constants.FRAGMENT_PRODUCTS,
                "Products"
            )
        }
    }

    override fun onResume() {
        super.onResume()
        if (activity is MainActivity) {
            (activity as MainActivity).showBottomNavigationBar()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}