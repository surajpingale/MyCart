package com.example.mycart.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dishapp.interfaces.CustomToolbar
import com.example.mycart.application.MyCartApplication
import com.example.mycart.databinding.FragmentDashboardBinding
import com.example.mycart.model.Product
import com.example.mycart.utils.Constants
import com.example.mycart.utils.CustomDialog
import com.example.mycart.utils.Response
import com.example.mycart.viewmodel.DashboardViewModel
import com.example.mycart.viewmodel.DashboardViewModelFactory
import com.example.mycart.views.activities.MainActivity
import com.example.mycart.views.adapter.DashboardAdapter
import com.example.mycart.views.adapter.DashboardSliderAdapter
import kotlinx.coroutines.*


class DashboardFragment : Fragment(), DashboardAdapter.OnDashboardItemClick {

    private var _binding: FragmentDashboardBinding? = null

    private val binding: FragmentDashboardBinding
        get() = _binding!!

    private val viewModel: DashboardViewModel by viewModels {
        DashboardViewModelFactory(
            (requireActivity().application as MyCartApplication)
                .fireStoreRepository
        )
    }

    private lateinit var productList: List<Product>
    private lateinit var mProductList: List<Product>
    private lateinit var dashboardAdapter: DashboardAdapter
    private lateinit var job: Job

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        setUpToolbar()
        initViewAndListeners()

        return binding.root
    }

    private fun initViewAndListeners() {
        val recyclerView = binding.rvProducts

        productList = ArrayList()

        dashboardAdapter = DashboardAdapter(requireActivity(), productList, this)

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireActivity(), 2)
            adapter = dashboardAdapter
            isNestedScrollingEnabled = false
        }
        getListOfProducts()
    }

    private fun getListOfProducts() {
        viewModel.getAllProducts()
        viewModel.productLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Response.Success -> {
                    mProductList = state.data!!
                    dashboardAdapter.updateList(mProductList)
                    addingImagesForSlider()
                }
                is Response.Error -> {
                    CustomDialog.showToast(requireActivity(), "Something wrong")
                }
            }
        }
    }

    private fun addingImagesForSlider() {
        val viewPager2 = binding.vp2Images
        val sliderImagesList = ArrayList<String>()
        for (i in mProductList) {
            val string = i.image
            sliderImagesList.add(string)
        }
        val dashImageSliderAdapter = DashboardSliderAdapter(
            requireActivity(),
            sliderImagesList
        )
        viewPager2.adapter = dashImageSliderAdapter

        /**
         * for auto sliding view pager
         */
        job = CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                delay(3000)
                withContext(Dispatchers.Main) {

                    if (viewPager2.currentItem == sliderImagesList.size - 1) {
                        viewPager2.currentItem = 0
                    } else {
                        viewPager2.currentItem = viewPager2.currentItem + 1
                    }
                }
            }
        }
    }

    private fun setUpToolbar() {
        activity?.let {
            (activity as CustomToolbar).onToolbarLoad(
                Constants.FRAGMENT_DASHBOARD,
                "Dashboard"
            )
        }
    }

    override fun onItemClicked(product: Product) {
        findNavController().navigate(
            DashboardFragmentDirections.fragDashboardToDetails(product)
        )
        if (activity is MainActivity) {
            (activity as MainActivity).hideBottomNavigationBar()
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
        job.cancel()
    }
}