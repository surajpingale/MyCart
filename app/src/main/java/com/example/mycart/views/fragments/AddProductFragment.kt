package com.example.mycart.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.dishapp.interfaces.CustomToolbar
import com.example.mycart.R
import com.example.mycart.application.MyCartApplication
import com.example.mycart.databinding.FragmentAddProductBinding
import com.example.mycart.model.Product
import com.example.mycart.utils.Constants
import com.example.mycart.utils.CustomDialog
import com.example.mycart.utils.Permissions
import com.example.mycart.utils.Response
import com.example.mycart.viewmodel.AddProductViewModel
import com.example.mycart.viewmodel.AddProductViewModelFactory


class AddProductFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentAddProductBinding? = null

    private val binding get() = _binding!!

    private var imageUrl = ""

    private val viewModel: AddProductViewModel by viewModels {
        AddProductViewModelFactory(
            (requireActivity().application as MyCartApplication).fireStoreRepository
        )
    }

    private val getImageFromGallery = registerForActivityResult(
        ActivityResultContracts
            .GetContent()
    ) { uri ->

        if (uri != null) {
            viewModel.uploadImageToCloud(
                context!!, uri
            )
            viewModel.imageLiveData.observe(viewLifecycleOwner) {
                when (it) {
                    is Response.Success -> {
                        imageUrl = it.data!!
                        Glide.with(requireActivity())
                            .load(imageUrl)
                            .placeholder(R.drawable.ic_user_placeholder)
                            .into(binding.ivProductPhoto)
                    }
                    is Response.Error -> {
                        Toast.makeText(requireActivity(), "Error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddProductBinding.inflate(inflater, container, false)

        setUpToolbar()
        initViews()

        return binding.root
    }

    private fun setUpToolbar() {
        activity?.let {
            (activity as CustomToolbar).onToolbarLoad(
                Constants.FRAGMENT_ADD_PRODUCT,
                "Add Product"
            )
        }
    }

    private fun initViews() {
        binding.btnAddProduct.setOnClickListener(this)
        binding.ivAddPhoto.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view!!.id) {

            binding.btnAddProduct.id -> {
                CustomDialog.showDialog(requireActivity())
                if (validateEntries()) {
                    addProduct()
                }
            }

            binding.ivAddPhoto.id -> {
                if (Permissions.checkStoragePermission(requireActivity())) {
                    getImageFromGallery.launch("image/*")
                } else {
                    Permissions.requestStoragePermission(requireActivity())
                }
            }
        }
    }

    private fun addProduct() {
        val productName = binding.etTvProductName.editText.text.toString().trim { it <= ' ' }
        val productPrice = binding.etTvProductPrice.editText.text.toString().trim { it <= ' ' }
        val description = binding.etTvProductDescription.editText.text.toString().trim { it <= ' ' }
        val productQuantity =
            binding.etTvProductQuantity.editText.text.toString().trim { it <= ' ' }

        /**
         * for getting user details
         */
        viewModel.getUserDetails()
        viewModel.userDetailLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Response.Success -> {
                    val user = it.data!!
                    val product = Product()
                    product.image = imageUrl
                    product.userId = user.id
                    product.userName = "${user.firstName} ${user.lastName}"
                    product.productName = productName
                    product.description = description
                    product.price = productPrice
                    product.quantity = productQuantity
                    viewModel.addProduct(product)
                }
            }
        }

        viewModel.productLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Response.Success -> {
                    CustomDialog.hideDialog()
                    activity?.onBackPressed()
                }
                is Response.Error -> {
                    CustomDialog.hideDialog()
                }
            }
        }
    }

    private fun validateEntries(): Boolean {
        val productName = binding.etTvProductName.editText.text.toString().trim { it <= ' ' }
        val productPrice = binding.etTvProductPrice.editText.text.toString().trim { it <= ' ' }
        val description = binding.etTvProductDescription.editText.text.toString().trim { it <= ' ' }
        val productQuantity =
            binding.etTvProductQuantity.editText.text.toString().trim { it <= ' ' }

        if (productName.isEmpty()) {
            binding.etTvProductName.errorText.visibility = View.VISIBLE
            binding.etTvProductName.errorText.text =
                resources.getString(R.string.error_msg_product_name)
        } else {
            binding.etTvProductName.errorText.visibility = View.INVISIBLE
        }

        if (productPrice.isEmpty()) {
            binding.etTvProductPrice.errorText.visibility = View.VISIBLE
            binding.etTvProductPrice.errorText.text =
                resources.getString(R.string.error_msg_product_price)
        } else {
            binding.etTvProductPrice.errorText.visibility = View.INVISIBLE
        }

        if (description.isEmpty()) {
            binding.etTvProductDescription.errorText.visibility = View.VISIBLE
            binding.etTvProductDescription.errorText.text =
                resources.getString(R.string.error_msg_product_description)
        } else {
            binding.etTvProductDescription.errorText.visibility = View.INVISIBLE
        }

        if (productQuantity.isEmpty()) {
            binding.etTvProductQuantity.errorText.visibility = View.VISIBLE
            binding.etTvProductQuantity.errorText.text =
                resources.getString(R.string.error_msg_product_quantity)
        } else {
            binding.etTvProductQuantity.errorText.visibility = View.INVISIBLE
        }

        return true
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}